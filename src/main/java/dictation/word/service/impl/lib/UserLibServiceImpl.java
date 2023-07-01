package dictation.word.service.impl.lib;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dictation.word.dao.lib.UserLibMapper;
import dictation.word.entity.lib.tables.Lib;
import dictation.word.entity.lib.tables.UserLib;
import dictation.word.exception.DelException;
import dictation.word.exception.IllegalDataException;
import dictation.word.exception.NoPermissionException;
import dictation.word.service.i.lib.LibService;
import dictation.word.service.i.lib.LibWordService;
import dictation.word.service.i.lib.UserLibService;
import dictation.word.service.i.word.WordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserLibServiceImpl extends ServiceImpl<UserLibMapper, UserLib> implements UserLibService {
    @Resource
    LibService libService;
    @Resource
    LibWordService libWordService;
    @Resource
    WordService wordService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeLib(int libId, int userId) {
        if (libId == 1) {
            throw new IllegalDataException("不能删除根词库");
        }
        remove(Wrappers.<UserLib>lambdaQuery()
                .eq(UserLib::getUserId, userId)
                .eq(UserLib::getLibId, libId));
        int uses = count(Wrappers.<UserLib>lambdaQuery()
                .eq(UserLib::getLibId, libId));
        if (uses == 0) {
            //删除库
            if (!libService.removeById(libId)) {
                throw new DelException("删除失败");
            }
            //删除库的单词
            if (!libWordService.removeByLibId(libId)) {
                throw new DelException("删除失败");
            }
            //删除自定义释义
            if (!wordService.removeByLibId(libId)) {
                throw new DelException("删除失败");
            }
        }
        return true;
    }

    @Override
    public boolean hasLib(int userId, int libId) {
        return count(Wrappers.<UserLib>lambdaQuery()
                .eq(UserLib::getUserId, userId)
                .eq(UserLib::getLibId, libId)) == 1;
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
