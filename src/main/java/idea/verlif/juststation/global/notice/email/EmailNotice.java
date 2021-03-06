package idea.verlif.juststation.global.notice.email;

import idea.verlif.juststation.global.notice.Notice;
import idea.verlif.juststation.global.notice.NoticeComponent;
import idea.verlif.juststation.global.notice.NoticeHandler;
import idea.verlif.juststation.global.notice.NoticeTag;
import idea.verlif.juststation.global.util.PrintUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

/**
 * 电子邮件通知
 *
 * @author Verlif
 */
@NoticeComponent(tags = NoticeTag.EMAIL)
public class EmailNotice extends NoticeHandler {

    private final EmailConfig emailConfig;

    private final Properties props;
    private final Authenticator authenticator;

    public EmailNotice(@Autowired EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
        props = buildProp();
        authenticator = buildAuthenticator(props);
    }

    @Override
    public boolean sendNotice(String target, Notice notice) {
        Session session = Session.getInstance(props, authenticator);
        try {
            //邮件内容
            Message msg = new MimeMessage(session);
            msg.setSubject(notice.getTitle());
            msg.setText(notice.getContent());
            //邮件发送者
            msg.setFrom(new InternetAddress(emailConfig.getEmail()));

            //发送邮件
            Transport transport = session.getTransport();
            transport.connect(emailConfig.getHost(), emailConfig.getEmail(), emailConfig.getPassword());
            transport.sendMessage(msg, new Address[]{new InternetAddress(target)});
            transport.close();
            return true;
        } catch (MessagingException e) {
            PrintUtils.print(Level.SEVERE, e.getMessage());
            return false;
        }
    }

    @Override
    public List<String> sendNotices(List<String> targetList, Notice notice) {
        List<String> list = new ArrayList<>();
        Session session = Session.getInstance(props, authenticator);
        try {
            //邮件内容
            Message msg = new MimeMessage(session);
            msg.setSubject(notice.getTitle());
            msg.setText(notice.getContent());
            //邮件发送者
            msg.setFrom(new InternetAddress(emailConfig.getEmail()));

            //发送邮件
            Transport transport = session.getTransport();
            transport.connect(emailConfig.getHost(), emailConfig.getEmail(), emailConfig.getPassword());
            int size = targetList.size();
            Address[] addresses = new Address[size];
            for (int i = 0; i < size; i++) {
                addresses[i] = new InternetAddress(targetList.get(i));
            }
            transport.sendMessage(msg, addresses);
            transport.close();
            return targetList;
        } catch (MessagingException e) {
            PrintUtils.print(Level.SEVERE, e.getMessage());
        }
        return list;
    }

    private Properties buildProp() {
        Properties props = new Properties();

        props.setProperty("mail.debug", String.valueOf(emailConfig.isDebug()));
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.host", emailConfig.getHost());
        props.setProperty("mail.transport.protocol", emailConfig.getProtocol());
        props.put("mail.smtp.port", emailConfig.getPort());
        props.put("mail.user", emailConfig.getEmail());
        props.put("mail.password", emailConfig.getPassword());

        return props;
    }

    private Authenticator buildAuthenticator(Properties props) {
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
    }
}
