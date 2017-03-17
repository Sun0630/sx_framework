package com.sx.baselibrary.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Sunxin
 * @Date 2017/3/17 16:48
 * @Description View注解的Annotation
 */
// @Target(ElementType.FIELD)   CONSTRUCTOR:构造器   METHOD://方法   TYPE：类
@Target(ElementType.FIELD)
//@Retention(RetentionPolicy.RUNTIME)  RUNTIME:运行时   CLASS:编译时  SOURCE：源码
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewById {
    int value();
}
