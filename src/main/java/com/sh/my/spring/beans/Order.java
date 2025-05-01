package com.sh.my.spring.beans;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * bean注入
 */
@Slf4j
public class Order {

    @Getter
    @Setter
    private User user;

    @Getter
    private Area area;

    public Order(Area area) {
        this.area = area;
        log.info("Order constructor");
    }

    static class User {
        public void sayHello() {
            log.info("Hello User!");
        }
    }

    static class Area {
        public void sayHello() {
            log.info("Hello Area!");
        }
    }

    public void sayHello() {
        this.user.sayHello();
        log.info("Hello Order!");
    }

    public static void main(String[] args) {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        RootBeanDefinition orderDef = new RootBeanDefinition(Order.class);
        // 设置单例模式
        orderDef.setScope(BeanDefinition.SCOPE_SINGLETON);
        RootBeanDefinition userDef = new RootBeanDefinition(User.class);
        RootBeanDefinition areaDef = new RootBeanDefinition(Area.class);
        // setter 方法注入
        orderDef.getPropertyValues().add("user", userDef);
        // 通过构造函数注入
        orderDef.getConstructorArgumentValues().addGenericArgumentValue(areaDef);
        factory.registerBeanDefinition("orderBean", orderDef);
        Order orderBean = factory.getBean("orderBean", Order.class);
        orderBean.sayHello();
        orderBean.getArea().sayHello();
        Order orderBean2 = factory.getBean("orderBean", Order.class);
        log.info("orderBean == orderBean2: {}", orderBean == orderBean2);
        log.info("orderBean.user == orderBean2.user: {}", orderBean.getUser() == orderBean2.getUser());
        // 由于没有注册 userDef，所以会报错
        User userBean = factory.getBean("userBean", User.class);
        userBean.sayHello();
    }

}
