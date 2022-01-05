package idea.verlif.juststation.global.base.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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
    protected Integer pageSize = 15;

    @Schema(name = "页码，从1开始")
    protected Integer pageNum = 1;

    @Schema(name = "排序列名，可以传入该对象的任意属性名")
    protected String orderBy;

    @Schema(name = "是否升序排列，默认true")
    protected boolean asc;

    public Pageable() {
    }

    public String getOrderBy() {
        return orderBy;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
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

}
