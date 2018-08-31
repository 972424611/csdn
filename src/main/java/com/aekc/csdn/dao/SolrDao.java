package com.aekc.csdn.dao;

import com.aekc.csdn.pojo.SolrBlog;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.List;

public interface SolrDao {

    void addAllBlogSolr(SolrInputDocument document) throws IOException, SolrServerException;

    List<SolrBlog> searchBySolr(SolrQuery solrQuery) throws SolrServerException;

    void deleteBlogSolrById(long id) throws IOException, SolrServerException;

    void addBlogSolr(SolrInputDocument document) throws IOException, SolrServerException;

    void deleteAllBlogSolr() throws IOException, SolrServerException;
}
