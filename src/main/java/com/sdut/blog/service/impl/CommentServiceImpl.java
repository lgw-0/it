package com.sdut.blog.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sdut.blog.mapper.BlogMapper;
import com.sdut.blog.mapper.CommentMapper;
import com.sdut.blog.mapper.UserMapper;
import com.sdut.blog.pojo.dto.Comment;
import com.sdut.blog.pojo.dto.SubComment;
import com.sdut.blog.pojo.dto.User;
import com.sdut.blog.pojo.vo.CommentVo;
import com.sdut.blog.pojo.vo.SubCommentVo;
import com.sdut.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @author 24699
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Object addComment(Integer blogId, Integer sendId, String content) {
        JSONObject res = new JSONObject();
        // 博客评论量+1
        if (blogMapper.addCommentCount(blogId)) {
            Comment comment = new Comment();
            comment.setBlogId(blogId);
            comment.setUserId(sendId);
            comment.setContent(content);
            comment.setStatus(1);
            comment.setCreateTime(System.currentTimeMillis());
            // 添加评论
            if (commentMapper.addComment(comment)) {
                res.put("code", 200);
                res.put("msg", "评论成功");
            } else {
                res.put("code", 500);
                res.put("msg", "评论失败");
            }
        } else {
            res.put("code", 500);
            res.put("msg", "该博客不存在");
        }
        return res;
    }

    @Override
    public Object addSubComment(Integer blogId, Integer commentId, Integer sendId, Integer receivedId, String content) {
        JSONObject res = new JSONObject();
        // 判断自己
        if (sendId.equals(receivedId)) {
            res.put("code", 500);
            res.put("msg", "不可回复自己");
            return res;
        }
        // 添加博客评论量
        if (blogMapper.addCommentCount(blogId)) {
            SubComment subComment = new SubComment();
            subComment.setBlogId(blogId);
            subComment.setCommentId(commentId);
            subComment.setSendId(sendId);
            subComment.setReceiveId(receivedId);
            subComment.setContent(content);
            subComment.setStatus(1);
            subComment.setCreateTime(System.currentTimeMillis());
            // 添加子评论
            if (commentMapper.addSubComment(subComment)) {
                res.put("code", 200);
                res.put("msg", "回复成功");
            } else {
                res.put("code", 500);
                res.put("msg", "回复失败");
            }
        } else {
            res.put("code", 500);
            res.put("msg", "该博客不存在");
        }
        return res;
    }

    @Override
    public Object getComment(Integer blogId) {
        JSONObject res = new JSONObject();
        if (blogMapper.getBlogById(blogId) != null) {
            ArrayList<Comment> comments = commentMapper.getComment(blogId);
            if (comments.size() == 0) {
                res.put("code", 200);
                res.put("msg", "评论为空");
                return res;
            }
            ArrayList<CommentVo> commentVos = getCommentVo(comments);
            res.put("code", 200);
            res.put("msg", "获取成功");
            res.put("commentVos", commentVos);
        } else {
            res.put("code", 500);
            res.put("msg", "博客不存在");
        }
        return res;
    }

    @Override
    public Object reportComment(Integer commentId) {
        JSONObject res = new JSONObject();
        if (commentMapper.getCommentById(commentId) != null) {
            if (commentMapper.reportComment(commentId)) {
                res.put("code", 200);
                res.put("msg", "举报成功");
            } else {
                res.put("code", 500);
                res.put("msg", "举报失败");
            }
        } else {
            res.put("code", 500);
            res.put("msg", "评论不存在");
        }
        return res;
    }

    @Override
    public Object reportSub(Integer subCommentId) {
        JSONObject res = new JSONObject();
        if (commentMapper.getSubCommentById(subCommentId) != null) {
            if (commentMapper.reportSub(subCommentId)) {
                res.put("code", 200);
                res.put("msg", "举报成功");
            } else {
                res.put("code", 500);
                res.put("msg", "举报失败");
            }
        } else {
            res.put("code", 500);
            res.put("msg", "评论不存在");
        }
        return res;
    }

    @Override
    public Object getSubComment(Integer commentId) {
        JSONObject res = new JSONObject();
        if (commentMapper.getCommentById(commentId) != null) {
            ArrayList<SubComment> subComments = commentMapper.getSubComment(commentId);
            if (subComments.size() == 0) {
                res.put("code", 200);
                res.put("msg", "子评论为空");
                return res;
            }
            ArrayList<SubCommentVo> subCommentVos = getSubCommentVo(subComments);
            res.put("code", 200);
            res.put("msg", "获取成功");
            res.put("commentVos", subCommentVos);
        } else {
            res.put("code", 500);
            res.put("msg", "评论不存在");
        }
        return res;
    }

    private ArrayList<CommentVo> getCommentVo(ArrayList<Comment> comments) {
        ArrayList<CommentVo> commentVos = new ArrayList<>();
        for (Comment comment : comments) {
            CommentVo commentVo = new CommentVo();
            User user = userMapper.getUserById(comment.getUserId());
            commentVo.setUserName(user.getName());
            commentVo.setAvatarUrl(user.getAvatarUrl());
            commentVo.setComment(comment);
            commentVos.add(commentVo);
        }
        return commentVos;
    }

    private ArrayList<SubCommentVo> getSubCommentVo(ArrayList<SubComment> subComments) {
        ArrayList<SubCommentVo> subCommentVos = new ArrayList<>();
        for (SubComment subComment : subComments) {
            SubCommentVo subCommentVo = new SubCommentVo();
            User send = userMapper.getUserById(subComment.getSendId());
            User receive = userMapper.getUserById(subComment.getReceiveId());
            subCommentVo.setSendName(send.getName());
            subCommentVo.setSendUrl(send.getAvatarUrl());
            subCommentVo.setReceiveName(receive.getName());
            subCommentVo.setReceiveUrl(receive.getAvatarUrl());
            subCommentVo.setSubComment(subComment);
            subCommentVos.add(subCommentVo);
        }
        return subCommentVos;
    }
}
