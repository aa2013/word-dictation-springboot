package dictation.word.service.i.lib;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import dictation.word.entity.lib.LibInfo;
import dictation.word.entity.lib.tables.Lib;

/**
 * @author ljh
 * @date 2022/12/14
 */
public interface LibService extends IService<Lib> {
    int addLib(Lib lib);

    PageInfo<LibInfo> getList(int pageNum, int paseSize);
}
