package dictation.word.dao.word;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dictation.word.entity.word.WordInfo;
import dictation.word.entity.word.tables.Word;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Mapper
public interface WordMapper extends BaseMapper<Word> {
    List<WordInfo> getWordInfo(@Param("libId") int libId);
}
