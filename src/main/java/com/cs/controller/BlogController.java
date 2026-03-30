package com.cs.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cs.dto.HttpStatusEnum;
import com.cs.dto.Result;
import com.cs.entity.Blog;
import com.cs.mapper.BlogMapper;
import com.cs.service.BlogService;
import com.cs.tool.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private BlogService blogService;

    //添加博客
    @RequestMapping("/save")
    public Result<String> addBlog(@RequestParam("blogTitle") String blogTitle,
                                  @RequestParam(name = "blogSubUrl", required = false) String blogSubUrl,
                                  @RequestParam("blogCategoryId") Integer blogCategoryId,
                                  @RequestParam("blogTags") String blogTags,
                                  @RequestParam("blogContent") String blogContent,
                                  @RequestParam("blogCoverImage") String blogCoverImage,
                                  @RequestParam("blogStatus") Byte blogStatus,
                                  @RequestParam("enableComment") Byte enableComment){
        if (!org.springframework.util.StringUtils.hasText(blogTitle)) {
            return Result.fail(HttpStatusEnum.PARAM_ERROR.getCode(), "请输入文章标题");
        }
        if (blogTitle.trim().length() > 150) {
            return Result.fail(HttpStatusEnum.PARAM_ERROR.getCode(),"标题过长");
        }
        if (!org.springframework.util.StringUtils.hasText(blogTags)) {
            return Result.fail(HttpStatusEnum.PARAM_ERROR.getCode(),"请输入文章标签");
        }
        if (blogTags.trim().length() > 150) {
            return Result.fail(HttpStatusEnum.PARAM_ERROR.getCode(),"标签过长");
        }
        if (blogSubUrl.trim().length() > 150) {
            return Result.fail(HttpStatusEnum.PARAM_ERROR.getCode(),"路径过长");
        }
        if (!org.springframework.util.StringUtils.hasText(blogContent)) {
            return Result.fail(HttpStatusEnum.PARAM_ERROR.getCode(),"请输入文章内容");
        }
        if (blogContent.trim().length() > 100000) {
            return Result.fail(HttpStatusEnum.PARAM_ERROR.getCode(),"文章内容过长");
        }
        if (!StringUtils.hasText(blogCoverImage)) {
            return Result.fail(HttpStatusEnum.PARAM_ERROR.getCode(),"封面图不能为空");
        }
        Blog blog=Blog.builder().blogTitle(blogTitle).blogSubUrl(blogSubUrl)
                .blogCategoryId(blogCategoryId).blogTags(blogTags).blogContent(blogContent)
                .blogCoverImage(blogCoverImage).blogStatus(blogStatus).enableComment(enableComment).build();
        return  blogService.saveBlog(blog);
    }


    //修改博客  全传过来，全段更新
    @RequestMapping("update")
    public Result<String> updateBlog(@RequestParam("blogTitle") String blogTitle,
                                     @RequestParam(name = "blogSubUrl", required = false) String blogSubUrl,
                                     @RequestParam("blogCategoryId") Integer blogCategoryId,
                                     @RequestParam("blogTags") String blogTags,
                                     @RequestParam("blogContent") String blogContent,
                                     @RequestParam("blogCoverImage") String blogCoverImage,
                                     @RequestParam("blogStatus") Byte blogStatus,
                                     @RequestParam("enableComment") Byte enableComment){
        if (!org.springframework.util.StringUtils.hasText(blogTitle)) {
            return Result.fail(HttpStatusEnum.PARAM_ERROR.getCode(), "请输入文章标题");
        }
        if (blogTitle.trim().length() > 150) {
            return Result.fail(HttpStatusEnum.PARAM_ERROR.getCode(),"标题过长");
        }
        if (!org.springframework.util.StringUtils.hasText(blogTags)) {
            return Result.fail(HttpStatusEnum.PARAM_ERROR.getCode(),"请输入文章标签");
        }
        if (blogTags.trim().length() > 150) {
            return Result.fail(HttpStatusEnum.PARAM_ERROR.getCode(),"标签过长");
        }
        if (blogSubUrl.trim().length() > 150) {
            return Result.fail(HttpStatusEnum.PARAM_ERROR.getCode(),"路径过长");
        }
        if (!org.springframework.util.StringUtils.hasText(blogContent)) {
            return Result.fail(HttpStatusEnum.PARAM_ERROR.getCode(),"请输入文章内容");
        }
        if (blogContent.trim().length() > 100000) {
            return Result.fail(HttpStatusEnum.PARAM_ERROR.getCode(),"文章内容过长");
        }
        if (!StringUtils.hasText(blogCoverImage)) {
            return Result.fail(HttpStatusEnum.PARAM_ERROR.getCode(),"封面图不能为空");
        }
        Blog blog=Blog.builder().blogTitle(blogTitle).blogSubUrl(blogSubUrl)
                .blogCategoryId(blogCategoryId).blogTags(blogTags).blogContent(blogContent)
                .blogCoverImage(blogCoverImage).blogStatus(blogStatus).enableComment(enableComment).build();
        return  blogService.update(blog);
    }


    //分页查询博客
    @RequestMapping("/query")
    public Result<Page<Blog>> queryBlog(@RequestParam Map<String,Object> param){
        //传入什么东西要
//        limit,page,还有什么  关键词吧应该是
        // 从 map 中提取 page 和 limit，categoryId 默认值可以根据需要设置
        long current = param.containsKey("page") ? Long.parseLong(param.get("page").toString()) : 1;
        long size = param.containsKey("limit") ? Long.parseLong(param.get("limit").toString()) : 10;
        Page<Blog> page = new Page<>(current, size);
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        String keyword = (String)param.get("keyword");
        if(StringUtils.hasText(keyword)){
            queryWrapper.apply("MATCH(blog_title, blog_content) AGAINST({0} IN BOOLEAN MODE)", keyword);
        }
        if (param.containsKey("categoryId")) {
            queryWrapper.eq(Blog::getBlogCategoryId, param.get("categoryId"));
        }

        Page<Blog> blogPage = blogService.page(page, queryWrapper);
        return Result.success("查询成功",blogPage);
    }
    //删除博客
    @RequestMapping("/delete")
    public Result<String> deleteBlog(@RequestParam("blogId")  Long blogId){
        if(blogId==null){
            return Result.fail(HttpStatusEnum.PARAM_ERROR.getCode(),"未传入博客Id");
        }
        LambdaUpdateWrapper<Blog> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Blog::getBlogId, blogId).set(Blog::getIsDeleted,1);
        try {
            blogMapper.update(updateWrapper);
        } catch (Exception e) {
            throw new BusinessException("删除失败");
        }
        return Result.success("删除成功",null);
    }


}
