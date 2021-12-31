package idea.verlif.justdemo.core.command;

import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.security.login.LoginService;
import idea.verlif.juststation.global.security.login.domain.LoginInfo;
import idea.verlif.juststation.global.security.login.domain.LoginTag;
import idea.verlif.juststation.global.util.PrintUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/18 13:12
 */
@ShellComponent("登录指令")
public class LoginCommand {

    @Autowired
    private LoginService loginService;

    @ShellMethod("命令行用户登录")
    public String login(
            @ShellOption(help = "登录用户名") String username,
            @ShellOption(help = "用户密码") String password,
            @ShellOption(help = "登录标志", defaultValue = "local") String tag) {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(username);
        loginInfo.setPassword(password);
        loginInfo.setTag(LoginTag.getTag(tag));
        BaseResult<?> result = loginService.login(loginInfo);
        PrintUtils.println(result.toString());
        return result.getMsg();
    }

}
