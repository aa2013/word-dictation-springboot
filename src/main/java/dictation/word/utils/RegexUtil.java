package dictation.word.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ljh
 * 正则表达式工具类
 */
public class RegexUtil {
    /**
     * 判断是否为手机号
     *
     * @param phone 字符串类型的手机号
     *              传入手机号,判断后返回
     * @return true为手机号
     */
    public static boolean isPhone(String phone) {
        if (phone == null) {
            return false;
        }
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            return m.matches();
        }
    }

    /**
     * 判断是否为邮箱
     *
     * @param email 邮箱
     * @return true为邮箱
     */
    public static boolean isEmail(String email) {
        if (email == null) {
            return false;
        }
        String regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
