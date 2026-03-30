package com.cs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data

public class BlogTagCount {
    @TableId(type = IdType.AUTO)
    private Integer tagId;

    private String tagName;

    private Integer tagCount;

}
