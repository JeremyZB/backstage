package com.bobo.framework.targets;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiDoc {
	public static final String update = "UPDATE";
	public static final String add = "ADD";
	
	public static final String author = "author";
	public static final String liwei = "liwei";
	public static final String wangfang = "wangfang";
	public static final String sxc = "sunxiachen";
	public static final String yanjing = "yanjing";
	public static final String xingjg = "xingjianguang";
	public static final String lup = "luping";
	public static final String zhanghl = "zhanghonglin";
	
	public static final String version_4_2_0 = "4.2.0";
	public static final String version_5_1_0 = "5.1.0";
	public static final String version_5_2_0 = "5.2.0";
	public static final String version_5_3_0 = "5.3.0";
	public static final String version_5_4_0 = "5.4.0";
	public static final String version_5_5_0 = "5.5.0";
	public static final String version_5_6_0 = "5.6.0";
	public static final String version_5_6_2= "5.6.2";
	public static final String version_5_7_0= "5.7.0";
	public static final String version_slef_1_0_0= "slef_1.0.0";

	/**
	 * 版本
	 */
	String version() default "";

	/**
	 * 作�??
	 */
	String author() default "author";

	/**
	 * 描述
	 */
	String describe() default "";

	/**
	 * 状�??
	 */
	String status() default update;

}
