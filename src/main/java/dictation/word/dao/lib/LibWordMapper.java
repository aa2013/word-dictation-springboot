package dictation.word.dao.lib;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dictation.word.entity.lib.tables.LibWord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Mapper
public interface LibWordMapper extends BaseMapper<LibWord> {
    int getLibWordCnt(@Param("libId") int libId);
}
