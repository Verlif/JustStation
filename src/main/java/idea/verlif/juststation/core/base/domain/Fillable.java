package idea.verlif.juststation.core.base.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/8/3 17:10
 */
public interface Fillable {

	default void fill(Object o) {
		Class<?> thisCl = getClass();
		if (thisCl == null) {
			return;
		}
		Class<?> cl = o.getClass();
		List<Field> list = new ArrayList<>();
		do {
			Collections.addAll(list, cl.getDeclaredFields());
			cl = cl.getSuperclass();
		} while (cl != null);
		for (Field field : list) {
			field.setAccessible(true);
			Field thisField = null;
			do {
				try {
					thisField = thisCl.getDeclaredField(field.getName());
					break;
				} catch (Exception e) {
					if (thisCl != null) {
						thisCl = thisCl.getSuperclass();
					} else {
						break;
					}
				}
			} while (thisCl != null);
			try {
				if (thisField != null) {
					IgnoredFill fill = thisField.getAnnotation(IgnoredFill.class);
					if (fill == null) {
						Object o1 = field.get(o);
						if (o1 != null) {
							thisField.setAccessible(true);
							thisField.set(this, o1);
						}
					}
				}
			} catch (Exception ignored) {
			}
		}
	}

	/**
	 * 自动填充数据，需要使用{@link AutoFill}注解来标记需要自动填充的属性及默认值 <br>
	 * 请注意，基础类型都有其默认值，基础类型只能在{@link FillMode#ALWAYS}模式下生效
	 */
	default void fill() {
		Class<?> thisCl = getClass();
		if (thisCl != null) {
			Class<?> cl = getClass();
			try {
				do {
					for (Field field : cl.getDeclaredFields()) {
						field.setAccessible(true);
						AutoFill fill = field.getAnnotation(AutoFill.class);
						if (fill != null) {
							Object o = field.get(this);
							if (fill.mode() == FillMode.ALWAYS || o == null) {
								switch (field.getType().getSimpleName()) {
									case "byte":
									case "Byte":
										field.setByte(this, Byte.parseByte(fill.value()));
										break;
									case "int":
									case "Integer":
										field.set(this, Integer.parseInt(fill.value()));
										break;
									case "long":
									case "Long":
										field.set(this, Long.parseLong(fill.value()));
										break;
									case "double":
									case "Double":
										field.set(this, Double.parseDouble(fill.value()));
										break;
									case "boolean":
									case "Boolean":
										field.set(this, Boolean.parseBoolean(fill.value()));
										break;
									case "Date":
										if (fill.value().length() == 0) {
											field.set(this, new Date());
										} else {
											field.set(this, DateFormat.getDateInstance().parse(fill.value()));
										}
										break;
									case "LocalDate":
										if (fill.value().length() == 0) {
											field.set(this, LocalDate.now());
										} else {
											field.set(this, LocalDate.parse(fill.value()));
										}
										break;
									case "LocalDateTime":
										if (fill.value().length() == 0) {
											field.set(this, LocalDateTime.now());
										} else {
											field.set(this, LocalDateTime.parse(fill.value()));
										}
										break;
									case "char":
									case "Character":
										field.set(this, fill.value().charAt(0));
										break;
									case "String":
										field.set(this, fill.value());
										break;
									default:
										if (Fillable.class.isAssignableFrom(field.getType())) {
											Fillable fillable = (Fillable) field.getType().newInstance();
											fillable.fill();
											field.set(this, fillable);
										} else {
											throw new UnsupportedOperationException("不支持该类型 - " + field.getType().getName());
										}
										break;
								}
							}
						}
					}
					cl = cl.getSuperclass();
				} while (cl != null);
			} catch (IllegalAccessException | ParseException | InstantiationException e) {
				e.printStackTrace();
			}
		}
	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@interface IgnoredFill {
	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@interface AutoFill {

		/**
		 * 填充内容
		 */
		String value();

		/**
		 * 填充类型
		 */
		FillMode mode() default FillMode.IF_NULL;
	}

	enum FillMode {

		/**
		 * 总是填充，无论是否是空值
		 */
		ALWAYS,

		/**
		 * 仅在空值时填充
		 */
		IF_NULL
	}
}
