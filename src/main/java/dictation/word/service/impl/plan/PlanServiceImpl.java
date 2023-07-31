package dictation.word.service.impl.plan;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import dictation.word.dao.plan.PlanMapper;
import dictation.word.entity.lib.tables.Lib;
import dictation.word.entity.plan.PlanImport;
import dictation.word.entity.plan.PlanInfo;
import dictation.word.entity.plan.tables.Plan;
import dictation.word.entity.plan.tables.PlanWord;
import dictation.word.exception.IllegalDataException;
import dictation.word.service.i.lib.LibService;
import dictation.word.service.i.plan.PlanService;
import dictation.word.service.i.plan.PlanWordService;
import dictation.word.utils.PageHelperUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PlanServiceImpl extends ServiceImpl<PlanMapper, Plan> implements PlanService {
    @Resource
    PlanWordService planWordService;
    @Resource
    LibService libService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(int planId) {
        removeById(planId);
        planWordService.removeAll(planId);
        return true;
    }

    @Override
    public PageInfo<PlanInfo> getList(int pageNum, int pageSize, String search, int userId) {
        PageHelper.startPage(pageNum, pageSize);
        List<Plan> list = list(Wrappers.<Plan>lambdaQuery()
                .eq(Plan::getUserId, userId)
                .and(StringUtils.isNotBlank(search),
                        query -> query.like(Plan::getName, search)
                                .or()
                                .like(Plan::getLibName, search)
                )
        );
        List<PlanInfo> data = new ArrayList<>(list.size());
        list.forEach(plan -> {
            int cnt = planWordService.getWordCnt(plan.getId());
            data.add(new PlanInfo(plan, cnt));
        });
        return PageHelperUtil.copyBasicInfo(new PageInfo<>(list), data);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addPlan(PlanImport plan, int userId) {
        List<PlanWord> words = plan.getWords();
        if (words == null || words.isEmpty()) {
            throw new IllegalDataException("单词不能为空");
        }
        Lib lib = libService.getById(plan.getLibId());
        plan.setLibName(lib.getLibName());
        plan.setUserId(userId);
        plan.setCreateTime(new Date());
        if (!save(plan)) {
            throw new IllegalDataException("保存失败");
        }
        words.forEach(word -> {
            word.setPlanId(plan.getId());
        });
        if (!planWordService.saveBatch(words)) {
            throw new IllegalDataException("保存失败");
        }
        return true;
    }
}
