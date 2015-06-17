package com.flowthings.client.domain.elements;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * An element that consists of a string payload and a label that desribes it.
 * Most of our custom types will be represented by a labelled string.
 */
public class Text implements MapLike {

  public static enum TYPE {

    TEXT_HTML("text/html"), HTML("html"), MARKDOWN("markdown"), TEXTILE("textile"), TEXT("text");

    private final String val;

    private TYPE(String val) {
      this.val = val;
    }

    public String getVal() {
      return val;
    }
  }

  protected Map<String, Object> fields = new HashMap<>();

  @Override
  public String getType() {
    return "text";
  }

  @Override
  public void put(String key, Object value) {
    fields.put(key, value);
  }

  @Override
  public Object get(String key) {
    return fields.get(key);
  }

  @Override
  public Iterator<String> iterator() {
    return fields.keySet().iterator();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fields == null) ? 0 : fields.hashCode());
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
    Text other = (Text) obj;
    if (fields == null) {
      if (other.fields != null) {
        return false;
      }
    } else if (!fields.equals(other.fields)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Text [fields=" + fields + "]";
  }

  public static class Builder {
    protected Text base;

    public Builder() {
      base = new Text();
    }

    public Builder setValue(String value) {
      base.put("value", value);
      return this;
    }

    public Builder setSafe(Boolean safe) {
      base.put("safe", safe);
      return this;
    }

    public Builder setType(TYPE type) {
      base.put("type", type == null ? null : type.getVal());
      return this;
    }

  }

}
