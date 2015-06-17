package com.flowthings.client.domain;

import com.flowthings.client.domain.elements.DropId;
import com.flowthings.client.domain.elements.Location;

public class Drop extends FlowDomainObject {
  protected DropElementsMap elems = new DropElementsMap();
  protected String path;
  protected Integer version = 0;
  protected DropId parentDropId;
  protected String fhash;
  protected Location location;
  protected String flowId;

  public String getFlowId() {
    return flowId;
  }

  public void setFlowId(String flowId) {
    this.flowId = flowId;
  }

  public DropElementsMap getElems() {
    return elems;
  }

  public void setElems(DropElementsMap elems) {
    this.elems = elems;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public DropId getParentDropId() {
    return parentDropId;
  }

  public void setParentDropId(DropId parentDropId) {
    this.parentDropId = parentDropId;
  }

  public String getFhash() {
    return fhash;
  }

  public void setFhash(String fhash) {
    this.fhash = fhash;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  @Override
  public String toString() {
    return "Drop [elems=" + elems + ", flowId=" + flowId + ", toString()=" + super.toString() + "]";
  }

  public static class Builder extends FlowDomainObject.Builder<Drop, Builder> {
    public Builder setPath(String path) {
      base.setPath(path);
      return this;
    }

    public Builder setFhash(String fhash) {
      base.setFhash(fhash);
      return this;
    }

    public Builder setPath(Location location) {
      base.setLocation(location);
      return this;
    }

    public Builder setElems(DropElementsMap elems) {
      base.setElems(elems);
      return this;
    }

    public Builder addElem(String key, Object value) {
      base.elems.put(key, value);
      return this;
    }
  }
}