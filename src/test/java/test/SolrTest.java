package test;

import com.aekc.csdn.common.PageQuery;
import com.aekc.csdn.pojo.SolrBlog;
import com.aekc.csdn.service.SolrService;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/*.xml"})
public class SolrTest {

    @Autowired
    private SolrService solrService;

    @Autowired
    private SolrServer solrServer;

    @Test
    public void testImport() throws IOException, SolrServerException {
        SolrBlog solrBlog = new SolrBlog();
        solrBlog.setId(1);
        solrBlog.setTitle("solr5的运用");
        solrBlog.setDescribe("solr5有两种运行模式，独立模式和云模式，独立模式是以core来管理，云模式是以collection来管理。");
        solrService.addBlogSolr(solrBlog);
    }

    @Test
    public void testSearch() {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNo(1);
        pageQuery.setPageSize(10);
        pageQuery.setQueryString("Java");
        List<SolrBlog> solrBlogList = solrService.searchBySolr(pageQuery);
        solrBlogList.forEach(System.out::println);
    }

    @Test
    public void testDelete() {
        solrService.deleteAllBlogSolr();
    }
}
