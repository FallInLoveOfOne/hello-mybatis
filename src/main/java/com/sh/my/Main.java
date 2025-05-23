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
        configuration.setCacheEnabled(true);// 开启二级缓存

        // 5. 创建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

        // 6. 开启Session，执行查询
        L1Cache(sqlSessionFactory);
        L2Cache(sqlSessionFactory);
    }

    private static void L1Cache(SqlSessionFactory sqlSessionFactory) {
        log.info("L1Cache=====");
        int id = 1;
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.selectUserById(id);
        User user1 = mapper.selectUserById(id);
        log.info("user==user1? {}", user == user1);
        //只要插入的数据，都会刷新缓存
        mapper.insertUser(new User(2, "小红@"));
        User user2 = mapper.selectUserById(id);
        log.info("user==user2? {}", user == user2);
        //sqlSession.clearCache();

        //一级缓存是sqlSession级别，即同一个SqlSession中的数据是共享的
        //相同数据放在map中，同一对象
        UserMapper mapper1 = sqlSessionFactory.openSession(true).getMapper(UserMapper.class);
        User user3 = mapper1.selectUserById(id);
        log.info("user==user3? {}", user == user3);
    }

    private static void L2Cache(SqlSessionFactory sqlSessionFactory) {
        log.info("L2Cache=====");
        int id = 1;
        //二级缓存是SqlSessionFactory级别，即同一个SqlSessionFactory中的数据是共享的
        //跨sqlSession，sqlSession关闭不能持有对象引用，所以会通过序列化保存数据状态
        //会先将对象序列化，然后进行缓存，新的Session查询到的数据，会进行反序列化,所以会有性能上的提升
        UserMapper mapper = sqlSessionFactory.openSession(true).getMapper(UserMapper.class);
        User user = mapper.selectUserById(id);
        UserMapper mapper2 = sqlSessionFactory.openSession(true).getMapper(UserMapper.class);
        User user1 = mapper2.selectUserById(id);
        log.info("user==user1? {}", user == user1);
    }

    public static void main(String[] args) {
        // System.out.println("Hello world!");
        HelloMybatis();
    }

}