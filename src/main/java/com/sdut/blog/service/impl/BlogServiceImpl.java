package com.sdut.blog.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sdut.blog.mapper.BlogMapper;
import com.sdut.blog.mapper.UserMapper;
import com.sdut.blog.pojo.dto.Blog;
import com.sdut.blog.pojo.dto.User;
import com.sdut.blog.pojo.vo.BlogVo;
import com.sdut.blog.pojo.vo.DetailVo;
import com.sdut.blog.service.BlogService;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 24699
 */
@Service
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Object getAllBlog(Integer pageSize, Integer pageNum) {
        JSONObject res = new JSONObject();
        // 分页管理
        PageHelper.startPage(pageNum,pageSize);
        ArrayList<Blog> blogs = blogMapper.getAllBlogs();
        PageInfo<Blog> blogPageInfo = new PageInfo<>(blogs);
        // 获取信息
        List<Blog> list = blogPageInfo.getList();
        long total = blogPageInfo.getTotal();
        ArrayList<BlogVo> blogVos = getBlogVo(list);
        res.put("code", 200);
        res.put("msg", "获取成功");
        res.put("blogs", blogVos);
        res.put("total", total);
        return res;
    }

    @Override
    public Object addBlog(Blog blog) {
        JSONObject res = new JSONObject();
        if (userMapper.addBlogSum(blog.getPublisher())) {
            // 初始化
            blog.setIsReprint(false);
            blog.setApproved(0);
            blog.setLikeCount(0);
            blog.setViewCount(0);
            blog.setCommentCount(0);
            blog.setReprintCount(0);
            blog.setCollectCount(0);
            blog.setPublishTime(System.currentTimeMillis());

            if (blogMapper.insertBlog(blog)) {
                res.put("code", 200);
                res.put("msg", "已提交，待审核");
            } else {
                res.put("code", 500);
                res.put("msg", "提交失败");
            }
        } else {
            res.put("code", 500);
            res.put("msg", "用户不存在");
        }
        return res;
    }

    @Override
    public Object delete(Integer blogId) {
        JSONObject res = new JSONObject();
        if (userMapper.reduceBlogSum(blogMapper.getBlogById(blogId).getPublisher())) {
            if (blogMapper.deleteBlogById(blogId)) {
                res.put("code", 200);
                res.put("msg", "删除成功");
            } else {
                res.put("code", 500);
                res.put("msg", "删除失败");
            }
        } else {
            res.put("code", 500);
            res.put("msg", "用户不存在");
        }
        return res;
    }

    @Override
    public Object updateBlog(Blog blog) {
        // 修改更新数据
        userMapper.updatePublishTimeById(blog.getPublisher(), System.currentTimeMillis());
        JSONObject res = new JSONObject();
        blog.setApproved(0);
        blog.setPublishTime(System.currentTimeMillis());
        if (blogMapper.updateBlog(blog)) {
            res.put("code", 200);
            res.put("msg", "更新成功,待审核");
        } else {
            res.put("code", 500);
            res.put("msg", "更新失败");
        }
        return res;
    }

    @Override
    public Object reprint(Integer userId, Integer blogId, String annotation) {
        /**
         * 深拷贝
         * sourceObject 源对象
         * destinationClass 目标Class
         * targetObject 目标对象
         */
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        JSONObject res = new JSONObject();

        if (!userId.equals(blogMapper.getBlogById(blogId).getPublisher())) {
            // 获取原博客，添加转发量
            Blog blog = blogMapper.selectBlogById(blogId);
            if (blog != null) {
                blogMapper.addReprintCount(blogId);
                // 拷贝原博客
                Blog reprint = mapper.map(blog, Blog.class);
                // 修改变量
                reprint.setPublisher(userId);
                reprint.setIsReprint(true);
                reprint.setAnnotation(annotation);
                reprint.setLikeCount(0);
                reprint.setViewCount(0);
                reprint.setCommentCount(0);
                reprint.setReprintCount(0);
                reprint.setCollectCount(0);
                reprint.setApproved(1);
                reprint.setPublishTime(System.currentTimeMillis());
                blogMapper.insertBlog(reprint);
                res.put("code", 200);
                res.put("msg", "转发成功");
            } else {
                res.put("code", 500);
                res.put("msg", "转发失败");
            }
        } else {
            res.put("code", 500);
            res.put("msg", "请勿转发个人博客");
        }
        return res;
    }

    @Override
    public Object getBlogsByConditions(Integer userId, String tag, String status, String title, Integer pageSize, Integer pageNum) {
        JSONObject res = new JSONObject();
        if(userMapper.getUserById(userId) != null) {
            ArrayList<Blog> blogs;
            if("原创".equals(status)) {
                PageHelper.startPage(pageNum,pageSize);
                blogs = blogMapper.getMyBlogsByCondition(userId, tag, title);
            }
            else if ("转发".equals(status)){
                PageHelper.startPage(pageNum,pageSize);
                blogs = blogMapper.getBlogsByCondition_reprint(userId);
            }
            else if ("待审核".equals(status)){
                PageHelper.startPage(pageNum,pageSize);
                blogs = blogMapper.getBlogsByCondition_waitJudge(userId, tag, title);
            }
            else if ("已退回".equals(status)){
                PageHelper.startPage(pageNum,pageSize);
                blogs = blogMapper.getBlogsByCondition_return(userId, tag, title);
            }
            else {
                PageHelper.startPage(pageNum,pageSize);
                blogs = blogMapper.getBlogsByCondition_report(userId, tag, title);
            }
            PageInfo<Blog> blogPageInfo = new PageInfo<>(blogs);
            List<Blog> list = blogPageInfo.getList();
            long total = blogPageInfo.getTotal();
            res.put("code", 200);
            res.put("msg", "获取成功");
            res.put("total", total);
            res.put("blogs", list);
        } else {
            res.put("code", 500);
            res.put("msg","用户不存在");
        }
        return res;
    }

    @Override
    public Object getDetail(Integer blogId) {
        JSONObject res = new JSONObject();
        DetailVo detail = new DetailVo();
        if (blogMapper.addViewCount(blogId)) {
            Blog blog = blogMapper.getBlogById(blogId);
            detail.setBlog(blog);
            if (blog.getApproved() == 4) {
                User admin = new User();
                admin.setName("管理员");
                detail.setPublisher(admin);
            } else {
                detail.setPublisher(userMapper.getUserById(blog.getPublisher()));
            }
            res.put("code", 200);
            res.put("msg", "获取成功");
            res.put("detail", detail);
        } else {
            res.put("code", 500);
            res.put("msg", "获取失败");
        }
        return res;
    }

    @Override
    public Object getHotBlogs() {
        JSONObject res = new JSONObject();
        ArrayList<Blog> blogs = blogMapper.getHotBlogs();
        if (blogs != null) {
            ArrayList<BlogVo> blogVos = getBlogVo(blogs);
            res.put("code", 200);
            res.put("msg", "热榜获取成功");
            res.put("blogs", blogVos);
        } else {
            res.put("code", 500);
            res.put("msg", "热榜获取失败");
        }
        return res;
    }

    @Override
    public Object reportBlog(Integer blogId) {
        JSONObject res = new JSONObject();
        if (blogMapper.getBlogById(blogId) != null) {
            if (blogMapper.reportBlog(blogId)) {
                res.put("code", 200);
                res.put("msg", "举报成功，待管理员审核！");
            } else {
                res.put("code", 500);
                res.put("msg", "举报失败");
            }
        } else {
            res.put("code", 500);
            res.put("msg", "该博客不存在");
        }
        return res;
    }

    @Override
    public Object searchBlog(String keyword, Integer pageSize, Integer pageNum) {
        JSONObject res = new JSONObject();
        if (!keyword.equals("")) {
            PageHelper.startPage(pageNum,pageSize);
            ArrayList<Blog> blogs = blogMapper.searchBlog(keyword);
            PageInfo<Blog> blogPageInfo = new PageInfo<>(blogs);
            List<Blog> list = blogPageInfo.getList();
            ArrayList<BlogVo> blogVos = getBlogVo(list);
            long total = blogPageInfo.getTotal();
            if (blogs.size() != 0) {
                res.put("code", 200);
                res.put("msg", "查找成功");
                res.put("blogs", blogVos);
                res.put("total", total);
            } else {
                res.put("code", 200);
                res.put("msg", "结果为空");
            }
        } else {
            res.put("code", 500);
            res.put("msg", "关键词不能为空");
        }
        return res;
    }

    @Override
    public Object insertPublic(Blog blog) {
        JSONObject res = new JSONObject();
        blog.setIsReprint(false);
        blog.setApproved(4);
        blog.setLikeCount(0);
        blog.setViewCount(0);
        blog.setCommentCount(0);
        blog.setReprintCount(0);
        blog.setCollectCount(0);
        blog.setPublishTime(System.currentTimeMillis());
        if (blogMapper.insertBlog(blog)) {
            res.put("code", 200);
            res.put("msg", "公告已发布");
        } else {
            res.put("code", 500);
            res.put("msg", "发布失败");
        }
        return res;
    }

    @Override
    public Object getAllMyBlog(Integer userId, Integer pageSize, Integer pageNum) {
        JSONObject res = new JSONObject();
        PageHelper.startPage(pageNum,pageSize);
        ArrayList<Blog> blogs = blogMapper.getAllMyBlogs(userId);
        PageInfo<Blog> blogPageInfo = new PageInfo<>(blogs);
        List<Blog> list = blogPageInfo.getList();
        long total = blogPageInfo.getTotal();
        if (list != null) {
            res.put("code", 200);
            res.put("msg", "获取成功");
            res.put("total", total);
            res.put("blogs", list);
        } else {
            res.put("code", 500);
            res.put("msg", "获取失败");
        }
        return res;
    }

    @Override
    public Object getTagBlogs(String tag, Integer pageSize, Integer pageNum) {
        JSONObject res = new JSONObject();
        ArrayList<Blog> blogs;
        if (checkTag(tag)) {
            if ("公告".equals(tag)) {
                PageHelper.startPage(pageNum,pageSize);
                blogs = blogMapper.getPublic("公告");
                PageInfo<Blog> blogPageInfo = new PageInfo<>(blogs);
                List<Blog> list = blogPageInfo.getList();
                ArrayList<BlogVo> blogVos = getPublic(list);
                long total = blogPageInfo.getTotal();
                res.put("code", 200);
                res.put("msg", "获取成功");
                res.put("blogs", blogVos);
                res.put("total", total);
                return res;
            } else {
                PageHelper.startPage(pageNum,pageSize);
                blogs = blogMapper.getTagBlogs(tag);
            }
            PageInfo<Blog> blogPageInfo = new PageInfo<>(blogs);
            List<Blog> list = blogPageInfo.getList();
            ArrayList<BlogVo> blogVos = getBlogVo(list);
            long total = blogPageInfo.getTotal();
            res.put("code", 200);
            res.put("msg", "获取成功");
            res.put("blogs", blogVos);
            res.put("total", total);
        } else {
            res.put("code", 500);
            res.put("msg", "获取失败");
        }
        return res;
    }

    private boolean checkTag(String tag) {
        switch (tag) {
            case "二手闲置":
            case "恋爱交友":
            case "求助打听":
            case "校园招聘":
            case "公告":
                return true;
            default:
                return false;
        }

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

    private ArrayList<BlogVo> getPublic(List<Blog> list) {
        ArrayList<BlogVo> blogVos = new ArrayList<>();
        for (Blog blog : list) {
            BlogVo blogVo = new BlogVo();
            blogVo.setBlog(blog);
            blogVo.setUserName("管理员");
            blogVo.setWriter("管理员");
            blogVo.setAvatarUrl("http://192.168.30.203:8888/blog/file/admin.png");
            blogVos.add(blogVo);
        }
        return blogVos;
    }
}
