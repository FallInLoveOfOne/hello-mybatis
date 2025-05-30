package com.sh.my.spring.mvc.config;


import com.sh.my.spring.mvc.filter.LogFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    /*@Bean
    public FilterRegistrationBean<LogFilter> logFilter() {
        FilterRegistrationBean<LogFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new LogFilter());
        registration.setName("logFilter");
        registration.setOrder(1); // ✅ 此处才真正生效
        registration.addUrlPatterns("/*");
        return registration;
    }*/
}

