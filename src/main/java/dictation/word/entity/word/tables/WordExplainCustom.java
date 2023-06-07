package dictation.word.entity.word.tables;

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
public class WordExplainCustom {
    Integer userId;
    Integer wordId;
    Integer libId;
    Integer expId;

}
