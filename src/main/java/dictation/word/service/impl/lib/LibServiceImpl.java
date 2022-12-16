package dictation.word.service.impl.lib;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import dictation.word.dao.lib.LibMapper;
import dictation.word.entity.lib.LibInfo;
import dictation.word.entity.lib.tables.Lib;
import dictation.word.exception.CreateNewException;
import dictation.word.service.i.lib.LibService;
import dictation.word.service.i.user.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Service
public class LibServiceImpl extends ServiceImpl<LibMapper, Lib> implements LibService, TokenService {
    @Autowired
    LibMapper libMapper;

    @Override
    public PageInfo<LibInfo> getListCommon(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        final List<LibInfo> libs = libMapper.getLibList(null);
        return new PageInfo<>(libs);
    }

    @Override
    public PageInfo<LibInfo> getListSelf(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        final List<LibInfo> libs = libMapper.getLibList(getCurrentUserId());
        return new PageInfo<>(libs);
    }

    @Override
    public int addLib(Lib lib) {
        lib.setCreator(getCurrentUserId());
        lib.setCreateTime(new Date());
        if (save(lib)) {
            return lib.getId();
        }
        throw new CreateNewException("新增库失败");
    }
}
