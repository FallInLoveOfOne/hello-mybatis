package com.sh.my.spring.beans;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import lombok.extern.slf4j.Slf4j;

/**
 * BeanPostProcessor 是 Spring 中用于在 bean 初始化前后进行处理的接口
 * 通过实现 BeanPostProcessor 接口，可以在 bean 的生命周期中插入自定义逻辑
 * postProcessBeforeInitialization 在 bean 初始化之前调用
 * postProcessAfterInitialization 在 bean 初始化之后调用
 */
@Slf4j
public class HelloBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        log.info("beforeInitialization: beanName={}", beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        log.info("afterInitialization: beanName={}", beanName);
        ClassLoader classLoader = bean.getClass().getClassLoader();
        // 若是接口
        Class<?>[] interfaces = bean.getClass().getInterfaces();
        Class<?> beClass = bean.getClass();
        Class<?>[] cls = new Class<?>[] { beClass };
        // 基于接口的jdk动态代理创建bean的代理对象，模拟增强
        Object newProxyInstance = Proxy.newProxyInstance(classLoader, interfaces, (proxy, method, args) -> {
            log.info("===start execute method: {}", method.getName());
            Object invoke = method.invoke(bean, args);
            log.info("===end execute method: {}", method.getName());
            return invoke;
        });
        // return bean;
        return newProxyInstance;
    }

    public interface IHelloBean {
        void sayHello();
    }

    static class HelloBean implements IHelloBean {
        @Override
        public void sayHello() {
            log.info("Hello my world!");
        }
    }

    public static void main(String[] args) {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        // 注册 BeanPostProcessor
        factory.addBeanPostProcessor(new HelloBeanPostProcessor());
        // bean元信息
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(HelloBean.class);
        // 注册bean
        factory.registerBeanDefinition("helloBean", rootBeanDefinition);
        Object bean = factory.getBean("helloBean");
        log.info("bean class name={}", bean.getClass().getName());
        // 由于Proxy.newProxyInstance基于接口的动态代理，所以这里需要强转为接口类型
        IHelloBean helloObject = (IHelloBean) bean;
        helloObject.sayHello();
    }

}
