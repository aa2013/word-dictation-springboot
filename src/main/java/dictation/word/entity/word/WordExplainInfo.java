package dictation.word.entity.word;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import dictation.word.entity.word.tables.WordExplain;
import lombok.*;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordExplainInfo {
    @JsonUnwrapped
    WordExplain explain;
    boolean customDefault = false;
}
