package dictation.word.entity.plan.tables;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanWord {
    @TableId(type = IdType.AUTO)
    Integer id;
    Integer planId;
    @NotBlank
    String word;
    @NotBlank
    @TableField("`explain`")
    String explain;
}
