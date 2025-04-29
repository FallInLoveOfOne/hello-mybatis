package com.sh.my.bytebuddy;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;

/**
 * @author sh
 * @since 2025/04/29
 */
@Slf4j
public class ByteBuddyFull {
    // ========== 定义一个接口 ==========
    public interface IHello {
        String sayHello();
    }

    // ========== 定义拦截器 ==========
    public static class HelloInterceptor {
        public static String intercept() {
            log.info("[Intercept] Method 被拦截了！");
            return "被拦截返回的新值！";
        }
    }

    // ========== 动态生成类 ==========
    public static void createDynamicClass() throws Exception {
        log.info("========== 创建动态类 ==========");
        Class<?> dynamicType = new ByteBuddy()
                .subclass(Object.class)
                .implement(IHello.class) // 动态实现接口
                .defineField("name", String.class, Visibility.PUBLIC) // 动态加字段
                .defineMethod("sayHello", String.class, Visibility.PUBLIC) // 动态加方法
                .intercept(FixedValue.value("Hello from Dynamic Class!")) // 直接返回字符串
                .method(ElementMatchers.named("toString")) // 重写 toString 方法
                .intercept(FixedValue.value("I'm a dynamic toString"))
                .make()
                .load(ByteBuddyFull.class.getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                .getLoaded();

        Object instance = dynamicType.getDeclaredConstructor().newInstance();
        log.info("toString() = " + instance.toString());

        Method method = dynamicType.getMethod("sayHello");
        log.info("sayHello() = " + method.invoke(instance));
    }

    // ========== 热插拔修改已有类 ==========
    public static void redefineClassAtRuntime() {
        log.info("========== 热插拔修改类 ==========");

        ByteBuddyAgent.install(); // 安装ByteBuddyAgent（attach到当前JVM）

        new AgentBuilder.Default()
                .type(ElementMatchers.named("java.lang.String"))
                .transform((builder, typeDescription, classLoader, module, protectionDomain) ->
                        builder.visit(Advice.to(AdviceInterceptor.class)
                                .on(ElementMatchers.named("toString")))
                )
                .installOnByteBuddyAgent();
    }

    // ========== Advice拦截器（切String的toString） ==========
    public static class AdviceInterceptor {
        @Advice.OnMethodExit
        static void exit(@Advice.Return(readOnly = false) Object returnValue) {
            returnValue = "[Modified by ByteBuddy Agent]"; // 修改返回值
        }
    }

    public static void main(String[] args) throws Exception {
        createDynamicClass();

        // 调用热插拔Agent修改 String
        redefineClassAtRuntime();

        // 验证 String.toString() 被改了
        String s = new String("Original");
        log.info("String.toString() = " + s.toString());
    }
}
