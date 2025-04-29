package com.sh.my.reflect;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author sh
 * @since 2025/04/29
 */
@Slf4j
public class HelloReflect {
    public static void main(String[] args) throws Exception {
        Show show = new Show();
        reflectFiled(show, "name");
    }


    /**
     * 反射字段
     *
     * @param target
     * @param filedName
     * @throws Exception
     */
    public static void reflectFiled(Show target, String filedName) throws Exception {
        Field declaredField = target.getClass().getDeclaredField(filedName);
        declaredField.setAccessible(true);
        Object value = declaredField.get(target);
        log.info("filed value: {}", value);
        declaredField.set(target, "show");

        Object set = declaredField.get(target);
        log.info("filed value: {}", set);

        reflectMethod(target, "show", new Object[]{888}, int.class);
        reflectMethod(target, "show", null, null);
    }

    /**
     * 反射方法
     *
     * @param target
     * @param methodName
     * @param args
     * @param params
     * @throws Exception
     */
    public static void reflectMethod(Show target, String methodName, Object[] args, Class<?>... params) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = target.getClass().getDeclaredMethod(methodName, params);
        method.setAccessible(true);
        Object invoke = method.invoke(target, args);
        log.info("method invoke result: {}", invoke);
    }
}

@Slf4j
@Data
class Show {
    private String name = "hello name field";

    private void show() {
        log.info("execute show method");
    }

    public int show(int a) {
        log.info("execute show method with param: {}", a);
        return a;
    }
}
