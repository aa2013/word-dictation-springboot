package dictation.word.entity.word;

import dictation.word.entity.word.tables.Word;
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
public class WordInfo extends Word {
    Explain explain;
}
