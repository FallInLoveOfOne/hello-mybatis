package com.sh.my.spring.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 自定义条件
 * 未使用
 */
public class ServletContextCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String property = context.getEnvironment().getProperty("app.web.enabled");
        // return "true".equals(property);
        return Boolean.parseBoolean(property);
    }

}
