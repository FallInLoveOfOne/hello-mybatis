package com.sh.my.spring.context;

import org.springframework.stereotype.Component;

/**
 * @author sh
 * @since 2025/05/29
 */
@Component
public class HelloMvcBean {
    public void sayHello() {
        System.out.println("HelloMvcBean sayHello");
    }

    public String hello() {
        return "HelloMvcBean hello";
    }
}
