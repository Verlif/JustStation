package idea.verlif.justdemo.core.base.biz;

import idea.verlif.juststation.core.BaseBizAto;
import idea.verlif.justdemo.core.base.domain.UserFavorite;
import idea.verlif.justdemo.core.base.mapper.UserFavoriteMapper;
import org.springframework.stereotype.Service;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/17 15:27
 */
@Service
public class UserFavoriteBiz extends BaseBizAto<UserFavorite, UserFavoriteMapper> {
}
