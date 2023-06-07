package dictation.word.service.i.lib;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import dictation.word.entity.lib.CommonLibInfo;
import dictation.word.entity.lib.LibInfo;
import dictation.word.entity.lib.tables.Lib;

/**
 * @author ljh
 * @date 2022/12/14
 */
public interface LibService extends IService<Lib> {
    int createLib(Lib lib, int userId);

    PageInfo<LibInfo> getListSelf(int pageNum, int pageSize, int userId);

    PageInfo<CommonLibInfo> getListCommon(int pageNum, int pageSize, int userId);

}
