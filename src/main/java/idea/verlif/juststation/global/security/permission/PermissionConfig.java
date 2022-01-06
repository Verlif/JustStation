package idea.verlif.juststation.global.security.permission;

import idea.verlif.juststation.global.security.permission.impl.PermissionDetectorImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Set;

/**
 * 权限配置
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/12/17 16:10
 */
@Configuration
public class PermissionConfig {

    @Bean
    @ConditionalOnMissingBean(PermissionMapper.class)
    public PermissionMapper permissionMapper() {
        return new PermissionMapper() {
            @Override
            public Set<String> getUserRoleSet(String username) {
                return Collections.emptySet();
            }

            @Override
            public Set<String> getUserKeySet(String username) {
                return Collections.emptySet();
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(PermissionDetector.class)
    public PermissionDetector permissionDetector() {
        return new PermissionDetectorImpl();
    }
}
