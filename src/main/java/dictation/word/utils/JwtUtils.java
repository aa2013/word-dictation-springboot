package dictation.word.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dictation.word.exception.NoPermissionException;
import dictation.word.exception.TokenExpireException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author ljh
 */
@Data
@Component
public class JwtUtils implements InitializingBean {
    @Autowired
    private StringRedisTemplate temp;
    /**
     * 7天过期
     */
    public static long LONG_EXPIRE_TIME = 7 * 24 * 60 * 60;
    /**
     * 一小时过期
     */
    public static long SHORT_EXPIRE_TIME = 60 * 60;
    private static final String SECRET = "ji8n3439n439n43ld9ne9343fdfer49h";
    /**
     * token的 请求头名称
     */
    private static final String HEADER = "Authorization";
    private static StringRedisTemplate redis;

    public static String getHeader() {
        return HEADER;
    }

    public static void logout(String token) {
        redis.delete(token);
    }

    /**
     * 创建 token
     *
     * @param account 账户，这里填你想在token保存的内容
     * @return 返回 token
     */
    public static String generateToken(JSONObject account) {
        String subject = account.toJSONString();
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + 1000 * LONG_EXPIRE_TIME);
        String token = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        redis.opsForValue().set(token, subject, LONG_EXPIRE_TIME, TimeUnit.SECONDS);
        return token;
    }


    public static Claims getClaimByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从token中获取保存的信息
     */
    public static JSONObject getAccountByToken(String token) {
        Claims claims = getClaimByToken(token);
        if (claims == null) {
            throw new TokenExpireException("登录失效");
        }
        final String subject = claims.getSubject();
        return JSON.parseObject(subject);
    }

    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }

    }

    public static String refreshToken(String token) {
        JSONObject account = JSONObject.parseObject(redis.opsForValue().get(token));
        if (account == null) {
            throw new NoPermissionException("没有权限");
        }
        redis.delete(token);
        return generateToken(account);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        redis = temp;
    }
}
