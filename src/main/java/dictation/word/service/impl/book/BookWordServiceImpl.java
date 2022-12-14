package dictation.word.service.impl.book;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dictation.word.dao.book.BookWordMapper;
import dictation.word.entity.book.BookWord;
import dictation.word.service.i.book.BookWordService;
import dictation.word.service.i.user.TokenService;
import org.springframework.stereotype.Service;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Service
public class BookWordServiceImpl extends ServiceImpl<BookWordMapper, BookWord> implements BookWordService, TokenService {
}
