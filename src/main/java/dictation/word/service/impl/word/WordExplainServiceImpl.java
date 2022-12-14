package dictation.word.service.impl.word;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dictation.word.dao.word.WordExplainMapper;
import dictation.word.entity.word.WordExplain;
import dictation.word.service.i.user.TokenService;
import dictation.word.service.i.word.WordExplainService;
import org.springframework.stereotype.Service;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Service
public class WordExplainServiceImpl extends ServiceImpl<WordExplainMapper, WordExplain> implements WordExplainService, TokenService {
}
