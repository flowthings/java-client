package com.flowthings.client.domain;

public class Flow extends FlowDomainObject {
  protected String name;
  protected String description;
  protected String path;
  protected String filter;
  protected Integer capacity;
  protected Boolean publicFlow;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }

  public Integer getCapacity() {
    return capacity;
  }

  public void setCapacity(Integer capacity) {
    this.capacity = capacity;
  }

  public Boolean getPublic() {
    return publicFlow;
  }

  public void setPublic(Boolean publicFlow) {
    this.publicFlow = publicFlow;
  }

  @Override
  public String toString() {
    return "Flow [path=" + path + ", toString()=" + super.toString() + "]";
  }

  public static class Builder extends FlowDomainObject.Builder<Flow, Builder> {
    public Builder setName(String name) {
      base.setName(name);
      return this;
    }

    public Builder setPath(String s) {
      base.setPath(s);
      return this;
    }

    public Builder setDescription(String description) {
      base.setDescription(description);
      return this;
    }

    public Builder setFilter(String filterConditions) {
      base.setFilter(filterConditions);
      return this;
    }

    public Builder setPublic(Boolean publicFlow) {
      base.setPublic(publicFlow);
      return this;
    }

    public Builder setLocation(Integer capacity) {
      base.setCapacity(capacity);
      return this;
    }

  }
}
