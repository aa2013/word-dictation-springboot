package dictation.word.service.i.user;

import com.baomidou.mybatisplus.extension.service.IService;
import dictation.word.entity.user.User;

/**
 * @author ljh
 * @date 2022/12/14
 */
public interface UserService extends IService<User> {
    User getUserByAccount(String account);

    boolean register(String email, String pwd, String pwdAgain, String emailCode);

    boolean updatePwd(String old, String now, String again, int userId);

    boolean sendForgetPwdCode(String email);

    boolean submitForgetPwdCode(String email, String code, String now, String again);
}
