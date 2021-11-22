package idea.verlif.juststation.core.test.command;

import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.core.base.result.ResultCode;
import idea.verlif.juststation.global.command.Command;
import idea.verlif.juststation.global.command.CommandCode;
import idea.verlif.juststation.global.command.exception.CommandException;
import idea.verlif.juststation.global.security.login.LoginService;
import idea.verlif.juststation.global.security.login.domain.LoginInfo;
import idea.verlif.juststation.global.security.login.domain.LoginTag;
import idea.verlif.juststation.global.util.PrintUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/18 13:12
 */
@Command.CommandInfo(key = "login", description = "后台登录指令")
public class LoginCommand implements Command {

    @Autowired
    private LoginService loginService;

    @Override
    public CommandCode run(String[] params) {
        if (params.length == 0) {
            throw new CommandException("缺少登录的用户");
        } else if (params.length == 1) {
            throw new CommandException("缺少用户密码");
        }
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(params[0]);
        loginInfo.setPassword(params[1]);
        if (params.length > 2) {
            loginInfo.setTag(LoginTag.getTag(params[2]));
        } else {
            loginInfo.setTag(LoginTag.LOCAL);
        }
        BaseResult<?> result = loginService.login(loginInfo);
        PrintUtils.println(result.toString());
        if (!result.equals(ResultCode.SUCCESS)) {
            return CommandCode.FAIL;
        } else {
            return CommandCode.OK;
        }
    }

    @Override
    public String[] params() {
        return new String[]{"用户登录名", "用户密码", "?登录标识"};
    }
}
