package idea.verlif.justdemo.core.base.domain.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import idea.verlif.justdemo.core.base.PageExtend;
import idea.verlif.justdemo.core.base.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/12 11:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "用户查询条件")
public class UserQuery extends PageExtend<User> {

    @Schema(name = "用户昵称筛选")
    private String nickname;

    public void setNickname(String nickname) {
        if (!"".equals(nickname)) {
            this.nickname = nickname;
        }
    }

    @Override
    public QueryWrapper<User> buildQueryWrapper() {
        QueryWrapper<User> wrapper = super.buildQueryWrapper();
        if (nickname != null) {
            wrapper.like("nickname", nickname);
        }
        return wrapper;
    }
}
