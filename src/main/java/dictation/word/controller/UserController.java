package dictation.word.controller;

import dictation.word.service.i.user.EmailService;
import dictation.word.service.i.user.UserService;
import dictation.word.utils.RSAUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;

/**
 * @author ljh
 * @date 2022/12/14
 */
@RestController
@RequestMapping("/user")
@Validated
public class UserController extends BaseController {

    @Resource
    UserService userService;
    @Resource
    EmailService emailService;

    @PostMapping("/register")
    public boolean sendForgetPwdCode(@NotBlank String email, @NotBlank String pwd, @NotBlank String pwdAgain, @NotBlank String emailCode) {
        return userService.register(email, pwd, pwdAgain, emailCode);
    }

    @PostMapping("/updatePwd")
    public boolean updatePwd(@NotBlank String old, @NotBlank String now, @NotBlank String again) {
        return userService.updatePwd(old, now, again, getCurrentUserId());
    }

    @PostMapping("/register/code")
    public boolean sendRegisterCode(@NotBlank String email) {
        return emailService.sendRegisterCode(email);
    }

    @PostMapping("/forgetPwd/code")
    public boolean sendForgetPwdCode(@NotBlank String email) {
        return userService.sendForgetPwdCode(email);
    }

    @PostMapping("/forgetPwd/submit")
    public boolean submitForgetPwdCode(@NotBlank String email, @NotBlank String code, @NotBlank String now, @NotBlank String again) {
        return userService.submitForgetPwdCode(email, code, now, again);
    }

    @GetMapping("/key")
    public String getPublicKey() {
        //获取前端登录用于加密的key
        StringBuilder builder = new StringBuilder(RSAUtil.getPublicKey());
        builder.insert(17, "DF47W");
        return builder.toString();
    }

    @GetMapping("/rsa")
    public String getRsa(String key) {
        return RSAUtil.encrypt(key);
    }
}
