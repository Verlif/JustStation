package idea.verlif.juststation.global.command;

import idea.verlif.juststation.global.command.exception.CommandException;
import idea.verlif.juststation.global.util.MessagesUtils;
import idea.verlif.juststation.global.util.PrintUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * 指令管理器 <br/>
 * 用于指令的载入、获取等操作
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/15 10:30
 */
@Component
public class RemCommandManager {

    private final HashMap<String, RemCommand> commandHashMap;
    private final Set<String> allowedKey;
    private final Set<String> blockKey;

    /**
     * 屏蔽模式；当开启屏蔽模式时，会忽略允许列表；反之则只会检测允许列表
     */
    private static boolean MODE_BLOCK = true;

    public RemCommandManager(
            @Autowired ApplicationContext appContext,
            @Autowired RemCommandConfig remCommandConfig) {
        // 初始化命令集
        commandHashMap = new HashMap<>();
        allowedKey = new HashSet<>();
        blockKey = new HashSet<>();

        // 加载指令模式与屏蔽名单
        if (remCommandConfig.getAllowed().length > 0) {
            MODE_BLOCK = false;
        }
        allowedKey.addAll(Arrays.asList(remCommandConfig.getAllowed()));
        blockKey.addAll(Arrays.asList(remCommandConfig.getBlocked()));

        // 加载指令
        String[] commandArray = appContext.getBeanNamesForType(RemCommand.class);
        for (String s : commandArray) {
            RemCommand command = (RemCommand) appContext.getBean(s);
            addCommand(command);
        }
    }

    public void loadCommand(Set<Class<? extends RemCommand>> commandSet) {
        // 指令清空
        this.commandHashMap.clear();
        this.blockKey.clear();
        // 加载用户指令
        for (Class<? extends RemCommand> command : commandSet) {
            addCommand(command);
        }
    }

    public Set<String> getAllCommandKey() {
        return commandHashMap.keySet();
    }

    public Set<RemCommand> getAllCommand() {
        return new HashSet<>(commandHashMap.values());
    }

    public RemCommand getCommand(String key) {
        return commandHashMap.get(key);
    }

    public void addFilter(String key) {
        blockKey.add(key);
    }

    public void removeFilter(String key) {
        blockKey.remove(key);
    }

    public RemCommandResult command(String input) {
        return command(input.trim().replaceAll(" +", " ").split(" "));
    }

    public RemCommandResult command(String[] input) {
        if (input != null && input.length > 0) {
            String key = input[0];
            RemCommand command = getCommand(key);
            if (command != null) {
                if (blockKey.contains(key)) {
                    throw new CommandException("指令被屏蔽 - " + key);
                }
                try {
                    RemCommandResult result;
                    if (input.length > 1) {
                        result = command.exec(Arrays.copyOfRange(input, 1, input.length));
                    } else {
                        result = command.exec(new String[]{});
                    }
                    switch (result.getCode()) {
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
                    return result;
                } catch (CommandException e) {
                    PrintUtils.println("[" + key + "] >> " + e.getMessage());
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                return RemCommandResult.build(RemCommandResult.Code.ERROR);
            } else {
                PrintUtils.println(MessagesUtils.message("command.error.unknown") + "[" + key + "]");
            }
        }
        return RemCommandResult.build(RemCommandResult.Code.FAIL, MessagesUtils.message("command.code.unknown"));
    }

    public void deleteCommand(Class<? extends RemCommand> cl) {
        Rci rci = cl.getAnnotation(Rci.class);
        if (rci == null) {
            deleteCommandKey(cl.getSimpleName());
        } else {
            // 获取命令属性
            String[] commandName = rci.key();
            // 清除命令
            for (String s : commandName) {
                deleteCommandKey(s);
            }
        }
    }

    private void deleteCommandKey(String commandKey) {
        commandHashMap.remove(commandKey);
    }

    private void addCommandKey(String commandKey, RemCommand command) {
        commandHashMap.put(commandKey, command);
    }

    /**
     * 底层添加指令方法
     *
     * @param command 指令对象
     */
    public void addCommand(RemCommand command) {
        // 获取注释标记
        Rci rci = command.getClass().getAnnotation(Rci.class);
        if (rci == null) {
            String key = command.getClass().getSimpleName();
            if (MODE_BLOCK && !blockKey.contains(key) || !MODE_BLOCK && allowedKey.contains(key)) {
                addCommandKey(key, command);
            }
        } else {
            // 获取命令属性
            String[] commandName = rci.key();
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
    }

    public void addCommand(Class<? extends RemCommand> cl) {
        // 获取注释标记
        Rci rci = cl.getAnnotation(Rci.class);
        // 获取命令属性
        String[] commandName = rci.key();
        try {
            RemCommand co = cl.newInstance();
            // 注入命令
            for (String s : commandName) {
                addCommandKey(s, co);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}