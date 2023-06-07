package dictation.word.controller;

import com.alibaba.fastjson.JSONObject;
import dictation.word.entity.user.User;
import dictation.word.utils.JwtUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class BaseController {


    /**
     * 获取当前的 token
     *
     * @return token
     */
    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
    }

    /**
     * 获取当前的 token
     *
     * @return token
     */
    public JSONObject getCurrentToken() {
        HttpServletRequest request = getRequest();
        final String token = request.getHeader(JwtUtils.getHeader());
        return JwtUtils.getAccountByToken(token);
    }

    /**
     * 获取当前的 用户 id
     *
     * @return userId
     */
    public int getCurrentUserId() {
        final JSONObject token = getCurrentToken();
        return token.getInteger("uid");
    }

    /**
     * 获取当前的 用户
     *
     * @return userId
     */
    public User getCurrentUser() {
        final JSONObject token = getCurrentToken();
        return token.getObject("details", User.class);
    }

}
