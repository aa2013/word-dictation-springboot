package dictation.word.controller;

import com.github.pagehelper.PageInfo;
import dictation.word.entity.plan.PlanImport;
import dictation.word.entity.plan.PlanInfo;
import dictation.word.entity.plan.tables.PlanWord;
import dictation.word.service.i.plan.PlanService;
import dictation.word.service.i.plan.PlanWordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/plan")
public class PlanController extends BaseController {
    @Resource
    PlanService planService;
    @Resource
    PlanWordService planWordService;

    @GetMapping("/list")
    PageInfo<PlanInfo> getList(int pageNum, int pageSize, String search) {
        return planService.getList(pageNum, pageSize, search, getCurrentUserId());
    }

    @PostMapping("/add")
    boolean addPlan(@RequestBody PlanImport planImport) {
        return planService.addPlan(planImport, getCurrentUserId());
    }

    @GetMapping("/words/{planId}")
    List<PlanWord> getWords(@PathVariable int planId) {
        return planWordService.getWords(planId);
    }

    @PostMapping("/remove/{planId}")
    boolean deletePlan(@PathVariable int planId) {
        return planService.delete(planId, getCurrentUserId());
    }
}
