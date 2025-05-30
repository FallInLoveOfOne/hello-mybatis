package com.sh.my.spring.mvc;

import com.sh.my.mybatis.model.User;
import com.sh.my.spring.context.HelloMvcBean;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author sh
 * @since 2025/05/29
 */
@Slf4j
@RestController
public class HelloController {

    @Resource
    private HelloMvcBean helloMvcBean;

    @GetMapping("/helloMvc")
    public String hello(HttpServletRequest request) {
        //return "Hello, Spring MVC with embedded Tomcat!";
        return helloMvcBean.hello();
    }

    @PostMapping("/helloJson")
    public User helloJson(@RequestBody User user) {
        log.info("user: {}", user);
        return user;
    }

    @PostMapping("/upload")
    public String upload(MultipartFile file) {
        return "upload";
    }
}
