package dictation.word.service.i.lib;

import com.baomidou.mybatisplus.extension.service.IService;
import dictation.word.entity.lib.tables.UserLib;

public interface UserLibService extends IService<UserLib> {
    boolean addLib(int libId, int userId);

    boolean hasLib(int userId, int libId);

    boolean removeLib(int libId, int userId);

}
