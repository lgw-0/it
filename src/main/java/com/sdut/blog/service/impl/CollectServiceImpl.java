package com.sdut.blog.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sdut.blog.mapper.BlogMapper;
import com.sdut.blog.mapper.CollectMapper;
import com.sdut.blog.mapper.UserMapper;
import com.sdut.blog.pojo.dto.Blog;
import com.sdut.blog.pojo.vo.BlogVo;
import com.sdut.blog.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 24699
 */
@Service
public class CollectServiceImpl implements CollectService {

    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Object addCollect(Integer blogId, Integer userId) {
        JSONObject res = new JSONObject();
        if (blogMapper.addCollectCount(blogId)) {
            if (collectMapper.addCollect(blogId, userId, System.currentTimeMillis())) {
                res.put("code", 200);
                res.put("msg", "收藏成功");
            } else {
                res.put("code", 500);
                res.put("msg", "收藏失败");
            }
        } else {
            res.put("code", 500);
            res.put("msg", "收藏博客不存在");
        }
        return res;
    }

    @Override
    public Object cancelCollect(Integer blogId, Integer userId) {
        JSONObject res = new JSONObject();
        if (blogMapper.reduceCollectCount(blogId)) {
            if (collectMapper.deleteCollect(blogId, userId)) {
                res.put("code", 200);
                res.put("msg", "取消成功");
            } else {
                res.put("code", 500);
                res.put("msg", "取消失败");
            }
        } else {
            res.put("code", 500);
            res.put("msg", "收藏博客不存在");
        }
        return res;
    }

    @Override
    public Object getCollect(Integer userId, Integer pageSize, Integer pageNum) {
        JSONObject res = new JSONObject();
        if (userMapper.getUserById(userId) != null) {
            PageHelper.startPage(pageNum,pageSize);
            ArrayList<Blog> blogs = collectMapper.getBlogs(userId);
            PageInfo<Blog> blogPageInfo = new PageInfo<>(blogs);
            List<Blog> list = blogPageInfo.getList();
            ArrayList<BlogVo> blogVos = getBlogVo(list);
            long total = blogPageInfo.getTotal();
            res.put("total", total);
            res.put("code", 200);
            res.put("msg", "收藏列表获取成功");
            res.put("blogs", blogVos);
        } else {
            res.put("code", 500);
            res.put("msg", "用户不存在");
        }
        return res;
    }

    @Override
    public Object getStatus(Integer blogId, Integer userId) {
        JSONObject res = new JSONObject();
        if (collectMapper.getStatus(blogId, userId) != null) {
            Boolean colStatus = collectMapper.getStatus(blogId, userId);
            res.put("code", 200);
            res.put("msg", "收藏状态获取成功");
            res.put("colStatus", colStatus);
        } else {
            res.put("code", 500);
            res.put("msg", "未收藏");
        }
        return res;
    }

    private ArrayList<BlogVo> getBlogVo(List<Blog> list) {
        ArrayList<BlogVo> blogVos = new ArrayList<>();
        for (Blog blog : list) {
            BlogVo blogVo = new BlogVo();
            blogVo.setBlog(blog);
            blogVo.setUserName(userMapper.getUserById(blog.getPublisher()).getName());
            blogVo.setWriter(userMapper.getUserById(blog.getFirstAuthor()).getName());
            blogVo.setAvatarUrl(userMapper.getUserById(blog.getPublisher()).getAvatarUrl());
            blogVos.add(blogVo);
        }
        return blogVos;
    }

}
