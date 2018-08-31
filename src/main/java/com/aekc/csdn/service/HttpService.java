package com.aekc.csdn.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

@Service
public interface HttpService {


    String doGet(String url) throws IOException, URISyntaxException;

    String doGet(String url, String encode) throws IOException, URISyntaxException;

    String doGet(String url, Map<String, String> params, String encode) throws IOException, URISyntaxException;
}
