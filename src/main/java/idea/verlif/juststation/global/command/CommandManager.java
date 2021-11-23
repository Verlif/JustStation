package idea.verlif.juststation.global.command;

import idea.verlif.juststation.global.command.exception.CommandException;
import idea.verlif.juststation.global.util.MessagesUtils;
import idea.verlif.juststation.global.util.PrintUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.logging.Level;

/**
 * 指令管理器 <br/>
 * 用于指令的载入、获取等操作
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/15 10:30
 */
@Component
public class CommandManager {

    private final HashMap<String, Command> commandHashMap;
    private final Set<String> allowedKey;
    private final Set<String> blockKey;

    /**
     * 屏蔽模式；当开启屏蔽模式时，会忽略允许列表；反之则只会检测允许列表
     */
    private static boolean MODE_BLOCK = true;

    public CommandManager(
            @Autowired ApplicationContext appContext,
            @Autowired CommandConfig commandConfig) {
        // 初始化命令集
        commandHashMap = new HashMap<>();
        allowedKey = new HashSet<>();
        blockKey = new HashSet<>();

        // 加载指令模式与屏蔽名单
        if (commandConfig.getAllowed().length > 0) {
            MODE_BLOCK = false;
        }
        allowedKey.addAll(Arrays.asList(commandConfig.getAllowed()));
        blockKey.addAll(Arrays.asList(commandConfig.getBlocked()));

        // 加载指令
        String[] commandArray = appContext.getBeanNamesForType(Command.class);
        for (String s : commandArray) {
            Command command = (Command) appContext.getBean(s);
            addCommand(command);
        }
    }

    public void loadCommand(Set<Class<? extends Command>> commandSet) {
        // 指令清空
        this.commandHashMap.clear();
        this.blockKey.clear();
        // 加载用户指令
        for (Class<? extends Command> command : commandSet) {
            addCommand(command);
        }
    }

    public Set<String> getAllCommandKey() {
        return commandHashMap.keySet();
    }

    public Set<Command> getAllCommand() {
        return new HashSet<>(commandHashMap.values());
    }

    public Command getCommand(String key) {
        return commandHashMap.get(key);
    }

    public void addFilter(String key) {
        blockKey.add(key);
    }

    public void removeFilter(String key) {
        blockKey.remove(key);
    }

    /**
     * 开启指令处理
     */
    public void start() {
        // 接受命令并执行
        PrintUtils.print(Level.INFO, MessagesUtils.message("command.info.init") + ": " + getAllCommand().size());
        Scanner scanner = new Scanner(System.in);
        while (true) {
            command(scanner.nextLine().trim().replaceAll(" +", " ").split(" "));
        }
    }

    public CommandCode command(String[] input) {
        if (input != null && input.length > 0) {
            String key = input[0];
            Command command = getCommand(key);
            if (command != null) {
                if (blockKey.contains(key)) {
                    throw new CommandException("指令被屏蔽 - " + key);
                }
                try {
                    CommandCode code;
                    if (input.length > 1) {
                        code = command.run(Arrays.copyOfRange(input, 1, input.length));
                    } else {
                        code = command.run(new String[]{});
                    }
                    switch (code) {
                        case OK:
                            PrintUtils.println("[" + key + "] >> " + MessagesUtils.message("command.code.ok"));
                            break;
                        case FAIL:
                            PrintUtils.println("[" + key + "] >> " + MessagesUtils.message("command.code.fail"));
                            break;
                        default:
                            PrintUtils.println("[" + key + "] >> " + MessagesUtils.message("command.code.error"));
                            break;
                    }
                    return code;
                } catch (CommandException e) {
                    PrintUtils.println("[" + key + "] >> " + e.getMessage());
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                return CommandCode.ERROR;
            } else {
                PrintUtils.println(MessagesUtils.message("command.error.unknown") + "[" + key + "]");
            }
        }
        return CommandCode.UNKNOWN;
    }

    /**
     * 运行指令
     *
     * @param key    指令Key
     * @param params 指令参数
     */
    public void runCommand(String key, String[] params) {
        Command command = getCommand(key);
        if (command == null) {
            command = getCommand("nsc");
            if (command != null) {
                command.run(new String[]{key});
            }
        } else {
            command.run(params);
        }
    }

    public void deleteCommand(Class<? extends Command> cl) {
        Command.CommandInfo commandInfo = cl.getAnnotation(Command.CommandInfo.class);
        if (commandInfo == null) {
            deleteCommandKey(cl.getSimpleName());
        } else {
            // 获取命令属性
            String[] commandName = commandInfo.key();
            // 清除命令
            for (String s : commandName) {
                deleteCommandKey(s);
            }
        }
    }

    private void deleteCommandKey(String commandKey) {
        commandHashMap.remove(commandKey);
    }

    private void addCommandKey(String commandKey, Command command) {
        commandHashMap.put(commandKey, command);
    }

    public void addCommand(Command command) {
        // 获取注释标记
        Command.CommandInfo commandInfo = command.getClass().getAnnotation(Command.CommandInfo.class);
        // 获取命令属性
        String[] commandName = commandInfo.key();
        // 注入命令
        for (String s : commandName) {
            if (MODE_BLOCK) {
                if (blockKey.contains(s)) {
                    continue;
                }
            } else {
                if (!allowedKey.contains(s)) {
                    continue;
                }
            }
            addCommandKey(s, command);
        }
    }

    public void addCommand(Class<? extends Command> cl) {
        // 获取注释标记
        Command.CommandInfo commandInfo = cl.getAnnotation(Command.CommandInfo.class);
        // 获取命令属性
        String[] commandName = commandInfo.key();
        try {
            Command co = cl.newInstance();
            // 注入命令
            for (String s : commandName) {
                addCommandKey(s, co);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}