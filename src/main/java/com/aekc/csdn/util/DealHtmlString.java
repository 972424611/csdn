package com.aekc.csdn.util;

import java.util.HashMap;
import java.util.Map;

public class DealHtmlString {

    private static final Map<String, String> charEntityMap = new HashMap<>();

    static {
        charEntityMap.put("&nbsp;", " ");
        charEntityMap.put("&lt;", "<");
        charEntityMap.put("&gt;", ">");
        charEntityMap.put("&amp;", "&");
        charEntityMap.put("&quot;", "\"");
    }

    public static String getBeautifyHtml(String html) {
        return replaceCharEntity(removalOfLine(removeHtmlLabel(html)));
    }

    public static String replaceCharEntity(String html) {
        for(Map.Entry<String, String> entry : charEntityMap.entrySet()) {
            if(html.contains(entry.getKey())) {
                html = html.replace(entry.getKey(), entry.getValue());
            }
        }
        return html;
    }

    public static String removalOfLine(String html) {
        return html.replace("\r", "")
                .replace("\n", "");
    }

    public static String removeHtmlLabel(String html) {
        return html.replaceAll("</?[^>]+>", "");
    }
}
