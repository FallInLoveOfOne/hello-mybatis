package com.sh.my.spring.mvc.filter;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

//@WebFilter(filterName = "encodingFilter", urlPatterns = "/*")
@Slf4j
//@Component
//@Order(1)
public class EncodingFilter implements Filter {

    private String encoding = StandardCharsets.UTF_8.name();

    @Override
    public void init(FilterConfig filterConfig) {
        String configEncoding = filterConfig.getInitParameter("encoding");
        if (configEncoding != null) {
            this.encoding = configEncoding;
        }
        log.info("[EncodingFilter] 初始化完成");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        request.setCharacterEncoding(encoding);
        response.setCharacterEncoding(encoding);
        log.info("✅EncodingFilter - encoding: {}", encoding);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // nothing to destroy
    }
}

