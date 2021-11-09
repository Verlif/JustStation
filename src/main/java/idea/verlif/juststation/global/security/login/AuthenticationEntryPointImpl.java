package idea.verlif.juststation.global.security.login;

import com.alibaba.fastjson.JSON;
import idea.verlif.juststation.core.base.BaseResult;
import idea.verlif.juststation.core.base.ResultCode;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.security.token.TokenService;
import idea.verlif.juststation.global.util.ServletUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * 认证失败处理类 返回未授权
 *
 * @author ruoyi
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

    @Resource
    private TokenService tokenService;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        LoginUser<?> loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            ServletUtil.renderString(response, JSON.toJSONString(new BaseResult<>(ResultCode.FAILURE_NOT_LOGIN)));
        } else {
            ServletUtil.renderString(response, JSON.toJSONString(new BaseResult<>(ResultCode.FAILURE_UNAVAILABLE)));
        }
    }
}
