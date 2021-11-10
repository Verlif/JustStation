package idea.verlif.juststation.core.test.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import idea.verlif.juststation.core.base.domain.Checkable;
import idea.verlif.juststation.global.security.login.domain.BaseUser;
import io.swagger.models.auth.In;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.Set;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/10 10:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("user")
@Schema(name = "用户信息")
public class User extends BaseUser<Integer> implements Checkable {

    @Schema(name = "用户ID")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @Schema(name = "用户昵称")
    private String nickname;

    @Schema(name = "权限Key集")
    @TableField(exist = false)
    private Set<String> keySet;

    @Schema(name = "角色集")
    @TableField(exist = false)
    private Set<String> roleSet;

    public User() {
        keySet = Collections.emptySet();
        roleSet = Collections.emptySet();
    }
}
