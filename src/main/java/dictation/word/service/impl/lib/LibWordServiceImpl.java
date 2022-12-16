package dictation.word.service.impl.lib;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dictation.word.dao.lib.LibWordMapper;
import dictation.word.entity.lib.tables.LibWord;
import dictation.word.service.i.lib.LibWordService;
import dictation.word.service.i.user.TokenService;
import org.springframework.stereotype.Service;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Service
public class LibWordServiceImpl extends ServiceImpl<LibWordMapper, LibWord> implements LibWordService, TokenService {
}
