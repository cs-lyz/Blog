package com.cs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
@Data
@TableName("tb_blog_comment")
public class BlogComment {
    @TableId(type = IdType.AUTO)
    private Long commentId;

    private Long blogId;

    private String commentator;

    private String email;

    private String websiteUrl;

    private String commentBody;

    private LocalDateTime commentCreateTime;

    private String commentatorIp;

    private String replyBody;

    private LocalDateTime replyCreateTime;

    private Byte commentStatus;

    private Byte isDeleted;

}