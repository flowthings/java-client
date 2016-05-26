package com.flowthings.client.response;

import java.util.HashMap;
import java.util.Map;

public class MapResponse extends Response<Map<String, Object>> {
  public MapResponse(Head head, String key, Object inner) {
    this.head = head;
    body = new HashMap<>();
    body.put(key, inner);
  }
}
