package com.sdut.blog;

import com.sdut.blog.mapper.AdminMapper;
import com.sdut.blog.mapper.BlogMapper;
import com.sdut.blog.mapper.UserMapper;
import com.sdut.blog.pojo.dto.Admin;
import com.sdut.blog.pojo.dto.Blog;
import com.sdut.blog.pojo.dto.User;
import com.sdut.blog.service.AdminService;
import com.sdut.blog.service.BlogService;
import com.sdut.blog.service.CommentService;
import com.sdut.blog.utils.JasyptEncryptorUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MyTest {
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private CommentService commentService;

    @Test
    public void selectByEmail() {
        String email = "lzk@qq.com";
        Admin admin = adminMapper.selectAdminByEmail(email);
        System.out.println(admin);
    }

    @Test
    public void selectUserByEmail() {
        String email = "1678019300@qq.com";
        User user = userMapper.selectUserByEmail(email);
        System.out.println(user);
    }

    @Test
    public void selectBlog() {
        ArrayList<Blog> blogs = blogMapper.getMyBlogsByCondition(2, "二手闲置", "test");
        for(Blog b : blogs) {
            System.out.println(b);
        }
    }

    @Test
    public void selectUser() {
        ArrayList<User> users = userMapper.findUser("test");
        for(User u : users) {
            System.out.println(u);
        }
    }

    @Test
    public void updateBlog() {
        Blog blog = blogMapper.selectBlogById(3);
        blog.setTitle("刚改的");
        blogMapper.updateBlog(blog);
    }

    @Test
    public void encode() {
        System.out.println(JasyptEncryptorUtils.encode("liu123"));
        System.out.println(JasyptEncryptorUtils.decode("uKk4MCDtTit77wuX/Mu4Aw=="));
    }

    @Test
    public void comment() {
        //System.out.println(commentService.addComment(2, 2, "用于回复测试"));
        System.out.println(commentService.addSubComment(2,1,1, 2,"lzk to gp"));
    }

    @Test
    public void getDetail() {
        System.out.println(blogMapper.getBlogById(2));
    }
}
