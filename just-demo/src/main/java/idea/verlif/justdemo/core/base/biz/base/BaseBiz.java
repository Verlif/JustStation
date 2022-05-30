package idea.verlif.justdemo.core.base.biz.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import idea.verlif.justdemo.core.base.PageExtend;
import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ResultCode;
import idea.verlif.juststation.global.base.result.ext.OkResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/17 11:41
 */
public abstract class BaseBiz<T, M extends BaseMapper<T>> {

    @Autowired
    protected M baseMapper;

    public BaseResult<?> getInfo(Serializable id) {
        T t = selectOne(id);
        if (t == null) {
            return new BaseResult<>(ResultCode.FAILURE_DATA_MISSING);
        } else {
            return new OkResult<>(t);
        }
    }

    /**
     * 获取单条数据
     *
     * @param id 数据唯一键值
     * @return 数据对象
     */
    public abstract T selectOne(Serializable id);

    public BaseResult<IPage<T>> getPage(PageExtend<T> query) {
        if (query == null) {
            query = new PageExtend<T>() {
            };
            query.setSize(-1);
        }
        IPage<T> page = selectPage(query);
        return new OkResult<>(page);
    }

    /**
     * 获取分页数据
     *
     * @param query 查询条件
     * @return 分页数据
     */
    public abstract IPage<T> selectPage(PageExtend<T> query);

    public BaseResult<T> insert(T t) {
        if (insertOne(t)) {
            return OkResult.empty();
        } else {
            return new BaseResult<>(ResultCode.FAILURE_INSERT);
        }
    }

    /**
     * 添加数据
     *
     * @param t 数据对象
     * @return 是否添加成功
     */
    public abstract boolean insertOne(T t);

    public BaseResult<T> update(T t) {
        if (updateOne(t)) {
            return OkResult.empty();
        } else {
            return new BaseResult<>(ResultCode.FAILURE_UPDATE);
        }
    }

    /**
     * 修改数据
     *
     * @param t 数据对象
     * @return 是否修改成功
     */
    public abstract boolean updateOne(T t);

    public BaseResult<T> delete(Serializable id) {
        if (deleteOne(id)) {
            return OkResult.empty();
        } else {
            return new BaseResult<>(ResultCode.FAILURE_DELETE);
        }
    }

    /**
     * 删除数据
     *
     * @param id 数据唯一键值
     * @return 是否删除成功
     */
    public abstract boolean deleteOne(Serializable id);
}
