package dictation.word.entity.word.tables;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import dictation.word.entity.word.Explain;
import lombok.*;

/**
 * @author ljh
 * @date 2022/12/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class WordExplain extends Explain {
    @TableId(type = IdType.AUTO)
    Integer id;
    Integer wordId;
    Integer libId;
    Boolean isDefault = false;

    public WordExplain(int wordId, int libId, Explain explain, boolean isDefault) {
        super(explain.getExplanation(), explain.getType());
        this.wordId = wordId;
        this.libId = libId;
        this.isDefault = isDefault;
    }
}
