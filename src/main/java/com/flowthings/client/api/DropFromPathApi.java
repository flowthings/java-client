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

  private static final Pattern PATTERN = Pattern.compile("(/[a-z0-9-_:]+)+/?");

  private String path;

  DropFromPathApi(String path) {
    if (!PATTERN.matcher(path).matches()) {
      throw new IllegalArgumentException(String.format("\"%s\" is not a valid path", path));
    }
    this.path = path;
  }

  public Request<Drop> create(Drop t) {
    t.setPath(path);
    return Request.createObjectRequest(Drop.class, Request.Action.CREATE).body(t);
  }

}
