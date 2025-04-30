package com.sh.my.mybatis.hello;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloMybatis {

    private static final String MYBATIS_CONFIG_XML = "mybatis-config.xml";
    private static final String MAPPER_USER_MAPPER_XML = "mapper/UserMapper.xml";

    public static void Hello() {
        log.info("Hello MyBatis!");
        // 1. 数据源（这里用MyBatis内置简单连接池 PooledDataSource）
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setDriver("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(
                "jdbc:mysql://127.0.0.1:3506/ttttt?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        // 2. 事务管理
        JdbcTransactionFactory transactionFactory = new JdbcTransactionFactory();

        // 3. 环境配置
        Environment environment = new Environment("development", transactionFactory, dataSource);

        // 4. 核心配置
        Configuration configuration = new Configuration(environment);
        // sql注解方式或xml与Mappper在同一个包下，可以通过如下扫描包的方式注册Mapper
        configuration.addMappers("com.sh.my.mybatis.mapper");
        // 注册实体别名
        configuration.getTypeAliasRegistry().registerAliases("com.sh.my.mybatis.model");
        configuration.setCacheEnabled(true);// 开启二级缓存
        // xml与Mapper不在同一个包下，需要通过如下xml文件注册，且pom需要配置xml资源
        // loadMapperByXml(configuration);

        // 5. 创建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

        // 6. 开启Session，执行查询
        HelloCache.L1Cache(sqlSessionFactory);
        HelloCache.L2Cache(sqlSessionFactory);
    }

    public static void HelloWithConfigFile() {
        log.info("Hello MyBatis with Config!");
        // 通过配置文件创建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory;
        try (InputStream inputStream = Resources.getResourceAsStream(MYBATIS_CONFIG_XML)) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load MyBatis config", e);
        }

        // 开启Session，执行查询
        HelloCache.L1Cache(sqlSessionFactory);
        HelloCache.L2Cache(sqlSessionFactory);
    }

    /**
     * xml与Mapper不在同一个包下，需要通过xml文件注册
     * xml结合namespace方式，通过如下方式注册
     * 
     * @param configuration
     */
    private static void loadMapperByXml(Configuration configuration) {
        try (InputStream input = Resources.getResourceAsStream(MAPPER_USER_MAPPER_XML)) {
            XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(
                    input, configuration, MAPPER_USER_MAPPER_XML, configuration.getSqlFragments());
            mapperBuilder.parse();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load MyBatis mapper", e);
        }
    }

    public static void main(String[] args) {
        Hello();
        HelloWithConfigFile();
    }

}
