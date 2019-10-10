package com.jimi.su.server.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 被标记的会被记录日志
 *@author 几米物联自动化部-韦姚忠
 *@date   2019年7月13日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Log {

	/**
	 * 日志描述
	 */
	String value();
}
