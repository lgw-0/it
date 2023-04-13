package com.sdut.blog.service;

/**
 * @author 24699
 */
public interface CollectService {
    Object addCollect(Integer blogId, Integer userId);

    Object cancelCollect(Integer blogId, Integer userId);

    Object getCollect(Integer id, Integer pageSize, Integer pageNum);

    Object getStatus(Integer blogId, Integer userId);
}
