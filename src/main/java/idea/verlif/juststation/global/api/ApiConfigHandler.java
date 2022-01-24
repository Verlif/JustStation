package idea.verlif.juststation.global.api;

import idea.verlif.juststation.global.api.config.ApiConfig;
import idea.verlif.juststation.global.api.config.BlockedApi;
import idea.verlif.juststation.global.util.PrintUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.logging.Level;
import java.util.regex.Pattern;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/1/21 11:13
 */
public class ApiConfigHandler {

    private final ApiConfig apiConfig;
    private final RequestMappingHandlerMapping handlerMapping;
    private final Map<RequestMappingInfo, HandlerMethod> methodMap;

    private final Map<String, List<RequestMappingInfo>> infoMap;

    public ApiConfigHandler(RequestMappingHandlerMapping handlerMapping, ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
        this.handlerMapping = handlerMapping;

        methodMap = handlerMapping.getHandlerMethods();
        infoMap = new HashMap<>();
    }

    public void config() {
        // 获取请求处理映射
        initInfoMap(methodMap.keySet());
        // 请求映射需要屏蔽的方法
        handlerBlocked();
    }

    /**
     * 初始化路径表
     *
     * @param infoSet 路径处理Set
     */
    private void initInfoMap(Set<RequestMappingInfo> infoSet) {
        // 载入路径表
        for (RequestMappingInfo info : infoSet) {
            for (String value : info.getPatternValues()) {
                infoMap.computeIfAbsent(value, k -> new ArrayList<>()).add(info);
            }
        }
    }

    private void handlerBlocked() {
        HashMap<String, ApiMethod> blockedMap = new HashMap<>();
        for (BlockedApi blockedApi : apiConfig.getBlocked()) {
            String[] apis = blockedApi.getApis();
            Set<String> match = new HashSet<>();
            // 遍历API配置的每一个API
            for (String api : apis) {
                match.addAll(match(blockedApi.getMode(), api));
            }
            // 处理匹配的路径
            for (String key : match) {
                blockedMap.computeIfAbsent(key, k -> new ApiMethod(key)).getMethods().addAll(Arrays.asList(blockedApi.getRequestMethods()));
            }
        }
        for (ApiMethod apiMethod : blockedMap.values()) {
            List<RequestMappingInfo> infos = infoMap.get(apiMethod.api);
            if (infos != null && infos.size() > 0) {
                for (RequestMappingInfo info : infos) {
                    // 先注销相关Info
                    handlerMapping.unregisterMapping(info);
                    info.getPatternValues().remove(apiMethod.api);
                    info.getDirectPaths().remove(apiMethod.api);
                    HandlerMethod handlerMethod = methodMap.get(info);
                    // 注册回不受影响的API
                    handlerMapping.registerMapping(info, handlerMethod.getBean(), handlerMethod.getMethod());
                    // 对屏蔽的API进行处理
                    Set<RequestMethod> set = info.getMethodsCondition().getMethods();
                    Set<RequestMethod> newSet = new HashSet<>(set);
                    // 填充方法
                    if (newSet.size() == 0) {
                        newSet.addAll(Arrays.asList(RequestMethod.values()));
                    }
                    apiMethod.getMethods().forEach(newSet::remove);
                    // 若此时还存在可用方法，则重新注册可用方法
                    if (newSet.size() > 0) {
                        RequestMappingInfo extraInfo = RequestMappingInfo.paths(apiMethod.api)
                                .methods(newSet.toArray(new RequestMethod[]{}))
                                .build();
                        handlerMapping.registerMapping(extraInfo, handlerMethod.getBean(), handlerMethod.getMethod());
                    }
                }
            }
        }
        if (blockedMap.size() > 0) {
            PrintUtils.print(Level.INFO, "Api blocked - " + blockedMap.keySet());
        }
    }

    /**
     * 匹配API
     *
     * @param mode 匹配模式
     * @param api  匹配的目标API
     * @return 匹配的API表
     */
    private Set<String> match(BlockedApi.Mode mode, String api) {
        Set<String> match = new HashSet<>();
        // 模式匹配规则
        switch (mode) {
            case REGEX: {
                Pattern pattern = Pattern.compile(api);
                for (String key : infoMap.keySet()) {
                    if (pattern.matcher(key).matches()) {
                        match.add(key);
                    }
                }
                break;
            }
            case PREFIX: {
                for (String key : infoMap.keySet()) {
                    if (key.startsWith(api)) {
                        match.add(key);
                    }
                }
                break;
            }
            case SUFFIX: {
                for (String key : infoMap.keySet()) {
                    if (key.endsWith(api)) {
                        match.add(key);
                    }
                }
                break;
            }
            case CONTAIN: {
                for (String key : infoMap.keySet()) {
                    if (key.contains(api)) {
                        match.add(key);
                    }
                }
                break;
            }
            default: {
                List<RequestMappingInfo> info = infoMap.get(api);
                if (info != null) {
                    match.add(api);
                }
            }
        }
        return match;
    }

    private static final class ApiMethod {

        private final String api;

        private final Set<RequestMethod> methods;

        public ApiMethod(String api) {
            this.api = api;
            methods = new HashSet<>();
        }

        public void addMethod(RequestMethod method) {
            methods.add(method);
        }

        public String getApi() {
            return api;
        }

        public Set<RequestMethod> getMethods() {
            return methods;
        }
    }
}
