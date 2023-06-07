package dictation.word.entity.plan;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import dictation.word.entity.plan.tables.Plan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanInfo {
    @JsonUnwrapped
    Plan plan;
    int cnt;
}
