package dictation.word.entity.lib.tables;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lib {
    @TableId(type = IdType.AUTO)
    Integer id;
    @NotBlank
    String libName;
    Integer creator;
    @NotNull
    Boolean common = false;
    Date createTime;
    Date updateTime;
}
