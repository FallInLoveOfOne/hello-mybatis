package com.sh.my.spring.beans;

import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 循环依赖
 */
@Slf4j
class A {
    @Getter
    @Setter
    private B b;

    public void sayHello() {
        log.info("Hello A!");
    }

}

@Slf4j
class B {
    @Getter
    @Setter
    private A a;

    public void sayHello() {
        log.info("Hello B!");
    }

}

@Slf4j
public class CircularDependency {
    public static void main(String[] args) {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        RootBeanDefinition defA = new RootBeanDefinition(A.class);
        defA.getPropertyValues().add("b", new RuntimeBeanReference("b"));
        factory.registerBeanDefinition("a", defA);

        // 会报错 B尚未注册
        // B bNot = factory.getBean("b", B.class);

        RootBeanDefinition defB = new RootBeanDefinition(B.class);
        defB.getPropertyValues().add("a", new RuntimeBeanReference("a"));
        factory.registerBeanDefinition("b", defB);

        A a = factory.getBean("a", A.class);
        a.getB().sayHello();
        B b = factory.getBean("b", B.class);
        b.getA().sayHello();

        log.info("a.getB()==b: {}", a.getB() == b);// true
        log.info("b.getA()==a: {}", b.getA() == a);// true

    }
}
