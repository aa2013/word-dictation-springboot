package dictation.word.service.impl.lib;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import dictation.word.dao.lib.LibMapper;
import dictation.word.entity.lib.CommonLibInfo;
import dictation.word.entity.lib.LibInfo;
import dictation.word.entity.lib.tables.Lib;
import dictation.word.entity.lib.tables.UserLib;
import dictation.word.exception.CreateNewException;
import dictation.word.service.i.lib.LibService;
import dictation.word.service.i.lib.UserLibService;
import dictation.word.utils.PageHelperUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Service
public class LibServiceImpl extends ServiceImpl<LibMapper, Lib> implements LibService {
    @Resource
    LibMapper libMapper;
    @Resource
    UserLibService userLibService;

    @Override
    public PageInfo<CommonLibInfo> getListCommon(int pageNum, int pageSize, int userId) {
        PageHelper.startPage(pageNum, pageSize);
        List<LibInfo> commonLibList = libMapper.getCommonLibList();
        PageInfo<LibInfo> oldPageInfo = new PageInfo<>(commonLibList);
        List<UserLib> userLibs = userLibService.list(Wrappers.<UserLib>lambdaQuery()
                .eq(UserLib::getUserId, userId));
        List<Integer> libIds = userLibs.stream().map(UserLib::getLibId).collect(Collectors.toList());
        List<CommonLibInfo> data = new ArrayList<>(commonLibList.size());
        commonLibList.forEach(libInfo -> {
            boolean hasLib = libIds.contains(libInfo.getId());
            boolean self = libInfo.getCreator().equals(userId);
            data.add(new CommonLibInfo(libInfo, hasLib, self));
        });
        return PageHelperUtil.copyBasicInfo(oldPageInfo, data);
    }

    @Override
    public PageInfo<LibInfo> getListSelf(int pageNum, int pageSize, int userId) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(libMapper.getUserLibList(userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createLib(Lib lib, int userId) {
        lib.setCreator(userId);
        lib.setCreateTime(new Date());
        if (!save(lib) || !userLibService.save(new UserLib(userId, lib.getId()))) {
            throw new CreateNewException("新增库失败");
        }
        return lib.getId();
    }
}
