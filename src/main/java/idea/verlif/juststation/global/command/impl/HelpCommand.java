package idea.verlif.juststation.global.command.impl;

import idea.verlif.juststation.global.command.Command;
import idea.verlif.juststation.global.command.CommandCode;
import idea.verlif.juststation.global.command.CommandManager;
import idea.verlif.juststation.global.util.OutUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.Set;

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
        Set<Command> commandSet = applicationContext.getBean(CommandManager.class).getAllCommand();
        for (Command command : commandSet) {
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
            if (commandParams.length > 0) {
                sb.append("\n");
                for (int i = 0; i < LEFT; i++) {
                    sb.append(" ");
                }
                sb.append(" params: ");
                for (String commandParam : commandParams) {
                    sb.append("[").append(commandParam).append("]").append(" ");
                }
            }
            OutUtils.printLine(sb.toString());
        }
        return CommandCode.OK;
    }

}
