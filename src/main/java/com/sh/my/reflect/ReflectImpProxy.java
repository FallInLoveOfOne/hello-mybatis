package com.sh.my.reflect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author sh
 * @since 2025/04/29
 */
@Slf4j
public class ReflectImpProxy {

    interface ITarget {
        int sayHello();

    }

    static class Target implements ITarget {
        @Override
        public int sayHello() {
            log.info("target say hello");
            return 666;
        }
    }

    @Slf4j
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static class ReflectProxy {
        private ITarget target;

        /**
         * 通过反射模拟动态代理
         * 调用目标方法
         *
         * @param method
         * @param args
         * @return
         */
        public Object callMethod(Method method, Object[] args) throws Exception {
            log.info("proxy start");
            method.setAccessible(true);
            Object result = method.invoke(target, args);
            log.info("invoke result: {}", result);
            log.info("proxy end");
            return result;
        }

    }

    public static void main(String[] args) {
        ITarget target = new Target();
        ReflectProxy proxy = new ReflectProxy(target);
        try {
            proxy.callMethod(target.getClass().getDeclaredMethod("sayHello"), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
