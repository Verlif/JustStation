package idea.verlif.juststation.global.validation;

import idea.verlif.parser.ParamParser;
import idea.verlif.parser.ParamParserService;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/1/27 14:50
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DateValid.DateValueValidator.class)
public @interface DateValid {

    /**
     * 仅允许在此之后的时间。
     * @see idea.verlif.parser.impl.DateParser#parser(String)
     */
    String after() default "";

    /**
     * 仅允许在此之前的时间。
     * @see idea.verlif.parser.impl.DateParser#parser(String)
     */
    String before() default "";

    /**
     * 判定时间大小时，是否允许相等的时间。
     */
    boolean allowedEqual() default true;

    /**
     * 错误提示
     */
    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class DateValueValidator implements ConstraintValidator<DateValid, Object> {

        private DateValid dateValid;
        private static final ParamParserService PPS;

        static {
            PPS = new ParamParserService();
        }

        public DateValueValidator() {
        }

        @Override
        public void initialize(DateValid constraintAnnotation) {
            this.dateValid = constraintAnnotation;
        }

        @Override
        public boolean isValid(Object o, ConstraintValidatorContext context) {
            ParamParser<Date> pp = PPS.getParser(Date.class);
            Date after = dateValid.after().length() == 0 ? null : pp.parser(dateValid.after());
            Date before = dateValid.before().length() == 0 ? null : pp.parser(dateValid.before());

            Date sou = null;
            if (o instanceof String) {
                sou = pp.parser((String) o);
            } else if (o instanceof Date) {
                sou = ((Date) o);
            } else if (o instanceof LocalDate) {
                ZoneId zoneId = ZoneId.systemDefault();
                LocalDate localDate = (LocalDate) o;
                ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
                sou = Date.from(zdt.toInstant());
            } else if (o instanceof LocalDateTime) {
                ZoneId zoneId = ZoneId.systemDefault();
                LocalDateTime localDateTime = (LocalDateTime) o;
                ZonedDateTime zdt = localDateTime.atZone(zoneId);
                sou = Date.from(zdt.toInstant());
            }

            if (sou == null) {
                return false;
            }
            if (after != null) {
                if (after.after(sou) || !dateValid.allowedEqual() && after.getTime() == sou.getTime()) {
                    return false;
                }
            }
            if (before != null) {
                return sou.before(before) && (dateValid.allowedEqual() || before.getTime() != sou.getTime());
            }
            return true;
        }
    }

}
