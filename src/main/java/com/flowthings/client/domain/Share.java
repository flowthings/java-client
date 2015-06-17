package com.flowthings.client.domain;

import java.util.HashMap;
import java.util.Map;

public class Share extends FlowDomainObject {
  protected String description;
  protected String issuedBy;
  protected String issuedTo;
  protected Map<String, Permissions> paths;
  protected Long duration;
  protected Long expiresInMs;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getIssuedBy() {
    return issuedBy;
  }

  public void setIssuedBy(String issuedBy) {
    this.issuedBy = issuedBy;
  }

  public String getIssuedTo() {
    return issuedTo;
  }

  public void setIssuedTo(String issuedTo) {
    this.issuedTo = issuedTo;
  }

  public Map<String, Permissions> getPaths() {
    return paths;
  }

  public void setPaths(Map<String, Permissions> paths) {
    this.paths = paths;
  }

  public Long getDuration() {
    return duration;
  }

  public void setDuration(Long duration) {
    this.duration = duration;
  }

  public Long getExpiresInMs() {
    return expiresInMs;
  }

  public void setExpiresInMs(Long expiresInMs) {
    this.expiresInMs = expiresInMs;
  }

  @Override
  public String toString() {
    return "Share [issuedBy=" + issuedBy + ", issuedTo=" + issuedTo + ", paths=" + paths + ", toString()="
        + super.toString() + "]";
  }

  // -------------------------------
  // BUILDER
  // -------------------------------
  public static class Builder extends FlowDomainObject.Builder<Share, Builder> {

    public Builder setIssuedTo(String o) {
      base.issuedTo = o;
      return this;
    }

    public Builder setPaths(Map<String, Permissions> o) {
      base.paths = o;
      return this;
    }

    public Builder setDuration(Long o) {
      base.duration = o;
      return this;
    }

    public Builder setDescription(String d) {
      base.description = d;
      return this;
    }

    public Builder addPath(String p, Permissions perms) {
      if (base.paths == null) base.paths = new HashMap<>();
      base.paths.put(p, perms);
      return this;
    }
  }
}
