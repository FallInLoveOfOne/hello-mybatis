package com.sh.my.spring.beans;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;


@Component
public class HelloFactoryBean implements FactoryBean<HelloUserBean> {

    @Override
    @Nullable
    public HelloUserBean getObject() throws Exception {
        HelloUserBean helloUserBean = HelloUserBean.builder().name("Alice").age(21).build();
        return helloUserBean;
    }

    @Override
    @Nullable
    public Class<?> getObjectType() {
        return HelloUserBean.class;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }

}
