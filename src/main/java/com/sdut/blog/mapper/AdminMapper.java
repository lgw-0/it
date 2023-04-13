package com.sdut.blog.mapper;

import com.sdut.blog.pojo.dto.Admin;
import com.sdut.blog.pojo.dto.Blog;
import com.sdut.blog.pojo.dto.Comment;
import com.sdut.blog.pojo.dto.SubComment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;

/**
 * @author 24699
 */
@Mapper
public interface AdminMapper {
    @Select("select * from admin where email = #{email}")
    Admin selectAdminByEmail(String email);

    @Select("select * from blog where approved = 0")
    ArrayList<Blog> selectUnJudged();

    @Select("select * from blog where approved = 3 and is_reprint=0")
    ArrayList<Blog> selectSnitch();

    @Select("select * from admin where id = #{adminId}")
    Admin getAdminById(Integer adminId);

    @Update("update blog set approved=#{approved}, judge_id=#{adminId} where id=#{blogId}")
    Boolean judge(Integer blogId, Integer adminId, Integer approved);

    @Select("select * from comment where statuss = 2")
    ArrayList<Comment> getJudgeComment();

    @Select("select * from sub_comment where statuss = 2")
    ArrayList<SubComment> getJudgeSub();

    @Insert("insert into admin (email,password,name) values (#{email},#{password},#{name})")
    Boolean insertAdmin(Admin admin);

    @Select("select count(*) from blog where tags=#{tags}")
    Object getNum(String tags);

    @Select("select count(*) from user where sex=#{sex}")
    Object getSexNum(String sex);
}
