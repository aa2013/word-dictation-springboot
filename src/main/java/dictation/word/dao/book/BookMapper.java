package dictation.word.dao.book;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dictation.word.entity.book.Book;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Mapper
public interface BookMapper extends BaseMapper<Book> {
}
