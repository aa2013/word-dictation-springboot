package dictation.word.service.impl.user;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dictation.word.dao.user.UserMapper;
import dictation.word.entity.user.User;
import dictation.word.entity.user.UserDetail;
import dictation.word.exception.IllegalDataException;
import dictation.word.service.i.user.EmailService;
import dictation.word.service.i.user.UserService;
import dictation.word.utils.RegexUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, UserDetailsService {
    @Resource
    BCryptPasswordEncoder passwordEncoder;
    @Resource
    EmailService emailService;

    @Override
    public boolean sendForgetPwdCode(String email) {
        if (getUserByAccount(email) == null) {
            throw new IllegalDataException("用户不存在，请先注册");
        }
        return emailService.sendResetPwdCode(email);
    }

    @Override
    public boolean submitForgetPwdCode(String email, String code, String now, String again) {
        User user = getUserByAccount(email);
        if (user == null) {
            throw new IllegalDataException("用户不存在，请先注册");
        }
        if (!emailService.verifyResetPwdCode(email, code)) {
            throw new IllegalDataException("验证码错误");
        }
        if (now.length() == 0) {
            throw new IllegalDataException("密码不能为空");
        } else if (!now.equals(again)) {
            throw new IllegalDataException("两次密码不一致");
        }
        user.setPassword(passwordEncoder.encode(now));
        return updateById(user);
    }

    @Override
    public boolean register(String email, String pwd, String pwdAgain, String emailCode) {
        if (getUserByAccount(email) != null) {
            throw new IllegalDataException("该邮箱已注册，请直接登录");
        }
        if (!RegexUtil.isEmail(email)) {
            throw new IllegalDataException("邮箱格式不正确");
        }
        if (pwd.length() == 0) {
            throw new IllegalDataException("密码不能为空");
        } else if (!pwd.equals(pwdAgain)) {
            throw new IllegalDataException("两次密码不一致");
        }
        if (!emailService.verifyRegisterCode(email, emailCode)) {
            throw new IllegalDataException("验证码错误");
        }
        int cnt = count() - 1;
        User user = new User(0, email, "注册用户" + cnt, passwordEncoder.encode(pwd));
        return save(user);

    }

    @Override
    public User getUserByAccount(String account) {
        return getOne(Wrappers
                .<User>lambdaQuery()
                .eq(User::getAccount, account)
        );
    }


    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        UserDetail user = new UserDetail(getUserByAccount(account));
        user.setAuthorities(getAuthorities(user.getId()));
        return user;
    }

    private List<GrantedAuthority> getAuthorities(int userId) {
        System.out.println(userId);
        List<String> roleNames = new ArrayList<>();
        return AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",", roleNames));
    }
}
