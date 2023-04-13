package com.sdut.blog.controller;

import com.sdut.blog.annation.PassToken;
import com.sdut.blog.annation.UserLoginToken;
import com.sdut.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 24699
 */
@CrossOrigin
@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;

    @UserLoginToken
    @PostMapping("/reportComment")
    public Object report(@RequestParam(value = "commentId", required = false) Integer commentId,
                         @RequestParam(value = "subCommentId",required = false) Integer subCommentId) {
        if (commentId != null) {
            return commentService.reportComment(commentId);
        } else {
            return commentService.reportSub(subCommentId);
        }
    }

    @PassToken
    @GetMapping("/getComment")
    public Object getComment(@RequestParam("blogId") Integer blogId) {
        return commentService.getComment(blogId);
    }

    @PassToken
    @GetMapping("/getSubComment")
    public Object getSubComment(@RequestParam("commentId") Integer commentId) {
        return commentService.getSubComment(commentId);
    }

    @UserLoginToken
    @PostMapping("/addComment")
    public Object addComment(@RequestParam(value = "blogId",required = false) Integer blogId,
                             @RequestParam(value = "commentId",required = false) Integer commentId,
                             @RequestParam(value = "sendId", required = false) Integer sendId,
                             @RequestParam(value = "receivedId", required = false) Integer receivedId,
                             @RequestParam("content") String content) {
        if (commentId == null) {
            return commentService.addComment(blogId, sendId, content);
        } else {
            return commentService.addSubComment(blogId,commentId, sendId, receivedId, content);
        }
    }
}
