package idea.verlif.juststation.core.test.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import idea.verlif.juststation.global.security.login.domain.BaseUser;
import idea.verlif.juststation.global.validation.Insert;
import idea.verlif.juststation.global.validation.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

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
    @Null(groups = Insert.class)
    @NotNull(groups = Update.class)
    private Integer userId;

    @Schema(name = "用户昵称")
    @NotNull
    @Size(min = 1, max = 24)
    private String nickname;

    public User() {
    }
}
