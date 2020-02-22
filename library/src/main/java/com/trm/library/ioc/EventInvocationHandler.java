package com.trm.library.ioc;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

public class EventInvocationHandler implements InvocationHandler {

    // 自定义中实现回调方法的类
    private Object target;

    // 拦截回调方法，执行自定义方法，所以需保存
    private HashMap<String, Method> hashMap;

    // 防误触处理
    private long currentTime;

    public EventInvocationHandler(Object target) {
        this.target = target;
        hashMap = new HashMap<>();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (target == null) return null;

        if (hashMap.containsKey(method.getName())) {
            if (System.currentTimeMillis() - currentTime < 1500) {
                Log.d("EventInvocationHandler", "连续点击拦截");
                return null;
            }
            currentTime = System.currentTimeMillis();

            // method 替换了，所以自定义的method需要和原先的方法 参数和返回值一样，否则执行 method.invoke 会抛异常
            method = hashMap.get(method.getName());
            // 方法有可能是private
            method.setAccessible(true);

            return method.invoke(target, args);
        }

        return null;
    }

    public void addMethod(String methodName, Method callMethod) {
        hashMap.put(methodName, callMethod);
    }
}
