package com.cs.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
@Data
public class BlogConfig {
    private String configName;

    private String configValue;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}