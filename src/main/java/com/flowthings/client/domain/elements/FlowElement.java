package com.flowthings.client.domain.elements;

import java.io.Serializable;

/** Wrapper around a piece of data that incorporates type information. */
public abstract class FlowElement<BASE_DATA_TYPE> implements Serializable {
  private static final long serialVersionUID = 592869494356230962L;
  protected BASE_DATA_TYPE data;

  public BASE_DATA_TYPE get() {
    return data;
  }

  public void put(BASE_DATA_TYPE t) {
    this.data = t;
  }

  @Override
  public String toString() {
    return data == null ? "null" : data.toString();
  }

  @Override
  public int hashCode() {
    return data == null ? super.hashCode() : data.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    return other != null && other.getClass().equals(getClass()) && data.equals(((FlowElement<?>) other).data);
  }
}
