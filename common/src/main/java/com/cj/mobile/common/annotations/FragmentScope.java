package com.cj.mobile.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * 包名:com.cj.mobile.common.annotations
 * 对象名: FragmentScope
 * 描述:注解限定作用域范围，在Fragment生命周期有效
 * 作者: 王力杨
 * 创建日期: 2016/5/31 9:40
 */
@Documented
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface FragmentScope {
}
