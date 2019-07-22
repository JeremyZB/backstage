package com.bobo.framework.targets;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: AccessTokenWhite
 * @Description: AccessToken 白名单方�?
 * @author: Leven
 * @date: 2017�?7�?19�? 上午10:03:49
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessTokenWhite {

	/**
	 * @Description: 白名单对应的渠道 "_ALL" 代表�?有渠�?
	 */
	String channels() default "_ALL";
	

	

}
