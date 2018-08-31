package com.aekc.csdn.dao.impl;

import com.aekc.csdn.dao.SolrDao;
import com.aekc.csdn.pojo.SolrBlog;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SolrDaoImpl implements SolrDao {

    @Autowired
    private SolrServer solrServer;

    @Override
    public void addAllBlogSolr(SolrInputDocument document) throws IOException, SolrServerException {
        solrServer.add(document);
        solrServer.commit();
    }

    @Override
    public List<SolrBlog> searchBySolr(SolrQuery solrQuery) throws SolrServerException {
        //根据条件查询索引库
        QueryResponse queryResponse = solrServer.query(solrQuery);
        SolrDocumentList solrDocumentList = queryResponse.getResults();
        List<SolrBlog> solrBlogList = new ArrayList<>();
        //取高亮显示
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        //取博客列表
        for(SolrDocument solrDocument : solrDocumentList) {
            SolrBlog solrBlog = new SolrBlog();
            //取高亮显示的结果
            List<String> list = highlighting.get(solrDocument.get("id")).get("title");
            String title;
            if(list != null && list.size() > 0) {
                title = list.get(0);
            } else {
                title = (String) solrDocument.get("title");
            }
            solrBlog.setId(Integer.valueOf((String) solrDocument.get("id")));
            solrBlog.setTitle(title);
            solrBlog.setDescribe((String) solrDocument.get("describe"));
            solrBlogList.add(solrBlog);
        }
        return solrBlogList;
    }

    @Override
    public void deleteBlogSolrById(long id) throws IOException, SolrServerException {
        solrServer.deleteByQuery("id:" + String.valueOf(id));
        solrServer.commit();
    }

    @Override
    public void addBlogSolr(SolrInputDocument document) throws IOException, SolrServerException {
        solrServer.add(document);
        solrServer.commit();
    }

    @Override
    public void deleteAllBlogSolr() throws IOException, SolrServerException {
        solrServer.deleteByQuery("*:*");
        solrServer.commit();
    }
}
