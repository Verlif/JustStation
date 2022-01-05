package idea.verlif.juststation.global.util;

import idea.verlif.juststation.global.base.domain.Pageable;
import idea.verlif.juststation.global.base.domain.SimPage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/16 14:51
 */
public class PageUtils {

    /**
     * 对列表进行分页管理 <br/>
     * 不支持排序
     *
     * @param list  需分页列表
     * @param query 分页条件
     * @param <T>   分页对象
     * @return 分页结果
     */
    public static <T> SimPage<T> page(List<T> list, Pageable<?> query) {
        SimPage<T> page = new SimPage<>();
        page.setTotal(list.size());
        page.setSize(query.getPageSize());
        page.setPages(page.getTotal() / page.getSize());
        if (page.getTotal() % page.getSize() > 0) {
            page.setPages(page.getPages() + 1);
        }
        page.setCurrent(query.getPageNum());
        if (query.getPageHead() > list.size()) {
            page.setRecords(new ArrayList<>());
        } else {
            int end = query.getPageHead() + query.getPageSize();
            page.setRecords(list.subList(query.getPageHead(), Math.min(end, list.size())));
        }
        return page;
    }
}
