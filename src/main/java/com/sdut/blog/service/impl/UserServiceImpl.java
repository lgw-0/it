package com.sdut.blog.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sdut.blog.mapper.UserMapper;
import com.sdut.blog.pojo.dto.User;
import com.sdut.blog.pojo.vo.UserVo;
import com.sdut.blog.service.MailService;
import com.sdut.blog.service.TokenService;
import com.sdut.blog.service.UserService;
import com.sdut.blog.utils.JasyptEncryptorUtils;
import com.sdut.blog.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 24699
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private MailService mailService;

    @Override
    public Object login(User user) {
        JSONObject res = new JSONObject();
        User currentUser = userMapper.selectUserByEmail(user.getEmail());
        if (currentUser == null) {
            res.put("code", 500);
            res.put("msg", "用户不存在");
        } else {
            String password = JasyptEncryptorUtils.decode(currentUser.getPassword());
            if (password.equals(user.getPassword())) {
                res.put("code", 200);
                res.put("msg","登录成功");
                String token = tokenService.getToken(user);
                res.put("token",token);
                res.put("currentUser", currentUser);
            } else {
                res.put("code", 500);
                res.put("msg","密码错误");
            }
        }
        return res;
    }

    @Override
    public Object register(UserVo userVo) {
        JSONObject res = new JSONObject();
        User user = userVo.getUser();

        if (userMapper.getUserByEmail(user.getEmail()) != null) {
            res.put("code",500);
            res.put("msg","该邮箱已被注册");
        }

        String verifyCode = (String) redisUtils.get(user.getEmail());

        // 判断验证码
        if (!verifyCode.equals(userVo.getVerifyCode())){
            res.put("code",500);
            res.put("msg","验证码错误");
        }
        else {
            //写入数据库
            user.setAvatarUrl("http://192.168.30.203:8888/blog/file/default_avatar.png");
            user.setCreateTime(System.currentTimeMillis());
            user.setUpdateTime(System.currentTimeMillis());
            user.setPassword(JasyptEncryptorUtils.encode(user.getPassword()));
            user.setBlogSum(0);
            user.setFans(0);
            user.setConcerned(0);
            userMapper.register(user);
            res.put("code",200);
            res.put("msg","注册成功");
        }
        return res;
    }

    @Override
    public Object send(String email, Boolean status) {
        JSONObject res = new JSONObject();
        // 注册
        if (status) {
            // 判断邮箱
            if (userMapper.selectUserByEmail(email)!=null){
                res.put("code",500);
                res.put("msg","该邮箱已被注册");
                return res;
            }
        }
        // 发送验证码
        if (mailService.sendMail(email)) {
            res.put("code",200);
            res.put("msg","验证码已发送，有效期为2分钟");
        } else {
            res.put("code",500);
            res.put("msg","发送失败，请重试");
        }
        return res;
    }

    @Override
    public Object getUserByToken(String token) {
        // 解码token 获取用户id
        Integer userId = tokenService.getUserIdByToken(token);
        return userMapper.getUserById(userId);
    }

    @Override
    public Object findUser(Integer pageSize, Integer pageNum, String keyword) {
        JSONObject res = new JSONObject();
        PageHelper.startPage(pageNum,pageSize);
        ArrayList<User> users = userMapper.findUser(keyword);
        if (users != null) {
            PageInfo<User> userPageInfo = new PageInfo<>(users);
            List<User> list = userPageInfo.getList();
            long total = userPageInfo.getTotal();
            res.put("code", 200);
            res.put("msg", "获取成功");
            res.put("users", list);
            res.put("total", total);
        } else {
            res.put("code", 500);
            res.put("msg", "获取失败");
        }
        return res;
    }

    @Override
    public Object avatarUpload(MultipartFile file, Integer userId) {
        JSONObject res = new JSONObject();
        if (file.isEmpty()) {
            res.put("code", 500);
            res.put("msg", "图片不存在");
        } else {
            //得到上传时的文件名
            String fileName = file.getOriginalFilename();
            int size = (int) file.getSize();
            System.out.println(fileName+"-->"+size);
            //上传的文件要存储的地址
            String storagePathPrefix = "D:/Blog/";
            Long time = System.currentTimeMillis();
//            System.out.println(time);
//            System.out.println(userId);
            //重命名
            String newName = userId.toString() + time + fileName.replace("-", "");
            String path = storagePathPrefix + userId + "/avatar/";
            File dest = new File(path + newName);
            //判断父目录是否存在
            if(!dest.getParentFile().exists()) {
                //getParentFile() : 获得父目录
                dest.getParentFile().mkdirs();
            }
            try{
                //transferTo(dest)方法将上传文件写到服务器上指定的文件
                file.transferTo(dest);

                //文件的映射地址
                String urlPath = null;

                String urlPrefix = "http://192.168.30.203:8888/blog/file/";
                urlPath = urlPrefix + userId+ "/avatar/" + newName;
                //把文件映射地址保存到用户信息表
                if (userMapper.updateAvatarById(userId, urlPath)) {
                    res.put("code", 200);
                    res.put("msg", "上传成功");
                    res.put("user", userMapper.getUserById(userId));
                }
                else {
                    res.put("code", 500);
                    res.put("msg", "上传失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                res.put("code", 500);
                res.put("msg", "上传失败");
            }
        }
        return res;
    }

    @Override
    public Object updateUser(Integer userId, String name, String sex, Long birthday, String major) {
        JSONObject res = new JSONObject();
        if (userMapper.getUserById(userId) != null) {
            if (userMapper.updateUser(userId, name, sex, birthday, major)) {
                res.put("code", 200);
                res.put("msg", "修改成功");
                User user = userMapper.getUserById(userId);
                res.put("user", user);
            } else {
                res.put("code", 500);
                res.put("msg", "修改失败");
            }
        } else {
            res.put("code", 500);
            res.put("msg", "该用户不存在");
        }
        return res;
    }

    @Override
    public Object updatePassword(String email, String password, String verifyCode) {
        JSONObject res = new JSONObject();
        User user = userMapper.getUserByEmail(email);
        if (user != null) {
            String realCode = (String) redisUtils.get(user.getEmail());
            if (realCode == null) {
                res.put("code", 500);
                res.put("msg", "验证码超时");
                return res;
            }
            // 判断验证码
            if (!realCode.equals(verifyCode)){
                res.put("code",500);
                res.put("msg","验证码错误");
            }
            else {
                String newPassword = JasyptEncryptorUtils.encode(password);
                if (userMapper.updatePassword(newPassword, user.getId())) {
                    res.put("code",200);
                    res.put("msg","密码修改成功");
                } else {
                    res.put("code",500);
                    res.put("msg","密码修改失败");
                }
            }
        } else {
            res.put("code",500);
            res.put("msg","该邮箱未注册");
        }
        return res;
    }

    @Override
    public Object getPassword(String email, String verifyCode) {
        JSONObject res = new JSONObject();
        User user = userMapper.getUserByEmail(email);
        if (user != null) {
            String realCode = (String) redisUtils.get(user.getEmail());
            if (realCode == null) {
                res.put("code", 500);
                res.put("msg", "验证码超时");
                return res;
            }
            // 判断验证码
            if (!realCode.equals(verifyCode)){
                res.put("code",500);
                res.put("msg","验证码错误");
            }
            else {
                String password = JasyptEncryptorUtils.decode(user.getPassword());
                System.out.println(password);
                res.put("code",200);
                res.put("msg","密码获取成功");
                res.put("password", password);
            }
        } else {
            res.put("code",500);
            res.put("msg","该邮箱未注册");
        }
        return res;
    }
}
