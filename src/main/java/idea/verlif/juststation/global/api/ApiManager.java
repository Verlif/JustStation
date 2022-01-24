package idea.verlif.juststation.global.api;

import idea.verlif.juststation.global.api.config.ApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;

/**
 * API管理器
 *
 * @author Verlif
 * @version 1.0
 * @date 2022/1/19 9:34
 */
@Component
@ConditionalOnProperty(prefix = "station.api", value = "enable")
public class ApiManager {

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Autowired
    private ApiConfig apiConfig;

    @PostConstruct
    public void config() {
        ApiConfigHandler handler = new ApiConfigHandler(handlerMapping, apiConfig);
        handler.config();
    }

}
