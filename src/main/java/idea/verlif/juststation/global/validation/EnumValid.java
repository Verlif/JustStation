package idea.verlif.juststation.global.validation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.Arrays;
import java.util.Locale;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/20 10:30
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EnumValid.EnumValueValidator.class)
public @interface EnumValid {

    /**
     * 允许的枚举类name() - String。<br/>
     * 当有值时，{@linkplain #blocked()}无效
     */
    String[] allowed() default {};

    /**
     * 禁止的枚举类name() - String。
     * 当{@linkplain #allowed()}有值时，此方法无效
     */
    String[] blocked() default {};

    /**
     * 是否允许空值
     */
    boolean nullable() default true;

    /**
     * 错误提示
     */
    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 枚举类参数验证器
     */
    class EnumValueValidator implements ConstraintValidator<EnumValid, Object> {

        private EnumValid enumValid;

        @Override
        public void initialize(EnumValid enumValid) {
            this.enumValid = enumValid;
        }

        @Override
        public boolean isValid(Object o, ConstraintValidatorContext context) {
            if (o == null) {
                return enumValid.nullable();
            }
            String key;
            if (o instanceof Enum<?>) {
                key = ((Enum<?>) o).name().toUpperCase(Locale.ROOT);
            } else {
                key = o.toString().toUpperCase(Locale.ROOT);
            }
            String finalKey = key;
            if (enumValid.allowed().length > 0) {
                return Arrays.stream(enumValid.allowed()).anyMatch(s -> s.toUpperCase(Locale.ROOT).equals(finalKey));
            } else {
                return Arrays.stream(enumValid.blocked()).noneMatch(s -> s.toUpperCase(Locale.ROOT).equals(finalKey));
            }
        }
    }
}
