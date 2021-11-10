package idea.verlif.juststation.global.security;

import idea.verlif.juststation.core.test.handler.PermissionMapperImpl;
import idea.verlif.juststation.core.test.handler.BaseUserMapperImpl;
import idea.verlif.juststation.global.security.impl.PermissionDetectorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 安全模块组件加载器
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/10 9:25
 */
@Configuration
public class SecurityComponentLoader {

    /**
     * 基础用户信息Mapper
     */
    @Bean
    public BaseUserMapper baseUserMapper() {
        return new BaseUserMapperImpl();
    }

    /**
     * 权限检测组件
     */
    @Bean(name = "pd")
    public PermissionDetector permissionDetector() {
        return new PermissionDetectorImpl();
    }

    /**
     * 权限获取组件
     */
    @Bean
    public PermissionMapper<?> permissionMapper() {
        return new PermissionMapperImpl();
    }
}
