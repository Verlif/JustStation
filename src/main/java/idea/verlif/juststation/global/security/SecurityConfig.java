package idea.verlif.juststation.global.security;

import idea.verlif.juststation.global.security.token.TokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 11:26
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 自定义用户认证逻辑
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 认证失败处理类
     */
    @Autowired
    private AuthenticationEntryPoint unauthorizedHandler;

    /**
     * Token过滤器
     */
    @Autowired
    private TokenFilter tokenFilter;

    /**
     * 跨域过滤器
     */
    @Autowired
    private CorsFilter corsFilter;

    @Autowired
    private PasswordEncoder encoder;

    /**
     * anyRequest          |   匹配所有请求路径 <br/>
     * access              |   SpringEl表达式结果为true时可以访问 <br/>
     * anonymous           |   匿名可以访问 <br/>
     * denyAll             |   用户不能访问 <br/>
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录） <br/>
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问 <br/>
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问 <br/>
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问 <br/>
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问 <br/>
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问 <br/>
     * permitAll           |   用户可以任意访问 <br/>
     * rememberMe          |   允许通过remember-me登录的用户访问 <br/>
     * authenticated       |   用户登录后可访问 <br/>
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // CSRF禁用，因为不使用session
                .csrf().disable()
                // 认证失败处理类
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 过滤请求
                .authorizeRequests()
                // 开放所有接口
                .anyRequest().permitAll().and()
                .headers()
                .frameOptions().disable()
                .and()
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(corsFilter, TokenFilter.class);
    }

    /**
     * 避免未注入错误
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 身份认证接口
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
    }

}
