package com.frobro.bcindex.web.service.apis;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by rise on 3/23/17.
 */
public class HttpApi {
  private static final String EMPTY_RESPONSE = "noop";

  public String makeApiCall(String endpoint) throws IOException {
    String response = EMPTY_RESPONSE;
    CloseableHttpClient httpClient = HttpClients.createDefault();
    try {

      HttpGet getRequest = new HttpGet(
          endpoint);
      getRequest.addHeader("accept", "application/json");
      response = httpClient.execute(getRequest, createResponseHandler());

    } finally {
      httpClient.close();
    }
    return response;
  }

  private ResponseHandler<String> createResponseHandler() {
    return new ResponseHandler<String>() {

      @Override
      public String handleResponse(
          final HttpResponse response) throws ClientProtocolException, IOException {

        int status = response.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
          HttpEntity entity = response.getEntity();
          return entity != null ? EntityUtils.toString(entity) : null;
        } else {
          throw new ClientProtocolException("Unexpected response status: " + status);
        }
      }
    };
  }

  public String publish(String endPoint, String data) throws IOException {
    String response;
    CloseableHttpClient httpClient = HttpClients.createDefault();

    try {
      HttpPost post = new HttpPost(endPoint);
      post.addHeader("accept","application/json; charset=utf8");
      post.setEntity(new StringEntity(data,"application/json","UTF-8"));
      response = httpClient.execute(post, createResponseHandler());

    } finally {
      httpClient.close();
    }
    return response;
  }
}

