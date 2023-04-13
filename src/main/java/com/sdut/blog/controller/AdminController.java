package com.sdut.blog.controller;

import com.sdut.blog.annation.AdminLoginToken;
import com.sdut.blog.annation.PassToken;
import com.sdut.blog.pojo.dto.Admin;
import com.sdut.blog.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 24699
 */
@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @AdminLoginToken
    @PostMapping("/getSexNum")
    public Object getSexNum() {
        return adminService.getSexNum();
    }

    @AdminLoginToken
    @PostMapping("/getNum")
    public Object getNum() {
        return adminService.getNum();
    }

    @AdminLoginToken
    @PostMapping("/giveAdmin")
    public Object giveAdmin(@RequestParam String email) {
        return adminService.giveAdmin(email);
    }

    @AdminLoginToken
    @PostMapping("/judgeComment")
    public Object judgeComment(@RequestParam(value = "status") Integer status,
                               @RequestParam(value = "commentId",required = false) Integer commentId,
                               @RequestParam(value = "subCommentId", required = false) Integer subCommentId) {
        if (commentId != null) {
            return adminService.judgeComment(status, commentId);
        } else {
            return adminService.judgeSubComment(status, subCommentId);
        }
    }

    @AdminLoginToken
    @GetMapping("/getComment")
    public Object getComment(@RequestParam(value = "pos",defaultValue = "father") String pos,
                             @RequestParam(value = "pageSize", defaultValue = "8") Integer pageSize,
                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        if ("father".equals(pos)) {
            return adminService.getJudgeComment(pageSize, pageNum);
        } else {
            return adminService.getJudgeSub(pageSize, pageNum);
        }
    }

    @AdminLoginToken
    @PostMapping("/doJudge")
    public Object judge(@RequestParam("blogId") Integer blogId,
                        @RequestParam("adminId") Integer adminId,
                        @RequestParam("approved") Integer approved,
                        @RequestParam("reason") String reason) {
        return adminService.doJudge(blogId, adminId, approved, reason);
    }

    @AdminLoginToken
    @GetMapping("/getJudge")
    public Object getJudge(@RequestParam("adminId") Integer adminId,
                        @RequestParam(value = "status",defaultValue = "审核") String status,
                        @RequestParam("pageSize") Integer pageSize,
                        @RequestParam("pageNum") Integer pageNum) {
        if ("审核".equals(status)){
            return adminService.getJudge(adminId, pageSize, pageNum);
        } else {
            return adminService.getSnitch(adminId, pageSize, pageNum);
        }
    }

    @PassToken
    @PostMapping("/login")
    public Object login(@RequestBody Admin admin) {
        return adminService.login(admin);
    }
}
