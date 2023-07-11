package dictation.word.service.impl.user;

import dictation.word.exception.EmailFormatException;
import dictation.word.service.i.user.EmailService;
import dictation.word.utils.RandomUtil;
import dictation.word.utils.RegexUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Service
public class EmailServiceImpl implements EmailService {
    @Resource
    StringRedisTemplate redis;
    public static final String EMAIL_BINDING_REDIS_KEY_PREFIX = "code-email-binding-";
    public static final String EMAIL_RESET_PWD_REDIS_KEY_PREFIX = "code-email-reset-";
    public static final String EMAIL_REGISTER_REDIS_KEY_PREFIX = "code-email-register-";
    public static final int EXPIRE_TIME = 5 * 60;
    @Value("${custom.email.sender}")
    private String sender;
    @Resource
    JavaMailSender mailSender;

    @Override
    public boolean sendRegisterCode(String email) {
        int[] ints = RandomUtil.getInts(0, 9, 6);
        String code = StringUtils.join(Arrays.stream(ints).boxed().toArray(Integer[]::new), "");
        String key = EMAIL_REGISTER_REDIS_KEY_PREFIX + email;
        return sendEmail(email, "单词本注册验证码", key, code);
    }

    @Override
    public boolean sendBindingCode(String receiver) {
        int[] ints = RandomUtil.getInts(0, 9, 6);
        String code = StringUtils.join(Arrays.stream(ints).boxed().toArray(Integer[]::new), "");
        String key = EMAIL_BINDING_REDIS_KEY_PREFIX + receiver;
        return sendEmail(receiver, "邮箱绑定验证码", key, code);
    }

    @Override
    public boolean sendResetPwdCode(String receiver) {
        int[] ints = RandomUtil.getInts(0, 9, 6);
        String code = StringUtils.join(Arrays.stream(ints).boxed().toArray(Integer[]::new), "");
        String key = EMAIL_RESET_PWD_REDIS_KEY_PREFIX + receiver;
        return sendEmail(receiver, "密码找回验证码", key, code);
    }

    public boolean verifyBindingCode(String email, String code) {
        String key = EMAIL_BINDING_REDIS_KEY_PREFIX + email;
        return verifyCode(key, code);
    }

    @Override
    public boolean verifyRegisterCode(String email, String emailCode) {
        return verifyCode(EMAIL_REGISTER_REDIS_KEY_PREFIX + email, emailCode);
    }

    @Override
    public boolean verifyResetPwdCode(String email, String code) {
        String key = EMAIL_RESET_PWD_REDIS_KEY_PREFIX + email;
        return verifyCode(key, code);
    }

    private boolean deleteCode(String key) {
        return Boolean.TRUE.equals(redis.delete(key));
    }

    public boolean verifyCode(String key, String code) {
        if (!exists(key)) return false;
        boolean verify = code.equals(redis.opsForValue().get(key));
        if (verify) {
            deleteCode(key);
        }
        return verify;
    }

    public boolean sendEmail(String toEmail, String subject, String key, String code) {
        boolean send;
        if (over1min(key)) {
            send = sendEmail(toEmail, subject, "你的验证码是：" + code + "。五分钟内有效。");
            if (send) {
                redis.opsForValue().set(key, code, EXPIRE_TIME, TimeUnit.SECONDS);
            }
        } else {
            String oldCode = redis.opsForValue().get(key);
            return sendEmail(toEmail, subject, "你的验证码是：" + oldCode + "。五分钟内有效。");
        }
        return send;
    }

    private boolean over1min(String key) {
        if (!exists(key)) return true;
        //单位秒
        Long expire = redis.opsForValue().getOperations().getExpire(key);
        if (expire == null) return true;
        return EXPIRE_TIME - expire > 60;
    }

    private boolean exists(String key) {
        final Boolean hasKey = redis.hasKey(key);
        return hasKey != null && hasKey;
    }

    public boolean sendEmail(String toEmail, String subject, String text) {
        if (!RegexUtil.isEmail(toEmail)) {
            throw new EmailFormatException("邮箱格式错误");
        }
        SimpleMailMessage message = new SimpleMailMessage();
        //发送者邮箱
        message.setFrom(sender);
        message.setTo(toEmail);
        //标题
        message.setSubject(subject);
        //正文
        message.setText(text);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
