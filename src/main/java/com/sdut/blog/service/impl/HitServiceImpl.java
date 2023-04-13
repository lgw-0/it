package com.sdut.blog.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sdut.blog.mapper.BlogMapper;
import com.sdut.blog.mapper.HitMapper;
import com.sdut.blog.service.HitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 24699
 */
@Service
public class HitServiceImpl implements HitService {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private HitMapper hitMapper;

    @Override
    public Object hit(Integer blogId, Integer userId) {
        JSONObject res = new JSONObject();
        if (blogMapper.addLikeCount(blogId)) {
            if (hitMapper.hit(blogId, userId, System.currentTimeMillis())) {
                res.put("code", 200);
                res.put("msg", "点赞成功");
            } else {
                res.put("code", 500);
                res.put("msg", "点赞失败");
            }
        } else {
            res.put("code", 500);
            res.put("msg", "收藏博客不存在");
        }
        return res;
    }

    @Override
    public Object deHit(Integer blogId, Integer userId) {
        JSONObject res = new JSONObject();
        if (blogMapper.reduceLikeCount(blogId)) {
            if (hitMapper.deHit(blogId, userId)) {
                res.put("code", 200);
                res.put("msg", "取消点赞成功");
            } else {
                res.put("code", 500);
                res.put("msg", "取消点赞失败");
            }
        } else {
            res.put("code", 500);
            res.put("msg", "点赞博客不存在");
        }
        return res;
    }

    @Override
    public Object getHit(Integer blogId, Integer userId) {
        JSONObject res = new JSONObject();
        if (hitMapper.getStatus(blogId, userId) != null) {
            Boolean hitStatus = hitMapper.getStatus(blogId, userId);
            res.put("code", 200);
            res.put("msg", "点赞状态获取成功");
            res.put("hitStatus", hitStatus);
        } else {
            res.put("code", 500);
            res.put("msg", "未点赞");
        }
        return res;
    }
}
