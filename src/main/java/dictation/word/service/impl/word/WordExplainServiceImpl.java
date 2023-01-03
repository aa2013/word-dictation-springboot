package dictation.word.service.impl.word;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dictation.word.dao.word.WordExplainMapper;
import dictation.word.entity.word.Explain;
import dictation.word.entity.word.tables.WordExplain;
import dictation.word.service.i.user.TokenService;
import dictation.word.service.i.word.WordExplainService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Service
public class WordExplainServiceImpl extends ServiceImpl<WordExplainMapper, WordExplain> implements WordExplainService, TokenService {

    @Override
    public List<Explain> getExplains(JSONObject json) {
        List<Explain> res = new ArrayList<>();
        JSONArray parts = json.getJSONArray("parts");
        for (int i = 0; i < parts.size(); i++) {
            JSONObject part = parts.getJSONObject(i);
            String type = part.getString("part");
            JSONArray means = part.getJSONArray("means");
            for (int j = 0; j < means.size(); j++) {
                res.add(new Explain(means.getString(j), type));
            }
        }
        // 以短释义优先
        res.sort(Comparator.comparingInt(a -> a.getExplanation().length()));
        return res;
    }

}
