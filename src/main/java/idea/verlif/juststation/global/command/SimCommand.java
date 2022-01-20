package idea.verlif.juststation.global.command;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.EqualsAndHashCode;

/**
 * 指令接口
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/15 10:11
 */
@EqualsAndHashCode
public abstract class SimCommand {

    private final StringBuilder sb;
    private JsonNode data;

    public SimCommand() {
        sb = new StringBuilder();
    }

    public SimCommandResult exec(String[] params) {
        sb.delete(0, sb.length());
        SimCommandResult.Code code = run(params);
        SimCommandResult result = new SimCommandResult();
        result.setCode(code);
        result.setData(data);
        if (sb.length() > 0) {
            result.setMessage(sb.substring(0, sb.length() - 1));
        }
        return result;
    }

    /**
     * 向远端输出单行信息 <br/>
     * 可重复调用，用于多行输出
     *
     * @param message 信息内容
     */
    public void outLine(CharSequence message) {
        sb.append(message).append("\n");
    }

    /**
     * 向远端输出数据 <br/>
     * 重复调用会替换之前的存储值
     *
     * @param data JSON格式数据
     */
    public void outData(JsonNode data) {
        this.data = data;
    }

    /**
     * 命令的执行内容
     * 此方法运行于主线程
     *
     * @param params 指令后的参数
     * @return 指令结果
     */
    protected abstract SimCommandResult.Code run(String[] params);

    /**
     * 参数说明
     *
     * @return 指令可用参数说明
     */
    public String[] params() {
        return null;
    }

}