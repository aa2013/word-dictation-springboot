package dictation.word.service.i.user;

public interface EmailService {
    boolean sendBindingCode(String receiver);

    boolean sendEmail(String toEmail, String subject, String text);

    boolean sendResetPwdCode(String receiver);

    boolean verifyBindingCode(String email, String code);

    boolean verifyResetPwdCode(String email, String code);
    boolean verifyCode(String key, String code);

    boolean verifyRegisterCode(String email, String emailCode);

    boolean sendRegisterCode(String email);

}
