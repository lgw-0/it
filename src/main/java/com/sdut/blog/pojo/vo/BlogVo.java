package com.sdut.blog.pojo.vo;

import com.sdut.blog.pojo.dto.Blog;
import lombok.Data;

/**
 * @author 24699
 */
@Data
public class BlogVo {
    private Blog blog;
    private String userName;
    private String avatarUrl;
    private String writer;
}
