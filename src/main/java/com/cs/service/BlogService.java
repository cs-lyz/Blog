package com.cs.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs.dto.HttpStatusEnum;
import com.cs.dto.Result;
import com.cs.entity.Blog;
import com.cs.entity.BlogCategory;
import com.cs.entity.BlogTag;
import com.cs.entity.BlogTagRelation;
import com.cs.mapper.*;
import com.cs.tool.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class BlogService extends ServiceImpl<BlogMapper, Blog> {

    @Autowired
    private BlogTagRelationMapper blogTagRelationMapper;
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private BlogCategoryMapper blogCategoryMapper;
    @Autowired
    private BlogTagMapper blogTagMapper;
    @Autowired
    private BlogCommentMapper blogCommentMapper;

    @Transactional   //@Transactional 默认只在遇到 RuntimeException 或 Error 时回滚
    public Result<String> saveBlog(Blog blog) {
        //插入博客
        //1.先看要归到哪一类
        //注意，先查后改了
        BlogCategory blogCategory = blogCategoryMapper.selectById(blog.getBlogCategoryId());
        if (blogCategory == null) {  //没有这一类，归到默认里面
            blog.setBlogCategoryId(0);
            blog.setBlogCategoryName("默认分类");  //默认 有默认分类
            blogCategory = blogCategoryMapper.selectById(0);//只有没有默认分类的时候才为空
            if (blogCategory == null) {
                throw new BusinessException("博客分类不存在，ID：" + blog.getBlogCategoryId());
            }
        }else{
            //有分类  新增加了一条这类的博客 这个查出来的blogCategory的rank也要加一
            blog.setBlogCategoryName(blogCategory.getCategoryName());
        }

        LambdaUpdateWrapper<BlogCategory>  updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(BlogCategory::getCategoryId,blog.getBlogCategoryId())
                .set(BlogCategory::getCategoryRank,blogCategory.getCategoryRank()+1);
        if(blogCategoryMapper.update(updateWrapper)==0){
            throw new BusinessException("更新失败");
        }

        blog.setCreateTime(LocalDateTime.now());
        blog.setUpdateTime(LocalDateTime.now());



        //OK 还有一个tag,跟tag relation
        //它会默认处理空格并过滤空值
        //排序
        String[] tag_raw = StringUtils.split(blog.getBlogTags(), ',');
        Set<String> set = new HashSet<>(Arrays.asList(tag_raw));
        String[] tag = set.toArray(new String[0]);

        List<Integer> allTag = new ArrayList<>();   //一会儿要留着更新relation
        List<BlogTag> notHasTag = new ArrayList<>();


        if (tag.length > 6) {
            throw new BusinessException("标签数量不能超过6个");
        }
        blog.setBlogTags(String.join(",", tag));//添加去重加去空格后的tag

        try {
            blogMapper.insert(blog);
        } catch (Exception e) {
            throw new BusinessException("插入失败");
        }
        for(String tagName:tag){
            LambdaQueryWrapper<BlogTag> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BlogTag::getTagName,tagName);
            BlogTag blogTag_d= blogTagMapper.selectOne(queryWrapper);
            if(blogTag_d==null){//如果没有这个标签，就要插入了
                BlogTag temp = new BlogTag();
                temp.setTagName(tagName);
                temp.setCreateTime(LocalDateTime.now());
                notHasTag.add(temp);
            }else {
                allTag.add(blogTag_d.getTagId());
            }
        }
//        MyBatis-Plus 的 insert 方法执行后，如果数据库的主键是自增列（如 MySQL 的 AUTO_INCREMENT），
//        框架会利用 MyBatis 的 useGeneratedKeys 机制，在插入成功后自动将生成的主键值设置到实体对象的对应字段上。
        for(BlogTag blogTag:notHasTag){
            if(blogTagMapper.insert(blogTag)==0){
                throw new BusinessException("插入标签失败");
            }
            //现在blogTag里面已经有tag_id了
            allTag.add(blogTag.getTagId());
        }
        //插入完标签了以后，该更新relation了
        try {
            if (!allTag.isEmpty()) {
                blogTagRelationMapper.insertBatch(blog.getBlogId(), allTag);
            }
        } catch (Exception e) {
            throw new BusinessException("标签关联插入失败", e);
        }
        return Result.success("保存成功",null);
    }

    @Transactional
    public Result<String> update(Blog blog) {
        //先看分类改了没有
        //1. 先判断博客存不存在
        Blog blog_r= blogMapper.selectById(blog.getBlogId());
        if (blog_r == null) {
            return Result.fail(HttpStatusEnum.PARAM_ERROR.getCode(), "博客不存在");
        }
        //原来博客里面的CategoryId
        BlogCategory blogCategory_old = blogCategoryMapper.selectById(blog_r.getBlogCategoryId());
        LambdaUpdateWrapper<BlogCategory>  updateWrapper;
        //查看新传进来的Id存不存在，然后更新rank
        if(!blog.getBlogCategoryId().equals(blog_r.getBlogCategoryId())){
            //传进来分类和之前不一样  更新分类表
            BlogCategory blogCategory_r = blogCategoryMapper.selectById(blog.getBlogCategoryId());
            BlogCategory blogCategory = null;   //
            //新传进来的已有分类
            if (blogCategory_r == null) {
                blog.setBlogCategoryId(0);
                blog.setBlogCategoryName("默认分类");  //默认 有默认分类
                blogCategory = blogCategoryMapper.selectById(0);//只有没有默认分类的时候才为空
                if (blogCategory == null) {
                    throw new BusinessException("博客分类不存在，ID：" + blog.getBlogCategoryId());
                }
                //然后默认分类的rank+1
                updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(BlogCategory::getCategoryId,0)
                        .set(BlogCategory::getCategoryRank,blogCategory.getCategoryRank()+1);
            }else{
                blogCategory=blogCategory_r;
                //有分类  新增加了一条这类的博客 这个查出来的blogCategory的rank也要加一
                blog.setBlogCategoryName(blogCategory_r.getCategoryName());
                //新分类的rank+1
                updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(BlogCategory::getCategoryId,blog.getBlogCategoryId())
                        .set(BlogCategory::getCategoryRank,blogCategory.getCategoryRank()+1);
            }
            //然后新分类+1的同时，老分类要-1
            //然后之前那个分类要减1
            LambdaUpdateWrapper<BlogCategory>  updateWrapper1 = new LambdaUpdateWrapper<>();
            updateWrapper1.eq(BlogCategory::getCategoryId,blog_r.getBlogCategoryId())
                    .set(BlogCategory::getCategoryRank,blogCategory_old.getCategoryRank()-1);

            try {
                blogCategoryMapper.update(updateWrapper);
                blogCategoryMapper.update(updateWrapper1);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


        //然后看标签一样不一样
        String[] tag_new_raw = StringUtils.split(blog.getBlogTags(), ',');
        Set<String> tag_set_new = new HashSet<>(Arrays.asList(tag_new_raw));
        String[] tag_new = tag_set_new.toArray(new String[0]);//去重后的tag

        if (tag_new.length > 6) {
            throw new BusinessException("标签数量不能超过6个");
        }

        //okok,可以把blog更新了
        try {
            blogMapper.updateById(blog);
        } catch (Exception e) {
            throw new BusinessException("更新博客失败", e);
        }

        //查出来默认是去重过的
        String[] tag_old = StringUtils.split(blog_r.getBlogTags(), ',');
        Set<String> tag_set_old = new HashSet<>(Arrays.asList(tag_old));

        List<Integer> allTag = new ArrayList<>();   //一会儿要留着更新relation
        List<BlogTag> notHasTag = new ArrayList<>();

        //tag也有更新
        if(!tag_set_new.equals(tag_set_old)){
            //okok 还是不等于  要更新什么？感觉更新一个关系表就行了
            //看什么，如果有新的tag要插入，不删除了吧
            //先看有没有新tag
            for(String tagName:tag_new){
                LambdaQueryWrapper<BlogTag> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(BlogTag::getTagName,tagName);
                BlogTag blogTag_d= blogTagMapper.selectOne(queryWrapper);
                if(blogTag_d==null){//如果没有这个标签，就要插入了
                    BlogTag temp = new BlogTag();
                    temp.setTagName(tagName);
                    temp.setCreateTime(LocalDateTime.now());
                    notHasTag.add(temp);
                }else {
                    allTag.add(blogTag_d.getTagId());
                }
            }
        }

//        MyBatis-Plus 的 insert 方法执行后，如果数据库的主键是自增列（如 MySQL 的 AUTO_INCREMENT），
//        框架会利用 MyBatis 的 useGeneratedKeys 机制，在插入成功后自动将生成的主键值设置到实体对象的对应字段上。
        for(BlogTag blogTag:notHasTag){
            try {
                blogTagMapper.insert(blogTag);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            //现在blogTag里面已经有tag_id了
            allTag.add(blogTag.getTagId());
        }
//        啊啊啊啊，终于最后一步了好吧，更新关系表
        //关系表的话，全删除，然后再重新插入
        LambdaQueryWrapper<BlogTagRelation> wrapper= new LambdaQueryWrapper<>();
        wrapper.eq(BlogTagRelation::getBlogId,blog.getBlogId());
        blogTagRelationMapper.delete(wrapper);
        //ok 再插入
        try {
            if (!allTag.isEmpty()) {
                blogTagRelationMapper.insertBatch(blog.getBlogId(), allTag);
            }
        } catch (Exception e) {
            throw new BusinessException("标签关联插入失败", e);
        }
        return Result.success("保存成功",null);

    }
}
