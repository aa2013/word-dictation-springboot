package dictation.word.service.i.word;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import dictation.word.entity.word.Explain;
import dictation.word.entity.word.tables.WordExplain;

import java.util.List;

/**
 * @author ljh
 * @date 2022/12/14
 */
public interface WordExplainService extends IService<WordExplain> {
    List<Explain> getExplains(JSONObject json);
    WordExplain getDefaultExplain(int libId,int wordId,int userId);

    boolean isCustomDefault(int libId, int wordId, Integer expId, int userId);
}
