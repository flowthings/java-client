package com.flowthings.client.api;

import java.util.List;
import java.util.regex.Pattern;

import com.flowthings.client.QueryOptions;
import com.flowthings.client.domain.Drop;

public class DropApi extends MutableDomainObjectApi<Drop> {

  private static final Pattern FLOW_ID_PATTERN = Pattern.compile("f[0-9a-f]{24}");

  private String flowId;

  public DropApi(String flowId) {
    super(Drop.class);
    if (!FLOW_ID_PATTERN.matcher(flowId).matches()) {
      throw new IllegalArgumentException(String.format("\"%s\" is not a valid flow id", flowId));
    }
    this.flowId = flowId;
  }

  @Override
  public Request<Drop> get(String id) {
    return super.get(id).flowId(flowId);
  }

  @Override
  public Request<List<Drop>> find(QueryOptions queryOptions) {
    return super.find(queryOptions).flowId(flowId);
  }

  @Override
  public Request<Drop> create(Drop t) {
    return super.create(t).flowId(flowId);
  }

  @Override
  public Request<Drop> update(String id, Drop t) {
    return super.update(id, t).flowId(flowId);
  }

  @Override
  public Request<Drop> delete(String id) {
    return super.delete(id).flowId(flowId);
  }

  public Request<Drop> deleteAll() {
    return Request.createObjectRequest(clazz, Request.Action.DELETE).flowId(flowId);
  }

  public Request<Drop> subscribe(SubscriptionCallback<Drop> callback) {
    return Request.createObjectRequest(clazz, Request.Action.SUBSCRIBE)//
        .flowId(flowId).addData("callback", callback);
  }

  public Request<Drop> unsubscribe() {
    return Request.createObjectRequest(clazz, Request.Action.UNSUBSCRIBE)//
        .flowId(flowId);
  }
}
