package com.sh.my.bytebuddy;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationTargetException;

/**
 * @author sh
 * @since 2025/04/29
 */
@Slf4j
public class HelloByteBuddy {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> dynamicType = generateClass();

        Object instance = dynamicType.getDeclaredConstructor().newInstance();
        log.info(instance.getClass().getName());
        log.info(instance.toString());
    }

    private static Class<?> generateClass() {
        Class<?> dynamicType = new ByteBuddy()
                .subclass(Object.class) // 继承自 Object
                .name("com.sh.my.bytebuddy.HelloDynamicClass") // 动态命名
                .method(ElementMatchers.named("toString")) // 匹配 toString方法
                //.intercept(FixedValue.value("Hello ByteBuddy!")) // 直接返回固定值
                .intercept(MethodDelegation.to(Interceptor.class))
                .make()
                .load(HelloByteBuddy.class.getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                .getLoaded();
        return dynamicType;
    }

    @Slf4j
    public static class Interceptor {
        public static String intercept() {
            log.info("Before Method");
            return "Hello Interceptor!";
        }
    }
}
