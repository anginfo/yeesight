package com.hailian.annotation;


import java.lang.annotation.*;

/**
 * 在Controller方法上加入改注解会自动记录日志
 * @author : zuoqb
 * @date : 2018/05/08
 */
@Target( { ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface Log {
	/**
	 * 描述.
	 */
	String description() default "";

}
