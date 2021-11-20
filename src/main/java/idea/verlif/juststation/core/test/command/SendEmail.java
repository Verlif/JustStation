package idea.verlif.juststation.core.test.command;

import idea.verlif.juststation.global.command.Command;
import idea.verlif.juststation.global.command.CommandCode;
import idea.verlif.juststation.global.notice.Notice;
import idea.verlif.juststation.global.notice.NoticeService;
import idea.verlif.juststation.global.notice.NoticeTag;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Verlif
 */
@Command.CommandInfo(key = "email", description = "发送邮件")
public class SendEmail implements Command {

    @Autowired
    private NoticeService noticeService;

    @Override
    public CommandCode run(String[] params) {
        if (params.length >= 3) {
            Notice notice = new Notice();
            notice.setTitle(params[1]);
            notice.setContent(params[2]);
            if (noticeService.sendNotice(params[0], notice, NoticeTag.EMAIL)) {
                return CommandCode.OK;
            } else {
                return CommandCode.FAIL;
            }
        } else {
            return CommandCode.FAIL;
        }
    }

    @Override
    public String[] params() {
        return new String[]{"收件人邮箱", "标题", "内容"};
    }
}
