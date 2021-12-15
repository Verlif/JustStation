# URL指令

做这个模块的目的只是为了好玩，这种方式其实和写一个API方法差不多，只是一些比较杂的功能写API的话，感觉没有必要。并且写起来还要写controller，写service什么的，太过于繁琐，还不如写一个指令来直接实现方便。

## 使用

实现`RemCommand`接口，并使用`@Rci`注解标记。例如：

```java
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
```

上面的邮件指令是结合了SpringShell的，目前还没有做两者的自动转换处理。

------
URL指令可以通过help来获取指令信息

```json
{
  "message": null,
  "code": "OK",
  "data": [
    {
      "key": [
        "email"
      ],
      "desc": "发送邮件",
      "params": [
        "收件人邮箱",
        "标题",
        "内容"
      ]
    },
    {
      "key": [
        "help",
        "h"
      ],
      "desc": "帮助",
      "params": null
    }
  ]
}
```

------

## RemCommandController

`RemCommandController`只是一个测试类，用于调用URL指令。对于测试类中的指令接口，可以通过以下格式执行指令`/command/email?params=target@qq.com,这里是标题,这里是内容`。
