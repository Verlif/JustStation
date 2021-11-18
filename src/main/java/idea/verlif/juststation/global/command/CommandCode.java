package idea.verlif.juststation.global.command;

import idea.verlif.juststation.global.util.MessagesUtils;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/18 14:23
 */
public enum CommandCode {

    /**
     * 指令正常完成
     */
    OK(MessagesUtils.message("command.code.ok")),
    /**
     * 指令执行失败
     */
    FAIL(MessagesUtils.message("command.code.fail")),
    /**
     * 指令出错
     */
    ERROR(MessagesUtils.message("command.code.error")),
    /**
     * 未知指令
     */
    UNKNOWN(MessagesUtils.message("command.code.unknown"));

    private final String desc;

    CommandCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
