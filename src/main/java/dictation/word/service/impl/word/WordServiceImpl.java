package dictation.word.service.impl.word;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dictation.word.dao.word.WordMapper;
import dictation.word.entity.word.Word;
import dictation.word.service.i.user.TokenService;
import dictation.word.service.i.word.WordService;
import org.springframework.stereotype.Service;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Service
public class WordServiceImpl extends ServiceImpl<WordMapper, Word> implements WordService, TokenService {
}
