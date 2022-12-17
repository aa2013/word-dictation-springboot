package dictation.word.service.i.word;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import dictation.word.entity.word.ImportWord;
import dictation.word.entity.word.WordInfo;
import dictation.word.entity.word.tables.Word;

import java.util.List;

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
    List<Integer> importSingle(ImportWord word);

    PageInfo<WordInfo> getList(int libId, int pageNum, int pageSize);
}
