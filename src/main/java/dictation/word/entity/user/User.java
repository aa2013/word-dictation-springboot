package dictation.word.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author ljh
 * @date 2022/7/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @NotNull(message = "用户Id不能为空")
    @TableId(type = IdType.AUTO)
    protected Integer id = 0;
    @NotNull(message = "账号不能为空")
    protected String account;
    @NotNull(message = "用户名不能为空")
    protected String userName;
    @NotNull(message = "密码不能为空")
    protected String password;
}
