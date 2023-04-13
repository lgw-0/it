package com.sdut.blog.service;

import com.sdut.blog.pojo.dto.User;
import com.sdut.blog.pojo.vo.UserVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 24699
 */
public interface UserService {

    Object login(User user);

    Object register(UserVo userVo);

    Object send(String email, Boolean status);

    Object getUserByToken(String token);

    Object findUser(Integer pageSize, Integer pageNum, String keyword);

    Object avatarUpload(MultipartFile file, Integer userId);

    Object updateUser(Integer userId, String name, String sex, Long birthday, String major);

    Object updatePassword(String email, String password, String verifyCode);

    Object getPassword(String email, String verifyCode);
}
