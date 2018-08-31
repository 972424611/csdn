package com.aekc.csdn.service;

import com.aekc.csdn.pojo.TbBlog;

import java.util.List;

public interface BlogService {

    void addBlogList(List<TbBlog> tbBlogList);

    TbBlog getBlogById(Integer id);
}
