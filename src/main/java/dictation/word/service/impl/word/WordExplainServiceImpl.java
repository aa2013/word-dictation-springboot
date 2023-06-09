package dictation.word.service.impl.word;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dictation.word.dao.word.WordExplainMapper;
import dictation.word.entity.word.Explain;
import dictation.word.entity.word.tables.WordExplain;
import dictation.word.entity.word.tables.WordExplainCustom;
import dictation.word.service.i.word.WordExplainCustomService;
import dictation.word.service.i.word.WordExplainService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Service
public class WordExplainServiceImpl extends ServiceImpl<WordExplainMapper, WordExplain> implements WordExplainService {
    @Resource
    WordExplainCustomService explainCustomService;

    @Override
    public boolean isCustomDefault(int libId, int wordId, Integer expId, int userId) {
        return explainCustomService.count(Wrappers.<WordExplainCustom>lambdaQuery()
                .eq(WordExplainCustom::getLibId, libId)
                .eq(WordExplainCustom::getWordId, wordId)
                .eq(WordExplainCustom::getExpId, expId)
                .eq(WordExplainCustom::getUserId, userId)
        ) == 1;
    }

    @Override
    public WordExplain getDefaultExplain(int libId, int wordId, int userId) {
        //先获取自己的默认释义
        WordExplainCustom custom = explainCustomService.getOne(libId, wordId, userId);
        if (custom != null) {
            return getById(custom.getExpId());
        }
        //没有设置自己的默认释义，寻找库里的默认释义
        return getOne(Wrappers.<WordExplain>lambdaQuery()
                .eq(WordExplain::getLibId, libId)
                .eq(WordExplain::getIsDefault, true)
                .eq(WordExplain::getWordId, wordId));
    }


}
