package com.flowthings.client.api;

import com.flowthings.client.domain.Drop;

public class WebsocketsDropResponse {
  public String type;
  public String resource;
  public Drop value;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getResource() {
    return resource;
  }

  public void setResource(String resource) {
    this.resource = resource;
  }

  public Drop getValue() {
    return value;
  }

  public void setValue(Drop value) {
    this.value = value;
  }
}
