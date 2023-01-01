package dictation.word.controller;

import com.github.pagehelper.PageInfo;
import dictation.word.entity.word.ImportWord;
import dictation.word.entity.word.WordExplainInfo;
import dictation.word.entity.word.WordInfo;
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
public class WordController {
    @Autowired
    WordService wordService;

    @PostMapping("/import/single")
    public List<Integer> importSingle(@Valid ImportWord word) throws IOException {
        return wordService.importSingle(word);
    }

    @GetMapping("/list")
    public PageInfo<WordInfo> getList(int libId, int pageNum, int pageSize) {
        return wordService.getList(libId, pageNum, pageSize);
    }

    @GetMapping("/otherExplains")
    public PageInfo<WordExplainInfo> getOtherLibExplains(int wordId, int libId, int pageNum, int pageSize) {
        return wordService.getOtherLibExplains(wordId, libId, pageNum, pageSize);
    }
}
