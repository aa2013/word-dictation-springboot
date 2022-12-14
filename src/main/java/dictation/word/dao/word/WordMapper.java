package dictation.word.dao.word;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dictation.word.entity.word.Word;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Mapper
public interface WordMapper extends BaseMapper<Word> {
}
