package dictation.word.entity.word.tables;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Word {
    @TableId(type = IdType.AUTO)
    Integer id;
    @NotBlank
    String word;
    String usSymbol;
    String enSymbol;
    String usSymbolMp3;
    String enSymbolMp3;
}
