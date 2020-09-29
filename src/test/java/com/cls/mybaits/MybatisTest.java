package com.cls.mybaits;

import com.cls.mybatis.dao.BlogMapper;
import com.cls.mybatis.datasource.BlogDataSourceFactory;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author linsong.chen
 * @date 2020-09-28 17:17
 */
public class MybatisTest {

    public SqlSessionFactory getSqlSessionFactory(){
        DataSource dataSource = BlogDataSourceFactory.getBlogDataSource();
        TransactionFactory transactionFactory =
                new JdbcTransactionFactory();
        Environment environment =
                new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(BlogMapper.class);
        SqlSessionFactory sqlSessionFactory =
                new SqlSessionFactoryBuilder().build(configuration);
        return sqlSessionFactory;
    }

    /**
     * 测试目的，默认实现DefaultSqlSession不是线程安全的，每次查询需要开启一个新的SqlSession
     * */
    @Test
    public void annotationSqlTest(){
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession(false);
        try {
            //1.用id的方式
//            sqlSession.insert("com.cls.mybatis.dao.BlogMapper.insertBlog","name"+Math.random());
//            sqlSession.insert("com.cls.mybatis.dao.BlogMapper.insertBlog","name2"+Math.random());
//           //2.用mapper的方式
            BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
            mapper.insertBlog("123");
            sqlSession.commit(true);
        }catch (Exception e){
            e.printStackTrace();
            sqlSession.rollback(true);
        }
    }

    /**
     * 测试目的，同一个方法，通过反射，可以让不同的实体类去执行该方法
     * */
    @Test
    public void differenceSqlSession(){
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession(false);
        try {
            BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
            Method method = mapper.getClass().getMethod("insertBlog",String.class);

            //获取一个新Mapper
            BlogMapper newMapper = sqlSession.getMapper(BlogMapper.class);
            method.invoke(newMapper,"456");

            sqlSession.commit(true);
        }catch (Exception e){
            e.printStackTrace();
            sqlSession.rollback(true);
        }
    }
}
