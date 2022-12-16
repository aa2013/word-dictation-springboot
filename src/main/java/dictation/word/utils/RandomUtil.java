package dictation.word.utils;

import java.util.Random;
import java.util.stream.IntStream;

/**
 * @author ljh
 */
public class RandomUtil {
    /**
     * 获取一个随机整数
     *
     * @param min 开始数字（包含）
     * @param max 结束数字（包含）
     */
    public static int getInt(int min, int max) {
        if (min > max) {
            int t = min;
            min = max;
            max = t;
        }
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * 获取多个随机整数
     *
     * @param min 开始数字（包含）
     * @param max 结束数字（包含）
     * @param n   生成个数
     */
    public static int[] getInts(int min, int max, int n) {
        if (min > max) {
            int t = min;
            min = max;
            max = t;
        }
        Random random = new Random();
        final IntStream ints = random.ints(min, max + 1);
        return ints.limit(n).toArray();
    }

    /**
     * 获取多个随机整数
     *
     * @param n 生成个长度
     */
    public static String getRandomString(int n) {
        char[] chars = new char[n];
        final int[] ints = getInts(0, 26, n);
        for (int i = 0; i < n; i++) {
            chars[i] = (char) ('a' + ints[i]);
        }
        return new String(chars);
    }
}
