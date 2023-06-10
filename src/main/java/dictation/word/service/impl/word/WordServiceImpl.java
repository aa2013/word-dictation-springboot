package dictation.word.service.impl.word;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import dictation.word.dao.word.WordMapper;
import dictation.word.entity.lib.tables.Lib;
import dictation.word.entity.lib.tables.LibWord;
import dictation.word.entity.word.*;
import dictation.word.entity.word.tables.Word;
import dictation.word.entity.word.tables.WordExplain;
import dictation.word.entity.word.tables.WordExplainCustom;
import dictation.word.exception.CreateNewException;
import dictation.word.exception.IllegalDataException;
import dictation.word.exception.NoPermissionException;
import dictation.word.exception.UpdateException;
import dictation.word.service.i.lib.LibService;
import dictation.word.service.i.lib.LibWordService;
import dictation.word.service.i.lib.UserLibService;
import dictation.word.service.i.word.WordExplainCustomService;
import dictation.word.service.i.word.WordExplainService;
import dictation.word.service.i.word.WordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Service
public class WordServiceImpl extends ServiceImpl<WordMapper, Word> implements WordService {
    @Resource
    LibService libService;
    @Resource
    UserLibService userLibService;
    @Resource
    LibWordService libWordService;
    @Resource
    WordExplainService explainService;
    @Resource
    WordExplainCustomService explainCustomService;
    @Resource
    WordMapper wordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changeDefaultExplains(int libId, int wordId, int defaultId, int userId) {
        Lib lib = libService.getById(libId);
        if (lib.getCreator().equals(userId)) {
            //是创建者
            return changeLibDefaultExplain(libId, wordId, defaultId);
        }
        return changeLibCustomDefaultExplain(libId, wordId, defaultId, userId);
    }

    private boolean changeLibCustomDefaultExplain(int libId, int wordId, int defaultId, int userId) {
        WordExplainCustom custom = explainCustomService.getOne(Wrappers.<WordExplainCustom>lambdaQuery()
                .eq(WordExplainCustom::getUserId, userId)
                .eq(WordExplainCustom::getWordId, wordId)
                .eq(WordExplainCustom::getLibId, libId));
        if (custom == null) {
            return explainCustomService.save(new WordExplainCustom(userId, wordId, libId, defaultId));
        } else {
            return explainCustomService.update(Wrappers.<WordExplainCustom>lambdaUpdate()
                    .eq(WordExplainCustom::getUserId, userId)
                    .eq(WordExplainCustom::getWordId, wordId)
                    .eq(WordExplainCustom::getLibId, libId)
                    .set(WordExplainCustom::getExpId, defaultId));
        }
    }

    private boolean changeLibDefaultExplain(int libId, int wordId, int defaultId) {
        boolean update = explainService.update(Wrappers.<WordExplain>lambdaUpdate()
                .eq(WordExplain::getWordId, wordId)
                .set(WordExplain::getIsDefault, false));
        if (!update) {
            throw new UpdateException("更新默认释义失败！");
        }
        update = explainService.update(Wrappers.<WordExplain>lambdaUpdate()
                .eq(WordExplain::getWordId, wordId)
                .eq(WordExplain::getId, defaultId)
                .set(WordExplain::getIsDefault, true));
        if (!update) {
            throw new UpdateException("更新默认释义失败！");
        }
        libService.update(Wrappers.<Lib>lambdaUpdate()
                .eq(Lib::getId, libId)
                .set(Lib::getUpdateTime, new Date()));
        return true;
    }


    @Override
    public List<WordExplainInfo> getWordExplains(int wordId, int libId, int userId) {
        List<WordExplain> explains = wordMapper.getOtherLibExplains(wordId, libId);
        List<WordExplainInfo> res = new ArrayList<>(explains.size());
        WordExplain defaultExplain = explainService.getDefaultExplain(libId, wordId, userId);
        boolean defaultIsCustom = explainService.isCustomDefault(libId, wordId, defaultExplain.getId(), userId);
        explains.forEach(explain -> {
            boolean customDefault = explain.equals(defaultExplain) && defaultIsCustom;
            res.add(new WordExplainInfo(explain, customDefault));
        });
        return res;
    }

    @Override
    public PageInfo<WordInfo> search(int libId, String word, int pageNum, int pageSize, boolean random, int userId) {
        final Lib lib = libService.getById(libId);
        if (lib == null) {
            throw new IllegalDataException("该库不存在！");
        }
        if (!userLibService.hasLib(userId, libId)) {
            throw new NoPermissionException("你没有此库的访问权");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<WordInfo> list = null;
        if (word == null || StringUtils.isBlank(word)) {
            list = wordMapper.getWordInfo(libId, null, random);
        } else {
            list = wordMapper.getWordInfo(libId, word, random);
        }
        list.forEach(wordInfo -> {
            wordInfo.setExplain(explainService.getDefaultExplain(libId, wordInfo.getId(), userId));
        });
        return new PageInfo<>(list);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Integer> importSingle(ImportWord word) throws IOException {
        final Lib lib = libService.getById(word.getLibId());
        if (lib == null) {
            throw new CreateNewException("给定词库不存在，导入失败！");
        }
        final Word one = getOne(Wrappers.<Word>lambdaQuery().eq(Word::getWord, word.getWord()));
        List<Integer> ids = new ArrayList<>();
        boolean insertExplain = false;
        TranslationResult translation = TranslationResult.translate(word.getWord());
        //总词库里面没有该单词
        if (one == null) {
            //获取音标并插入
            word.setEnSymbol(translation.getEnSymbol());
            word.setUsSymbol(translation.getUsSymbol());
            word.setEnSymbolMp3(translation.getEnMp3());
            word.setUsSymbolMp3(translation.getUsMp3());
            if (!save(word)) {
                throw new CreateNewException("导入失败！");
            }
            ids.add(word.getId());
            insertExplain = true;
        } else {
            word.setId(one.getId());
            ids.add(word.getId());
            // 总词库里面有，判断公共词库有没有
            List<Integer> commonLibIds = libService.list(Wrappers
                            .<Lib>lambdaQuery()
                            .eq(Lib::getCommon, true))
                    .stream()
                    .map(Lib::getId)
                    .collect(Collectors.toList());
            int count = libWordService.count(Wrappers
                    .<LibWord>lambdaQuery()
                    .eq(LibWord::getWordId, one.getId())
                    .in(LibWord::getLibId, commonLibIds));
            if (count == 0) {
                //公共词库中没有该单词，需要插入释义
                insertExplain = true;
            }
        }
        if (insertExplain) {
            // 导入释义
            List<Explain> explainList = translation.getExplains();
            List<WordExplain> explains = new ArrayList<>(explainList.size());
            boolean first = true;
            for (Explain explain : explainList) {
                explains.add(new WordExplain(word.getId(), lib.getId(), explain, first));
                first = false;
            }
            if (!explainService.saveBatch(explains)) {
                throw new CreateNewException("释义导入失败！");
            }
            for (WordExplain explain : explains) {
                ids.add(explain.getId());
            }
        }
        //将词库与单词关联
        if (!libWordService.save(new LibWord(lib.getId(), word.getId()))) {
            throw new CreateNewException("关联词库导入失败！");
        }
        libService.update(Wrappers.<Lib>lambdaUpdate()
                .eq(Lib::getId, lib.getId())
                .set(Lib::getUpdateTime, new Date()));
        if (!ids.isEmpty()) {
            libService.update(Wrappers.<Lib>lambdaUpdate()
                    .eq(Lib::getId, lib.getId())
                    .set(Lib::getUpdateTime, new Date()));
        }
        return ids;
    }
}
