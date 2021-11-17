package idea.verlif.juststation.core.test.domain;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/17 15:25
 */
@Schema(name = "用户喜欢的东西")
public class UserFavorite {

    @Schema(name = "用户ID")
    private Integer userId;

    @Schema(name = "物品名称")
    private String name;
}
