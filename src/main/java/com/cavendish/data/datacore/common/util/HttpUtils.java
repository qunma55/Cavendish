package com.cavendish.data.datacore.common.util;

import com.google.common.io.CharStreams;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 通用的HTTP工具类
 *
 * @author hevigo9
 */
public class HttpUtils {
  private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

  // 日志保留请求现场 请求地址，请求时间，请求结果，...
  // 多个普通参数，FORM表单，JSON数据，XML数据
  // GET POST

  public static void main(String[] args) throws Exception {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpGet httpGet = null;
    try {
      URI uri =
          new URIBuilder()
              .setScheme("https")
              .setHost("restapi.amap.com")
              .setPath("/v3/config/district")
              .setParameter("key", "ff42e94f707fb86646d59f8d72c46862")
              .setParameter("keywords", "山东")
              .setParameter("subdistrict", "2")
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
      if (!Objects.isNull(response)) {
        HttpEntity responseEntity = response.getEntity();
        InputStream instream = responseEntity.getContent();
        String str = CharStreams.toString(new InputStreamReader(instream, StandardCharsets.UTF_8));
        System.out.println(str);
      }
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
}
