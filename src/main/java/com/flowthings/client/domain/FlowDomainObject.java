package com.flowthings.client.domain;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;

public abstract class FlowDomainObject implements Serializable {
  private static final long serialVersionUID = -1;
  protected String id;
  protected Date creationDate;
  protected String creatorId;
  protected Date lastEditDate;
  protected String lastEditorId;

  public FlowDomainObject() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public String getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(String creatorId) {
    this.creatorId = creatorId;
  }

  public Date getLastEditDate() {
    return lastEditDate;
  }

  public void setLastEditDate(Date lastEditDate) {
    this.lastEditDate = lastEditDate;
  }

  public String getLastEditorId() {
    return lastEditorId;
  }

  public void setLastEditorId(String lastEditorId) {
    this.lastEditorId = lastEditorId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    FlowDomainObject other = (FlowDomainObject) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "FlowDomainObject{" +
        "id='" + id + '\'' +
        ", creationDate=" + creationDate +
        ", lastEditDate=" + lastEditDate +
        '}';
  }

  @SuppressWarnings("unchecked")
  public abstract static class Builder<T extends FlowDomainObject, B extends Builder<T, B>> {
    protected T base;

    public Builder() {
      Type t = getClass().getGenericSuperclass();
      ParameterizedType pt = (ParameterizedType) t;
      Class<T> c = (Class<T>) pt.getActualTypeArguments()[0];
      try {
        base = c.newInstance();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    public B setId(String id) {
      base.setId(id);
      return (B) this;
    }

    public B setCreatorId(String creatorId) {
      base.setCreatorId(creatorId);
      return (B) this;
    }

    public T get() {
      return base;
    }
  }
}
