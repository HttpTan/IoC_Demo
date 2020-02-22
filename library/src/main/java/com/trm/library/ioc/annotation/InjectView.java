package com.trm.library.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) // 作用于成员变量上
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectView {
    int value(); //控件id 的值
}
