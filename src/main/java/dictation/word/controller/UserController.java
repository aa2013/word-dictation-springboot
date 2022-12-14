package dictation.word.controller;

import dictation.word.utils.RSAUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ljh
 * @date 2022/12/14
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/key")
    public String getPublicKey() {
        return RSAUtil.getPublicKey();
    }
}
