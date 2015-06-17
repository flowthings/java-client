package com.flowthings.client.domain;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class Token extends FlowDomainObject {
  private static final SecureRandom random = new SecureRandom();
  public String description;
  public String issuedBy;
  public Map<String, TokenPermissions> paths;
  public Long duration;
  public String tokenString;
  public Long expiresInMs;

  public static boolean isValid(String s) {
    return s != null && s.length() == 16;
  }

  public boolean isMaster() {
    return false;
  }

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

  public Map<String, TokenPermissions> getPaths() {
    return paths;
  }

  public void setPaths(Map<String, TokenPermissions> paths) {
    this.paths = paths;
  }

  public Long getDuration() {
    return duration;
  }

  public void setDuration(Long duration) {
    this.duration = duration;
  }

  public String getTokenString() {
    return tokenString;
  }

  public void setTokenString(String tokenString) {
    this.tokenString = tokenString;
  }

  public Long getExpiresInMs() {
    return expiresInMs;
  }

  public void setExpiresInMs(Long expiresInMs) {
    this.expiresInMs = expiresInMs;
  }

  public static SecureRandom getRandom() {
    return random;
  }

  @Override
  public String toString() {
    return "Token [issuedBy=" + issuedBy + ", paths=" + paths + ", toString()=" + super.toString() + "]";
  }

  // -------------------------------
  // BUILDER
  // -------------------------------
  public static class Builder extends FlowDomainObject.Builder<Token, Builder> {

    public Builder setPaths(HashMap<String, TokenPermissions> o) {
      base.paths = o;
      return this;
    }

    public Builder setDuration(Long o) {
      base.duration = o;
      return this;
    }

    public Builder setDescription(String o) {
      base.description = o;
      return this;
    }

    public Builder addPath(String p, TokenPermissions perms) {
      if (base.paths == null) base.paths = new HashMap<>();
      base.paths.put(p, perms);
      return this;
    }
  }
}
