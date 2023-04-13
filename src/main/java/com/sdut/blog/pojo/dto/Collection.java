package com.sdut.blog.pojo.dto;

import lombok.Data;

/**
 * @author 24699
 */
@Data
public class Collection {
    private Integer userId;
    private Integer blogId;
    private Long collectTime;
}
