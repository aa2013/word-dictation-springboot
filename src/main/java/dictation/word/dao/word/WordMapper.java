package dictation.word.dao.word;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dictation.word.entity.word.Explain;
import dictation.word.entity.word.WordInfo;
import dictation.word.entity.word.tables.Word;
import dictation.word.entity.word.tables.WordExplain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Mapper
public interface WordMapper extends BaseMapper<Word> {
    List<WordInfo> getWordInfo(@Param("libId") int libId, @Param("word") String word, @Param("random") boolean random);

    List<WordInfo> getAllWordInfo(@Param("word") String word, @Param("random") boolean random);

    List<Explain> getDefaultWordExplainByLib(@Param("wordId") int wordId, @Param("libId") int libId);

    List<WordExplain> getOtherLibExplains(@Param("wordId") int wordId, @Param("libId") int libId);
}
