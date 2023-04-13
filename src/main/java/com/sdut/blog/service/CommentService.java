package com.sdut.blog.service;

/**
 * @author 24699
 */
public interface CommentService {
    Object addComment(Integer blogId, Integer sendId, String content);

    Object addSubComment(Integer blogId, Integer commentId, Integer sendId, Integer receivedId, String content);

    Object getSubComment(Integer commentId);

    Object getComment(Integer blogId);

    Object reportComment(Integer commentId);

    Object reportSub(Integer subCommentId);
}
