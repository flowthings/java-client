package com.flowthings.client.api;

import java.util.regex.Pattern;

import com.flowthings.client.domain.Drop;

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
