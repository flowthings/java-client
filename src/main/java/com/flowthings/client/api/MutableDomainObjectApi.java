package com.flowthings.client.api;


public class MutableDomainObjectApi<T> extends DomainObjectApi<T> {

  public MutableDomainObjectApi(Class<T> clazz) {
    super(clazz);
  }

  public Request<T> update(String id, T t) {
    return Request.createObjectRequest(clazz, Request.Action.UPDATE)//
        .body(t).id(id);
  }

}
