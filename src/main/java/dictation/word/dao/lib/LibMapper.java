package dictation.word.dao.lib;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dictation.word.entity.lib.LibInfo;
import dictation.word.entity.lib.tables.Lib;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Mapper
public interface LibMapper extends BaseMapper<Lib> {
    List<LibInfo> getLibList(@Param("userId") Integer userId);
}
