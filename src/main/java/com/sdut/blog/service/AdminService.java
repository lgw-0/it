package com.sdut.blog.service;

import com.sdut.blog.pojo.dto.Admin;

/**
 * @author 24699
 */
public interface AdminService {
    Object login(Admin admin);

    Object getJudge(Integer adminId, Integer pageSize, Integer pageNum);

    Object getSnitch(Integer adminId, Integer pageSize, Integer pageNum);

    Object doJudge(Integer blogId, Integer adminId, Integer approved, String reason);

    Object getJudgeComment(Integer pageSize, Integer pageNum);

    Object getJudgeSub(Integer pageSize, Integer pageNum);

    Object judgeComment(Integer status, Integer commentId);

    Object judgeSubComment(Integer status, Integer subCommentId);

    Object giveAdmin(String email);

    Object getNum();

    Object getSexNum();
}
