package dictation.word.service.i.user;

import com.alibaba.fastjson.JSONObject;
import dictation.word.entity.user.User;
import dictation.word.utils.JwtUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @author ljh
 * @date 2022/7/19
 */

public interface TokenService {
    /**
     * 获取当前的 token
     *
     * @return token
     */
    default JSONObject getCurrentToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        final String token = request.getHeader(JwtUtils.getHeader());
        return JwtUtils.getAccountByToken(token);
    }

    /**
     * 获取当前的 用户 id
     *
     * @return userId
     */
    default int getCurrentUserId() {
        final JSONObject token = getCurrentToken();
        return token.getInteger("uid");
    }

    /**
     * 获取当前的 用户 信息
     *
     * @return user
     */
    default User getCurrentUser() {
        final JSONObject token = getCurrentToken();
        return token.getObject("details", User.class);
    }

    /**
     * 获取当前的 用户 信息
     *
     * @return user
     */
    default List<String> getCurrentUserRoleNames() {
        final JSONObject token = getCurrentToken();
        String auth = token.getString("auth");
        auth = auth.replace(" ", "");
        auth = auth.substring(1, auth.length() - 1);
        return Arrays.asList(auth.split(","));
    }

}
