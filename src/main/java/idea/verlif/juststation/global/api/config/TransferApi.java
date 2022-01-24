package idea.verlif.juststation.global.api.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/1/21 11:06
 */
public class TransferApi {

    private static final String SPLIT = ",";

    /**
     * 原Api
     */
    private String src;

    private final List<String> srcList;

    /**
     * 目标Api
     */
    private String target;

    public TransferApi() {
        srcList = new ArrayList<>();
    }

    public void setSrc(String src) {
        this.src = src;
        if (src != null) {
            for (String s : src.split(SPLIT)) {
                srcList.add(s.trim());
            }
        }
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public List<String> getSrcList() {
        return srcList;
    }
}
