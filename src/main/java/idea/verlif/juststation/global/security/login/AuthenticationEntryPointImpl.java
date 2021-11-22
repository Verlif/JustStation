package idea.verlif.juststation.global.security.login;

import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.core.base.result.ResultCode;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.util.SecurityUtils;
import idea.verlif.juststation.global.util.ServletUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * 认证失败处理类 返回未授权
 *
 * @author Verlif
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        // 验证用户是否登录，这里采用判定token的方式
        LoginUser<?> loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            ServletUtils.sendResult(response, new BaseResult<>(ResultCode.FAILURE_NOT_LOGIN));
        } else {
            ServletUtils.sendResult(response, new BaseResult<>(ResultCode.FAILURE_UNAVAILABLE));
        }
    }
}
