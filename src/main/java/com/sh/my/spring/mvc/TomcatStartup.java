package com.sh.my.spring.mvc;

import com.sh.my.spring.context.MainConfig;
import com.sh.my.spring.mvc.filter.EncodingFilter;
import com.sh.my.spring.mvc.filter.LogFilter;
import jakarta.servlet.Filter;
import jakarta.servlet.ServletRegistration;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.fusesource.jansi.AnsiConsole;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;
import java.nio.file.Files;

/**
 * @author sh
 * @since 2025/05/29
 */
public class TomcatStartup {

    /*public static void main(String[] args) {
        try {
            Tomcat tomcat = new Tomcat();
            tomcat.setPort(8787);
            tomcat.getHost().setAppBase(".");

            // 创建 Web 应用上下文
            Context ctx = tomcat.addContext("/", new File(".").getAbsolutePath());

            // 创建 Spring 容器
            AnnotationConfigWebApplicationContext springContext = new AnnotationConfigWebApplicationContext();
            springContext.register(MainConfig.class);  // 你的配置类

            // 创建 DispatcherServlet
            DispatcherServlet dispatcherServlet = new DispatcherServlet(springContext);

            // 注册到 Tomcat 中
            Tomcat.addServlet(ctx, "dispatcher", dispatcherServlet);
            ctx.addServletMappingDecoded("/", "dispatcher");

            tomcat.start();
            System.out.println("✅ Tomcat started at http://localhost:8787");
            for (ServletRegistration servlet : ctx.getServletContext().getServletRegistrations().values()) {
                System.out.println("Registered servlet: " + servlet.getName() + " -> " + servlet.getClassName());
            }

            tomcat.getServer().await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public static void main(String[] args) {
        System.setProperty("logback.ansi.enabled", "true"); //
        AnsiConsole.systemInstall();
        try {
            Tomcat tomcat = new Tomcat();
            tomcat.setPort(8787);
            tomcat.getConnector();  // 触发创建默认Connector
            //tomcat.getHost().setAppBase(".");
            tomcat.setBaseDir("temp/tomcat");

            // 使用临时目录作为 context 路径
            File baseDir = Files.createTempDirectory("tomcat-base").toFile();
            Context ctx = tomcat.addContext("", baseDir.getAbsolutePath());

            // ✅ 注册 Filter —— 必须在获取 ServletContext 之前
            registerFilter(ctx);

            // 创建 Spring 容器
            AnnotationConfigWebApplicationContext springContext = new AnnotationConfigWebApplicationContext();
            springContext.setServletContext(ctx.getServletContext());  // 绑定 ServletContext
            springContext.register(MainConfig.class);
            // 一定要refresh！
            springContext.refresh();

            // 创建 DispatcherServlet
            DispatcherServlet dispatcherServlet = new DispatcherServlet(springContext);

            // 注册 Servlet
            Tomcat.addServlet(ctx, "dispatcher", dispatcherServlet);
            ctx.addServletMappingDecoded("/", "dispatcher");

            // 启动
            tomcat.start();
            System.out.println("✅ Tomcat started at http://localhost:8787");

            for (ServletRegistration servlet : ctx.getServletContext().getServletRegistrations().values()) {
                System.out.println("Registered servlet: " + servlet.getName() + " -> " + servlet.getClassName());
            }

            tomcat.getServer().await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            AnsiConsole.systemUninstall();
        }
    }


    private static FilterDef createFilterDef(String name, Filter filter) {
        FilterDef def = new FilterDef();
        def.setFilterName(name);
        def.setFilter(filter);
        def.setFilterClass(filter.getClass().getName());
        return def;
    }

    private static FilterMap createFilterMap(String name, String urlPattern) {
        FilterMap map = new FilterMap();
        map.setFilterName(name);
        map.addURLPattern(urlPattern);
        return map;
    }

    private static void registerFilter(Context ctx) {
        ctx.addFilterDef(createFilterDef("encodingFilter", new EncodingFilter()));
        ctx.addFilterMap(createFilterMap("encodingFilter", "/*"));

        ctx.addFilterDef(createFilterDef("logFilter", new LogFilter()));
        ctx.addFilterMap(createFilterMap("logFilter", "/*"));
    }


}
