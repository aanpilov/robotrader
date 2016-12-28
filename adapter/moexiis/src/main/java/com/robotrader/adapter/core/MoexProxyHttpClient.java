/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.adapter.core;

import java.net.URI;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author aav
 */
public class MoexProxyHttpClient implements MoexProxy {
    private static final String BASE_URI = "http://www.moex.com/iss/";
    private static final String SECURITIES_URI = BASE_URI + "engines/stock/markets/shares/boards/TQBR/securities.xml";
    private final HttpClient httpClient;
    private final RequestConfig config;
    
    public MoexProxyHttpClient() {
        HttpHost proxy = new HttpHost("172.18.243.66", 3128, "http");
        config = RequestConfig.custom()
                    .setProxy(proxy)
                    .build();
        httpClient = HttpClientBuilder.create().build();        
    }
    
        
    
    @Override
    public String getCandles(String securityCode, String from, String till, int interval) {
        try {
            StringBuilder requestUrl = new StringBuilder();
            requestUrl.append(BASE_URI);
            requestUrl.append("engines/stock/markets/shares/securities/").append(securityCode).append("/candles.xml");

            URI uri = new URIBuilder(requestUrl.toString()).addParameter("from", from).addParameter("till", till).addParameter("interval", Integer.toString(interval)).build();
            return executeGetRequest(uri.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSecurities() {
        return executeGetRequest(SECURITIES_URI);
    }
    
    private String executeGetRequest(String url) {
        try {
            HttpGet getRequest = new HttpGet(url);
            getRequest.setConfig(config);
            HttpResponse response = httpClient.execute(getRequest);
            if(response.getStatusLine().getStatusCode() != 200) {
                throw new Exception(response.getStatusLine().getReasonPhrase());
            }
            
            String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
            EntityUtils.consume(response.getEntity());
            
            return responseBody;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String[] args) throws Exception {
        MoexProxyHttpClient client = new MoexProxyHttpClient();
        System.out.println(client.getCandles("SBER", "2016-09-01 00:00:00", "2016-11-10 19:55:00", 7));
    }

    
}
