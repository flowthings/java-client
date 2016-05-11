package com.flowthings.client.domain;

public class LocalTrack extends FlowDomainObject {
  protected String source;
  protected String description;
  protected String js;
  protected Boolean disabled;
  protected String deviceId;

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getJs() {
    return js;
  }

  public void setJs(String js) {
    this.js = js;
  }

  public Boolean getDisabled() {
    return disabled;
  }

  public void setDisabled(Boolean disabled) {
    this.disabled = disabled;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public static class Builder extends FlowDomainObject.Builder<LocalTrack, Builder> {
    public Builder setSource(String source) {
      base.setSource(source);
      return this;
    }

    public Builder setDescription(String description) {
      base.setDescription(description);
      return this;
    }

    public Builder setJs(String js) {
      base.setJs(js);
      return this;
    }

    public Builder setDisabled(Boolean disabled) {
      base.setDisabled(disabled);
      return this;
    }

    public Builder setDeviceId(String deviceId) {
      base.setDeviceId(deviceId);
      return this;
    }
  }
}
