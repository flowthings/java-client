package com.flowthings.client.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

public abstract class Api {

  protected static Logger logger = Logger.getLogger("com.flow.client.api.Api");

  protected String collectResponse(boolean gzip, InputStream response) throws IOException {
    InputStream in = response;
    if (gzip){
      in = new GZIPInputStream(response);
    }
    BufferedReader r = new BufferedReader(new InputStreamReader(in));
    StringBuilder out = new StringBuilder();
    String s = null;
    while ((s = r.readLine()) != null) {
      out.append(s + '\n');
    }
    return out.toString();
  }

  protected void setRequestProperties(URLConnection connection, Map<String, Object> headers) {
    for (Map.Entry<String, Object> e : headers.entrySet()) {
      connection.setRequestProperty(e.getKey(), e.getValue().toString());
    }
    // if (logger.isLoggable(Level.INFO)) {
    // for (Map.Entry<String, Object> e : headers.entrySet()) {
    // logger.log(Level.INFO, String.format("Header:\t%s\t->\t%s", e.getKey(),
    // e.getValue()));
    // }
    // }
  }

  /**
   * A consistent interface for sending requests via multiple APIs
   */
  public abstract <S> FlowthingsFuture<S> sendAsync(final Request<S> request);

  public abstract boolean supportsSubscribe();

  public abstract void close();

}
