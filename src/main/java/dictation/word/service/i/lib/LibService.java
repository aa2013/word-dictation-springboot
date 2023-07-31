package dictation.word.service.i.lib;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import dictation.word.entity.lib.CommonLibInfo;
import dictation.word.entity.lib.LibInfo;
import dictation.word.entity.lib.tables.Lib;

import java.util.List;

/**
 * @author ljh
 * @date 2022/12/14
 */
public interface LibService extends IService<Lib> {
    boolean createLib(Lib lib, int userId);

    List<LibInfo> getListSelf(int userId);

    PageInfo<CommonLibInfo> getListCommon(int pageNum, int pageSize, String search, int userId);

    LibInfo getLibInfo(int libId, int userId);

    boolean updateLib(Lib lib, int userId);
}
