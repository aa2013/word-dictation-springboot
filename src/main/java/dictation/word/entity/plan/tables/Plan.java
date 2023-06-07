package dictation.word.entity.plan.tables;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Plan {
    @TableId(type = IdType.AUTO)
    Integer id;
    Integer userId;
    @NotBlank
    @TableField("`name`")
    String name;
    String libName;
    @NotNull
    Boolean disorder = false;
    @NotNull
    Boolean ch2en = false;
    @NotNull
    @Min(1)
    @TableField("`repeat`")
    Integer repeat = 1;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date createTime;
}
