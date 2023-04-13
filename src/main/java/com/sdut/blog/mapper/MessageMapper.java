package com.sdut.blog.mapper;

import com.sdut.blog.pojo.dto.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

/**
 * @author 24699
 */
@Mapper
public interface MessageMapper {
    @Insert("insert into message (user_id,content,readed,create_time,tag) " +
            "values (#{userId},#{content},#{readed},#{createTime},#{tag})")
    void addMessage(Message message);

    @Select("select * from message where user_id = #{userId} and readed = #{readed} " +
            "order by create_time desc")
    ArrayList<Message> getMessage(Integer userId, Boolean readed);
}
