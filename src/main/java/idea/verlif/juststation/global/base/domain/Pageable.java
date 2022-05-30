package idea.verlif.juststation.global.base.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

/**
 * 可分页排序属性 <br>
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/7/8 10:20
 */
@Data
@Schema(name = "可分页属性")
public abstract class Pageable<T> {

    @Schema(name = "每页大小")
    protected Integer size = 15;

    @Schema(name = "页码，从1开始")
    protected Integer current = 1;

    @Schema(name = "排序列名，可以传入该对象的任意属性名")
    protected String orderBy;

    @Schema(name = "是否升序排列，默认true")
    protected boolean asc;

    public Pageable() {
    }

    public String getOrderBy() {
        return orderBy;
    }

    /**
     * 排序
     *
     * @param orderBy 排序字段
     * @see #setOrderBy(Field)
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

    public void setOrderBy(Field field) {
        if (field == null) {
            return;
        }
        orderBy = field.getName();
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }


    @Schema(hidden = true)
    public Integer getPageHead() {
        return (current - 1) * size;
    }

    public void setCurrent(Integer current) {
        if (current < 1) {
            this.current = 1;
        } else {
            this.current = current;
        }
    }

}
