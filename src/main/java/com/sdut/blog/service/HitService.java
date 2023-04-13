package com.sdut.blog.service;

/**
 * @author 24699
 */
public interface HitService {
    Object hit(Integer blogId, Integer userId);

    Object deHit(Integer blogId, Integer userId);

    Object getHit(Integer blogId, Integer userId);
}
