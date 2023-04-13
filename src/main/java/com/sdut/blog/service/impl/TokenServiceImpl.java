package com.sdut.blog.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.sdut.blog.mapper.UserMapper;
import com.sdut.blog.pojo.dto.Admin;
import com.sdut.blog.pojo.dto.User;
import com.sdut.blog.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 24699
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public String getToken(User user) {
        String token="";
        token= JWT.create().withAudience(String.valueOf(user.getId()))
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }

    @Override
    public String getTokenForAdmin(Admin admin) {
        String token="";
        token= JWT.create().withAudience(String.valueOf(admin.getId()))
                .sign(Algorithm.HMAC256(admin.getPassword()));
        return token;
    }

    @Override
    public Integer getUserIdByToken(String token) {
        String userId = JWT.decode(token).getAudience().get(0);
        return Integer.parseInt(userId);
    }

    @Override
    public Boolean checkToken(String token) {
        // 执行认证
        if (token == null) {
            throw new RuntimeException("无token，请重新登录");
        }
        // 获取 token 中的 user id
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new RuntimeException("401");
        }
        Integer uid = Integer.valueOf(userId);
        User user = userMapper.getUserById(uid);
        if (user == null) {
            throw new RuntimeException("用户不存在，请重新登录");
        }
        // 验证 token   JWTVerifier解析对象，使用的算法和secret要与创建token时保持一致
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new RuntimeException("401");
        }
        return true;
    }


}
