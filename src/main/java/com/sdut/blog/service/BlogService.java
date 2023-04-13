package com.sdut.blog.service;

import com.sdut.blog.pojo.dto.Blog;

/**
 * @author 24699
 */
public interface BlogService {
    Object getAllBlog(Integer pageSize, Integer pageNum);

    Object addBlog(Blog blog);

    Object getTagBlogs(String tag, Integer pageSize, Integer pageNum);

    Object delete(Integer blogId);

    Object updateBlog(Blog blog);

    Object reprint(Integer userId, Integer blogId, String annotation);

    Object getBlogsByConditions(Integer userId, String tag, String status, String title, Integer pageSize, Integer pageNum);

    Object getDetail(Integer blogId);

    Object getHotBlogs();

    Object reportBlog(Integer blogId);

    Object searchBlog(String keyword, Integer pageSize, Integer pageNum);

    Object insertPublic(Blog blog);

    Object getAllMyBlog(Integer userId, Integer pageSize, Integer pageNum);
}
