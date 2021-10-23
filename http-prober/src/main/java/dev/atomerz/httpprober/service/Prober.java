package dev.atomerz.httpprober.service;

import dev.atomerz.httpprober.entity.HttpService;
import dev.atomerz.httpprober.entity.HttpService.Status;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpConnection;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.impl.Http1StreamListener;
import org.apache.hc.core5.http.impl.bootstrap.HttpRequester;
import org.apache.hc.core5.http.impl.bootstrap.RequesterBootstrap;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.http.protocol.HttpCoreContext;
import org.apache.hc.core5.util.Timeout;
import org.springframework.stereotype.Component;

@Component
public class Prober {

  private final HttpCoreContext coreContext;
  HttpRequester requester;

  public Prober() {
    requester = RequesterBootstrap.bootstrap()
        .setStreamListener(new Http1StreamListener() {

          @Override
          public void onRequestHead(final HttpConnection connection, final HttpRequest request) {
          }

          @Override
          public void onResponseHead(final HttpConnection connection, final HttpResponse response) {
          }

          @Override
          public void onExchangeComplete(final HttpConnection connection, final boolean keepAlive) {
          }

        })
        .setSocketConfig(SocketConfig.custom()
            .setSoTimeout(5, TimeUnit.SECONDS)
            .build())
        .create();
    coreContext = HttpCoreContext.create();
  }

  public void updateStatus(List<HttpService> services) {
    for (HttpService service : services) {
      URL url;
      try {
        url = new URL(service.getUrl());
      } catch (MalformedURLException e) {
        throw new AssertionError(e);
      }
      HttpHost target = new HttpHost(url.getHost());
      ClassicHttpRequest request = ClassicRequestBuilder.get()
          .setHttpHost(target)
          .setPath(url.getPath())
          .build();
      try (ClassicHttpResponse response = requester
          .execute(target, request, Timeout.ofSeconds(1), coreContext)) {
        EntityUtils.consume(response.getEntity());
        if (response.getCode() == 200) {
          service.setStatus(Status.OK);
        } else {
          service.setStatus(Status.FAIL);
        }
      } catch (HttpException | IOException e) {
        service.setStatus(Status.FAIL);
      }
    }
  }
}
