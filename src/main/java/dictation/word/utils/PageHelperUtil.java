package dictation.word.utils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

public class PageHelperUtil {
    /**
     * pagehelper 手动分页
     *
     * @param currentPage 当前页
     * @param pageSize    页面大小
     * @param list        分页原 List
     * @param <T>         类型
     * @return 分页信息
     */
    public static <T> PageInfo<T> getPageInfo(int currentPage, int pageSize, List<T> list) {
        int total = list.size();
        if (total > pageSize) {
            int toIndex = pageSize * currentPage;
            if (toIndex > total) {
                toIndex = total;
            }
            list = list.subList(pageSize * (currentPage - 1), toIndex);
        }
        Page<T> page = new Page<>(currentPage, pageSize);
        page.addAll(list);
        page.setPages((total + pageSize - 1) / pageSize);
        page.setTotal(total);
        return new PageInfo<>(page);
    }

    /**
     * pagehelper 复制分页基本信息，不包含 list
     *
     * @param origin 原始信息
     * @param data   新分页数据
     * @return 分页信息
     */
    public static <T, U> PageInfo<U> copyBasicInfo(PageInfo<T> origin, List<U> data) {
        PageInfo<U> now = new PageInfo<>();
        now.setTotal(origin.getTotal());
        now.setPages(origin.getPages());
        now.setIsFirstPage(origin.isIsFirstPage());
        now.setIsLastPage(origin.isIsLastPage());
        now.setEndRow(origin.getEndRow());
        now.setHasNextPage(origin.isHasNextPage());
        now.setHasPreviousPage(origin.isHasPreviousPage());
        now.setNavigateFirstPage(origin.getNavigateFirstPage());
        now.setNavigatePages(origin.getNavigatePages());
        now.setNavigateLastPage(origin.getNavigateLastPage());
        now.setNavigatepageNums(origin.getNavigatepageNums());
        now.setNextPage(origin.getNextPage());
        now.setPageNum(origin.getPageNum());
        now.setPageSize(origin.getPageSize());
        now.setPrePage(origin.getPrePage());
        now.setSize(origin.getSize());
        now.setStartRow(origin.getStartRow());
        now.setList(data);
        return now;
    }
}
