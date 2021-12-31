package idea.verlif.justdemo.core.command;

import idea.verlif.juststation.global.command.Rci;
import idea.verlif.juststation.global.command.RemCommand;
import idea.verlif.juststation.global.command.RemCommandResult;
import idea.verlif.juststation.global.notice.Notice;
import idea.verlif.juststation.global.notice.NoticeService;
import idea.verlif.juststation.global.notice.NoticeTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author Verlif
 */
@ShellComponent("邮件指令")
@Rci(key = "email", description = "发送邮件")
public class EmailCommand extends RemCommand {

    @Autowired
    private NoticeService noticeService;

    @Override
    public RemCommandResult.Code run(String[] params) {
        if (params.length >= 3) {
            Notice notice = new Notice();
            notice.setTitle(params[1]);
            notice.setContent(params[2]);
            if (noticeService.sendNotice(params[0], notice, NoticeTag.EMAIL)) {
                outLine("发送成功");
                return RemCommandResult.Code.OK;
            } else {
                outLine("发送失败");
                return RemCommandResult.Code.FAIL;
            }
        } else {
            outLine("参数不足");
            return RemCommandResult.Code.FAIL;
        }
    }

    @ShellMethod("发送邮件")
    public String email(
            @ShellOption(help = "收件人地址") @Email String target,
            @ShellOption(help = "邮件标题") @NotBlank String title,
            @ShellOption(help = "邮件内容") @NotBlank String content
    ) {
        Notice notice = new Notice();
        notice.setTitle(title);
        notice.setContent(content);
        if (noticeService.sendNotice(target, notice, NoticeTag.EMAIL)) {
            return "发送成功";
        } else {
            return "发送失败";
        }
    }

    @Override
    public String[] params() {
        return new String[]{"收件人邮箱", "标题", "内容"};
    }
}
