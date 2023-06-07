package dictation.word.service.i.plan;

import com.baomidou.mybatisplus.extension.service.IService;
import dictation.word.entity.plan.tables.PlanWord;

import java.util.List;

public interface PlanWordService extends IService<PlanWord> {
    int getWordCnt(Integer planId);

    List<PlanWord> getWords(int planId);

    boolean removeAll(int planId);
}
