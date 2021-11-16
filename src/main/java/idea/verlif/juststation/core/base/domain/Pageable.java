package idea.verlif.juststation.core.base.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

/**
 * 可分页排序属性 <br>
 * 需要进行条件判定时，请重写{@link Pageable#buildQueryWrapper()}
 * 并使用 {@code QueryWrapper<EquipShipCert> wrapper = super.buildQueryWrapper();} 来获取查询条件集 <br>
 * 这里使用的是MyBatisPlus的方式进行的分页，请注意使用泛型
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/7/8 10:20
 */
@Data
@Schema(name = "可分页属性")
public abstract class Pageable<T> {

    @Schema(name = "每页大小")
    private Integer pageSize = 15;

    @Schema(name = "页码，从1开始")
    private Integer pageNum = 1;

    @Schema(name = "排序列名，可以传入该对象的任意属性名")
    private String orderBy;

    @Schema(name = "是否升序排列，默认true")
    private boolean asc;

    public Pageable() {
    }

    public String getOrderBy() {
        return orderBy;
    }

    /**
     * 排序规则 <br>
     * 为了SQL安全，暂时不兼容泛型类字段外的属性（会被过滤）
     *
     * @param field 需要排序的字段
     */
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
                orderBy = tableId.value();
            }
        }
    }

    /**
     * 排序
     *
     * @param orderBy 排序字段
     * @see Pageable#setOrderBy(Field)
     */
    public void setOrderBy(String orderBy) {
        try {
            Class<?> cl = Class.forName(
                    ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName());
            do {
                try {
                    Field field = cl.getDeclaredField(orderBy);
                    setOrderBy(field);
                    break;
                } catch (NoSuchFieldException ignored) {
                    cl = cl.getSuperclass();
                }
            } while (cl != null);
        } catch (ClassNotFoundException ignored) {
        }
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    /**
     * 获取排序查询对象
     *
     * @author Verlif
     * @date 2021/7/8 11:36
     */
    public QueryWrapper<T> buildQueryWrapper() {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        if (orderBy != null) {
            wrapper.orderBy(true, asc, orderBy);
        }
        return wrapper;
    }

    /**
     * 返回模糊查询对应的列名
     *
     * @return 模糊查询对应的表中列名
     */
    protected String nameColumn() {
        return null;
    }

    @Schema(hidden = true)
    public Integer getPageHead() {
        return (pageNum - 1) * pageSize;
    }

    public void setPageNum(Integer pageNum) {
        if (pageNum < 1) {
            this.pageNum = 1;
        } else {
            this.pageNum = pageNum;
        }
    }

    /**
     * 获取分页对象
     */
    public IPage<T> buildIPage() {
        return buildPage();
    }

    public Page<T> buildPage() {
        if (pageNum > 0) {
            return new Page<>(pageNum, pageSize);
        } else {
            return new Page<>(pageNum, -1);
        }
    }
}
