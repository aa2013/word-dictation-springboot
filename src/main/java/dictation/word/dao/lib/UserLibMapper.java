package dictation.word.dao.lib;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dictation.word.entity.lib.tables.UserLib;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserLibMapper extends BaseMapper<UserLib> {
    int getLibUseCnt(@Param("libId") int libId);
}
