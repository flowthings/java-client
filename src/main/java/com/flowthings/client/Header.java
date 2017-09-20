package com.flowthings.client;

import java.util.HashMap;
import java.util.Map;

public class Header {
  public static final String FLOW_VERSION_STRING = "Flow java client v4.0";

  public enum MIME_TYPE {
    JSON("application/json"), XML("text/xml");
    public final String value;

    private MIME_TYPE(String s) {
      this.value = s;
    }
  }

  private Credentials credentials;

  public Header(Credentials credentials) {
    this.credentials = credentials;
  }

  public Map<String, Object> toMap() {
    Map<String, Object> m = new HashMap<>();
    m.put("Accept", MIME_TYPE.JSON.value);
    m.put("X-Auth-Token", credentials.token);
    m.put("USER-AGENT", FLOW_VERSION_STRING);
    // if (t == RequestType.POST || t == RequestType.PUT) {
    m.put("Content-type", MIME_TYPE.JSON.value);
    // }
    return m;
  }

}
