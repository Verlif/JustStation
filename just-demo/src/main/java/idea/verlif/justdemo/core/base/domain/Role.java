package idea.verlif.justdemo.core.base.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/10 11:32
 */
@TableName("role")
@Data
@Schema(name = "角色信息")
public class Role {

    @TableId(value = "role_id", type = IdType.AUTO)
    @Schema(name = "角色ID")
    private Integer roleId;

    @Schema(name = "角色名称")
    private String roleName;

    /**
     * 角色Key
     */
    public enum RoleKey {
        /**
         * 访客
         */
        VISITOR,
        /**
         * 注册用户
         */
        USER,
        /**
         * 管理员
         */
        ADMIN
    }
}
