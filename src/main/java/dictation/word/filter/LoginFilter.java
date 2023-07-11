package dictation.word.filter;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import dictation.word.entity.response.ResultBean;
import dictation.word.entity.response.ResultCode;
import dictation.word.entity.user.UserDetail;
import dictation.word.utils.JwtUtils;
import dictation.word.utils.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录过滤器
 *
 * @author ljh
 */
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final StringRedisTemplate redisTemplate;

    /**
     * 登陆拦截请求
     */
    public LoginFilter(AuthenticationManager manager, StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.authenticationManager = manager;
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/user/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            // 从请求中获取账号密码，通过 UserServiceImpl 中的 loadUserByUsername 进行对比认证
            String account = request.getParameter("account");
            account = account != null ? account.trim() : "";
            String password = request.getParameter("password");
            password = password != null ? password.trim().replace(" ", "+") : "";
            //解密与数据库比对
            account = RSAUtil.decrypt(account);
            password = RSAUtil.decrypt(password);
            UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(account, password);
            this.setDetails(request, authRequest);
            return this.authenticationManager.authenticate(authRequest);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // 认证通过处理
        boolean remember = Boolean.parseBoolean(request.getParameter("remember"));
        UserDetail userDetail = (UserDetail) authResult.getPrincipal();
        String auth = authResult.getAuthorities().toString();
        // 构建 token subject
        JSONObject account = new JSONObject();
        account.put("uid", userDetail.getId());
        account.put("auth", auth);
        account.put("details", userDetail);
        String token = JwtUtils.generateToken(account, remember);
        SecurityContextHolder.getContext().setAuthentication(authResult);
        response.setHeader(JwtUtils.getHeader(), token);
        response.setHeader("Access-control-Expose-Headers", JwtUtils.getHeader());
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        Integer userId = userDetail.getId();
        Map<String, Object> res = new HashMap<>(4);
        res.put("userId", userId);
        res.put("account", userDetail.getAccount());
        res.put("userName", userDetail.getUserName());
        res.put("auth", authResult.getAuthorities().toString());
        outputStream.write(JSONUtil.toJsonStr(ResultBean.suc(res)).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
        System.out.println("认证成功 " + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // 认证失败处理
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType("application/json;charset=UTF-8");
        outputStream.write(JSONUtil.toJsonStr(ResultBean.fail(ResultCode.LOGIN_FAILED)).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
        System.out.println("认证失败");
    }
}
