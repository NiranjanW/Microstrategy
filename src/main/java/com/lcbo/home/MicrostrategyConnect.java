package com.lcbo.home;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.protocol.HttpContext;
import org.apache.http.*;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Closeable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.Future;


public class MicrostrategyConnect {

    private String baseURL;
    private String username;
    private String password;
    private CookieStore cookieStore;
    private  HttpClientContext httpContext;
    private CloseableHttpClient httpClient;
    private Collection<Header> headers;
    private ObjectMapper mapper;



    private CloseableHttpClient createHttpClient()
    {
        CloseableHttpClient httpClient;
//        CommonHelperFunctions helperFunctions = new CommonHelperFunctions();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(306);
        cm.setDefaultMaxPerRoute(108);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(15000)
                .setSocketTimeout(15000).build();
        httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setDefaultRequestConfig(requestConfig).build();
        return httpClient;
    }

    public MicrostrategyConnect(String url, String username , String password){

        this.baseURL =url + "/api";
        this.username =username;
        this.password = password;

        CookieStore cookieStore = new BasicCookieStore();

        httpContext = HttpClientContext.create();
        httpContext.setCookieStore(this.cookieStore);

        RequestConfig requestConfig =RequestConfig.custom()
                .setConnectionRequestTimeout(30000)
                .setSocketTimeout(30000).build();

        httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        this.headers = new ArrayList<Header>();
        this.headers.add(new BasicHeader(HttpHeaders.CONTENT_TYPE,"application/json"));
        this.headers.add(new BasicHeader(HttpHeaders.ACCEPT, "application/json"));


    }

    public void connect() throws MicroStrategyException{
        Map<String,String> payload = new Hashtable<String,String>();
        payload.put("username",username);
        payload.put("password" ,password);

        try {
            HttpPost request = new HttpPost(baseURL + "/auth/login");
            request.setEntity(new StringEntity(mapper.writeValueAsString(payload)));
            request.setHeaders(this.headers.toArray(new Header[this.headers.size()]));
            CloseableHttpResponse response = httpClient.execute(request,httpContext);
        } catch (Exception e) {
           throw new MicroStrategyException(e);
        }

    }


}
