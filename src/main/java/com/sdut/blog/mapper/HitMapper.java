package com.sdut.blog.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author 24699
 */
@Mapper
public interface HitMapper {
    @Insert("insert into hit (blog_id, user_id, create_time) values " +
            "(#{blogId}, #{userId}, #{createTime})")
    Boolean hit(Integer blogId, Integer userId, Long createTime);

    @Delete("delete from hit where blog_id=#{blogId} and user_id=#{userId}")
    Boolean deHit(Integer blogId, Integer userId);

    @Select("select * from hit where blog_id=#{blogId} and user_id=#{userId}")
    Boolean getStatus(Integer blogId, Integer userId);
}
