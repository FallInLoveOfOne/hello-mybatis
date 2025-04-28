package com.sh.my.reflect;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;

/**
 * @author sh
 * @since 2025/04/28
 */
@Slf4j
public class Hello {

    public static void main(String[] args) {
        Person man = () -> {
            log.info("Real Person say hello");
            return 777;
        };
        HelloInvocationHandler helloInvocationHandler = new HelloInvocationHandler(man);
        Person proxyInstance = (Person) Proxy.newProxyInstance(Person.class.getClassLoader(), new Class[]{Person.class}, helloInvocationHandler);
        proxyInstance.sayHello();
    }
}

interface Person {
    int sayHello();
}

@Slf4j
class Man implements Person {

    private static final int GOOD = 666;

    @Override
    public int sayHello() {
        log.info("Man say hello");
        return GOOD;
    }
}
