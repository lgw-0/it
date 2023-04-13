package com.sdut.blog.pojo.dto;

import lombok.Data;

/**
 * @author 24699
 */
@Data
public class User {
    private Integer id;
    private String email;
    private String password;
    private String name;
    private String sex;
    private Long birthday;
    private Long createTime;
    private Long updateTime;
    private String phone;
    private String major;
    private String personalProfile;
    private String avatarUrl;
    private Integer blogSum;
    private Integer fans;
    private Integer concerned;
}
