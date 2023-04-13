package com.sdut.blog.mapper;

import com.sdut.blog.pojo.dto.Comment;
import com.sdut.blog.pojo.dto.SubComment;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 24699
 */
@Mapper
public interface CommentMapper {
    @Insert("insert into comment (blog_id, user_id, create_time, content, statuss) " +
            "values (#{blogId}, #{userId}, #{createTime}, #{content}, #{status})")
    Boolean addComment(Comment comment);

    @Insert("insert into sub_comment (blog_id, comment_id, send_id, receive_id, create_time, content, statuss) " +
            "values (#{blogId}, #{commentId}, #{sendId}, #{receiveId}, #{createTime}, #{content}, #{status})")
    Boolean addSubComment(SubComment subComment);

    @Select("select * from comment where (statuss=1 or statuss=2) and blog_id=#{blogId} order by create_time desc")
    ArrayList<Comment> getComment(Integer blogId);

    @Select("select * from comment where id = #{commentId}")
    Comment getCommentById(Integer commentId);

    @Select("select * from sub_comment where id=#{subCommentId}")
    SubComment getSubCommentById(Integer subCommentId);

    @Select("select * from sub_comment where (statuss=1 or statuss=2) and comment_id=#{commentId} order by create_time desc")
    ArrayList<SubComment> getSubComment(Integer commentId);

    @Delete("delete from comment where id = #{commentId}")
    Boolean deleteComment(Integer commentId);

    @Delete("delete from sub_comment where comment_id = #{commentId}")
    Boolean deleteSub(Integer commentId);

    @Delete("delete from sub_comment where id = #{subCommentId}")
    Boolean deleteSubById(Integer subCommentId);

    @Select("select * from sub_comment where comment_id = #{commentId}")
    List<SubComment> getSubCommentByCommentId(Integer commentId);

    @Update("update comment set statuss = 2 where id = #{commentId}")
    Boolean reportComment(Integer commentId);

    @Update("update sub_comment set statuss = 2 where id = #{subCommentId}")
    Boolean reportSub(Integer subCommentId);

    @Update("update comment set statuss = 1 where id = #{commentId}")
    Boolean passJudge(Integer commentId);

    @Update("update sub_comment set statuss = 1 where id = #{subCommentId}")
    Boolean passSubJudge(Integer subCommentId);
}
