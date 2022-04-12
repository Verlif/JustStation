package idea.verlif.justdemo.core.norepeat;

import idea.verlif.spring.norepeat.entity.RequestFlag;
import idea.verlif.spring.norepeat.judgment.RepeatJudgment;
import org.springframework.stereotype.Component;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/4/11 16:14
 */
@Component
public class NoRepeatImpl implements RepeatJudgment {

    @Override
    public boolean isRepeat(RequestFlag oldFlag, RequestFlag newFlag) {
        return true;
    }
}
