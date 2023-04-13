package com.sdut.blog.mapper;

import com.sdut.blog.pojo.dto.Blog;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

/**
 * @author 24699
 */
@Mapper
public interface CollectMapper {
    @Insert("insert into collection (blog_id, user_id, collect_time) values " +
            "(#{blogId}, #{userId}, #{collectTime})")
    Boolean addCollect(Integer blogId, Integer userId, Long collectTime);

    @Delete("delete from collection where blog_id=#{blogId} and user_id=#{userId}")
    Boolean deleteCollect(Integer blogId, Integer userId);

    @Select("select * from blog where id in (select blog_id from collection where user_id=#{userId})")
    ArrayList<Blog> getBlogs(Integer userId);

    @Select("select * from collection where blog_id=#{blogId} and user_id=#{userId}")
    Boolean getStatus(Integer blogId, Integer userId);
}
