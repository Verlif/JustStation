package idea.verlif.juststation.core.test.controller;

import idea.verlif.juststation.global.command.RemCommandManager;
import idea.verlif.juststation.global.command.RemCommandResult;
import idea.verlif.juststation.global.security.permission.Perm;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/18 14:11
 */
@RestController
@RequestMapping("/command")
@Api(tags = "指令URL")
public class RemCommandController {

    @Autowired
    private RemCommandManager commandManager;

    /**
     * 远程指令接口，允许通过URL的方式进行指令控制
     *
     * @param command 指令内容
     * @return 指令执行结果
     */
    @Perm(hasRole = "admin")
    @PostMapping("/{command}")
    @Operation(summary = "远程指令")
    public RemCommandResult command(
            @PathVariable("command") String command,
            @RequestParam(required = false) String[] params) {
        return commandManager.command(command, params);
    }
}
