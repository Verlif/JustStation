package idea.verlif.juststation.core.base.domain;

import io.swagger.annotations.ApiModelProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 允许检查是否有属性空缺或属性格式 <br>
 * 用{@link Uncheck}注解来标注不需要检测的属性及标签 <br>
 * 用{@link IgnoreCheck}注解来标注忽略检测的属性 <br>
 * <p>
 * 用{@link Format}注解来标记需要验证格式的属性，并调用{@link Checkable#formatCheck()}来判断格式 <br>
 * 可以判定的类型包括了实现了{@link Object#toString()}方法的所有类，
 * 原理就是通过调用{@code toString}方法来进行正则匹配
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/7/20 17:39
 */
public interface Checkable {

	/**
	 * 是否存在空值属性，判断所有没有标签或默认标签的属性
	 *
	 * @return 若存在空值属性，则返回属性值名称；否则返回NULL
	 */
	default String hasNull() {
		return hasNull("");
	}

	/**
	 * 是否存在空值属性，判断所有没有标签、默认标签及特定标签的属性
	 *
	 * @param tag 判断标签
	 * @return 若存在空值属性，则返回属性值名称（做了ApiModelProperty兼容，有这个注解则返回value()值）；否则返回NULL
	 */
	default String hasNull(String tag) {
		Class<?> cl = getClass();
		List<Field> list = new ArrayList<>();
		do {
			Collections.addAll(list, cl.getDeclaredFields());
			cl = cl.getSuperclass();
		} while (cl != null);
		try {
			for (Field field : list) {
				field.setAccessible(true);
				// 判断是否需要检测
				if (needCheck(field, tag)) {
					Object o = field.get(this);
					if (o == null) {
						return getFieldName(field);
					}
					// 字符串特殊判断
					if (o instanceof String) {
						if (((String) o).length() == 0) {
							return getFieldName(field);
						}
					}
					// 对日期进行判断
					if (o instanceof Date) {
						if (o.toString().length() == 0) {
							return getFieldName(field);
						}
					}
					// 对整形的值做判断
					if(o instanceof Integer){
						if ((Integer)o < 0) {
							return getFieldName(field)+"不能为负数！";
						}
					}
					// 对整形的值做判断
					if(o instanceof Long){
						if ((Long)o < 0) {
							return getFieldName(field)+"不能为负数！";
						}
					}
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 需要空值检测
	 *
	 * @param field 属性对象
	 * @param tag   需检测标签
	 * @return 是否需要空值检测
	 * @throws IllegalAccessException 值不存在
	 */
	default boolean needCheck(Field field, String tag) throws IllegalAccessException {
		// 判定是否忽略
		if (field.getAnnotation(IgnoreCheck.class) != null) {
			return false;
		}
		// 判定不检测标签
		Uncheck uncheck = field.getAnnotation(Uncheck.class);
		if (uncheck != null) {
			List<String> tags = Arrays.asList(uncheck.tags());
			return !tags.contains(tag);
		}
		// 判定检测标签
		return true;
	}

	/**
	 * 检测属性格式
	 *
	 * @return 错误格式的属性名；为null则没有错误
	 */
	default String formatCheck() {
		Class<?> cl = getClass();
		List<Field> list = new ArrayList<>();
		do {
			if (Checkable.class.isAssignableFrom(cl)) {
				Collections.addAll(list, cl.getDeclaredFields());
			}
			cl = cl.getSuperclass();
		} while (cl != null);
		try {
			for (Field field : list) {
				field.setAccessible(true);
				Format format = field.getAnnotation(Format.class);
				// 判断是否需要检测
				if (format != null) {
					Object o = field.get(this);
					if (o == null) {
						if (!format.nullable()) {
							return getFieldName(field);
						}
					} else {
						if (!o.toString().matches(format.value())) {
							return getFieldName(field);
						}
					}
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取属性名称
	 *
	 * @param field 属性对象
	 * @return 属性名称
	 */
	default String getFieldName(Field field) {
		ApiModelProperty property = field.getAnnotation(ApiModelProperty.class);
		return property == null ? field.getName() : property.value() + "-" + field.getName();
	}

	/**
	 * 标记不需要检测的属性，有默认标签（"" - 空字符串）
	 */
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@interface Uncheck {

		/**
		 * 检测标签，用于分类检测 <br>
		 */
		String[] tags() default "";

	}

	/**
	 * 标记完全忽略检测的属性
	 */
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@interface IgnoreCheck {
	}

	/**
	 * 格式检测
	 */
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@interface Format {

		/**
		 * 用正则表达式来限定属性的值
		 *
		 * @return 需要符合的正则表达式
		 */
		String value();

		/**
		 * 是否允许空值
		 *
		 * @return true - 允许空值，当值为null时跳过检测；不允许空值，当值为null时返回此属性
		 */
		boolean nullable() default true;
	}
}
