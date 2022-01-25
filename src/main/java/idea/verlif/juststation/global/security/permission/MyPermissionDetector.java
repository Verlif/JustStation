package idea.verlif.juststation.global.security.permission;

import idea.verlif.juststation.global.util.SecurityUtils;
import idea.verlif.spring.permission.PermData;
import idea.verlif.spring.permission.PermissionDetector;
import org.springframework.stereotype.Component;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/1/25 17:12
 */
@Component
public class MyPermissionDetector implements PermissionDetector<String> {

    @Override
    public boolean hasRole(PermData<String> data, String role) {
        return data.getRoles().stream().anyMatch(s -> s.equals(role));
    }

    @Override
    public boolean hasKey(PermData<String> data, String key) {
        return data.getKeys().stream().anyMatch(s -> s.equals(key));
    }

    @Override
    public PermData<String> getRequestData() {
        return SecurityUtils.getLoginUser();
    }
}
