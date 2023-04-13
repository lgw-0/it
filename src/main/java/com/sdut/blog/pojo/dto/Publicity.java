package com.sdut.blog.pojo.dto;

import lombok.Data;

/**
 * @author 24699
 */
@Data
public class Publicity {
    private Integer id;
    private String content;
    private Integer adminId;
    private Integer receivedId;
    private Long createTime;
}
