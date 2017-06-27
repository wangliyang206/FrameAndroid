package com.cj.mobile.common.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * @Title: ActivityScope
 * @Package: com.cj.mobile.common.annotations
 * @Description: 注解限定作用域范围，在Activity生命周期有效
 * @author: 王力杨
 * @date: 2016/5/25 9:42
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope {
}
