package dictation.word.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class RSAUtil {
    public static final byte[] PRI = new byte[]{77, 73, 73, 67, 101, 65, 73, 66, 65, 68, 65, 78, 66, 103, 107, 113, 104, 107, 105, 71, 57, 119, 48, 66, 65, 81, 69, 70, 65, 65, 83, 67, 65, 109, 73, 119, 103, 103, 74, 101, 65, 103, 69, 65, 65, 111, 71, 66, 65, 77, 68, 113, 100, 68, 54, 79, 56, 119, 49, 110, 106, 65, 52, 115, 66, 118, 116, 80, 103, 66, 117, 108, 106, 113, 85, 49, 66, 50, 90, 119, 89, 86, 79, 98, 90, 121, 54, 66, 75, 113, 43, 70, 67, 48, 89, 105, 77, 70, 99, 81, 71, 86, 57, 105, 87, 84, 113, 108, 107, 43, 66, 74, 47, 79, 81, 84, 112, 56, 113, 85, 107, 70, 67, 103, 118, 104, 83, 49, 98, 68, 100, 70, 69, 53, 119, 122, 67, 84, 71, 73, 55, 53, 70, 122, 119, 71, 73, 65, 113, 106, 109, 103, 83, 56, 65, 66, 106, 78, 101, 97, 116, 81, 73, 88, 105, 104, 112, 99, 89, 49, 51, 51, 106, 80, 47, 116, 65, 72, 52, 114, 50, 103, 72, 82, 103, 86, 88, 86, 78, 114, 121, 55, 71, 80, 100, 85, 53, 116, 98, 102, 75, 48, 100, 78, 102, 82, 73, 90, 77, 86, 114, 90, 79, 101, 108, 57, 117, 55, 101, 78, 65, 103, 77, 66, 65, 65, 69, 67, 103, 89, 69, 65, 111, 103, 103, 75, 76, 77, 85, 97, 74, 101, 99, 84, 118, 111, 43, 67, 75, 98, 67, 68, 100, 68, 85, 103, 48, 50, 102, 118, 50, 68, 50, 74, 115, 90, 82, 48, 72, 74, 53, 71, 119, 80, 100, 112, 71, 98, 88, 55, 106, 104, 83, 113, 105, 117, 47, 113, 55, 66, 82, 73, 87, 80, 107, 114, 51, 70, 69, 90, 47, 69, 43, 57, 121, 115, 114, 72, 47, 70, 76, 74, 71, 114, 55, 57, 69, 82, 53, 54, 98, 114, 101, 118, 53, 112, 99, 71, 114, 80, 109, 101, 117, 108, 47, 71, 70, 54, 74, 115, 88, 71, 72, 82, 65, 80, 54, 69, 114, 43, 86, 66, 88, 120, 105, 97, 51, 81, 103, 81, 43, 51, 98, 90, 68, 89, 105, 69, 105, 81, 106, 54, 86, 77, 118, 111, 86, 99, 112, 101, 72, 74, 52, 72, 106, 104, 73, 106, 85, 51, 90, 47, 100, 54, 54, 88, 87, 111, 47, 56, 100, 83, 69, 67, 81, 81, 68, 105, 72, 68, 118, 82, 89, 51, 54, 107, 75, 56, 49, 69, 72, 71, 99, 105, 119, 107, 80, 47, 116, 119, 51, 107, 78, 48, 97, 68, 50, 122, 73, 89, 57, 75, 78, 102, 106, 97, 110, 66, 98, 104, 87, 89, 57, 52, 56, 52, 47, 80, 84, 122, 88, 50, 89, 50, 70, 75, 68, 72, 80, 102, 107, 104, 54, 108, 71, 48, 105, 122, 122, 98, 51, 110, 57, 98, 104, 120, 85, 66, 68, 70, 47, 70, 65, 107, 69, 65, 50, 109, 114, 107, 82, 70, 65, 90, 49, 69, 82, 104, 66, 78, 105, 121, 47, 82, 79, 110, 55, 52, 101, 105, 70, 110, 85, 87, 102, 120, 89, 113, 98, 103, 83, 106, 111, 48, 97, 81, 101, 48, 82, 86, 104, 65, 48, 75, 119, 70, 114, 105, 43, 90, 99, 43, 70, 87, 65, 85, 101, 48, 55, 113, 107, 76, 115, 57, 112, 101, 120, 76, 117, 80, 51, 110, 57, 47, 114, 49, 75, 67, 118, 116, 75, 81, 74, 66, 65, 73, 97, 88, 114, 100, 102, 72, 78, 79, 81, 43, 109, 112, 73, 51, 111, 103, 68, 106, 121, 74, 120, 88, 57, 55, 72, 111, 89, 86, 89, 116, 122, 86, 86, 106, 107, 106, 104, 88, 50, 68, 117, 109, 55, 114, 52, 43, 90, 111, 102, 83, 115, 51, 67, 72, 85, 103, 49, 82, 72, 66, 57, 100, 117, 89, 88, 66, 117, 70, 56, 84, 72, 117, 102, 50, 79, 114, 78, 100, 53, 108, 102, 89, 97, 77, 48, 67, 81, 70, 98, 113, 56, 113, 117, 56, 116, 73, 113, 107, 75, 51, 101, 105, 82, 74, 103, 120, 109, 107, 76, 88, 49, 115, 106, 90, 68, 51, 114, 68, 100, 49, 56, 81, 117, 89, 51, 103, 74, 116, 55, 109, 56, 113, 68, 54, 68, 54, 48, 56, 102, 100, 97, 86, 79, 84, 43, 73, 88, 74, 52, 49, 97, 87, 76, 118, 73, 68, 108, 72, 76, 49, 115, 79, 114, 54, 69, 102, 88, 83, 47, 47, 83, 119, 69, 67, 81, 81, 67, 48, 65, 67, 79, 80, 80, 65, 100, 102, 54, 81, 55, 48, 66, 97, 85, 51, 73, 57, 116, 89, 121, 71, 80, 100, 90, 51, 68, 115, 118, 104, 108, 86, 53, 47, 73, 84, 57, 87, 52, 114, 121, 78, 97, 118, 100, 88, 106, 104, 88, 47, 82, 114, 81, 114, 112, 84, 117, 82, 116, 117, 75, 79, 68, 65, 51, 113, 104, 67, 68, 80, 71, 48, 103, 103, 74, 47, 89, 49, 103, 72, 97, 110, 52, 119};
    public static final byte[] PUB = new byte[]{77, 73, 71,
            102, 77, 65, 48, 71, 67, 83, 113, 71, 83, 73, 98, 51, 68, 81, 69, 66, 65, 81, 85, 65, 65, 52, 71, 78, 65, 68, 67, 66, 105, 81, 75, 66, 103, 81, 68, 65, 54, 110, 81, 43, 106, 118, 77, 78, 90, 52, 119, 79, 76, 65, 98, 55, 84, 52, 65, 98, 112, 89, 54, 108, 78, 81, 100, 109, 99, 71, 70, 84, 109, 50, 99, 117, 103, 83, 113, 118, 104, 81, 116, 71, 73, 106, 66, 88, 69, 66, 108, 102, 89, 108, 107, 54, 112, 90, 80, 103, 83, 102, 122, 107, 69, 54, 102, 75, 108, 74, 66, 81, 111, 76, 52, 85, 116, 87, 119, 51, 82, 82, 79, 99, 77, 119, 107, 120, 105, 79, 43, 82, 99, 56, 66, 105, 65, 75, 111, 53, 111, 69, 118, 65, 65, 89, 122, 88, 109, 114, 85, 67, 70, 52, 111, 97, 88, 71, 78, 100, 57, 52, 122, 47, 55, 81, 66, 43, 75, 57, 111, 66, 48, 89, 70, 86, 49, 84, 97, 56, 117, 120, 106, 51, 86, 79, 98, 87, 51, 121, 116, 72, 84, 88, 48, 83, 71, 84, 70, 97, 50, 84, 110, 112, 102, 98, 117, 51, 106, 81, 73, 68, 65, 81, 65, 66};

    public static String getBytesString(String s) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        String result = Arrays.toString(bytes);
        return "new byte[]{" + result.substring(1, result.length() - 1) + "}";
    }

    public static String getPublicKey() {
        return new String(PUB);
    }

    private static String getPrivateKeyBytes(KeyPair keyPair) {
        return getBytesString(new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded())));
    }

    private static String getPublicKeyBytes(KeyPair keyPair) {
        return getBytesString(new String(Base64.encodeBase64(keyPair.getPublic().getEncoded())));
    }

    /**
     * 初始化方法，产生key pair，提供provider和random
     *
     * @return KeyPair instance
     */
    private static KeyPair initKey() {

        try {
            //添加provider
            Provider provider = new BouncyCastleProvider();
            Security.addProvider(provider);
            //产生用于安全加密的随机数
            SecureRandom random = new SecureRandom();

            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", provider);
            generator.initialize(1024, random);
            return generator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encrypt(String str) {
        String res = "";
        try {
            res = encrypt(str, new String(PUB));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String decrypt(String str) {
        String res = "";
        try {
            res = decrypt(str, new String(PRI));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes(StandardCharsets.UTF_8));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(inputByte));
    }
}
