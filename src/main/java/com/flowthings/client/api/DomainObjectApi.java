package com.flowthings.client.api;

import java.util.List;

import com.flowthings.client.QueryOptions;

/**
 * Methods to create {@link Request} objects, based on actions which the user
 * might want to perform
 *
 * @author matt
 */
public class DomainObjectApi<T> {

  protected Class<T> clazz;

  public DomainObjectApi(Class<T> clazz) {
    this.clazz = clazz;
  }

  /**
   * Retrieve a single object by ID
   * 
   * @param id
   *          - the object's ID
   * @return - A request object, to pass to an API
   */
  public Request<T> get(String id) {
    return Request.createObjectRequest(clazz, Request.Action.GET).id(id);
  }

  /**
   * Find multiple objects using search parameters
   * 
   * @param queryOptions
   *          - search parameters
   * @return - A request object, to pass to an API
   */
  public Request<List<T>> find(QueryOptions queryOptions) {
    return Request.createListRequest(clazz, Request.Action.FIND).params(queryOptions);
  }

  /**
   * Create a new object
   * 
   * @param t
   *          - the object to create
   * @return - A request object, to pass to an API
   */
  public Request<T> create(T t) {
    return Request.createObjectRequest(clazz, Request.Action.CREATE).body(t);
  }

  /**
   * Delete an object by ID
   * 
   * @param id
   *          - the object's ID
   * @return - A request object, to pass to an API
   */
  public Request<T> delete(String id) {
    return Request.createObjectRequest(clazz, Request.Action.DELETE).id(id);
  }

  /**
   * Find multiple objects using search parameters
   * 
   * @return - A request object, to pass to an API
   */
  public Request<List<T>> find() {
    return find(new QueryOptions());
  }

}
