package dictation.word.service.impl.word;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dictation.word.dao.word.WordExplainCustomMapper;
import dictation.word.entity.word.tables.WordExplainCustom;
import dictation.word.service.i.word.WordExplainCustomService;
import org.springframework.stereotype.Service;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Service
public class WordExplainCustomServiceImpl extends ServiceImpl<WordExplainCustomMapper, WordExplainCustom> implements WordExplainCustomService {

    @Override
    public WordExplainCustom getOne(int libId, int wordId, int userId) {
        return getOne(Wrappers.<WordExplainCustom>lambdaQuery()
                .eq(WordExplainCustom::getLibId, libId)
                .eq(WordExplainCustom::getWordId, wordId)
                .eq(WordExplainCustom::getUserId, userId));
    }
}
