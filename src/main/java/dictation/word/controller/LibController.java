package dictation.word.controller;

import com.github.pagehelper.PageInfo;
import dictation.word.entity.lib.CommonLibInfo;
import dictation.word.entity.lib.LibInfo;
import dictation.word.entity.lib.tables.Lib;
import dictation.word.service.i.lib.LibService;
import dictation.word.service.i.lib.UserLibService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author ljh
 * @date 2022/12/14
 */
@RestController
@RequestMapping("/lib")
@Validated
public class LibController extends BaseController {
    @Resource
    LibService libService;
    @Resource
    UserLibService userLibService;

    @PostMapping("/create")
    public boolean createLib(@Valid @RequestBody Lib lib) {
        return libService.createLib(lib, getCurrentUserId());
    }

    @PostMapping("/update")
    public boolean updateLib(@Valid @RequestBody Lib lib) {
        return libService.updateLib(lib, getCurrentUserId());
    }

    @PostMapping("/add/{libId}")
    public boolean addLib(@PathVariable int libId) {
        return userLibService.addLib(libId, getCurrentUserId());
    }

    @PostMapping("/remove/{libId}")
    public boolean removeLib(@PathVariable int libId) {
        return userLibService.removeLib(libId, getCurrentUserId());
    }

    @GetMapping("/list/self")
    public List<LibInfo> listSelf() {
        return libService.getListSelf(getCurrentUserId());
    }

    @GetMapping("/info/{libId}")
    public LibInfo getLibInfo(@PathVariable int libId) {
        return libService.getLibInfo(libId, getCurrentUserId());
    }

    @GetMapping("/list/common")
    public PageInfo<CommonLibInfo> listCommon(int pageNum, int pageSize) {
        return libService.getListCommon(pageNum, pageSize, getCurrentUserId());
    }
}
