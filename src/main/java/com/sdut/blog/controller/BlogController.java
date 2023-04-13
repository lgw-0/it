package com.sdut.blog.controller;

import com.sdut.blog.annation.PassToken;
import com.sdut.blog.annation.UserLoginToken;
import com.sdut.blog.pojo.dto.Blog;
import com.sdut.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 24699
 */
@CrossOrigin
@RestController
public class BlogController {
    @Autowired
    private BlogService blogService;



    @PassToken
    @PostMapping("/getDetail")
    public Object getDetail(@RequestParam("blogId") String blogId) {
        return blogService.getDetail(Integer.valueOf((blogId)));
    }

    @PassToken
    @GetMapping("/getBlogs")
    public Object getTagBlogs(@RequestParam(value = "tag", defaultValue = "全部") String tag,
                              @RequestParam("pageSize") Integer pageSize,
                              @RequestParam("pageNum") Integer pageNum) {
        if (!"全部".equals(tag)) {
            return blogService.getTagBlogs(tag, pageSize, pageNum);
        } else {
            return blogService.getAllBlog(pageSize, pageNum);
        }
    }

    @PassToken
    @GetMapping("/getHot")
    private Object getHot() {
        return blogService.getHotBlogs();
    }

    @PassToken
    @GetMapping("/searchBlogs")
    public Object searchBlog(@RequestParam("keyword") String keyword,
                             @RequestParam("pageSize") Integer pageSize,
                             @RequestParam("pageNum") Integer pageNum) {
        return blogService.searchBlog(keyword, pageSize, pageNum);
    }

    @UserLoginToken
    @GetMapping("/myBlog")
    public Object getMyBlogs(@RequestParam("userId") Integer userId,
                             @RequestParam(value = "tag",defaultValue = "二手闲置") String tag,
                             @RequestParam(value = "status", defaultValue = "全部") String  status,
                             @RequestParam(value = "title", defaultValue = "") String title,
                             @RequestParam("pageSize") Integer pageSize,
                             @RequestParam("pageNum") Integer pageNum) {
        if ("全部".equals(status)) {
            return blogService.getAllMyBlog(userId, pageSize, pageNum);
        } else {
            return blogService.getBlogsByConditions(userId, tag, status, title, pageSize, pageNum);
        }
    }

    @UserLoginToken
    @PostMapping("/addBlog")
    public Object addBlog(@RequestBody Blog blog) {
        if ("公告".equals(blog.getTags())) {
            return blogService.insertPublic(blog);
        } else {
            return blogService.addBlog(blog);
        }
    }

    @UserLoginToken
    @PostMapping("/deleteBlog")
    public Object deleteBlog(@RequestParam("blogId") Integer blogId) {
        return blogService.delete(blogId);
    }

    @UserLoginToken
    @PostMapping("/updateBlog")
    public Object updateBlog(@RequestBody Blog blog) {
        return blogService.updateBlog(blog);
    }

    @UserLoginToken
    @PostMapping("/reportBlog")
    public Object reportBlog(@RequestParam("blogId") Integer blogId) {
        return blogService.reportBlog(blogId);
    }

    @UserLoginToken
    @PostMapping("/reprint")
    public Object reprint(@RequestParam("userId") Integer userId,
                          @RequestParam("blogId") Integer blogId,
                          @RequestParam("annotation") String annotation) {
        return blogService.reprint(userId, blogId, annotation);
    }
}
