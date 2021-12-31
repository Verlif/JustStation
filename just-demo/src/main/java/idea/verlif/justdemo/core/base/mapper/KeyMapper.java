package idea.verlif.justdemo.core.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import idea.verlif.justdemo.core.base.domain.Key;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/10 11:43
 */
@Mapper
public interface KeyMapper extends BaseMapper<Key> {

    /**
     * 获取用户的Key集
     *
     * @param username 用户名
     * @return Key集
     */
    Set<String> getUserKeySet(String username);
}
