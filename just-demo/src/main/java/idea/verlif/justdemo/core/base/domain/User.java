package idea.verlif.justdemo.core.base.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import idea.verlif.juststation.global.security.login.domain.BaseUser;
import idea.verlif.juststation.global.validation.EnumValid;
import idea.verlif.juststation.global.validation.group.All;
import idea.verlif.juststation.global.validation.group.Insert;
import idea.verlif.juststation.global.validation.group.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
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
    @Null(groups = {Insert.class, Update.class})
    private Integer userId;

    @Schema(name = "用户昵称")
    @NotBlank(groups = {Insert.class, Update.class})
    @Size(min = 1, max = 24)
    private String nickname;

    @EnumValid(allowed = {"admin"}, nullable = false,
            groups = All.class,
            message = "不允许的角色")
    @TableField(exist = false)
    private Role.RoleKey roleKey;

    public void setRoleKey(String roleKey) {
        this.roleKey = Role.RoleKey.valueOf(roleKey);
    }

    public void setRoleKey(Role.RoleKey roleKey) {
        this.roleKey = roleKey;
    }

    public User() {
    }
}
