package com.sh.my.spring.beans;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LifeBean implements InitializingBean, DisposableBean {
    public LifeBean() {
        log.info("构造方法constructor");
    }

    @Override
    public void afterPropertiesSet() {
        log.info("初始化 afterPropertiesSet");
    }

    @Override
    public void destroy() {
        log.info("销毁 destroy");
    }

    public void sayHello() {
        log.info("Hello LifeBean!");
    }

    public static void main(String[] args) {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        RootBeanDefinition lifeDef = new RootBeanDefinition(LifeBean.class);
        factory.registerBeanDefinition("lifeBean", lifeDef);
        LifeBean lifeBean = factory.getBean("lifeBean", LifeBean.class);
        lifeBean.sayHello();
        factory.destroySingletons(); // 会调用 destroy()
        factory.removeBeanDefinition("lifeBean"); // 删除 bean 定义
        LifeBean lifeBean2 = factory.getBean("lifeBean", LifeBean.class);
        lifeBean2.sayHello();
    }
}
