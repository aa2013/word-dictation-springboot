package dictation.word.filter;

import com.alibaba.fastjson.JSONObject;
import dictation.word.exception.TokenExpireException;
import dictation.word.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ljh
 * 身份验证过滤器
 */
@Slf4j
public class AuthenticationFilter extends BasicAuthenticationFilter {

    private final StringRedisTemplate redisTemplate;

    public AuthenticationFilter(AuthenticationManager authenticationManager, StringRedisTemplate redisTemplate) {
        super(authenticationManager);
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(request);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(JwtUtils.getHeader());
        if (token == null) {
            throw new AccessDeniedException("没有权限");
        }
        if (JwtUtils.isTokenExpired(token)) {
            token = JwtUtils.refreshToken(token);
        }
        JSONObject subject = JwtUtils.getAccountByToken(token);
        String info = redisTemplate.opsForValue().get(token);

        if (info == null) {
            redisTemplate.delete(token);
            throw new TokenExpireException("登录失效");
        }

        String auth = subject.getString("auth")
                .replace("[", "")
                .replace("]", "");

        return new UsernamePasswordAuthenticationToken(
                subject,
                token,
                AuthorityUtils.commaSeparatedStringToAuthorityList(auth)
        );
    }
}
