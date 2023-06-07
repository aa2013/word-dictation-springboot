package dictation.word.controller;

import com.github.pagehelper.PageInfo;
import dictation.word.entity.word.ImportWord;
import dictation.word.entity.word.WordExplainInfo;
import dictation.word.entity.word.WordInfo;
import dictation.word.exception.IllegalDataException;
import dictation.word.service.i.word.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author ljh
 * @date 2022/12/14
 */
@RestController
@RequestMapping("/word")
@Validated
public class WordController extends BaseController {
    @Autowired
    WordService wordService;

    @PostMapping("/import/single")
    public List<Integer> importSingle(@Valid ImportWord word) throws IOException {
        return wordService.importSingle(word);
    }

    @GetMapping("/list")
    public PageInfo<WordInfo> getList(int libId, int pageNum, int pageSize) {
        return wordService.search(libId, null, pageNum, pageSize, false, getCurrentUserId());
    }

    @GetMapping("/randomList")
    public PageInfo<WordInfo> getRandomList(int libId, int size) {
        PageInfo<WordInfo> search = wordService.search(libId, null, 1, size, true, getCurrentUserId());
        if (search.getList().size() != size) {
            throw new IllegalDataException("数量不足");
        }
        return search;
    }

    @GetMapping("/search")
    public PageInfo<WordInfo> search(int libId, String word, int pageNum, int pageSize) {
        return wordService.search(libId, word, pageNum, pageSize, false, getCurrentUserId());
    }

    @GetMapping("/explains")
    public List<WordExplainInfo> getOtherLibExplains(int wordId, int libId) {
        return wordService.getWordExplains(wordId, libId, getCurrentUserId());
    }

    @GetMapping("/changeDefaultExplain")
    public boolean changeDefaultExplains(int libId, int wordId, int defaultId) {
        return wordService.changeDefaultExplains(libId,wordId, defaultId,getCurrentUserId());
    }
}
