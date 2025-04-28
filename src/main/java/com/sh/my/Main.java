package com.sh.my;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

@Slf4j
public class Main {

    public static void HelloMybatis() {
        log.info("Hello MyBatis!");
        // 1. 数据源（这里用MyBatis内置简单连接池 PooledDataSource）
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setDriver("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.01:3506/ttttt?serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        // 2. 事务管理
        JdbcTransactionFactory transactionFactory = new JdbcTransactionFactory();

        // 3. 环境配置
        Environment environment = new Environment("development", transactionFactory, dataSource);

        // 4. 核心配置
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(UserMapper.class); // 注册Mapper
        configuration.getTypeAliasRegistry().registerAlias(User.class); // 注册类型别名

        // 5. 创建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

        // 6. 开启Session，执行查询
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = userMapper.selectUserById(1);
            log.info("查询到用户信息: ID={}, Name={}", user.getId(), user.getName());
        }
    }

    public static void main(String[] args) {
        // System.out.println("Hello world!");
        HelloMybatis();
    }

}