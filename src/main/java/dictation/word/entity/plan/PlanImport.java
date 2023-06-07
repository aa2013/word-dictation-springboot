package dictation.word.entity.plan;

import dictation.word.entity.plan.tables.Plan;
import dictation.word.entity.plan.tables.PlanWord;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class PlanImport extends Plan {
    @NotNull
    Integer libId;
    List<PlanWord> words;
}
