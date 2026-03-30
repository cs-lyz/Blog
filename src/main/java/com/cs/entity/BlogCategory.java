package com.cs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
@Data
@TableName("tb_blog_category")
public class BlogCategory {
    @TableId(type = IdType.AUTO)
    private Integer categoryId;

    private String categoryName;

    private String categoryIcon;

    private Integer categoryRank;

    private Byte isDeleted;

    private LocalDateTime createTime;

}