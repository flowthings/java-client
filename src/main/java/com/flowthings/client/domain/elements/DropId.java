package com.flowthings.client.domain.elements;

import java.io.Serializable;

public class DropId implements Serializable {
  private String flowId;
  private String dropId;
  private static final long serialVersionUID = 358388496061380755L;

  public DropId(String flowId, String dropId) {
    this.flowId = flowId;
    this.dropId = dropId;
  }

  public String getFlowId() {
    return flowId;
  }

  public void setFlowId(String flowId) {
    this.flowId = flowId;
  }

  public String getDropId() {
    return dropId;
  }

  public void setDropId(String dropId) {
    this.dropId = dropId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((dropId == null) ? 0 : dropId.hashCode());
    result = prime * result + ((flowId == null) ? 0 : flowId.hashCode());
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
    DropId other = (DropId) obj;
    if (dropId == null) {
      if (other.dropId != null) {
        return false;
      }
    } else if (!dropId.equals(other.dropId)) {
      return false;
    }
    if (flowId == null) {
      if (other.flowId != null) {
        return false;
      }
    } else if (!flowId.equals(other.flowId)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "DropId [flowId=" + flowId + ", dropId=" + dropId + "]";
  }

}
