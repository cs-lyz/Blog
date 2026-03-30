package com.cs.controller;

import com.cs.dto.HttpStatusEnum;
import com.cs.dto.Result;
import com.cs.entity.Blog;
import com.cs.mapper.BlogMapper;
import com.cs.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private BlogService blogService;

    //添加博客
    @RequestMapping("/save")
    public Result<String> addBlog(@RequestBody @RequestParam("blogTitle") String blogTitle,
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
        return  blogService.save(blog);
    }


    //修改博客  全传过来，全段更新
    @RequestMapping("update")
    public Result<String> updateBlog(@RequestBody @RequestParam("blogTitle") String blogTitle,
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
    public  queryBlog(@RequestBody ){
        
    }
    //删除博客




}
