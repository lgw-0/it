package com.sdut.blog.pojo.vo;

import com.sdut.blog.pojo.dto.Comment;
import lombok.Data;

/**
 * @author 24699
 */
@Data
public class CommentVo {
    private Comment comment;
    private String userName;
    private String avatarUrl;
}
