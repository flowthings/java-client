package com.flowthings.client.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Device extends FlowDomainObject {
  protected String displayName;
  protected String path;
  protected String tokenId;
  protected HashMap<String, Object> deviceSummary;
  protected String status;
  protected Date lastSeen;
  protected Long reportPeriodicity;
  protected HashMap<String, Map<String, Object>> tracks;

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getTokenId() {
    return tokenId;
  }

  public void setTokenId(String tokenId) {
    this.tokenId = tokenId;
  }

  public HashMap<String, Object> getDeviceSummary() {
    return deviceSummary;
  }

  public void setDeviceSummary(HashMap<String, Object> deviceSummary) {
    this.deviceSummary = deviceSummary;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Date getLastSeen() {
    return lastSeen;
  }

  public void setLastSeen(Date lastSeen) {
    this.lastSeen = lastSeen;
  }

  public Long getReportPeriodicity() {
    return reportPeriodicity;
  }

  public void setReportPeriodicity(Long reportPeriodicity) {
    this.reportPeriodicity = reportPeriodicity;
  }

  public HashMap<String, Map<String, Object>> getTracks() {
    return tracks;
  }

  public void setTracks(HashMap<String, Map<String, Object>> tracks) {
    this.tracks = tracks;
  }

  @Override
  public String toString() {
    return "Device [displayName=" + displayName + ", path=" + path + ", status=" + status
        + ", tracks=[" + getTracks().keySet() + "], toString()="
        + super.toString() + "]";
  }

  public static class Builder extends FlowDomainObject.Builder<Device, Builder> {
    public Builder setDisplayName(String o) {
      base.displayName = o;
      return this;
    }

    public Builder setPath(String o) {
      base.path = o;
      return this;
    }

    public Builder setTokenId(String o) {
      base.tokenId = o;
      return this;
    }

    public Builder setReportPeriodicity(Long o) {
      base.reportPeriodicity = o;
      return this;
    }
  }
}
