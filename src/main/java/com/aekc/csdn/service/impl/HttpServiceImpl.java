package com.aekc.csdn.service.impl;

import com.aekc.csdn.service.HttpService;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Service
public class HttpServiceImpl implements HttpService {


    private static final String CHARSET = "UTF-8";

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpService.class);

    private final CloseableHttpClient httpClient;

    private final RequestConfig requestConfig;

    @Autowired
    public HttpServiceImpl(CloseableHttpClient httpClient, RequestConfig requestConfig) {
        this.httpClient = httpClient;
        this.requestConfig = requestConfig;
    }

    @Override
    public String doGet(String url) throws IOException, URISyntaxException {
        return doGet(url, null, CHARSET);
    }

    @Override
    public String doGet(String url, String encode) throws IOException, URISyntaxException {
        return doGet(url, null, encode);
    }

    @Override
    public String doGet(String url, Map<String, String> params, String encode) throws IOException, URISyntaxException {
        URI uri;
        if(params == null) {
            uri = URI.create(url);
        } else {
            // 设置参数
            URIBuilder builder = new URIBuilder(url);
            for(Map.Entry<String, String> entry : params.entrySet()) {
                builder.addParameter(entry.getKey(), entry.getValue());
            }
            uri = builder.build();
        }
        LOGGER.info("执行Http Get请求，URL：" + uri);
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setConfig(requestConfig);
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            if (encode == null) {
                encode = CHARSET;
            }
            return EntityUtils.toString(response.getEntity(), encode);
        }
    }
}
