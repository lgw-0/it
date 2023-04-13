package com.sdut.blog.pojo.dto;

import lombok.Data;

/**
 * @author 24699
 */
@Data
public class SubComment {
    private Integer id;
    private Integer blogId;
    private Integer commentId;
    private Integer sendId;
    private Integer receiveId;
    private String content;
    private Long createTime;
    private Integer status;
}
