package com.sdut.blog.service.impl;

import com.sdut.blog.mapper.UserMapper;
import com.sdut.blog.service.MailService;
import com.sdut.blog.service.TokenService;
import com.sdut.blog.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisUtils redisUtils;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public boolean sendMail(String email) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            //主题
            mailMessage.setSubject("验证码邮件");
            //生成随机数
            String code = randomCode();

            //把email和生成的code放到redis中，email为key，code为value,有效时间设为120s
            redisUtils.set(email, code);
            redisUtils.expire(email, 120);

            //内容
            mailMessage.setText("您收到的验证码是："+code);
            //发给谁
            mailMessage.setTo(email);
            //你自己的邮箱
            mailMessage.setFrom(from);
            //发送
            mailSender.send(mailMessage);
            return  true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public String randomCode(){
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }
}
