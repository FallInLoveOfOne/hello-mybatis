package com.sh.my.spring;

import java.util.Locale;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainSpring {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class)) {
            HelloSpring helloSpring = context.getBean(HelloSpring.class);
            helloSpring.hello();
            String greeting = context.getMessage("greeting", null, Locale.CHINA);
            log.info("Greeting message: {}", greeting);
        }
    }

}
