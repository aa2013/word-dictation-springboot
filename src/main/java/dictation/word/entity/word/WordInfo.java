package dictation.word.entity.word;

import dictation.word.entity.word.tables.Word;
import lombok.*;

import java.util.List;

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
    List<Explain> explains;
}
