package com.cs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cs.entity.BlogTagRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlogTagRelationMapper extends BaseMapper<BlogTagRelation> {
    void insertBatch(@Param("blogId") Long blogId, @Param("allTag") List<Integer> allTag);
}
