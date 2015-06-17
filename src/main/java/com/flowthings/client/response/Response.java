package com.flowthings.client.response;

import java.util.List;

import com.flowthings.client.Serializer;

public class Response<T> {
  protected Head head;
  protected T body;

  public Head getHead() {
    return head;
  }

  public void setHead(Head head) {
    this.head = head;
  }

  public T get() {
    if (!head.isOk()) {
      throw new IllegalStateException("Cannot get the body of an error response");
    }
    return body;
  }

  public void setBody(T t) {
    body = t;
  }

  public T getBody() {
    return get();
  }

  public String json() {
    return Serializer.toJson(this);
  }

  @Override
  public String toString() {
    return "FlowResponse [head=" + head + ", body=" + body + "]";
  }

  public static class Head {
    private boolean ok;
    private List<String> messages;
    private List<String> errors;
    private int status;
    private String msgId;

    public boolean isOk() {
      return ok;
    }

    public void setOk(boolean ok) {
      this.ok = ok;
    }

    public List<String> getMessages() {
      return messages;
    }

    public void setMessages(List<String> messages) {
      this.messages = messages;
    }

    public List<String> getErrors() {
      return errors;
    }

    public void setErrors(List<String> errors) {
      this.errors = errors;
    }

    public int getStatus() {
      return status;
    }

    public void setStatus(int status) {
      this.status = status;
    }

    public String getMsgId() {
      return msgId;
    }

    public void setMsgId(String msgId) {
      this.msgId = msgId;
    }

    @Override
    public String toString() {
      return "Head [ok=" + ok + ", messages=" + messages + ", errors=" + errors + ", status=" + status + "]";
    }

  }
}
