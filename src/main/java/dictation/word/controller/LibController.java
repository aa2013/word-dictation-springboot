package dictation.word.controller;

import com.github.pagehelper.PageInfo;
import dictation.word.entity.lib.LibInfo;
import dictation.word.entity.lib.tables.Lib;
import dictation.word.service.i.lib.LibService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author ljh
 * @date 2022/12/14
 */
@RestController
@RequestMapping("/lib")
@Validated
public class LibController {
    @Autowired
    LibService libService;

    @PostMapping("/add")
    public int addLib(@Valid Lib lib) {
        return libService.addLib(lib);
    }

    @GetMapping("/list/self")
    public PageInfo<LibInfo> listSelf(int pageNum, int pageSize) {
        return libService.getListSelf(pageNum, pageSize);
    }

    @GetMapping("/list/common")
    public PageInfo<LibInfo> listCommon(int pageNum, int pageSize) {
        return libService.getListCommon(pageNum, pageSize);
    }
}
