package idea.verlif.justdemo.core.base.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/10 11:34
 */
@TableName("key_")
@Schema(name = "权限Key信息")
public class Key {

    @TableId(value = "key_id", type = IdType.AUTO)
    @Schema(name = "key值ID")
    private Integer keyId;

    @TableField("key_")
    @Schema(name = "key值")
    private String key;
}
