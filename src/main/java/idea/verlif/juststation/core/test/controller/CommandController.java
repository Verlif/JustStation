package idea.verlif.juststation.core.test.controller;

import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.core.base.result.ext.FailResult;
import idea.verlif.juststation.core.base.result.ext.OkResult;
import idea.verlif.juststation.global.command.CommandCode;
import idea.verlif.juststation.global.command.CommandManager;
import idea.verlif.juststation.global.security.permission.Perm;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/18 14:11
 */
@RestController
@RequestMapping("/command")
@Api(tags = "指令URL")
public class CommandController {

    @Autowired
    private CommandManager commandManager;

    /**
     * 远程指令接口，允许通过URL的方式进行指令控制
     *
     * @param command 指令内容
     * @return 指令执行结果
     */
    @Perm(hasRole = "admin")
    @GetMapping
    @Operation(summary = "远程指令")
    public BaseResult<?> command(@RequestParam("command") String command) {
        CommandCode commandCode = commandManager.command(command.trim().replaceAll(" +", " ").split(" "));
        switch (commandCode) {
            case OK:
                return new OkResult<>();
            case UNKNOWN:
                return new FailResult<>("未知指令: " + command);
            case FAIL:
                return new FailResult<>("指令失败");
            case ERROR:
                return new FailResult<>("指令错误");
            default:
                return new FailResult<>();
        }
    }
}
