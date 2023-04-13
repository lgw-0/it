package com.sdut.blog.service;


import com.sdut.blog.pojo.dto.Admin;
import com.sdut.blog.pojo.dto.User;

/**
 * @author 24699
 */
public interface TokenService {
    String getToken(User user);

    String getTokenForAdmin(Admin admin);

    Integer getUserIdByToken(String token);

    Boolean checkToken(String token);
}
