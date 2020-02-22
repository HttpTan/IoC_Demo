package com.trm.library.ioc;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.trm.library.ioc.annotation.ContentView;
import com.trm.library.ioc.annotation.Event;
import com.trm.library.ioc.annotation.InjectView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectManager {

    public static void inject(Activity activity) {

        // 注入 布局
        injectLayout(activity);

        // 注入 控件
        injectViews(activity);

        // 注入 事件，包含点击和长按
        injectEvents(activity);

    }

    private static void injectLayout(Activity activity) {
        // 获取注解的类
        Class<? extends Activity> clazz = activity.getClass();

        // 获取类上的 ContentView 注解
        ContentView annotation = clazz.getAnnotation(ContentView.class);
        // 获取注解上的值，layoutId
        int layoutId = annotation.value();
        if (layoutId <= 0) return;

        // 第一种方式 设置布局
        //activity.setContentView(layoutId);

        // 第二种方式， 通过反射获得
        try {
            // getMethod，获取当前或者父类的public方法
            Method setContentView = clazz.getMethod("setContentView", int.class);
            // 执行setContentView方法，参数含义： 在 activity 中执行，参数 layoutId
            setContentView.invoke(activity, layoutId);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }


    private static void injectViews(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();

        // 获取所有成员变量, getDeclaredFields 获取所有成员变量
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // 获取成员变量上的 InjectView注解
            InjectView annotation = field.getAnnotation(InjectView.class);
            if (annotation == null) continue;

            int viewId = annotation.value();
            if (viewId <= 0) continue;

            // 第一中方法找出 View
            //Object view = activity.findViewById(viewId);

            try {
                // 第二中方法找出 View
                Method findViewById = clazz.getMethod("findViewById", int.class);
                Object view = findViewById.invoke(activity, viewId);

                // setAccessible 使私有变量可赋值
                field.setAccessible(true);
                field.set(activity, view);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }


    // 事件拦截注意，会被后面的setOnClickListener覆盖的
    private static void injectEvents(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            // 获取方法中所有的注解
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation == null) continue;
                Class<? extends Annotation> annotationType = annotation.annotationType();
                // 获取注解中的 Event 注解
                Event event = annotationType.getAnnotation(Event.class);
                if (event == null) continue;

                // 得到注解中注解的三要素
                String strSetter = event.listenerSetter();
                Class<?> listenerType = event.listenerType();
                String strCallback = event.listenerCallback();

                // AOP 切面
                EventInvocationHandler handler = new EventInvocationHandler(activity);
                handler.addMethod(strCallback, method);
                Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, handler);

                try {
                    // 获取注解中的id值
                    Method valueMethod = annotationType.getDeclaredMethod("value");
                    Log.d("rongmin", valueMethod.getName()+", " + annotationType.getName());
                    int[] viewIds = (int[]) valueMethod.invoke(annotation);

                    for (int viewId : viewIds) {
                        if (viewId <= 0) continue;
                        View view = activity.findViewById(viewId);
                        if (view == null) continue;

                        // 获取view中的 listenerSetter 的方法
                        Method setter = view.getClass().getMethod(strSetter, listenerType);
                        // 执行view 中的 listenerSetter的方法，并拦截其中的 strCallback 方法
                        setter.invoke(view, listener);
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
