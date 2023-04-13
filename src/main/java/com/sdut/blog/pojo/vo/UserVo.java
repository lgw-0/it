package com.sdut.blog.pojo.vo;

import com.sdut.blog.pojo.dto.User;
import lombok.Data;

/**
 * @author 24699
 */
@Data
public class UserVo {
    private User user;
    private String verifyCode;
    private String token;
}
