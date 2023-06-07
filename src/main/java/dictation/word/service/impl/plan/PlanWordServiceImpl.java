package dictation.word.service.impl.plan;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dictation.word.dao.plan.PlanWordMapper;
import dictation.word.entity.plan.tables.PlanWord;
import dictation.word.service.i.plan.PlanWordService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanWordServiceImpl extends ServiceImpl<PlanWordMapper, PlanWord> implements PlanWordService {
    @Override
    public boolean removeAll(int planId) {
        return remove(Wrappers.<PlanWord>lambdaQuery()
                .eq(PlanWord::getPlanId, planId));
    }

    @Override
    public int getWordCnt(Integer planId) {
        return count(Wrappers.<PlanWord>lambdaQuery()
                .eq(PlanWord::getPlanId, planId));
    }

    @Override
    public List<PlanWord> getWords(int planId) {
        return list(Wrappers.<PlanWord>lambdaQuery()
                .eq(PlanWord::getPlanId, planId));
    }
}
