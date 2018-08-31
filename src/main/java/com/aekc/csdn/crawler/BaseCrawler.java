package com.aekc.csdn.crawler;

import com.aekc.csdn.pojo.Blog;
import com.aekc.csdn.pojo.SolrBlog;
import com.aekc.csdn.pojo.TbBlog;
import com.aekc.csdn.service.BlogService;
import com.aekc.csdn.service.HttpService;
import com.aekc.csdn.service.SolrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Component
public abstract class BaseCrawler implements Crawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseCrawler.class);

    @Autowired
    private BlogService blogService;

    @Autowired
    private HttpService httpService;

    @Autowired
    private SolrService solrService;

    @Override
    public void run() {start();}

    public String doGet(String url) throws IOException, URISyntaxException {
        return httpService.doGet(url);
    }

    public String doGet(String url, String encode) throws IOException, URISyntaxException {
        return httpService.doGet(url, encode);
    }

    /**
     * 开始抓取数据
     */
    public void start() {
        Integer totalPage = getTotalPage();
        //分页抓取
        for(int i = 1; i <= totalPage; i++) {
            LOGGER.info("当前第{}页，总共{}页", i, totalPage);
            Blog blog = doStart(String.valueOf(i));
            if(blog == null) {
                LOGGER.info("抓取到0条数据");
                continue;
            }
            LOGGER.info("抓取到{}条数据", blog.getSolrBlogList().size());
            //保存到数据库
            saveDataToDB(blog.getTbBlogList());
            for(int j = 0; j < blog.getTbBlogList().size(); j++) {
                int id = blog.getTbBlogList().get(j).getId();
                blog.getSolrBlogList().get(j).setId(id);
            }
            //保存到索引库
            saveDataToSolr(blog.getSolrBlogList());
        }
    }

    /**
     * 抓取获取到商品集合
     *
     * @param page
     * @return
     */
    private Blog doStart(String page) {
        String url = getPageUrl(page, "Java", "blog");
        LOGGER.info(" URL is " + url);
        String html = null;
        try {
            html = httpService.doGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(html == null) {
            return null;
        }
        return doParser(html);
    }

    private void saveDataToDB(List<TbBlog> tbBlogList) {
        blogService.addBlogList(tbBlogList);
    }

    private void saveDataToSolr(List<SolrBlog> solrBlogList) {
        solrService.importAllBlogSolr(solrBlogList);
    }

    /**
     * 解析html，生成Blog对象
     *
     * @param html 网页html内容
     * @return Blog合集
     */
    protected abstract Blog doParser(String html);

    /**
     * 根据页数得到url
     *
     * @param page 页码
     * @param query 查询内容
     * @param type 查询类别
     * @return 页面的url
     */
    protected abstract String getPageUrl(String page, String query, String type);

    /**
     * 获取总页数
     *
     * @return 总页数
     */
    protected abstract Integer getTotalPage();

}
