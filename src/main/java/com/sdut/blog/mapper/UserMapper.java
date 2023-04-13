package com.sdut.blog.mapper;

import com.sdut.blog.pojo.dto.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;

/**
 * @author 24699
 */
@Mapper
public interface UserMapper {
    @Insert("insert into user (email,password,name,sex,birthday,phone,major,personal_profile," +
            "avatar_url,create_time,update_time,blog_sum,fans,concerned) values (#{email},#{password},#{name},#{sex},#{birthday}," +
            "#{phone},#{major},#{personalProfile},#{avatarUrl},#{createTime},#{updateTime},#{blogSum},#{fans},#{concerned})")
    boolean register(User user);

    @Select("select * from user where email = #{email}")
    User selectUserByEmail(String email);

    @Select("select * from user where (email like concat('',#{keyword},'%') or " +
            "name like concat('%',#{keyword},'%')) and email not in (select email from admin)")
    ArrayList<User> findUser(String keyword);

    @Update("update user set avatar_url = #{urlPath} where id = #{userId}")
    Boolean updateAvatarById(Integer userId, String urlPath);

    @Select("select * from user where id = #{uid}")
    User getUserById(Integer uid);

    @Update("update user set update_time = #{publishTime} where id = #{publisher}")
    void updatePublishTimeById(Integer publisher, Long publishTime);

    @Update("update user set name=#{name},sex=#{sex},birthday=#{birthday},major=#{major} where id=#{userId}")
    Boolean updateUser(Integer userId, String name, String sex, Long birthday, String major);

    @Select("select * from user where email = #{email}")
    User getUserByEmail(String email);

    @Update("update user set blog_sum = blog_sum+1 where id = #{publisher}")
    Boolean addBlogSum(Integer publisher);

    @Update("update user set blog_sum = blog_sum-1 where id = #{publisher}")
    Boolean reduceBlogSum(Integer publisher);

    @Update("update user set password = #{newPassword} where id = #{id}")
    Boolean updatePassword(String newPassword, Integer id);
}
