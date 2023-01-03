package dictation.word.service.impl.word;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import dictation.word.dao.word.WordMapper;
import dictation.word.entity.lib.tables.Lib;
import dictation.word.entity.lib.tables.LibWord;
import dictation.word.entity.word.Explain;
import dictation.word.entity.word.ImportWord;
import dictation.word.entity.word.WordExplainInfo;
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
import dictation.word.utils.TranslationUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

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
    public Map<String, String> getCiBaSymbols(JSONObject data) {
        Map<String, String> map = new HashMap<>();
        map.put("us", data.getString("ph_am"));
        map.put("en", data.getString("ph_en"));
        map.put("us-mp3", data.getString("ph_am_mp3"));
        map.put("en-mp3", data.getString("ph_en_mp3"));
        return map;
    }

    public String[] parseSymbols(Elements es) {
        String[] symbols = new String[2];
        Element parent = es.get(0);
        Elements children = parent.children();
        int i = 0;
        for (Element e : children) {
            String symbol = e.text().replaceAll("英|美", "");
            symbols[i++] = symbol;
        }
        return symbols;
    }

    @Override
    public PageInfo<WordExplainInfo> getOtherLibExplains(int wordId, int excludeLib, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<WordExplainInfo> list = wordMapper.getOtherLibExplains(wordId, excludeLib);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<WordInfo> search(int libId, String word, int pageNum, int pageSize,boolean random) {
        final Lib lib = libService.getById(libId);
        if (lib == null) {
            throw new IllegalDataException("该库不存在！");
        }
        if (!lib.getCommon() && lib.getCreator() != getCurrentUserId()) {
            throw new NoPermissionException("这不是你的私有库");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<WordInfo> list = null;
        if (word == null || StringUtils.isBlank(word)) {
            list = wordMapper.getWordInfo(libId, null,random);
        } else {
            list = wordMapper.getWordInfo(libId, word,random);
        }
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
        JSONObject ciBa = TranslationUtil.getCiBa(word.getWord());
        if (one == null) {
            //总词库里面没有该单词
            //获取音标并插入
            Map<String, String> symbols = getCiBaSymbols(ciBa);
            word.setEnSymbol(symbols.get("en"));
            word.setUsSymbol(symbols.get("us"));
            word.setEnSymbolMp3(symbols.get("en-mp3"));
            word.setUsSymbolMp3(symbols.get("us-mp3"));
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
        libService.update(Wrappers.<Lib>lambdaUpdate()
                .eq(Lib::getId, lib.getId())
                .set(Lib::getUpdateTime, new Date()));
        // 导入释义
        List<Explain> explainList = explainService.getExplains(ciBa);
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
        return ids;
    }
}
