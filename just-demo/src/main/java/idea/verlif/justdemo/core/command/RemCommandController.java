package idea.verlif.justdemo.core.command;

import idea.verlif.juststation.global.command.SimCommandManager;
import idea.verlif.juststation.global.command.SimCommandResult;
import idea.verlif.juststation.global.security.permission.Perm;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 远程指令执行接口
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/18 14:11
 */
@RestController
@RequestMapping("/command")
@Api(tags = "远程URL指令测试")
public class RemCommandController {

    /**
     * 远程指令管理器
     */
    @Autowired
    private SimCommandManager commandManager;

    /**
     * 远程指令接口，允许通过URL的方式进行指令控制
     *
     * @param command 指令内容
     * @return 指令执行结果
     */
    @Perm(hasRole = "admin")
    @PostMapping("/{command}")
    @Operation(summary = "远程指令")
    public SimCommandResult command(
            @PathVariable("command") String command,
            @RequestParam(required = false) String[] params) {
        return commandManager.command(command, params);
    }
}
