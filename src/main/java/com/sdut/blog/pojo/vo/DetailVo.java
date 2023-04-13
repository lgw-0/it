package com.sdut.blog.pojo.vo;

import com.sdut.blog.pojo.dto.Blog;
import com.sdut.blog.pojo.dto.User;
import lombok.Data;

/**
 * @author 24699
 */
@Data
public class DetailVo {
    private Blog blog;
    private User publisher;
}
