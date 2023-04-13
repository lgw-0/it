package com.sdut.blog.pojo.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author 24699
 */
@Data
public class Blog {
    private Integer id;
    private Integer publisher;
    private String title;
    private String content;
    private Integer approved;
    private Long publishTime;
    private String tags;
    private Boolean isReprint;
    private String annotation;
    private Integer firstAuthor;
    private String category;
    private Integer likeCount;
    private Integer viewCount;
    private Integer commentCount;
    private Integer reprintCount;
    private Integer collectCount;
}
