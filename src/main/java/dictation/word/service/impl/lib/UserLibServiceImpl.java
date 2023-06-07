package dictation.word.service.impl.lib;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dictation.word.dao.lib.UserLibMapper;
import dictation.word.entity.lib.tables.Lib;
import dictation.word.entity.lib.tables.UserLib;
import dictation.word.exception.NoPermissionException;
import dictation.word.service.i.lib.LibService;
import dictation.word.service.i.lib.UserLibService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserLibServiceImpl extends ServiceImpl<UserLibMapper, UserLib> implements UserLibService {
    @Resource
    LibService libService;

    @Override
    public boolean removeLib(int libId, int userId) {
        return remove(Wrappers.<UserLib>lambdaQuery()
                .eq(UserLib::getUserId, userId)
                .eq(UserLib::getLibId, libId));
    }

    @Override
    public boolean hasLib(int userId, int libId) {
        return count(Wrappers.<UserLib>lambdaQuery()
                .eq(UserLib::getUserId, userId)
                .eq(UserLib::getLibId, libId)) != 1;
    }

    @Override
    public boolean addLib(int libId, int userId) {
        Lib lib = libService.getById(libId);
        if (!lib.getCommon()) {
            throw new NoPermissionException("非公共库，无权限");
        }
        return save(new UserLib(userId, libId));
    }
}
