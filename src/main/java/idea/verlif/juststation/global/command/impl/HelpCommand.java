package idea.verlif.juststation.global.command.impl;

import com.alibaba.fastjson.JSONArray;
import idea.verlif.juststation.global.command.Rci;
import idea.verlif.juststation.global.command.RemCommand;
import idea.verlif.juststation.global.command.RemCommandManager;
import idea.verlif.juststation.global.command.RemCommandResult;
import idea.verlif.juststation.global.util.PrintUtils;
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
@Rci(key = {"help", "h"}, description = "帮助")
public class HelpCommand extends RemCommand {

    /**
     * 排版间距
     */
    private static final int LEFT = 30;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public RemCommandResult.Code run(String[] params) {
        // 获取所有指令
        Set<RemCommand> commandSet = applicationContext.getBean(RemCommandManager.class).getAllCommand();

        HashMap<RemCommand, String> hashMap = new HashMap<>(commandSet.size() * 2);
        List<RemCommand> list = new ArrayList<>(commandSet);
        // 加载所有指令Key
        for (RemCommand command : commandSet) {
            // 获取注释标记
            Rci rci = command.getClass().getAnnotation(Rci.class);
            // 获取命令属性
            String[] commandName = rci.key();
            // 注入命令
            for (String s : commandName) {
                hashMap.put(command, s);
                break;
            }
        }
        // 对指令Key进行排序
        list.sort(Comparator.comparingInt(o -> hashMap.get(o).charAt(0)));

        JSONArray array = new JSONArray();
        for (RemCommand command : list) {
            CommandInfo commandInfo = new CommandInfo();
            Rci info = command.getClass().getAnnotation(Rci.class);
            // 设定指令Key
            commandInfo.setKey(info.key());
            // 设定指令描述
            commandInfo.setDesc(info.description());
            // 设定指令参数说明
            commandInfo.setParams(command.params());
            array.add(commandInfo);
        }
        outData(array);
        return RemCommandResult.Code.OK;
    }

    @Data
    private static class CommandInfo implements Serializable {

        private String[] key;

        private String desc;

        private String[] params;
    }
}
