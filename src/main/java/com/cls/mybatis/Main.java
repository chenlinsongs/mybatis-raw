package com.cls.mybatis;

import com.cls.mybatis.dao.BlogMapper;
import com.cls.mybatis.datasource.BlogDataSourceFactory;
import com.cls.mybatis.entity.Blog;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;

/**
 * @author linsong.chen
 * @date 2020-09-28 11:50
 */
public class Main {
    public static void main(String[] args) {
        DataSource dataSource = BlogDataSourceFactory.getBlogDataSource();
        TransactionFactory transactionFactory =
                new JdbcTransactionFactory();
        Environment environment =
                new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(BlogMapper.class);
        SqlSessionFactory sqlSessionFactory =
                new SqlSessionFactoryBuilder().build(configuration);

        SqlSession session = sqlSessionFactory.openSession();
        Blog blog = session.selectOne(
                "com.cls.mybatis.dao.BlogMapper.selectBlog", "1");
        System.out.println(blog.getName());


    }
}
