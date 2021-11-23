package idea.verlif.juststation.global.command.impl;

import idea.verlif.juststation.global.command.Command;
import idea.verlif.juststation.global.command.CommandCode;
import idea.verlif.juststation.global.command.CommandManager;
import idea.verlif.juststation.global.util.PrintUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.*;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/18 11:56
 */
@Command.CommandInfo(key = {"help", "h"}, description = "帮助")
public class HelpCommand implements Command {

    /**
     * 排版间距
     */
    private static final int LEFT = 30;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public CommandCode run(String[] params) {
        // 获取所有指令
        Set<Command> commandSet = applicationContext.getBean(CommandManager.class).getAllCommand();

        HashMap<Command, String> hashMap = new HashMap<>(commandSet.size() * 2);
        List<Command> list = new ArrayList<>(commandSet);
        // 加载所有指令Key
        for (Command command : commandSet) {
            // 获取注释标记
            Command.CommandInfo commandInfo = command.getClass().getAnnotation(Command.CommandInfo.class);
            // 获取命令属性
            String[] commandName = commandInfo.key();
            // 注入命令
            for (String s : commandName) {
                hashMap.put(command, s);
                break;
            }
        }
        // 对指令Key进行排序
        list.sort(Comparator.comparingInt(o -> hashMap.get(o).charAt(0)));

        for (Command command : list) {
            CommandInfo info = command.getClass().getAnnotation(CommandInfo.class);
            String[] commandName = info.key();
            StringBuilder sb = new StringBuilder();
            sb.append(Arrays.toString(commandName));
            int length = sb.length();
            for (int i = length; i < LEFT; i++) {
                sb.append(" ");
            }
            sb.append(" - ").append(info.description());
            String[] commandParams = command.params();
            if (commandParams != null && commandParams.length > 0) {
                sb.append("\n");
                for (int i = 0; i < LEFT; i++) {
                    sb.append(" ");
                }
                sb.append(" └ ");
                for (String commandParam : commandParams) {
                    sb.append("[").append(commandParam).append("]").append(" ");
                }
            }
            PrintUtils.println(sb.toString());
        }
        return CommandCode.OK;
    }

}
