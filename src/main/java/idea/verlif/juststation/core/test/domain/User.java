package idea.verlif.juststation.core.test.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import idea.verlif.juststation.global.security.login.domain.BaseUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/10 10:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("user")
@Schema(name = "用户信息")
public class User extends BaseUser {

    @Schema(name = "用户ID")
    @TableId(value = "user_id", type = IdType.AUTO)
    @IgnoreCheck
    private Integer userId;

    @Schema(name = "用户昵称")
    private String nickname;

    public User() {
    }
}
