package idea.verlif.juststation.global.notice;

import java.util.List;

/**
 * 通知处理器
 *
 * @author Verlif
 */
public interface NoticeHandler {

    /**
     * 发送通知
     *
     * @param target 接收目标
     * @param notice 通知内容
     * @return 是否发送成功
     */
    boolean sendNotice(String target, Notice notice);

    /**
     * 批量发送通知
     *
     * @param targetList 接收目标列表
     * @param notice     通知内容
     * @return 已成功发送的目标列表
     */
    List<String> sendNotices(List<String> targetList, Notice notice);
}
