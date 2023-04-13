package com.sdut.blog.service;

/**
 * @author 24699
 */
public interface MessageService {
    void blogreturn(Integer blogId, String reason, Integer userId);

    void sendComment(Integer commentId, Integer userId);

    Object getMessage(Integer userId, Boolean readed, Integer pageSize, Integer pageNum);
}
