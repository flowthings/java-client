package com.flowthings.client.api;

public class MutableDomainObjectApi<T> extends DomainObjectApi<T> {

  public MutableDomainObjectApi(Class<T> clazz) {
    super(clazz);
  }

  /**
   * Update an object with new attributes
   * 
   * @param id
   *          - the object's ID
   * @param t
   *          - an object containing new values
   * @return - A request object, to pass to an API
   */
  public Request<T> update(String id, T t) {
    return Request.createObjectRequest(clazz, Request.Action.UPDATE)//
        .body(t).id(id);
  }

}
