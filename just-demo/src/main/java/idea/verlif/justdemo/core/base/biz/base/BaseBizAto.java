package idea.verlif.justdemo.core.base.biz.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import idea.verlif.justdemo.core.base.PageWithMP;

import java.io.Serializable;

/**
 * BaseBiz的基础使用，基于BaseMapper的默认方法 <br/>
 * 方法中的ID均表示被{@code @TableId}注释的属性值
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/17 11:57
 */
public class BaseBizAto<T, M extends BaseMapper<T>> extends BaseBiz<T, M> {

    /**
     * 默认使用BaseMapper的selectById方法
     *
     * @param id 数据唯一键值
     * @return 查询到的数据
     */
    @Override
    public T selectOne(Serializable id) {
        return baseMapper.selectById(id);
    }

    /**
     * 默认使用BaseMapper的selectPage方法
     *
     * @param query 查询条件
     * @return 分页数据
     */
    @Override
    public IPage<T> selectPage(PageWithMP<T> query) {
        return baseMapper.selectPage(query.buildPage(), query.buildQueryWrapper());
    }

    /**
     * 默认使用BaseMapper的insert方法
     *
     * @param t 数据对象
     * @return 是否添加成功
     */
    @Override
    public boolean insertOne(T t) {
        return baseMapper.insert(t) > 0;
    }

    /**
     * 默认使用BaseMapper的updateById方法
     *
     * @param t 数据对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateOne(T t) {
        return baseMapper.updateById(t) > 0;
    }

    /**
     * 默认使用BaseMapper的deleteById方法
     *
     * @param id 数据唯一键值
     * @return 是否删除成功
     */
    @Override
    public boolean deleteOne(Serializable id) {
        return baseMapper.deleteById(id) > 0;
    }
}
