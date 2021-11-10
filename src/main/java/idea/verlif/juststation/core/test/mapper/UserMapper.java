package idea.verlif.juststation.core.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import idea.verlif.juststation.core.test.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/10 10:16
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 通过用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息；null - 不存在的用户名
     */
    User getUserByName(@Param("username") String username);
}
