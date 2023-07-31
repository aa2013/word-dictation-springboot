package dictation.word.service.i.plan;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import dictation.word.entity.plan.PlanImport;
import dictation.word.entity.plan.PlanInfo;
import dictation.word.entity.plan.tables.Plan;

public interface PlanService extends IService<Plan> {
    PageInfo<PlanInfo> getList(int pageNum, int pageSize, String search, int userId);

    boolean addPlan(PlanImport planImport, int userId);

    boolean delete(int planId);
}
