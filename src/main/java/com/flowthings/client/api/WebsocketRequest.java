package com.flowthings.client.api;

import java.util.Map;

public class WebsocketRequest<T> {
  public String msgId;
  public String object;
  public String type;
  public String flowId;
  public String id;
  public T value;
  private Map<String, String> options;

  public String getMsgId() {
    return msgId;
  }

  public void setMsgId(String msgId) {
    this.msgId = msgId;
  }

  public String getObject() {
    return object;
  }

  public void setObject(String object) {
    this.object = object;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getFlowId() {
    return flowId;
  }

  public void setFlowId(String flowId) {
    this.flowId = flowId;
  }

  public T getValue() {
    return value;
  }

  public void setValue(T value) {
    this.value = value;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setOptions(Map<String, String> options) {
    this.options = options;
  }

  public Map<String, String> getOptions() {
    return options;
  }
}
