package dictation.word.entity.word;

import dictation.word.entity.word.tables.Word;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class ImportWord extends Word {
    @NotNull
    Integer libId;
    @NotEmpty
    @Valid
    List<Explain> explains;
}
