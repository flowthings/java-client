package com.flowthings.client.domain;

public class Track extends FlowDomainObject {
  protected String source;
  protected String destination;
  protected String description;
  protected String filter;
  protected String js;
  protected Boolean disabled;

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
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

  @Override
  public String toString() {
    return "Track [source=" + source + ", destination=" + destination + ", toString()=" + super.toString() + "]";
  }

  public static class Builder extends FlowDomainObject.Builder<Track, Builder> {
    public Builder setDescription(String description) {
      base.setDescription(description);
      return this;
    }

    public Builder setSource(String sourcePath) {
      base.setSource(sourcePath);
      return this;
    }

    public Builder setDestination(String destinationPath) {
      base.setDestination(destinationPath);
      return this;
    }

    public Builder setFilter(String filterString) {
      base.setFilter(filterString);
      return this;
    }

    public Builder setJs(String js) {
      base.setJs(js);
      return this;
    }
  }
}
