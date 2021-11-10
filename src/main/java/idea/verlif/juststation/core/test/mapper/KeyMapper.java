package idea.verlif.juststation.core.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import idea.verlif.juststation.core.test.domain.Key;
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
     * @param userId 用户ID
     * @return Key集
     */
    Set<String> getUserKeySet(Integer userId);
}
