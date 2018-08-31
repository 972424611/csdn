package test.crawler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import test.crawler.pojo.Item;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdCrawler {

    private static final String BASE_URL = "https://list.jd.com/list.html?cat=670,671,672&page={page}";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public void start() {

        // 入口页面地址
        String  startUrl = StringUtils.replace(BASE_URL, "{page}", "1");
        // 获取到总页数
        String html = doGet(startUrl);
        // 解析html
        Document document = Jsoup.parse(html);
        String pageText = document.select("#J_topPage").text();
        String[] strs = pageText.split("\\D+");
        Integer totalPage = Integer.parseInt(strs[1]);

        StringBuilder stringBuilder = new StringBuilder();
        // 分页查询数据
        for(int i = 0; i < totalPage; i++) {
            String url = BASE_URL.replace("{page}", String.valueOf(i));
            System.out.println(url);
            String content = doGet(url);
            Document root = Jsoup.parse(content);
            Elements elements = root.select("#plist .gl-item");
            Map<Long, Item> itemMap = new HashMap<>();
            for(Element li : elements) {
                Item item = new Item();
                Element div = li.child(0);
                Long id = Long.valueOf(div.attr("data-sku"));
                String image = li.select(".p-img img").attr("src");
                if(StringUtils.isBlank(image)) {
                    image = li.select(".p-img img").attr("data-lazy-img");
                }
                image = "http:" + image;
                String title = li.select(".p-name").text();
                item.setId(id);
                item.setImage(image);
                item.setTitle(title);
                itemMap.put(id, item);
            }

            // 获取商品价格
            List<String> idList = new ArrayList<>();
            for(Long id : itemMap.keySet()) {
                idList.add("J_" + id);
            }
            String priceUrl = "http://p.3.cn/prices/mgets?skuIds=" + StringUtils.join(idList, ',');
            String jsonData = doGet(priceUrl);
            try {
                ArrayNode arrayNode = (ArrayNode) MAPPER.readTree(jsonData);
                for(JsonNode jsonNode : arrayNode) {
                    Long id = Long.valueOf(StringUtils.substringAfter(jsonNode.get("id").asText(), "_"));
                    itemMap.get(id).setPrice(jsonNode.get("p").asLong());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 获取商品买点
            idList.clear();
            for(Long id : itemMap.keySet()) {
                idList.add("AD_" + id);
            }
            String adUrl = "http://ad.3.cn/ads/mgets?skuIds=" + StringUtils.join(idList, ',');
            jsonData = doGet(adUrl);
            try {
                ArrayNode arrayNode = (ArrayNode) MAPPER.readTree(jsonData);
                for(JsonNode jsonNode : arrayNode) {
                    Long id = Long.valueOf(StringUtils.substringAfter(jsonNode.get("id").asText(), "_"));
                    itemMap.get(id).setSellPoint(jsonNode.get("ad").asText());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            for(Item item : itemMap.values()) {
                stringBuilder.append(item.toString()).append("\n");
            }
        }
        try {
            FileUtils.writeStringToFile(new File("/home/twilight/item.txt"),
                                        stringBuilder.toString(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String doGet(String url) {

        // 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建http GET请求
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpClient.execute(httpGet);
            // 判断返回状态是否为200
            if(response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(new JdCrawler().doGet("http://kns.cnki.net/KXReader/Detail?dbcode=CJFD&filename=ZGFX201501004&uid=WEEvREdxOWJmbC9oM1NjYkZCbDZZNTBMY3loV0hMaHV0dk85c1FjRlVTR1A=$R1yZ0H6jyaa0en3RxVUd8df-oHi7XMMDo7mtKT6mSmEvTuk11l2gFA!!"));
    }
}
