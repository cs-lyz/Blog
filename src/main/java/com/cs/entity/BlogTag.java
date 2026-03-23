package com.cs.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
@Data
public class BlogTag {
    private Integer tagId;

    private String tagName;

    private Byte isDeleted;

    private LocalDateTime createTime;

}