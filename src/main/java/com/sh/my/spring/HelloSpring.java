package com.sh.my.spring;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HelloSpring {

    public void hello() {
        log.info("Hello Spring!");
    }

}
