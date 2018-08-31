package com.aekc.csdn.service.impl;

import com.aekc.csdn.common.BeanValidator;
import com.aekc.csdn.common.PageQuery;
import com.aekc.csdn.dao.SolrDao;
import com.aekc.csdn.pojo.SolrBlog;
import com.aekc.csdn.service.SolrService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolrServiceImpl implements SolrService {

    private final SolrDao solrDao;

    @Autowired
    public SolrServiceImpl(SolrDao solrDao) {
        this.solrDao = solrDao;
    }

    @Override
    public void importAllBlogSolr(List<SolrBlog> solrBlogList) {
        try {
            for(SolrBlog solrBlog : solrBlogList) {
                SolrInputDocument document = new SolrInputDocument();
                document.addField("id", solrBlog.getId());
                document.addField("title", solrBlog.getTitle());
                document.addField("describe", solrBlog.getDescribe());
                solrDao.addAllBlogSolr(document);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<SolrBlog> searchBySolr(PageQuery pageQuery) {
        BeanValidator.check(pageQuery);
        SolrQuery solrQuery = new SolrQuery();
        //solrQuery.setQuery("*" + pageQuery.getQueryString() + "*");
        solrQuery.setQuery("searchText:" + pageQuery.getQueryString());
        solrQuery.setStart(pageQuery.getPageNo() - 1);
        solrQuery.setRows(pageQuery.getPageSize());
        //设置默认搜索域
        //solrQuery.set("df", "searchText");
        //设置高亮显示
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("title");
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");
        //执行查询
        List<SolrBlog> solrBlogList = null;
        try {
            solrBlogList = solrDao.searchBySolr(solrQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return solrBlogList;
    }

    @Override
    public void deleteSolrByBlogId(long id) {
        try {
            solrDao.deleteBlogSolrById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addBlogSolr(SolrBlog solrBlog) {
        try {
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", solrBlog.getId());
            document.addField("title", solrBlog.getTitle());
            document.addField("describe", solrBlog.getDescribe());
            solrDao.addBlogSolr(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateBlogSolr(SolrBlog solrBlog) {
        deleteSolrByBlogId(solrBlog.getId());
        addBlogSolr(solrBlog);
    }

    @Override
    public void deleteAllBlogSolr() {
        try {
            solrDao.deleteAllBlogSolr();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
