package com.flowthings.client.api;

import java.util.List;
import java.util.regex.Pattern;

import com.flowthings.client.QueryOptions;
import com.flowthings.client.domain.Drop;

/**
 * See {@link DomainObjectApi}
 *
 * @author matt
 */
public class DropApi extends MutableDomainObjectApi<Drop> {

  private static final Pattern FLOW_ID_PATTERN = Pattern.compile("f[0-9a-f]{24}");

  private String flowId;

  DropApi(String flowId) {
    super(Drop.class);
    if (!FLOW_ID_PATTERN.matcher(flowId).matches()) {
      throw new IllegalArgumentException(String.format("\"%s\" is not a valid flow id", flowId));
    }
    this.flowId = flowId;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.flowthings.client.api.DomainObjectApi#get(java.lang.String)
   */
  @Override
  public Request<Drop> get(String id) {
    return super.get(id).flowId(flowId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.flowthings.client.api.DomainObjectApi#find(com.flowthings.client.
   * QueryOptions)
   */
  @Override
  public Request<List<Drop>> find(QueryOptions queryOptions) {
    return super.find(queryOptions).flowId(flowId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.flowthings.client.api.DomainObjectApi#create(java.lang.Object)
   */
  @Override
  public Request<Drop> create(Drop t) {
    return super.create(t).flowId(flowId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.flowthings.client.api.DomainObjectApi#update(java.lang.String,
   * java.lang.Object)
   */
  @Override
  public Request<Drop> update(String id, Drop t) {
    return super.update(id, t).flowId(flowId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.flowthings.client.api.DomainObjectApi#delete(java.lang.String)
   */
  @Override
  public Request<Drop> delete(String id) {
    return super.delete(id).flowId(flowId);
  }

  /**
   * Remove all Drops from a Flow.
   * 
   * @return - A request object, to pass to an API
   */
  public Request<Drop> deleteAll() {
    return Request.createObjectRequest(clazz, Request.Action.DELETE).flowId(flowId);
  }

  /**
   * Subscribe to a Flow. All Drops sent to that Flow will be pushed to the
   * client
   * 
   * @param callback
   *          - called each time a new Drop arrives
   * @return - A request object, to pass to an API
   */
  public Request<Drop> subscribe(SubscriptionCallback<Drop> callback) {
    return Request.createObjectRequest(clazz, Request.Action.SUBSCRIBE)//
        .flowId(flowId).addData("callback", callback);
  }

  /**
   * @return - A request object, to pass to an API
   */
  public Request<Drop> unsubscribe() {
    return Request.createObjectRequest(clazz, Request.Action.UNSUBSCRIBE)//
        .flowId(flowId);
  }
}
