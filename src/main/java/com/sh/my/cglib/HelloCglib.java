package com.sh.my.cglib;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author sh
 * @since 2025/04/29
 */
@Slf4j
public class HelloCglib {


    @Slf4j
    static class UserService {

        public void addUser(String name) {
            log.info("执行添加用户: {}", name);
        }

        public void deleteUser(String name) {
            log.info("执行删除用户: {}", name);
        }
    }


    @Slf4j
    static class CglibInterceptor implements MethodInterceptor {

        private Object target;

        public CglibInterceptor(Object target) {
            this.target = target;
        }

        /**
         * 所有方法都会走到这里
         */
        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            log.info("[CGLIB] 方法前置: " + method.getName());

            // 调用父类原本的方法
            Object result = proxy.invokeSuper(obj, args);

            log.info("[CGLIB] 方法后置: " + method.getName());
            return result;
        }
    }

    public static void main(String[] args) {
        UserService target = new UserService();

        // 创建Enhancer
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(UserService.class); // 设置父类
        enhancer.setCallback(new CglibInterceptor(target)); // 设置回调拦截器

        // 生成代理对象
        UserService proxy = (UserService) enhancer.create();

        // 调用代理方法
        proxy.addUser("Tom");
        log.info("------------");
        proxy.deleteUser("Jerry");
    }

}
