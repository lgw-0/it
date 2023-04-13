package com.sdut.blog.controller;

import com.sdut.blog.annation.UserLoginToken;
import com.sdut.blog.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 24699
 */
@RestController
@CrossOrigin
public class CollectController {
    @Autowired
    private CollectService collectService;

    @UserLoginToken
    @GetMapping("/getCollect")
    public Object getCollect(@RequestParam("userId") Integer userId,
                             @RequestParam("pageSize") Integer pageSize,
                             @RequestParam("pageNum") Integer pageNum) {
        return collectService.getCollect(userId, pageSize, pageNum);
    }

    @UserLoginToken
    @PostMapping("/collect")
    public Object collect(@RequestParam("collect") Integer collect,
                          @RequestParam("blogId") Integer blogId,
                          @RequestParam("userId") Integer userId) {
        if (collect == 1) {
            return collectService.addCollect(blogId, userId);
        }
        else if (collect == 0){
            return collectService.cancelCollect(blogId, userId);
        } else {
            return collectService.getStatus(blogId, userId);
        }
    }
}
