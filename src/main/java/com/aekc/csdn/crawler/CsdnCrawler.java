package com.aekc.csdn.crawler;

import com.aekc.csdn.pojo.Blog;
import com.aekc.csdn.pojo.SolrBlog;
import com.aekc.csdn.pojo.TbBlog;
import com.aekc.csdn.util.DealHtmlString;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class CsdnCrawler extends BaseCrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsdnCrawler.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String baseUrl;

    public CsdnCrawler() {}

    public CsdnCrawler(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    private String getUrl(String page, String query, String type) {
        String[] searchList = new String[] {"{page}", "{query}", "{type}"};
        String[] replacementList = new String[] {page, query, type};
        return StringUtils.replaceEach(baseUrl, searchList, replacementList);
    }

    private TbBlog getTbBlog(String url) {
        System.out.println(url);
        TbBlog tbBlog = new TbBlog();
        String html = null;
        try {
            html = super.doGet(url);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        Document document = Jsoup.parse(html);
        String title = document.select(".title-article").html();
        if(StringUtils.isBlank(title)) {
            return null;
        }
        String content = document.select("article").html();
        content = DealHtmlString.removeHtmlLabel(content);
        content = DealHtmlString.replaceCharEntity(content);
        tbBlog.setContent(content);
        tbBlog.setAuthor(document.select("#uid").html());
        tbBlog.setTitle(document.select(".title-article").html());
        tbBlog.setTime(document.select(".time").html());
        tbBlog.setCreateTime(new Date());
        System.out.println(tbBlog.toString());
        return tbBlog;
    }

    private SolrBlog getLuBlog(Element element) {
        SolrBlog solrBlog = new SolrBlog();
        String title = element.select("dt").select("a").html();
        if(StringUtils.isBlank(title)) {
            return null;
        }
        title = DealHtmlString.getBeautifyHtml(title);
        String describe = element.select("dd").select(".search-detail").html();
        describe = DealHtmlString.getBeautifyHtml(describe);
        solrBlog.setTitle(title);
        solrBlog.setDescribe(describe);
//        solrBlog.setCreateTime(new Date());
//        solrBlog.setUpdateTime(new Date());
        return solrBlog;
    }

    @Override
    protected Blog doParser(String html) {
        Document document = Jsoup.parse(html);
        //获取商品的列表
        Elements elements = document.select(".search-list");
        Blog blog = new Blog();
        List<SolrBlog> solrBlogList = new ArrayList<>();
        List<TbBlog> tbBlogList = new ArrayList<>();
        for(Element element : elements) {
            String url = element.select("dt").select("a").attr("href");
            SolrBlog solrBlog = getLuBlog(element);
            if(solrBlog != null) {
                solrBlogList.add(solrBlog);
            }
            TbBlog tbBlog = getTbBlog(url);
            if(tbBlog != null) {
                tbBlogList.add(tbBlog);
            }
        }
        blog.setSolrBlogList(solrBlogList);
        blog.setTbBlogList(tbBlogList);
        return blog;
    }

    @Override
    protected String getPageUrl(String page, String query, String type) {
        return getUrl(page, query, type);
    }

    @Override
    protected Integer getTotalPage() {
        String html;
        try {
            html = doGet(getUrl("1", "Java", "blog"));
        } catch (Exception e) {
            LOGGER.error("getTotalPage error!", e);
            return 0;
        }
        Document document = Jsoup.parse(html);
        String pageHtml = document.select(".page-nav .text").html();
        String[] total = pageHtml.split("\\D+");
        Integer blogTotal = Integer.parseInt(total[1]);
        if(blogTotal > 400) {
            return 20;
        }
        return blogTotal;
    }

}
