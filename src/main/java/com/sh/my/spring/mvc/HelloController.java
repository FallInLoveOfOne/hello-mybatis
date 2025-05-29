package com.sh.my.spring.mvc;

import com.sh.my.spring.context.HelloMvcBean;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sh
 * @since 2025/05/29
 */
@RestController
public class HelloController {

    @Resource
    private HelloMvcBean helloMvcBean;

    @GetMapping("/helloMvc")
    public String hello(HttpServletRequest request) {
        //return "Hello, Spring MVC with embedded Tomcat!";
        return helloMvcBean.hello();
    }
}
