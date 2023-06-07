package dictation.word.service.impl.user;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dictation.word.dao.user.UserMapper;
import dictation.word.entity.user.User;
import dictation.word.entity.user.UserDetail;
import dictation.word.service.i.user.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, UserDetailsService {
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
