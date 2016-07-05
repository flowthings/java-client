package com.flowthings.client.api;

import com.flowthings.client.QueryOptions;
import com.flowthings.client.domain.Drop;

import java.util.List;
import java.util.regex.Pattern;

/**
 * See {@link DomainObjectApi}
 *
 * @author matt
 */
public class DropFromPathApi {

//  private static final Pattern FLOW_ID_PATTERN = Pattern.compile("f[0-9a-f]{24}");

  private String path;

  DropFromPathApi(String path) {
//    if (!FLOW_ID_PATTERN.matcher(flowId).matches()) {
//      throw new IllegalArgumentException(String.format("\"%s\" is not a valid flow id", flowId));
//    }
    this.path = path;
  }

  public Request<Drop> create(Drop t) {
    t.setPath(path);
    return Request.createObjectRequest(Drop.class, Request.Action.CREATE).body(t);
  }

}
