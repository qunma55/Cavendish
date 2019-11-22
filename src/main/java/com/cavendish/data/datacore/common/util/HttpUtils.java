package com.cavendish.data.datacore.common.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * 通用的HTTP工具类
 *
 * @author hevigo9
 */
public class HttpUtils {
  private final static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

  public static void main(String[] args) {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpGet httpGet = null;
    try {
      URI uri =
          new URIBuilder()
              .setScheme("http")
              .setHost("www.google.com")
              .setPath("/search")
              .setParameter("q", "httpclient")
              .setParameter("btnG", "Google Search")
              .setParameter("aq", "f")
              .setParameter("oq", "")
              .build();
      httpGet = new HttpGet(uri);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    CloseableHttpResponse response = null;
    try {
      if (Objects.isNull(httpGet)) {
        return;
      }
      response = httpClient.execute(httpGet);
      // 响应体
      HttpEntity responseEntity = response.getEntity();
      System.out.println("响应状态：" + response.getStatusLine());
      // gzip,deflate,compress
      System.out.println("响应体编码方式：" + responseEntity.getContentEncoding());
      // 响应类型如text/html charset也有可能在ContentType中
      System.out.println("响应体类型：" + responseEntity.getContentType());
      /*
       EntityUtils.toString()方法会将响应体的输入流关闭，相当于消耗了响应体， 此时连接会回到httpclient中的连接管理器的连接池中，如果
       下次访问的路由是一样的， 则此连接可以被复用。
      */
      System.out.println("响应体内容：" + EntityUtils.toString(responseEntity));
      // 如果关闭了httpEntity的inputStream，httpEntity长度应该为0，而且再次请求相同路由的连接可以共用一个连接。
      // 可以通过设置连接管理器最大连接为1来验证。
      response = httpClient.execute(httpGet);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (!Objects.isNull(response)) {
          // 关闭连接，则此次连接被丢弃
          response.close();
        }
        httpClient.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  // 日志保留请求现场 请求地址，请求时间，请求结果，...
  // 多个普通参数，FORM表单，JSON数据，XML数据
  // GET POST
}
