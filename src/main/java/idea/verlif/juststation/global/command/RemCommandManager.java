package idea.verlif.juststation.global.command;

import idea.verlif.juststation.global.command.exception.CommandException;
import idea.verlif.juststation.global.log.LogService;
import idea.verlif.juststation.global.util.MessagesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

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
    private final RemCommandConfig commandConfig;

    /**
     * 屏蔽模式；当开启屏蔽模式时，会忽略允许列表；反之则只会检测允许列表
     */
    private static boolean MODE_BLOCK = true;

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private LogService logService;

    public RemCommandManager(
            @Autowired RemCommandConfig remCommandConfig) {
        // 初始化命令集
        commandHashMap = new HashMap<>();
        allowedKey = new HashSet<>();
        blockKey = new HashSet<>();
        this.commandConfig = remCommandConfig;

        if (commandConfig.isEnable()) {
            // 加载指令模式与屏蔽名单
            if (remCommandConfig.getAllowed().length > 0) {
                MODE_BLOCK = false;
            }
            allowedKey.addAll(Arrays.asList(remCommandConfig.getAllowed()));
            blockKey.addAll(Arrays.asList(remCommandConfig.getBlocked()));
        }
    }

    @PostConstruct
    public void init() {
        if (commandConfig.isEnable()) {
            Map<String, RemCommand> beans = appContext.getBeansOfType(RemCommand.class);
            Set<String> names = new HashSet<>();
            // 加载指令
            for (RemCommand command : beans.values()) {
                addCommand(command);
                names.add(command.getClass().getSimpleName());
            }
            logService.info("RemCommand had been loaded " + names.size() + " - " + Arrays.toString(names.toArray(new String[]{})));
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

    public RemCommandResult command(String key, String params) {
        if (params != null) {
            return command(key, params.trim().replaceAll(" +", " ").split(" "));
        } else {
            return command(key, (String[]) null);
        }
    }

    public RemCommandResult command(String[] input) {
        return command(input[0], Arrays.copyOfRange(input, 1, input.length));
    }

    public RemCommandResult command(String key, String[] params) {
        if (key != null && key.length() > 0) {
            RemCommand command = getCommand(key);
            if (command != null) {
                if (blockKey.contains(key)) {
                    throw new CommandException("blocked - " + key);
                }
                try {
                    RemCommandResult result;
                    if (params != null && params.length > 1) {
                        result = command.exec(params);
                    } else {
                        result = command.exec(new String[]{});
                    }
                    switch (result.getCode()) {
                        case OK:
                            logService.info("[" + key + "] >> " + MessagesUtils.message("command.code.ok"));
                            break;
                        case FAIL:
                            logService.info("[" + key + "] >> " + MessagesUtils.message("command.code.fail"));
                            break;
                        default:
                            logService.info("[" + key + "] >> " + MessagesUtils.message("command.code.error"));
                            break;
                    }
                    return result;
                } catch (CommandException e) {
                    logService.warn("[" + key + "] >> " + e.getMessage());
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                return RemCommandResult.build(RemCommandResult.Code.ERROR);
            } else {
                logService.info(MessagesUtils.message("command.error.unknown") + "[" + key + "]");
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
            logService.info(command.getClass().getSimpleName() + " doesn't has @Rci");
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