package com.cj.mobile.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * @Title: ActivityContext
 * @Package: com.cj.mobile.common.annotations
 * @Description:    注解限定作用域范围，在Activity生命周期有效
 * @author: 王力杨(liyang.wang@21chinamall.com)
 * @date: 2016/8/11 9:42
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityContext {

}
