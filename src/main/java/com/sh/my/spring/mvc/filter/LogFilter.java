package com.sh.my.spring.mvc.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

//@WebFilter(filterName = "logFilter", urlPatterns = "/*")
@Slf4j
//@Component
//@Order(2)
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("[LogFilter] 初始化完成");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String traceId = UUID.randomUUID().toString();

        log.info("✅LogFilter 请求开始 - TraceId: {}, Method: {}, URI: {}",
                traceId, req.getMethod(), req.getRequestURI());

        chain.doFilter(request, response);

        log.info("✅LogFilter 请求结束 - TraceId: {}", traceId);
    }

    @Override
    public void destroy() {
        log.info("[LogFilter] 已销毁");
    }
}

