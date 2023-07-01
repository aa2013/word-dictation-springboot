package dictation.word.service.impl.lib;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import dictation.word.config.QiNiuStorage;
import dictation.word.dao.lib.LibMapper;
import dictation.word.entity.lib.CommonLibInfo;
import dictation.word.entity.lib.LibInfo;
import dictation.word.entity.lib.tables.Lib;
import dictation.word.entity.lib.tables.UserLib;
import dictation.word.exception.CreateNewException;
import dictation.word.exception.NoPermissionException;
import dictation.word.exception.UnavailableException;
import dictation.word.service.i.lib.LibService;
import dictation.word.service.i.lib.UserLibService;
import dictation.word.utils.PageHelperUtil;
import dictation.word.utils.RegexUtil;
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
    @Resource
    QiNiuStorage qiNiuStorage;

    @Override
    public boolean updateLib(Lib lib, int userId) {
        Lib dbLib = getById(lib.getId());
        if (!dbLib.getCreator().equals(userId)) {
            throw new NoPermissionException("无权限修改他人创建的词库");
        }
        String b64Cover = lib.getCover();
        if (b64Cover == null) {
            return updateById(lib);
        }
        if (!RegexUtil.isBase64(b64Cover)) {
            return true;
        }
        String url = qiNiuStorage.uploadByBase64("wordDictation/cover" + lib.getId() + ".jpg", b64Cover);
        if (url == null) {
            System.err.println("图片上传失败！");
            throw new UnavailableException("图片上传失败！");
        }
        lib.setCover(url);
        return updateById(lib);
    }

    @Override
    public LibInfo getLibInfo(int libId, int userId) {
        if (!userLibService.hasLib(userId, libId)) {
            throw new NoPermissionException("你没有此库的访问权");
        }
        return libMapper.getLibInfo(libId, userId);
    }

    @Override
    public PageInfo<CommonLibInfo> getListCommon(int pageNum, int pageSize, int userId) {
        PageHelper.startPage(pageNum, pageSize);
        List<LibInfo> commonLibList = libMapper.getCommonLibList();
        PageInfo<LibInfo> oldPageInfo = new PageInfo<>(commonLibList);
        List<UserLib> userLibs = userLibService.list(Wrappers.<UserLib>lambdaQuery().eq(UserLib::getUserId, userId));
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
    public List<LibInfo> getListSelf(int userId) {
        List<LibInfo> userLibList = libMapper.getUserLibList(userId);
        userLibList.forEach(lib -> {
            lib.setSelf(lib.getCreator().equals(userId));
        });
        return userLibList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createLib(Lib lib, int userId) {
        String b64Cover = lib.getCover();
        lib.setCover(null);
        lib.setCreator(userId);
        lib.setCreateTime(new Date());
        lib.setUpdateTime(new Date());
        if (!save(lib)) {
            throw new CreateNewException("新增库失败");
        }
        Integer libId = lib.getId();
        if (!userLibService.save(new UserLib(userId, libId))) {
            throw new CreateNewException("新增库失败");
        }
        if (!RegexUtil.isBase64(b64Cover)) {
            return true;
        }
        String url = qiNiuStorage.uploadByBase64("wordDictation/cover" + libId + ".jpg", b64Cover);
        if (url == null) {
            System.err.println("图片上传失败！");
            return true;
        }
        lib.setCover(url);
        updateById(lib);
        return true;
    }
}
