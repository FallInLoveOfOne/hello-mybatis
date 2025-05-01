package com.sh.my.spring.core;

import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloCore {


    public static void main(String[] args) {
        String s = "Hello Spring Core!";
        log.info(s);
        boolean hasText = StringUtils.hasText(s);;
        log.info("StringUtils.hasText: {}", hasText);
    }

}
