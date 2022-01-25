package idea.verlif.juststation.global.notice.email;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/7/13 14:57
 */
@Configuration
@ConfigurationProperties(prefix = "station.email")
public class EmailConfig {

    /**
     * 是否开启debug
     */
    protected boolean debug;

    /**
     * 邮件服务器主机地址
     */
    protected String host = "";

    /**
     * 邮件服务器端口
     */
    protected String port = "";

    /**
     * 邮件协议名称
     */
    protected String protocol = "";

    /**
     * 邮箱服务器邮箱地址
     */
    protected String email = "";

    /**
     * 邮箱服务器授权码
     */
    protected String password = "";

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
