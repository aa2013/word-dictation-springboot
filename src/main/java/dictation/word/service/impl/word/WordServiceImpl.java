package dictation.word.service.impl.word;

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
import dictation.word.utils.NetUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static dictation.word.entity.word.tables.Word.JINSAN;

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
    public String[] parseSymbols(String word) throws IOException {
        String resp = NetUtil.get(JINSAN + word);
        Document document = Jsoup.parse(resp);
        Elements symbolUl = document.getElementsByClass("Mean_symbols__fpCmS");
        return parseSymbols(symbolUl);
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
    public PageInfo<WordInfo> getList(int libId, int pageNum, int pageSize) {
        final Lib lib = libService.getById(libId);
        if (lib == null) {
            throw new IllegalDataException("该库不存在！");
        }
        if (!lib.getCommon() && lib.getCreator() != getCurrentUserId()) {
            throw new NoPermissionException("这不是你的私有库");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<WordInfo> list = wordMapper.getWordInfo(libId);
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
        if (one == null) {
            //总词库里面没有该单词
            //获取音标并插入
            String[] symbols = parseSymbols(word.getWord());
            word.setEnSymbol(symbols[0]);
            word.setUsSymbol(symbols[1]);
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
        List<Explain> explainList = explainService.getExplains(word.getWord());
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
