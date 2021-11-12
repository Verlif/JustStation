package idea.verlif.juststation.global.security.token;

import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.core.base.result.ResultCode;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.util.SecurityUtils;
import idea.verlif.juststation.global.util.ServletUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token过滤器 验证token有效性
 *
 * @author Enzo
 */
@Component
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TokenConfig tokenConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = request.getHeader(tokenConfig.getHeader());
        // 没有token则移交给下一个过滤器
        if (token == null) {
            doFilter(request, response, chain);
            return;
        }
        //token解析失败
        if (StringUtils.isNotEmpty(token) && tokenService.parseToken(token) == null) {
            ServletUtils.sendResult(response, new BaseResult<>(ResultCode.FAILURE_TOKEN));
            return;
        }
        LoginUser<?> loginUser = tokenService.getLoginUser(request);
        //刷新token过期时间
        if (loginUser != null && SecurityUtils.getAuthentication() == null) {
            tokenService.refreshToken(loginUser);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        chain.doFilter(request, response);
    }
}
