package com.cs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
@Data
@TableName("tb_blog_tag")
public class BlogTag {
    @TableId(type = IdType.AUTO)
    private Integer tagId;

    private String tagName;

    private Byte isDeleted;

    private LocalDateTime createTime;

}