package com.sdut.blog.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sdut.blog.mapper.MessageMapper;
import com.sdut.blog.mapper.UserMapper;
import com.sdut.blog.pojo.dto.Message;
import com.sdut.blog.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 24699
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public void blogreturn(Integer blogId, String reason, Integer userId) {
        Message message = new Message();
        message.setUserId(userId);
        message.setContent(reason);
        message.setReaded(false);
        message.setTag("博客退回");
        message.setCreateTime(System.currentTimeMillis());
        messageMapper.addMessage(message);
    }

    @Override
    public void sendComment(Integer commentId, Integer userId) {
        Message message = new Message();
        message.setUserId(userId);
        message.setContent("您的评论不合规，已被管理员删除");
        message.setReaded(false);
        message.setTag("评论非法");
        message.setCreateTime(System.currentTimeMillis());
        messageMapper.addMessage(message);
    }

    @Override
    public Object getMessage(Integer userId, Boolean readed, Integer pageSize, Integer pageNum) {
        JSONObject res = new JSONObject();
        if (userMapper.getUserById(userId) != null) {
            PageHelper.startPage(pageNum,pageSize);
            ArrayList<Message> messages = messageMapper.getMessage(userId, readed);
            PageInfo<Message> messagePageInfo = new PageInfo<>(messages);
            List<Message> list = messagePageInfo.getList();
            long total = messagePageInfo.getTotal();
            res.put("code", 200);
            res.put("msg", "消息获取成功");
            res.put("messages", list);
            res.put("total", total);
        } else {
            res.put("code", 500);
            res.put("msg", "该用户不存在");
        }
        return res;
    }
}
