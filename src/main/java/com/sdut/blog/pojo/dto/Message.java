package com.sdut.blog.pojo.dto;

import lombok.Data;

/**
 * @author 24699
 */
@Data
public class Message {
    private Integer id;
    private Integer userId;
    private String content;
    private Boolean readed;
    private String tag;
    private Long createTime;
}
