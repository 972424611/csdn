package com.aekc.csdn.mapper;

import com.aekc.csdn.pojo.TbBlog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlogMapper {

    /**
     * 批量新增博客
     */
    void insertBlogList(List<TbBlog> tbBlogList);

    void insertBlog(TbBlog tbBlog);

    TbBlog selectBlogById(Integer id);
}
