package dictation.word.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dictation.word.exception.NoPermissionException;
import dictation.word.exception.TokenExpireException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class JwtUtils implements InitializingBean {
    @Autowired
    private StringRedisTemplate temp;
    /**
     * 30天过期
     */
    public static long LONG_EXPIRE_TIME = 30 * 24 * 60 * 60;
    /**
     * 7天过期
     */
    public static long SHORT_EXPIRE_TIME = 7 * 24 * 60 * 60;
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
     * @param account 账户，这里实际上是填你想在 token保存的内容，我写成的是jsonObject对象（fastjson库），也可以用HashMap之类的随便你
     * @return 返回 token
     */
    public static String generateToken(JSONObject account, boolean remember, Long time, boolean useRedis) {
        //subject即为主题，就是我们要存在token中的内容，先转为json字符串
        String subject = account.toJSONString();
        //获取当前时间，用于构造token过期时间
        Date nowDate = new Date();
        long expireTime = time == null ? (remember ? LONG_EXPIRE_TIME : SHORT_EXPIRE_TIME) : time;
        log.info("expireTime:{}", expireTime);
        //过期时间为当前日期+持续时长，java里面是存储的毫秒，所以需要把持续时长乘1000
        Date expireDate = new Date(nowDate.getTime() + 1000 * expireTime);
        //调用jwt构造器来构造token
        String token = Jwts.builder()
                //设置你的主题（也就是你要存在里面的信息）
                .setSubject(subject)
                //设置token发布时间（其实就是开始时间）
                .setIssuedAt(nowDate)
                //设置过期时间
                .setExpiration(expireDate)
                //使用HS512进行签名加密
                .signWith(SignatureAlgorithm.HS512, SECRET)
                //这一步执行后就构造成功了
                .compact();
        //构造成功后将它存入redis数据里面，你存别的地方也可以，其实不存也可以，反正前端请求携带就行
        //参数介绍（从左至右）：key，value，时长（过期倒计时），时长单位（你前面写的时长的单位，我这里是秒）
        if (useRedis) {
            redis.opsForValue().set(token, subject, expireTime, TimeUnit.SECONDS);
        }
        //返回给调用者
        return token;
    }

    public static String generateToken(JSONObject account, boolean remember, boolean useRedis) {
        return generateToken(account, remember, null, useRedis);
    }

    public static String generateToken(JSONObject account, boolean remember) {
        return generateToken(account, remember, null, true);
    }

    public static String generateToken(JSONObject account, long time, boolean useRedis) {
        return generateToken(account, false, time, useRedis);
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
        Claims claim = getClaimByToken(token);
        assert claim != null;
        Date issue = claim.getIssuedAt();
        Date expiration = claim.getExpiration();
        if (expiration.getTime() - issue.getTime() > SHORT_EXPIRE_TIME * 1000) {
            generateToken(account, true, null, true);
        }
        return generateToken(account, false, null, true);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        redis = temp;
    }
}
