package com.sdut.blog.pojo.vo;

import com.sdut.blog.pojo.dto.SubComment;
import lombok.Data;

/**
 * @author 24699
 */
@Data
public class SubCommentVo {
    private SubComment subComment;
    private String sendName;
    private String sendUrl;
    private String receiveName;
    private String receiveUrl;
}
