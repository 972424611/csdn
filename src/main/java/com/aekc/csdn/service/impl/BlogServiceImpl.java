package com.aekc.csdn.service.impl;

import com.aekc.csdn.mapper.BlogMapper;
import com.aekc.csdn.pojo.TbBlog;
import com.aekc.csdn.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Override
    public void addBlogList(List<TbBlog> tbBlogList) {
        blogMapper.insertBlogList(tbBlogList);
    }

    @Override
    public TbBlog getBlogById(Integer id) {
        if(id != null) {
            return blogMapper.selectBlogById(id);
        }
        return null;
    }
}
