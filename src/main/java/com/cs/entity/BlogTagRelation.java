package com.cs.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class BlogTagRelation {
    private Long relationId;

    private Long blogId;

    private Integer tagId;

    private LocalDateTime createTime;

}