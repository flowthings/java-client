package com.flowthings.client.api;

import java.util.List;

import com.flowthings.client.QueryOptions;

public class DomainObjectApi<T> {

  protected Class<T> clazz;

  // protected String url = "https://api.flowthings.io/v0.1/";

  public DomainObjectApi(Class<T> clazz) {
    this.clazz = clazz;
  }

  public Request<T> get(String id) {
    return Request.createObjectRequest(clazz, Request.Action.GET)//
        .id(id);
  }

  public Request<List<T>> find(QueryOptions queryOptions) {
    return Request.createListRequest(clazz, Request.Action.FIND)//
        .params(queryOptions);
  }

  public Request<T> create(T t) {
    return Request.createObjectRequest(clazz, Request.Action.CREATE)//
        .body(t);
  }

  public Request<T> delete(String id) {
    return Request.createObjectRequest(clazz, Request.Action.DELETE)//
        .id(id);
  }

  //
  // public Request<DomainObjectResponse<T>, T> deleteMember(String id, String
  // memberName) {
  // return Request.createObjectRequest(clazz, Request.Action.DELETE,
  // credentials)//
  // .method(RequestType.DELETE)//
  // .path(buildPath(id, memberName));
  // }
  //
  // public Request<DomainObjectResponse<T>, T> setMember(String id, String
  // memberName, String memberValue) {
  // return Request.createObjectRequest(clazz, credentials)//
  // .method(RequestType.PUT)//
  // .path(buildPath(id, memberName));
  // }
  //
  // public Request<DomainObjectResponse<T>, T> getMember(String id, String
  // memberName) {
  // return Request.createObjectRequest(clazz, credentials)//
  // .method(RequestType.GET)//
  // .path(buildPath(id, memberName));
  // }

  public Request<List<T>> find() {
    return find(new QueryOptions());
  }

  @SuppressWarnings("unchecked")
  public static <T> Class<T> castClass(Class<?> aClass) {
    return (Class<T>) aClass;
  }

}
