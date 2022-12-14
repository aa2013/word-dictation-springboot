package dictation.word.entity.word;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordExplain {
    @TableId(type = IdType.AUTO)
    Integer id;
    Integer wordId;
    Integer bookId;
    String explanation;
    String type;
}
