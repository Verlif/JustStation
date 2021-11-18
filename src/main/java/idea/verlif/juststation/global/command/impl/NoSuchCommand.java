package idea.verlif.juststation.global.command.impl;

import idea.verlif.juststation.global.command.Command;
import idea.verlif.juststation.global.command.CommandCode;
import idea.verlif.juststation.global.command.exception.CommandException;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/18 11:24
 */
@Command.CommandInfo(key = "nsc", description = "没有相关指令")
public class NoSuchCommand implements Command {

    @Override
    public CommandCode run(String[] params) {
        if (params.length > 0) {
            throw new CommandException("没有找到相关指令 - " + params[0]);
        } else {
            throw new CommandException("未知指令");
        }
    }
}
