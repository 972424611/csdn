package com.aekc.csdn.service;

import com.aekc.csdn.common.PageQuery;
import com.aekc.csdn.pojo.SolrBlog;
import com.aekc.csdn.pojo.TbBlog;

import java.util.List;

public interface SolrService {

    void importAllBlogSolr(List<SolrBlog> solrBlogList);

    List<SolrBlog> searchBySolr(PageQuery pageQuery);

    void deleteSolrByBlogId(long id);

    void addBlogSolr(SolrBlog solrBlog);

    void updateBlogSolr(SolrBlog solrBlog);

    void deleteAllBlogSolr();
}
