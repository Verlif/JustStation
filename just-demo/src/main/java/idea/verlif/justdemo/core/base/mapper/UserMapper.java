package idea.verlif.justdemo.core.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import idea.verlif.justdemo.core.base.domain.User;
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

    /**
     * 通过用户名与用户密码获取用户信息
     *
     * @param username 用户名
     * @param password 用户密码
     * @return 用户信息；null - 数据不匹配
     */
    User selectByNameAndPwd(@Param("username") String username, @Param("password") String password);
}
