package idea.verlif.juststation.global.security.login.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * 登录用户权限信息
 *
 * @author Enzo
 */
@Data
public class LoginUser<T extends BaseUser> implements UserDetails {

    private static final long serialVersionUID = 1L;

    /**
     * 用户登录Token
     */
    private String token;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 登录信息过期时间
     */
    private Long expireTime;

    /**
     * 用户信息
     */
    private T user;

    /**
     * 用户拥有的关键词组
     */
    private Set<String> keySet;

    /**
     * 用户拥有的角色组
     */
    private Set<String> roleSet;

    public LoginUser() {
    }

    public LoginUser(T user) {
        this.user = user;
    }

    /**
     * 填充用户的权限信息
     *
     * @param keySet  关键词组
     * @param roleSet 角色组
     * @return 当前的登录对象
     */
    public LoginUser<T> withPermission(Set<String> keySet, Set<String> roleSet) {
        this.keySet = keySet;
        this.roleSet = roleSet;
        return this;
    }

    /**
     * 账户是否未过期,过期无法验证
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 指定用户是否解锁,锁定的用户无法进行身份验证
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 指示是否已过期的用户的凭据(密码),过期的凭据防止认证
     */
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否可用 ,禁用的用户不能身份验证
     */
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
