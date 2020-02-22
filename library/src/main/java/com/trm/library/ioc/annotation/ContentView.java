package com.trm.library.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) //注解的作用域，类之上
@Retention(RetentionPolicy.RUNTIME) //运行时注解
public @interface ContentView {
    int value(); //注解的参数，layout id
}
