package idea.verlif.juststation.global.api;

import idea.verlif.juststation.global.log.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.regex.Pattern;

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
    private ApplicationContext context;

    @Autowired
    private ApiConfig apiConfig;

    @Autowired
    private LogService logService;

    @PostConstruct
    public void config() {
        /*
         FIXME: 这里存在一个BUG
         当有一个方法被多个路径使用，并提供了多种请求方法时，如果此时屏蔽路径A的一种请求方法和路径B的一种请求方法，则A与B的这两种请求方法都会被屏蔽掉。
         */
        Set<String> blockedList = new HashSet<>();
        Map<String, List<RequestMappingInfo>> infoMap = new HashMap<>();
        // 获取请求处理映射
        RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = handlerMapping.getHandlerMethods();
        // 载入路径表
        for (RequestMappingInfo info : map.keySet()) {
            for (String value : info.getPatternValues()) {
                infoMap.computeIfAbsent(value, k -> new ArrayList<>()).add(info);
            }
        }
        // 请求映射需要屏蔽的方法
        Map<RequestMappingInfo, Set<String>> urlRemoved = new HashMap<>();
        Map<RequestMappingInfo, Set<RequestMethod>> methodRemoved = new HashMap<>();
        // 遍历每一项API配置
        Set<RequestMappingInfo> temp = new HashSet<>();
        for (JustApi justApi : apiConfig.getBlocked()) {
            String[] apis = justApi.getApis();
            Set<String> match = new HashSet<>();
            // 遍历API配置的每一个API
            for (String api : apis) {
                // 模式匹配规则
                switch (justApi.getMode()) {
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
            }
            // 处理匹配的路径
            for (String key : match) {
                List<RequestMappingInfo> infoList = infoMap.get(key);
                for (RequestMappingInfo info : infoList) {
                    Set<String> urlList = urlRemoved.computeIfAbsent(info, k -> new HashSet<>());
                    urlList.add(key);
                    temp.add(info);
                }
            }
            // 添加本次受到影响的API方法
            for (RequestMappingInfo info : temp) {
                Set<RequestMethod> set = methodRemoved.computeIfAbsent(info, k -> new HashSet<>());
                set.addAll(Arrays.asList(justApi.getRequestMethods()));
                blockedList.addAll(urlRemoved.get(info));
            }
            temp.clear();
        }
        // 屏蔽api
        for (RequestMappingInfo info : urlRemoved.keySet()) {
            // 首先注销此处理映射
            handlerMapping.unregisterMapping(info);
            // 当此处理存在其他路径时，重注册
            Set<String> urlList = info.getPatternValues();
            Set<String> urlListRemoved = urlRemoved.get(info);
            if (urlList.size() > urlListRemoved.size()) {
                // 去除屏蔽API后重注册
                urlListRemoved.forEach(urlList::remove);
                HandlerMethod handlerMethod = map.get(info);
                handlerMapping.registerMapping(info, handlerMethod.getBean(), handlerMethod.getMethod());
            }
            // 新增API以重建不受影响的方法
            Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
            if (methods.size() == 0) {
                methods.addAll(Arrays.asList(RequestMethod.values()));
            }
            // 当且仅当原API信息的方法数大于需屏蔽的方法数时才重建
            if (methods.size() > methodRemoved.get(info).size()) {
                Set<RequestMethod> now = new HashSet<>(methods);
                now.removeAll(methodRemoved.get(info));
                RequestMappingInfo extraInfo = RequestMappingInfo.paths(urlListRemoved.toArray(new String[]{}))
                        .methods(now.toArray(new RequestMethod[]{}))
                        .build();
                HandlerMethod handlerMethod = map.get(info);
                handlerMapping.registerMapping(extraInfo, handlerMethod.getBean(), handlerMethod.getMethod());
            }
        }
        if (blockedList.size() > 0) {
            logService.info("Api blocked list - " + Arrays.toString(blockedList.toArray()));
        }
    }
}
