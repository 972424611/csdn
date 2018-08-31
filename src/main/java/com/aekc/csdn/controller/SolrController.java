package com.aekc.csdn.controller;

import com.aekc.csdn.common.PageQuery;
import com.aekc.csdn.common.ResultJson;
import com.aekc.csdn.pojo.SolrBlog;
import com.aekc.csdn.pojo.TbBlog;
import com.aekc.csdn.service.BlogService;
import com.aekc.csdn.service.SolrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SolrController {

    @Autowired
    private SolrService solrService;

    @Autowired
    private BlogService blogService;

    @ResponseBody
    @RequestMapping(value = "/querySolr")
    public ResultJson searchBySolr(PageQuery pageQuery) {
        List<SolrBlog> solrBlogList = solrService.searchBySolr(pageQuery);
        return ResultJson.success(solrBlogList);
    }

    @ResponseBody
    @RequestMapping(value = "/getBlog/{id}")
    public ResultJson getBlog(@PathVariable Integer id) {
        TbBlog tbBlog = blogService.getBlogById(id);
        return ResultJson.success(tbBlog);
    }
}
