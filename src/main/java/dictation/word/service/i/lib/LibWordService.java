package dictation.word.service.i.lib;

import com.baomidou.mybatisplus.extension.service.IService;
import dictation.word.entity.lib.tables.LibWord;

/**
 * @author ljh
 * @date 2022/12/14
 */
public interface LibWordService extends IService<LibWord> {
    boolean removeByLibId(int libId);
}
