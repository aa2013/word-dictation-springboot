package dictation.word.service.i.word;

import com.baomidou.mybatisplus.extension.service.IService;
import dictation.word.entity.word.tables.WordExplainCustom;

/**
 * @author ljh
 * @date 2022/12/14
 */
public interface WordExplainCustomService extends IService<WordExplainCustom> {
    WordExplainCustom getOne(int libId, int wordId, int userId);
}
