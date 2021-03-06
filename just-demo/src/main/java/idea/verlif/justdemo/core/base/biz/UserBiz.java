package idea.verlif.justdemo.core.base.biz;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import idea.verlif.justdemo.core.base.biz.base.BaseBizAto;
import idea.verlif.justdemo.core.base.domain.User;
import idea.verlif.justdemo.core.base.domain.req.UpdatePassword;
import idea.verlif.justdemo.core.base.mapper.UserMapper;
import idea.verlif.justdemo.core.login.service.OnlineService;
import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ResultCode;
import idea.verlif.juststation.global.base.result.ext.FailResult;
import idea.verlif.juststation.global.base.result.ext.OkResult;
import idea.verlif.juststation.global.rsa.RsaService;
import idea.verlif.juststation.global.security.login.LoginService;
import idea.verlif.juststation.global.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/10 10:20
 */
@Service
public class UserBiz extends BaseBizAto<User, UserMapper> {

    @Autowired
    private LoginService loginService;

    @Autowired
    private OnlineService onlineService;

    @Autowired
    private RsaService rsaService;

    /**
     * 获取个人信息
     *
     * @return 查询结果
     */
    public BaseResult<User> getSelfInfo() {
        return getInfoByName(SecurityUtils.getUsername());
    }

    /**
     * 获取用户个人信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    public BaseResult<User> getInfoByName(String username) {
        User user = baseMapper.getUserByName(username == null ? SecurityUtils.getUsername() : username);
        if (user == null) {
            return new FailResult<>("用户不存在");
        } else {
            return new OkResult<>(user);
        }
    }

    @Override
    public BaseResult<User> update(User user) {
        // 用户修改限定修改个人信息
        user.setUsername(SecurityUtils.getUsername());
        return super.update(user);
    }

    @Override
    public boolean updateOne(User user) {
        return baseMapper.update(
                user,
                new UpdateWrapper<User>().lambda().eq(User::getUsername, user.getUsername())) > 0;
    }

    /**
     * 修改密码
     *
     * @param up 密码修改信息
     * @return 修改结果
     */
    public BaseResult<?> updatePassword(UpdatePassword up) {
        // 旧密码解密
        up.setOld(rsaService.decryptByPrivateKey(up.getKeyId(), up.getOld()));
        User user = baseMapper.selectByNameAndPwd(SecurityUtils.getUsername(), up.getOld());
        if (user == null) {
            return new FailResult<>("原密码错误");
        } else {
            // 新密码解密
            up.setNow(rsaService.decryptByPrivateKey(up.getKeyId(), up.getNow()));
            user.setPassword(up.getNow());
            BaseResult<?> result = update(user);
            // 密码修改成功后，强制退出当前用户所有登录
            if (result.equals(ResultCode.SUCCESS)) {
                onlineService.logoutUser(user.getUsername(), null);
            }
            return result;
        }
    }

    /**
     * 注册新用户
     *
     * @param user 新用户信息
     * @return 注册结果
     */
    public BaseResult<?> register(User user) {
        // 用户密码解密
        user.setPassword(rsaService.decryptByPrivateKey(user.getKeyId(), user.getPassword()));
        try {
            // 密码进行重编码，如果密码解密步骤出错，则会抛出IllegalArgumentException异常
            user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
            if (baseMapper.insert(user) > 0) {
                return new OkResult<>(user).msg("注册成功");
            } else {
                return new FailResult<>("注册失败");
            }
        } catch (DuplicateKeyException ignored) {
            return new FailResult<>("已存在用户名 - " + user.getUsername());
        } catch (IllegalArgumentException ignored) {
            return new FailResult<>("密钥过期");
        }
    }
}
