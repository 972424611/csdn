package com.aekc.csdn.pojo;

import java.util.List;

public class Blog {

    private List<SolrBlog> solrBlogList;

    private List<TbBlog> tbBlogList;

    public List<SolrBlog> getSolrBlogList() {
        return solrBlogList;
    }

    public void setSolrBlogList(List<SolrBlog> solrBlogList) {
        this.solrBlogList = solrBlogList;
    }

    public List<TbBlog> getTbBlogList() {
        return tbBlogList;
    }

    public void setTbBlogList(List<TbBlog> tbBlogList) {
        this.tbBlogList = tbBlogList;
    }
}
