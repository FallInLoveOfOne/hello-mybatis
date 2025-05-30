package com.sh.my.spring.context;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableTransactionManagement
@MapperScan("com.sh.my.mybatis.mapper")
@ComponentScan("com.sh.my")
@EnableWebMvc
@PropertySource("classpath:application.properties")
public class MainConfig {

}
