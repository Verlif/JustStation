package idea.verlif.justdemo.core.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import idea.verlif.juststation.global.base.domain.Pageable;

import java.lang.reflect.Field;

/**
 * 结合了MybatisPlus的分页查询条件<br/>
 * 需要进行条件判定时，请重写{@link #buildQueryWrapper()}<br/>
 * 并使用 {@code QueryWrapper<EquipShipCert> wrapper = super.buildQueryWrapper();} 来获取查询条件集 <br/>
 * 这里使用的是MyBatisPlus的方式进行的分页，请注意使用泛型
 *
 * @author Verlif
 * @version 1.0
 * @date 2022/1/5 16:05
 */
public class PageExtend<T> extends Pageable<T> {

    /**
     * 排序规则 <br>
     * 为了SQL安全，暂时不兼容泛型类字段外的属性（会被过滤）
     *
     * @param field 需要排序的字段
     */
    @Override
    public void setOrderBy(Field field) {
        if (field == null) {
            return;
        }
        field.setAccessible(true);
        TableField tableField = field.getAnnotation(TableField.class);
        if (tableField != null && tableField.exist()) {
            orderBy = tableField.value();
        } else {
            TableId tableId = field.getAnnotation(TableId.class);
            if (tableId != null) {
                orderBy = tableId.value().length() == 0 ? field.getName() : tableId.value();
            } else {
                orderBy = field.getName();
            }
        }
    }

    /**
     * 获取排序查询对象
     *
     * @author Verlif
     * @date 2021/7/8 11:36
     */
    public QueryWrapper<T> buildQueryWrapper() {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.orderBy(orderBy != null, asc, orderBy);
        return wrapper;
    }

    /**
     * 获取分页条件，用于Mybatis-plus的Mapper中的分页条件。
     *
     * @return 分页条件
     */
    public Page<T> buildPage() {
        int s = size > 0 ? size : -1;
        Page<T> page = new Page<>(current, s);
        if (orderBy != null) {
            page.addOrder(new OrderItem(orderBy, asc));
        }
        return page;
    }
}
