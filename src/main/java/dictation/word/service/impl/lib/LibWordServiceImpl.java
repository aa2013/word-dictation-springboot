package dictation.word.service.impl.lib;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dictation.word.dao.lib.LibWordMapper;
import dictation.word.entity.lib.tables.LibWord;
import dictation.word.service.i.lib.LibWordService;
import org.springframework.stereotype.Service;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Service
public class LibWordServiceImpl extends ServiceImpl<LibWordMapper, LibWord> implements LibWordService {
    @Override
    public boolean removeByLibId(int libId) {
        return remove(Wrappers.<LibWord>lambdaQuery()
                .eq(LibWord::getLibId,libId));
    }
}
