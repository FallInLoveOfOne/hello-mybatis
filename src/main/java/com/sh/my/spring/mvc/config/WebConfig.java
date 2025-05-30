package com.sh.my.spring.mvc.config;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /*@Bean
    public FilterRegistrationBean<LogFilter> logFilter() {
        FilterRegistrationBean<LogFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new LogFilter());
        registration.setName("logFilter");
        registration.setOrder(1); // ✅ 此处才真正生效
        registration.addUrlPatterns("/*");
        return registration;
    }*/

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter jacksonConverter) {
                ObjectMapper objectMapper = jacksonConverter.getObjectMapper();
                // jackson默认是false=忽略未知属性（全局）
                // 若不使用全局忽略，可在实体中单独忽略@JsonIgnoreProperties(ignoreUnknown = true)
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                // 对于LocalDateTime，LocalDate，LocalTime，Date
                objectMapper.registerModule(new JavaTimeModule());
                objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                objectMapper.setDateFormat(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")); // 用于 Date 类型

                // 对于 Java 8+的时间类型（LocalDateTime），需要使用配置器
                objectMapper.configOverride(LocalDateTime.class)
                        .setFormat(JsonFormat.Value.forPattern("yyyy-MM-dd HH:mm:ss"));
            }
        }
    }


}

