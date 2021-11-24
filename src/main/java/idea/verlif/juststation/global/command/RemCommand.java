package idea.verlif.juststation.global.command;

import com.alibaba.fastjson.JSON;
import lombok.EqualsAndHashCode;

/**
 * 指令接口
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/15 10:11
 */
@EqualsAndHashCode
public abstract class RemCommand {

    private final StringBuilder sb;
    private JSON data;

    public RemCommand() {
        sb = new StringBuilder();
    }

    public RemCommandResult exec(String[] params) {
        sb.delete(0, sb.length());
        RemCommandResult.Code code = run(params);
        RemCommandResult result = new RemCommandResult();
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
    public void outData(JSON data) {
        this.data = data;
    }

    /**
     * 命令的执行内容
     * 此方法运行于主线程
     *
     * @param params 指令后的参数
     * @return 指令结果
     */
    protected abstract RemCommandResult.Code run(String[] params);

    /**
     * 参数说明
     *
     * @return 指令可用参数说明
     */
    public String[] params() {
        return null;
    }

}