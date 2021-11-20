package idea.verlif.juststation.global.notice;

import idea.verlif.juststation.global.util.OutUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

/**
 * 通知服务
 *
 * @author Verlif
 */
@Service
public class NoticeService {

    private static final HashMap<NoticeTag, NoticeHandler> HANDLER_HASH_MAP;

    static {
        HANDLER_HASH_MAP = new HashMap<>();
    }

    /**
     * 向通知服务中注册服务
     *
     * @param tag           通知标志
     * @param noticeHandler 需要注册的服务；已存在的通知标志会覆盖
     */
    public static void registerHandler(NoticeTag tag, NoticeHandler noticeHandler) {
        HANDLER_HASH_MAP.put(tag, noticeHandler);
    }

    /**
     * 发送通知
     *
     * @param target 接收目标
     * @param notice 通知内容
     * @param tag    通知标志
     * @return 是否发送成功
     */
    public boolean sendNotice(String target, Notice notice, NoticeTag tag) {
        NoticeHandler handler = HANDLER_HASH_MAP.get(tag);
        if (handler == null) {
            OutUtils.printLog(Level.WARNING, "no such notice - " + tag);
            return false;
        } else {
            return handler.sendNotice(target, notice);
        }
    }

    /**
     * 批量发送通知
     *
     * @param targetList 接收目标列表
     * @param notice     通知内容
     * @param tag        通知标志
     * @return 已成功发送的目标列表
     */
    public List<String> sendNotices(List<String> targetList, Notice notice, NoticeTag tag) {
        NoticeHandler handler = HANDLER_HASH_MAP.get(tag);
        if (handler == null) {
            return new ArrayList<>();
        } else {
            return handler.sendNotices(targetList, notice);
        }
    }
}
