package dictation.word.controller;

import dictation.word.utils.RSAUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ljh
 * @date 2022/12/14
 */
@RestController
@RequestMapping("/user")
@Validated
public class UserController extends BaseController{
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
