package idea.verlif.justdemo.core.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import idea.verlif.justdemo.core.base.domain.UserFavorite;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/17 15:26
 */
@Mapper
public interface UserFavoriteMapper extends BaseMapper<UserFavorite> {
}
