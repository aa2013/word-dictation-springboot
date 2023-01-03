package dictation.word.service.i.word;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import dictation.word.entity.word.ImportWord;
import dictation.word.entity.word.WordExplainInfo;
import dictation.word.entity.word.WordInfo;
import dictation.word.entity.word.tables.Word;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author ljh
 * @date 2022/12/14
 */
public interface WordService extends IService<Word> {

    /**
     * 导入单词
     *
     * @param word 待导入单词
     * @return id，第一个为单词id，后续为释义 id
     */
    List<Integer> importSingle(ImportWord word) throws IOException;

    Map<String,String> getCiBaSymbols(JSONObject data);

    PageInfo<WordExplainInfo> getOtherLibExplains(int wordId, int excludeLib, int pageNum, int pageSize);

    PageInfo<WordInfo> search(int libId, String word, int pageNum, int pageSize,boolean random);
}
