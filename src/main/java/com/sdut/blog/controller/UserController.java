package com.sdut.blog.controller;

import com.sdut.blog.annation.PassToken;
import com.sdut.blog.annation.UserLoginToken;
import com.sdut.blog.pojo.dto.User;
import com.sdut.blog.pojo.vo.UserVo;
import com.sdut.blog.service.MessageService;
import com.sdut.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 24699
 */
@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

    @UserLoginToken
    @PostMapping("/getPassword")
    public Object getPassword(@RequestParam("email") String email,
                              @RequestParam("verifyCode") String verifyCode) {
        return userService.getPassword(email, verifyCode);
    }

    @UserLoginToken
    @PostMapping("/updatePassword")
    public Object updatePassword(@RequestParam("email") String email,
                                 @RequestParam("password") String password,
                                 @RequestParam("verifyCode") String verifyCode) {
        return userService.updatePassword(email, password, verifyCode);
    }

    @UserLoginToken
    @GetMapping("/getMessage")
    public Object getMessage(@RequestParam("userId") Integer userId,
                             @RequestParam("readed") Boolean readed,
                             @RequestParam("pageSize") Integer pageSize,
                             @RequestParam("pageNum") Integer pageNum) {
        return messageService.getMessage(userId, readed, pageSize, pageNum);
    }

    @PassToken
    @GetMapping("/findUser")
    public Object findUser(@RequestParam(value = "pageSize",defaultValue = "1")Integer pageSize,
                           @RequestParam(value = "pageNum",defaultValue = "10")Integer pageNum,
                           @RequestParam(value = "keyword")String keyword) {
        return userService.findUser(pageSize, pageNum, keyword);
    }

    @UserLoginToken
    @PostMapping("/updateUser")
    public Object updateUser(@RequestParam("id") Integer userId,
                             @RequestParam("name") String name,
                             @RequestParam("sex") String sex,
                             @RequestParam("birthday") Long birthday,
                             @RequestParam("major") String major) {
        return userService.updateUser(userId, name, sex, birthday, major);
    }

    @UserLoginToken
    @PostMapping("/avatarUpload")
    public Object avatarUpload(@RequestParam("userId") Integer userId,
                               @RequestParam("file") MultipartFile file) {
        return userService.avatarUpload(file, userId);
    }

    @PassToken
    @PostMapping("/login")
    public Object login(@RequestBody User user) {
        return userService.login(user);
    }

    @PassToken
    @PostMapping("/getUser")
    public Object getUser(@RequestParam String token) {
        return  userService.getUserByToken(token);
    }

    @PassToken
    @PostMapping("/send")
    public Object send(@RequestParam("email") String email,
                       @RequestParam(value = "status", defaultValue = "1") Boolean status) {
        return userService.send(email, status);
    }

    @PassToken
    @PostMapping("/register")
    public Object register(@RequestBody UserVo userVo) {
        return userService.register(userVo);
    }
}
