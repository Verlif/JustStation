package idea.verlif.juststation.global.command;

import idea.verlif.juststation.global.command.exception.CommandException;
import idea.verlif.juststation.global.log.LogService;
import idea.verlif.juststation.global.util.MessagesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty(prefix = "station.command", value = "enable")
public class SimCommandManager {

    private final HashMap<String, SimCommand> commandHashMap;
    private final Set<String> allowedKey;
    private final Set<String> blockKey;

    /**
     * 屏蔽模式；当开启屏蔽模式时，会忽略允许列表；反之则只会检测允许列表
     */
    private static boolean MODE_BLOCK = true;

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private LogService logService;

    public SimCommandManager(
            @Autowired SimCommandConfig simCommandConfig) {
        // 初始化命令集
        commandHashMap = new HashMap<>();
        allowedKey = new HashSet<>();
        blockKey = new HashSet<>();

        // 加载指令模式与屏蔽名单
        if (simCommandConfig.getAllowed().length > 0) {
            MODE_BLOCK = false;
        }
        allowedKey.addAll(Arrays.asList(simCommandConfig.getAllowed()));
        blockKey.addAll(Arrays.asList(simCommandConfig.getBlocked()));
    }

    @PostConstruct
    public void init() {
        Map<String, SimCommand> beans = appContext.getBeansOfType(SimCommand.class);
        Set<String> names = new HashSet<>();
        // 加载指令
        for (SimCommand command : beans.values()) {
            addCommand(command);
            names.add(command.getClass().getSimpleName());
        }
        if (names.size() == 0) {
            logService.info("No remCommand had been loaded");
        } else {
            logService.info("RemCommand had been loaded " + names.size() + " - " + Arrays.toString(names.toArray(new String[]{})));
        }
    }

    public void loadCommand(Set<Class<? extends SimCommand>> commandSet) {
        // 指令清空
        this.commandHashMap.clear();
        this.blockKey.clear();
        // 加载用户指令
        for (Class<? extends SimCommand> command : commandSet) {
            addCommand(command);
        }
    }

    public Set<String> getAllCommandKey() {
        return commandHashMap.keySet();
    }

    public Set<SimCommand> getAllCommand() {
        return new HashSet<>(commandHashMap.values());
    }

    public SimCommand getCommand(String key) {
        return commandHashMap.get(key);
    }

    public void addFilter(String key) {
        blockKey.add(key);
    }

    public void removeFilter(String key) {
        blockKey.remove(key);
    }

    public SimCommandResult command(String input) {
        return command(input.trim().replaceAll(" +", " ").split(" "));
    }

    public SimCommandResult command(String key, String params) {
        if (params != null) {
            return command(key, params.trim().replaceAll(" +", " ").split(" "));
        } else {
            return command(key, (String[]) null);
        }
    }

    public SimCommandResult command(String[] input) {
        return command(input[0], Arrays.copyOfRange(input, 1, input.length));
    }

    public SimCommandResult command(String key, String[] params) {
        if (key != null && key.length() > 0) {
            SimCommand command = getCommand(key);
            if (command != null) {
                if (blockKey.contains(key)) {
                    throw new CommandException("blocked - " + key);
                }
                try {
                    SimCommandResult result;
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
                return SimCommandResult.build(SimCommandResult.Code.ERROR);
            } else {
                logService.info(MessagesUtils.message("command.error.unknown") + "[" + key + "]");
            }
        }
        return SimCommandResult.build(SimCommandResult.Code.FAIL, MessagesUtils.message("command.code.unknown"));
    }

    public void deleteCommand(Class<? extends SimCommand> cl) {
        Sci sci = cl.getAnnotation(Sci.class);
        if (sci == null) {
            deleteCommandKey(cl.getSimpleName());
        } else {
            // 获取命令属性
            String[] commandName = sci.key();
            // 清除命令
            for (String s : commandName) {
                deleteCommandKey(s);
            }
        }
    }

    private void deleteCommandKey(String commandKey) {
        commandHashMap.remove(commandKey);
    }

    private void addCommandKey(String commandKey, SimCommand command) {
        commandHashMap.put(commandKey, command);
    }

    /**
     * 底层添加指令方法
     *
     * @param command 指令对象
     */
    public void addCommand(SimCommand command) {
        // 获取注释标记
        Sci sci = command.getClass().getAnnotation(Sci.class);
        if (sci == null) {
            logService.info(command.getClass().getSimpleName() + " doesn't has @Rci");
            String key = command.getClass().getSimpleName();
            if (MODE_BLOCK && !blockKey.contains(key) || !MODE_BLOCK && allowedKey.contains(key)) {
                addCommandKey(key, command);
            }
        } else {
            // 获取命令属性
            String[] commandName = sci.key();
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

    public void addCommand(Class<? extends SimCommand> cl) {
        // 获取注释标记
        Sci sci = cl.getAnnotation(Sci.class);
        // 获取命令属性
        String[] commandName = sci.key();
        try {
            SimCommand co = cl.newInstance();
            // 注入命令
            for (String s : commandName) {
                addCommandKey(s, co);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}