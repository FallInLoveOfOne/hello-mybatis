package com.sh.my.spring.aop;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.DefaultAopProxyFactory;
import org.springframework.aop.framework.ProxyFactory;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import lombok.extern.slf4j.Slf4j;

interface IBusinessService {
    void busy();
}

@Slf4j
class BusinessService implements IBusinessService {
    public void doSomething() {
        log.info("my will do===");
    }

    @Override
    public void busy() {
        log.info("implement busy===");
    }

}

@Slf4j
class BeforeSayHello implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        log.info("before advice method: {}", method.getName());
    }
}

// 环绕通知（可以控制方法执行）
@Slf4j
class AroundAdvice implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String methodName = invocation.getMethod().getName();
        log.info("🔍 开始执行方法: " + methodName);
        long start = System.currentTimeMillis();

        try {
            // 执行原方法
            Object result = invocation.proceed();
            return result;
        } catch (Exception ex) {
            log.info("⚠️ 异常捕获: " + ex.getMessage());
            throw ex;
        } finally {
            long end = System.currentTimeMillis();
            log.info("⏱️ 方法执行耗时: " + (end - start) + "ms");
        }
    }
}

@Slf4j
public class HelloProxyFactory {

    private static void jdkProxy() {
        IBusinessService target = new BusinessService();
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setProxyTargetClass(false);
        // 设置接口,如果设置了接口，则可使用JDK动态代理，否则使用CGLIB动态代理
        proxyFactory.setInterfaces(IBusinessService.class);
        proxyFactory.setTarget(target);
        // 设置jdk动态代理
        proxyFactory.setExposeProxy(false);
        proxyFactory.addAdvice(new BeforeSayHello());
        proxyFactory.addAdvice(new AroundAdvice());
        Object proxy = proxyFactory.getProxy();
        // $Proxy{num}
        log.info("proxy class name={}", proxy.getClass().getName());
        // 基于接口的jdk动态代理，只能使用接口来调用方法
        IBusinessService proxyService = (IBusinessService) proxy;
        log.info("proxyService class name={}", proxyService.getClass().getName());
        proxyService.busy();
    }

    private static void cglibProxy() {
        BusinessService target = new BusinessService();
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setProxyTargetClass(true);
        // 设置目标类
        proxyFactory.setTarget(target);
        proxyFactory.addAdvice(new BeforeSayHello());
        proxyFactory.addAdvice(new AroundAdvice());
        Object proxy = proxyFactory.getProxy();
        // SpringCGLIB$${num}
        log.info("proxy class name={}", proxy.getClass().getName());
        // 基于类的CGLIB动态代理，可以使用类来调用方法
        IBusinessService proxyService = (BusinessService) proxy;
        log.info("proxyService class name={}", proxyService.getClass().getName());
        proxyService.busy();
    }

    public static void main(String[] args) {
        jdkProxy();
        cglibProxy();
        DefaultAopProxyFactory factory = new DefaultAopProxyFactory();
    }
}
