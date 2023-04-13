package com.sdut.blog.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sdut.blog.mapper.*;
import com.sdut.blog.pojo.dto.*;
import com.sdut.blog.service.AdminService;
import com.sdut.blog.service.MessageService;
import com.sdut.blog.service.TokenService;
import com.sdut.blog.utils.JasyptEncryptorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 24699
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MessageService messageService;
    @Autowired
    private TokenService tokenService;

    @Override
    public Object login(Admin admin) {
        JSONObject res = new JSONObject();
        if (adminMapper.selectAdminByEmail(admin.getEmail()) == null) {
            res.put("code", 500);
            res.put("msg", "管理员不存在");
        } else {
            Admin adminBase = adminMapper.selectAdminByEmail(admin.getEmail());
            String password = JasyptEncryptorUtils.decode(adminBase.getPassword());
            if (password.equals(admin.getPassword())) {
                res.put("code", 200);
                res.put("msg","登录成功");
                String token = tokenService.getTokenForAdmin(admin);
                res.put("token",token);
                res.put("admin", adminBase);
            } else {
                res.put("code", 500);
                res.put("msg","密码错误");
            }
        }
        return res;
    }

    @Override
    public Object getJudge(Integer adminId, Integer pageSize, Integer pageNum) {
        JSONObject res = new JSONObject();
        if (adminMapper.getAdminById(adminId) != null) {
            PageHelper.startPage(pageNum,pageSize);
            ArrayList<Blog> blogs = adminMapper.selectUnJudged();
            PageInfo<Blog> blogPageInfo = new PageInfo<>(blogs);
            List<Blog> list = blogPageInfo.getList();
            long total = blogPageInfo.getTotal();
            res.put("code", 200);
            res.put("msg", "获取成功");
            res.put("blogs", list);
            res.put("total", total);
        } else {
            res.put("code", 500);
            res.put("msg","管理员不存在");
        }
        return res;
    }

    @Override
    public Object getSnitch(Integer adminId, Integer pageSize, Integer pageNum) {
        JSONObject res = new JSONObject();
        if (adminMapper.getAdminById(adminId) != null) {
            PageHelper.startPage(pageNum,pageSize);
            ArrayList<Blog> blogs = adminMapper.selectSnitch();
            PageInfo<Blog> blogPageInfo = new PageInfo<>(blogs);
            List<Blog> list = blogPageInfo.getList();
            long total = blogPageInfo.getTotal();
            res.put("code", 200);
            res.put("msg", "获取成功");
            res.put("blogs", list);
            res.put("total", total);
        } else {
            res.put("code", 500);
            res.put("msg","管理员不存在");
        }
        return res;
    }

    @Override
    public Object doJudge(Integer blogId, Integer adminId, Integer approved, String reason) {
        JSONObject res = new JSONObject();
        if (blogMapper.getBlogById(blogId) != null) {
            if (approved == 2 && (reason.equals("") || reason == null)) {
                res.put("code", 500);
                res.put("msg", "审核失败，退回原因不能为空");
                return res;
            }
            if (adminMapper.judge(blogId, adminId, approved)) {
                if (approved == 1) {
                    res.put("code", 200);
                    res.put("msg", "审核通过");
                }
                else {
                    messageService.blogreturn(blogId, reason, blogMapper.getBlogById(blogId).getPublisher());
                    res.put("code", 200);
                    res.put("msg", "已退回");
                }
            }
        } else {
            res.put("code", "500");
            res.put("msg", "该博客不存在");
        }
        return res;
    }

    @Override
    public Object getJudgeComment(Integer pageSize, Integer pageNum) {
        JSONObject res = new JSONObject();
        PageHelper.startPage(pageNum,pageSize);
        ArrayList<Comment> judgeComment = adminMapper.getJudgeComment();
        PageInfo<Comment> commentPageInfo = new PageInfo<>(judgeComment);
        List<Comment> list = commentPageInfo.getList();
        long total = commentPageInfo.getTotal();
        res.put("code", 200);
        res.put("msg", "举报一级评论获取成功");
        res.put("comment", list);
        res.put("total", total);
        return res;
    }

    @Override
    public Object getJudgeSub(Integer pageSize, Integer pageNum) {
        JSONObject res = new JSONObject();
        PageHelper.startPage(pageNum,pageSize);
        ArrayList<SubComment> subComments = adminMapper.getJudgeSub();
        PageInfo<SubComment> subCommentPageInfo = new PageInfo<>(subComments);
        List<SubComment> list = subCommentPageInfo.getList();
        long total = subCommentPageInfo.getTotal();
        res.put("code", 200);
        res.put("msg", "举报二级评论获取成功");
        res.put("comment", list);
        res.put("total", total);
        return res;
    }

    @Override
    public Object judgeComment(Integer status, Integer commentId) {
        JSONObject res = new JSONObject();
        if (commentMapper.getCommentById(commentId) != null) {
            if (status == 1) {
                if (commentMapper.passJudge(commentId)) {
                    res.put("code", 200);
                    res.put("msg", "评论审核通过");
                } else {
                    res.put("code", 500);
                    res.put("msg", "评论审核失败");
                }
                return res;
            }
            // 发送消息
            messageService.sendComment(commentId,commentMapper.getCommentById(commentId).getUserId());
            // 删除评论
            if (commentMapper.getSubCommentByCommentId(commentId).size() == 0) {
                commentMapper.deleteComment(commentId);
                res.put("code", 200);
                res.put("msg", "删除一级评论成功");
            } else {
                if (commentMapper.deleteComment(commentId) && commentMapper.deleteSub(commentId)) {
                    res.put("code", 200);
                    res.put("msg", "一级及其二级评论删除成功");
                } else {
                    res.put("code", 500);
                    res.put("msg", "删除失败");
                }
            }
        } else {
            res.put("code", 500);
            res.put("msg","一级评论不存在");
        }
        return res;
    }

    @Override
    public Object judgeSubComment(Integer status, Integer subCommentId) {
        JSONObject res = new JSONObject();
        if (commentMapper.getSubCommentById(subCommentId) != null) {
            if (status == 1) {
                if (commentMapper.passSubJudge(subCommentId)) {
                    res.put("code", 200);
                    res.put("msg", "评论审核通过");
                } else {
                    res.put("code", 500);
                    res.put("msg", "评论审核失败");
                }
                return res;
            }
            messageService.sendComment(subCommentId,commentMapper.getSubCommentById(subCommentId).getSendId());
            if (commentMapper.deleteSubById(subCommentId)) {
                res.put("code", 200);
                res.put("msg", "二级评论删除成功");
            } else {
                res.put("code", 500);
                res.put("msg", "删除失败");
            }
        } else {
            res.put("code", 500);
            res.put("msg","二级评论不存在");
        }
        return res;
    }

    @Override
    public Object giveAdmin(String email) {
        JSONObject res = new JSONObject();
        User user = userMapper.getUserByEmail(email);
        if (user != null) {
            Admin admin = new Admin();
            admin.setEmail(email);
            admin.setName(user.getName());
            admin.setPassword(user.getPassword());
            if (adminMapper.insertAdmin(admin)) {
                res.put("code", 200);
                res.put("msg", "管理员添加成功");
            } else {
                res.put("code", 500);
                res.put("msg", "管理员添加失败");
            }
        } else {
            res.put("code", 500);
            res.put("msg", "该用户不存在");
        }
        return res;
    }

    @Override
    public Object getNum() {
        JSONObject res = new JSONObject();
        res.put("number1", adminMapper.getNum("二手闲置"));
        res.put("number2", adminMapper.getNum("求助打听"));
        res.put("number3", adminMapper.getNum("恋爱交友"));
        res.put("number4", adminMapper.getNum("校园招聘"));
        return res;
    }

    @Override
    public Object getSexNum() {
        JSONObject res = new JSONObject();
        res.put("sex1", adminMapper.getSexNum("男"));
        res.put("sex2", adminMapper.getSexNum("女"));
        return res;
    }
}
