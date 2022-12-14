package dictation.word.dao.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dictation.word.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
