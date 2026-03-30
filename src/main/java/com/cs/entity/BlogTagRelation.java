package com.cs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("tb_blog_tag_relation")
public class BlogTagRelation {
    @TableId(type = IdType.AUTO)
    private Long relationId;

    private Long blogId;

    private Integer tagId;

    private LocalDateTime createTime;

}