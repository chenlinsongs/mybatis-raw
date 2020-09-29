package com.cls.mybatis.dao;

import com.cls.mybatis.entity.Blog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author linsong.chen
 * @date 2020-09-28 14:49
 */
public interface BlogMapper {
    @Select("select * from mybatis_test where id = #{id}")
    Blog selectBlog(String id);

    @Insert("insert into mybatis_test (name) value (#{name})")
    int insertBlog(@Param(value = "name") String name);
}
