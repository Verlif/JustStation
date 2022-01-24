package idea.verlif.juststation.global.api.config;

import idea.verlif.juststation.global.util.PrintUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/1/19 10:35
 */
public class BlockedApi {

    private static final String SPLIT = ",";

    /**
     * api路径，多路径用英文,隔开
     */
    private String api;

    /**
     * 匹配模式
     */
    private String mode;

    /**
     * 请求方法，多方法用英文,隔开，包括POST、GET、DELETE等，不区分大小写。<br/>
     * 为空时，则包括了所有的方法。
     */
    private String method;

    public void setApi(String api) {
        this.api = api;
    }

    public String getApi() {
        return api;
    }

    /**
     * 获取所有的API
     */
    public String[] getApis() {
        if (api == null) {
            return new String[]{};
        }
        String[] apis = api.split(SPLIT);
        for (int i = 0, length = apis.length; i < length; i++) {
            apis[i] = apis[i].trim();
        }
        return apis;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public RequestMethod[] getRequestMethods() {
        if (method == null) {
            return RequestMethod.values();
        } else {
            String[] ms = method.toUpperCase(Locale.ROOT).split(SPLIT);
            if (ms.length == 0) {
                return RequestMethod.values();
            } else {
                List<RequestMethod> list = new ArrayList<>();
                for (String m : ms) {
                    try {
                        RequestMethod method = RequestMethod.valueOf(m.trim());
                        list.add(method);
                    } catch (Exception ignored) {
                        PrintUtils.print("No request method such as - " + m + " at " + api);
                    }
                }
                return list.toArray(new RequestMethod[]{});
            }
        }
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Mode getMode() {
        if (mode == null) {
            return Mode.DEFAULT;
        } else {
            return Mode.getMode(mode.toUpperCase(Locale.ROOT));
        }
    }

    @Override
    public String toString() {
        return "JustApi{" +
                "api='" + Arrays.toString(getApis()) + '\'' +
                ", method='" + Arrays.toString(getRequestMethods()) + '\'' +
                '}';
    }

    /**
     * 匹配模式
     */
    public enum Mode {
        /**
         * 默认匹配模式，字段全匹配
         */
        DEFAULT,

        /**
         * 正则匹配
         */
        REGEX,

        /**
         * 前缀
         */
        PREFIX,

        /**
         * 后缀
         */
        SUFFIX,

        /**
         * 包含
         */
        CONTAIN
        ;

        public static Mode getMode(String mode) {
            try {
                return Mode.valueOf(mode);
            } catch (IllegalArgumentException ignored) {
                return DEFAULT;
            }
        }
    }
}
