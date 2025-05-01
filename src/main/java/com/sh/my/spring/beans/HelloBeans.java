package com.sh.my.spring.beans;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloBeans {

    static class HelloObject {
        public void sayHello() {
            log.info("Hello Object!");
        }
    }

    public static void main(String[] args) {
        // Spring BeanFactory 是一个简单的容器，负责创建和管理 bean 的生命周期
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // bean元信息
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(HelloObject.class);
        // 注册bean
        beanFactory.registerBeanDefinition("helloObject", rootBeanDefinition);
        Object bean = beanFactory.getBean("helloObject");
        log.info("bean class name={}", bean.getClass().getName());
        HelloObject helloObject = (HelloObject) bean;
        helloObject.sayHello();
    }
}
