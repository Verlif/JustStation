package idea.verlif.juststation.global.command.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import idea.verlif.juststation.global.command.Sci;
import idea.verlif.juststation.global.command.SimCommand;
import idea.verlif.juststation.global.command.SimCommandManager;
import idea.verlif.juststation.global.command.SimCommandResult;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.util.*;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/18 11:56
 */
@Sci(key = {"help", "h"}, description = "帮助")
public class HelpCommand extends SimCommand {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public SimCommandResult.Code run(String[] params) {
        // 获取所有指令
        Set<SimCommand> commandSet = applicationContext.getBean(SimCommandManager.class).getAllCommand();

        HashMap<SimCommand, String> hashMap = new HashMap<>(commandSet.size() * 2);
        List<SimCommand> list = new ArrayList<>(commandSet);
        // 加载所有指令Key
        for (SimCommand command : commandSet) {
            // 获取注释标记
            Sci sci = command.getClass().getAnnotation(Sci.class);
            // 获取命令属性
            String[] commandName = sci.key();
            // 注入命令
            for (String s : commandName) {
                hashMap.put(command, s);
                break;
            }
        }
        // 对指令Key进行排序
        list.sort(Comparator.comparingInt(o -> hashMap.get(o).charAt(0)));

        ArrayNode array = mapper.createArrayNode();
        for (SimCommand command : list) {
            CommandInfo commandInfo = new CommandInfo();
            Sci info = command.getClass().getAnnotation(Sci.class);
            // 设定指令Key
            commandInfo.setKey(info.key());
            // 设定指令描述
            commandInfo.setDesc(info.description());
            // 设定指令参数说明
            commandInfo.setParams(command.params());
            array.add(mapper.valueToTree(commandInfo));
        }
        outData(array);
        return SimCommandResult.Code.OK;
    }

    @Data
    private static class CommandInfo implements Serializable {

        private String[] key;

        private String desc;

        private String[] params;
    }
}
