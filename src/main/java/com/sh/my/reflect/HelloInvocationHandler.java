package com.sh.my.reflect;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;

/**
 * @author sh
 * @since 2025/04/28
 */
@Slf4j
public class HelloInvocationHandler implements InvocationHandler {
    private Object target;

    public HelloInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) throws Throwable {
        log.info("method: {}", method);
        Object result = method.invoke(target, args);
        log.info("result: {}", result);
        return result;
    }
}
