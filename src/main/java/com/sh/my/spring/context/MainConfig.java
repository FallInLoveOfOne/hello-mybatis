package com.sh.my.spring.context;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan("com.sh.my.mybatis.mapper")
@ComponentScan("com.sh.my")
public class MainConfig {

}
