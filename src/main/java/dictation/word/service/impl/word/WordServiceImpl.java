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
    public boolean removeByLibId(int libId) {
        //删除库时删除对应的词库单词释义
        explainService.remove(Wrappers.<WordExplain>lambdaQuery()
                .eq(WordExplain::getLibId, libId));
        explainCustomService.remove(Wrappers.<WordExplainCustom>lambdaQuery()
                .eq(WordExplainCustom::getLibId, libId));
        return true;
    }

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
        WordExplainCustom custom = explainCustomService.getOne(Wrappers.<WordExplainCustom>lambdaQuery().eq(WordExplainCustom::getUserId, userId).eq(WordExplainCustom::getWordId, wordId).eq(WordExplainCustom::getLibId, libId));
        if (custom == null) {
            return explainCustomService.save(new WordExplainCustom(userId, wordId, libId, defaultId));
        } else {
            return explainCustomService.update(Wrappers.<WordExplainCustom>lambdaUpdate().eq(WordExplainCustom::getUserId, userId).eq(WordExplainCustom::getWordId, wordId).eq(WordExplainCustom::getLibId, libId).set(WordExplainCustom::getExpId, defaultId));
        }
    }

    private boolean changeLibDefaultExplain(int libId, int wordId, int defaultId) {
        boolean update = explainService.update(Wrappers.<WordExplain>lambdaUpdate().eq(WordExplain::getWordId, wordId).set(WordExplain::getIsDefault, false));
        if (!update) {
            throw new UpdateException("更新默认释义失败！");
        }
        update = explainService.update(Wrappers.<WordExplain>lambdaUpdate().eq(WordExplain::getWordId, wordId).eq(WordExplain::getId, defaultId).set(WordExplain::getIsDefault, true));
        if (!update) {
            throw new UpdateException("更新默认释义失败！");
        }
        libService.update(Wrappers.<Lib>lambdaUpdate().eq(Lib::getId, libId).set(Lib::getUpdateTime, new Date()));
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
        List<WordInfo> list = null;
        PageHelper.startPage(pageNum, pageSize);
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
    public boolean importSingle(ImportWord word,int userId) throws IOException {
        final Lib lib = libService.getById(word.getLibId());
        if (lib == null) {
            throw new CreateNewException("给定词库不存在，导入失败！");
        }
        if (!lib.getCreator().equals(userId)) {
            throw new NoPermissionException("只能导入到自己创建的词库中");
        }
        // 先获取是否存在，如果总词库中存在，则直接插入id
        final Word one = getOne(Wrappers.<Word>lambdaQuery().eq(Word::getWord, word.getWord()));
        if (one == null) {
            //数据库里面没有该单词
            //获取释义
            TranslationResult translation = TranslationResult.translate(word.getWord());
            //获取音标并插入
            word.setEnSymbol(translation.getEnSymbol());
            word.setUsSymbol(translation.getUsSymbol());
            word.setEnSymbolMp3(translation.getEnMp3());
            word.setUsSymbolMp3(translation.getUsMp3());
            if (!save(word)) {
                throw new CreateNewException("导入失败！");
            }
            // 导入释义
            List<Explain> explainList = translation.getExplains();
            List<WordExplain> explains = new ArrayList<>(explainList.size());
            boolean first = true;
            for (Explain explain : explainList) {
                //导入释义到本库
                explains.add(new WordExplain(word.getId(), lib.getId(), explain, first));
                //导入释义到总词库
                explains.add(new WordExplain(word.getId(), 1, explain, first));
                first = false;
            }
            if (!explainService.saveBatch(explains)) {
                throw new CreateNewException("释义导入失败！");
            }
            // 插入单词到总词库
            if (!libWordService.save(new LibWord(1, word.getId()))) {
                throw new CreateNewException("总词库导入失败！");
            }
            //更新总库时间
            libService.update(Wrappers.<Lib>lambdaUpdate().eq(Lib::getId, 1)
                    .set(Lib::getUpdateTime, new Date()));
        } else {
            //总库中存在，复制一份释义到本库
            List<WordExplain> explainList = explainService.list(Wrappers.<WordExplain>lambdaQuery()
                            .eq(WordExplain::getLibId, 1)
                            .eq(WordExplain::getWordId, one.getId()))
                    .stream()
                    //复制到本库
                    .peek(explain -> explain.setLibId(word.getLibId()))
                    .collect(Collectors.toList());
            // 保存
            if (!explainService.saveBatch(explainList)) {
                throw new CreateNewException("释义导入失败！");
            }
        }
        int newWordId = one == null ? word.getId() : one.getId();
        //将词库与单词关联
        if (!libWordService.save(new LibWord(lib.getId(), newWordId))) {
            throw new CreateNewException("关联词库导入失败！");
        }
        //更新本库时间
        libService.update(Wrappers.<Lib>lambdaUpdate().eq(Lib::getId, lib.getId())
                .set(Lib::getUpdateTime, new Date()));
        return true;
    }
}
