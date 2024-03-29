package idea.verlif.juststation.global.base.domain;

import java.util.List;

/**
 * 简单分页
 *
 * @author Verlif
 */
public class SimPage<T> {

    protected List<T> records;
    protected long total = 0;
    protected long size = 0;
    protected long current = 1;
    protected long pages = 1;

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public long getPages() {
        return pages;
    }

    public void setPages(long pages) {
        this.pages = pages;
    }
}
