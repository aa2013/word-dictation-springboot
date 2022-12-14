package dictation.word.service.impl.book;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dictation.word.dao.book.BookMapper;
import dictation.word.entity.book.Book;
import dictation.word.service.i.book.BookService;
import dictation.word.service.i.user.TokenService;
import org.springframework.stereotype.Service;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book>implements BookService, TokenService {
}
