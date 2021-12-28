package idea.verlif.juststation.global.security.token;

import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ResultCode;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.util.ServletUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import reactor.util.annotation.NonNull;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token过滤器 验证token有效性
 *
 * @author Verlif
 */
@Component
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TokenConfig tokenConfig;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {
        String token = tokenService.getTokenFromRequest(request);
        // 没有token则移交给下一个过滤器
        if (token == null) {
            chain.doFilter(request, response);
            return;
        }
        //token解析失败
        if (StringUtils.isNotEmpty(token) && tokenService.parseToken(token) == null) {
            ServletUtils.sendResult(response, new BaseResult<>(ResultCode.FAILURE_TOKEN));
            return;
        }
        LoginUser loginUser = tokenService.getUserByToken(token);
        //刷新token过期时间
        if (loginUser != null) {
            // 填充本次的登录用户信息
            tokenService.refreshUser(loginUser);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        chain.doFilter(request, response);
    }
}
