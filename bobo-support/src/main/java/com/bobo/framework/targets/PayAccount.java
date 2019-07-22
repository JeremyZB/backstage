package com.bobo.framework.targets;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: PayAccount
 * @Description: PayAccount
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PayAccount {

	/**
	 * 0：监听请求和返回,1:监听请求,2:监听返回
	 * 
	 * @return
	 */
	String type() default "0";
}
