package com.flowthings.client.api;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import com.flowthings.client.Credentials;
import com.flowthings.client.Header;
import com.flowthings.client.Serializer;
import com.flowthings.client.exception.AuthorizationException;
import com.flowthings.client.exception.BadRequestException;
import com.flowthings.client.exception.ConnectionRefusedException;
import com.flowthings.client.exception.FlowthingsException;
import com.flowthings.client.exception.NotFoundException;
import com.flowthings.client.response.ListResponse;
import com.flowthings.client.response.ObjectResponse;
import com.flowthings.client.response.Response;

/**
 * Send requests over HTTP. Responses are synchronous.
 *
 * @author matt
 */
public class RestApi extends Api {
  protected static Logger logger = Logger.getLogger("com.flow.client.api.RestApi");
  private final ExecutorService pool;
  private Credentials credentials;
  private String url;
  private static Map<Request.Action, String> methods = new HashMap<>();
  static {
    methods.put(Request.Action.CREATE, "POST");
    methods.put(Request.Action.DELETE, "DELETE");
    methods.put(Request.Action.FIND, "GET");
    methods.put(Request.Action.UPDATE, "PUT");
    methods.put(Request.Action.GET, "GET");
  }

  public RestApi(Credentials credentials) {
    this(credentials, "api.flowthings.io/v0.1", true);
  }

  public RestApi(Credentials credentials, String host, boolean secure) {
    this.credentials = credentials;
    final String transport = secure ? "https://" : "http://";
    this.url = transport + host;
    this.pool = Executors.newCachedThreadPool();
  }

  @SuppressWarnings("unchecked")
  public <S> S send(Request<S> request) throws FlowthingsException {
    try {
      String queryString = toQueryString(request.buildHttpPath(credentials, url), request.queryOptions.toMap());
      HttpURLConnection connection = (HttpURLConnection) (new URL(queryString).openConnection());
      String method = methods.get(request.action);
      if (method == null) {
        throw new FlowthingsException("Cannot send " + request.action + " using the RestAPI");
      }
      Map<String, Object> headers = new Header(credentials).toMap();
      setRequestProperties(connection, headers);
      connection.setDoInput(true);
      connection.setRequestMethod(method.toString());
      if (request.body != null) {
        connection.setDoOutput(true);
        try (DataOutputStream out = new DataOutputStream(connection.getOutputStream())) {
          // logger.log(Level.INFO, request.body);
          out.writeBytes(request.body);
          out.flush();
        }
      }
      // Todo - big cleanup here
      String stringResponse = null;
      try {
        stringResponse = collectResponse(connection.getInputStream());
        Response<S> response = Serializer.fromJson(stringResponse, request.typeToken);
        int status = response.getHead().getStatus();
        if (response.getHead().isOk()) {
          return response.getBody();
        } else if (status == 404) {
          throw new NotFoundException(response.getHead().getErrors().get(0));
        } else if (status == 403) {
          throw new AuthorizationException(response.getHead().getErrors().get(0));
        } else {
          throw new BadRequestException(response.getHead().getErrors().get(0));
        }
      } catch (java.net.ConnectException e) {
        throw new ConnectionRefusedException(String.format("Error connecting to %s: %s", url, e.getMessage()));
      } catch (Exception e) {
        stringResponse = collectResponse(connection.getErrorStream());

        // Messy
        if (request.listResponse){
          Response response = Serializer.fromJson(stringResponse, ListResponse.ERROR.class);
          int status = response.getHead().getStatus();
          if (status == 404) {
            throw new NotFoundException(response.getHead().getErrors().get(0));
          } else if (status == 403) {
            throw new AuthorizationException(response.getHead().getErrors().get(0));
          } else {
            throw new BadRequestException(response.getHead().getErrors().get(0));
          }

        } else {
          Response response = Serializer.fromJson(stringResponse, ObjectResponse.ERROR.class);
          int status = response.getHead().getStatus();
          if (status == 404) {
            throw new NotFoundException(response.getHead().getErrors().get(0));
          } else if (status == 403) {
            throw new AuthorizationException(response.getHead().getErrors().get(0));
          } else {
            throw new BadRequestException(response.getHead().getErrors().get(0));
          }
        }

      }
    } catch (IOException e) {
      throw new FlowthingsException(e);
    }
  }

  @Override
  public <S> FlowthingsFuture<S> sendAsync(final Request<S> request){
    return new FlowthingsFuture<>(pool.submit(new Callable<S>() {
      @Override
      public S call() throws Exception {
          return send(request);
      }
    }));
  }

  public boolean supportsSubscribe() {
    return false;
  }

  protected String toQueryString(String url, Map<String, String> parameters) throws UnsupportedEncodingException {
    if (parameters == null || parameters.isEmpty()) {
      return url;
    }
    StringBuilder b = new StringBuilder(url).append('?');
    for (Iterator<Map.Entry<String, String>> iter = parameters.entrySet().iterator(); iter.hasNext();) {
      Map.Entry<String, String> e = iter.next();
      b.append(URLEncoder.encode(e.getKey(), "UTF-8") + "=" + URLEncoder.encode(e.getValue(), "UTF-8"));
      if (iter.hasNext()) {
        b.append('&');
      }
    }
    return b.toString();
  }
}
