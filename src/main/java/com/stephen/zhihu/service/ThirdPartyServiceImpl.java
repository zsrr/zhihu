package com.stephen.zhihu.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stephen.zhihu.exception.AccessTokenInvalidException;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class ThirdPartyServiceImpl implements ThirdPartyService {

    private static final String QQ_BASE_URL = "https://graph.qq.com/oauth2.0/me/";
    private static final String WECHAT_BASE_URL = "https://api.weixin.qq.com/sns/auth/";

    private CloseableHttpClient client;

    public ThirdPartyServiceImpl() {
        client = HttpClients.createDefault();
    }

    @Override
    public void checkQQAccessToken(String openId, String accessToken) throws IOException {
        HttpGet hg = new HttpGet();
        hg.setConfig(RequestConfig.custom().
                setConnectTimeout(5000).
                setConnectionRequestTimeout(1000).
                setSocketTimeout(5000).
                build());
        try {
            URI uri = new URIBuilder(QQ_BASE_URL).addParameter("access_token", accessToken).build();
            hg.setURI(uri);
            CloseableHttpResponse response = client.execute(hg);
            String responseStr = IOUtils.toString(response.getEntity().getContent(), "utf-8");
            ObjectNode on = new ObjectMapper().readValue(getJsonString(responseStr), ObjectNode.class);
            if (on.has("error") || !on.get("openid").asText().equals(openId)) {
                throw new AccessTokenInvalidException();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private String getJsonString(String raw) {
        int begin = raw.indexOf('{');
        int end = raw.indexOf('}') + 1;
        return raw.substring(begin, end);
    }

    @Override
    public void checkWechatAccessToken(String openId, String accessToken) throws IOException {
        HttpGet hg = new HttpGet();
        hg.setConfig(RequestConfig.custom().
                setConnectTimeout(5000).
                setConnectionRequestTimeout(1000).
                setSocketTimeout(5000).
                build());
        try {
            URI uri = new URIBuilder(WECHAT_BASE_URL).
                    addParameter("access_token", accessToken).
                    addParameter("openid", openId).
                    build();
            hg.setURI(uri);
            CloseableHttpResponse response = client.execute(hg);
            String responseStr = IOUtils.toString(response.getEntity().getContent(), "utf-8");
            ObjectNode on = new ObjectMapper().readValue(responseStr, ObjectNode.class);
            if (on.get("errorcode").asInt() != 0) {
                throw new AccessTokenInvalidException();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void destroy() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
