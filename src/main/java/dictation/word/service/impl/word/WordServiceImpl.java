package dictation.word.service.impl.word;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dictation.word.dao.word.WordMapper;
import dictation.word.entity.lib.tables.Lib;
import dictation.word.entity.lib.tables.LibWord;
import dictation.word.entity.word.Explain;
import dictation.word.entity.word.ImportWord;
import dictation.word.entity.word.WordInfo;
import dictation.word.entity.word.tables.Word;
import dictation.word.entity.word.tables.WordExplain;
import dictation.word.exception.CreateNewException;
import dictation.word.exception.IllegalDataException;
import dictation.word.exception.NoPermissionException;
import dictation.word.service.i.lib.LibService;
import dictation.word.service.i.lib.LibWordService;
import dictation.word.service.i.user.TokenService;
import dictation.word.service.i.word.WordExplainService;
import dictation.word.service.i.word.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Service
public class WordServiceImpl extends ServiceImpl<WordMapper, Word> implements WordService, TokenService {
    @Autowired
    LibService libService;
    @Autowired
    LibWordService libWordService;
    @Autowired
    WordExplainService explainService;
    @Autowired
    WordMapper wordMapper;

    @Override
    public List<WordInfo> getList(int libId, int pageNum, int pageSize) {
        final Lib lib = libService.getById(libId);
        if (lib == null) {
            throw new IllegalDataException("该库不存在！");
        }
        if (!lib.getCommon() && lib.getCreator() != getCurrentUserId()) {
            throw new NoPermissionException("这不是你的私有库");
        }
        return wordMapper.getWordInfo(libId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Integer> importSingle(ImportWord word) {
        final Lib lib = libService.getById(word.getLibId());
        if (lib == null) {
            throw new CreateNewException("给定词库不存在，导入失败！");
        }
        final Word one = getOne(Wrappers.<Word>lambdaQuery().eq(Word::getWord, word.getWord()));
        if (one == null) {
            //总词库里面没有该单词
            if (!save(word)) {
                throw new CreateNewException("导入失败！");
            }
        } else {
            word.setId(one.getId());
        }
        List<Integer> ids = new ArrayList<>();
        ids.add(word.getId());
        //将词库与单词关联
        if (!libWordService.save(new LibWord(lib.getId(), word.getId()))) {
            throw new CreateNewException("关联词库导入失败！");
        }
        // 导入释义
        List<WordExplain> explains = new ArrayList<>(word.getExplains().size());
        for (Explain explain : word.getExplains()) {
            explains.add(new WordExplain(word.getId(), lib.getId(), explain));
        }
        if (!explainService.saveBatch(explains)) {
            throw new CreateNewException("释义导入失败！");
        }
        for (WordExplain explain : explains) {
            ids.add(explain.getId());
        }
        return ids;
    }
}
