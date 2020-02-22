package com.trm.library.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE) // 注解中的注解
@Retention(RetentionPolicy.RUNTIME)
public @interface Event {
    // 事件的三要素

    // 1. 设置监听的方法
    String listenerSetter();

    // 2. 监听类的类型
    Class<?> listenerType();

    // 3. 需要拦截的回调
    String listenerCallback();
}
