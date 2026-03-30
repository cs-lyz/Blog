package com.cs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
@Data
@TableName("tb_config")
public class BlogConfig {
    @TableId(type = IdType.AUTO)
    private String configName;

    private String configValue;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}