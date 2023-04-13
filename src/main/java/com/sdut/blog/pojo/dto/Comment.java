package com.sdut.blog.pojo.dto;

import lombok.Data;

/**
 * @author 24699
 */
@Data
public class Comment {
    private Integer id;
    private Integer blogId;
    private Integer userId;
    private Long createTime;
    private String content;
    private Integer status;
}
