package com.sdut.blog.controller;

import com.sdut.blog.annation.UserLoginToken;
import com.sdut.blog.service.BlogService;
import com.sdut.blog.service.HitService;
import com.sdut.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 24699
 */
@CrossOrigin
@RestController
public class HitController {
    @Autowired
    private HitService hitService;

    @UserLoginToken
    @PostMapping("/hit")
    public Object hit(@RequestParam("hit") Integer hit,
                      @RequestParam("blogId") Integer blogId,
                      @RequestParam("userId") Integer userId) {
        if (hit == 1) {
            return hitService.hit(blogId, userId);
        }
        else if (hit == 0){
            return hitService.deHit(blogId, userId);
        } else {
            return hitService.getHit(blogId, userId);
        }
    }

}
