package com.flowthings.client.response;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.flowthings.client.domain.FlowDomainObject;

public class ListResponse<T extends FlowDomainObject> extends Response<List<T>> implements Iterable<T> {
  public ListResponse() {
    body = new ArrayList<>();
  }

  public static class ERROR<T extends FlowDomainObject> extends ListResponse<T> {
    @Override
    public List<T> get() {
      throw new IllegalStateException("Cannot get body of an error response");
    }

    @Override
    public List<T> getBody() {
      throw new IllegalStateException("Cannot get body of an error response");
    }
  }

  public int size() {
    return body.size();
  }

  @Override
  public Iterator<T> iterator() {
    return body.iterator();
  }
}
