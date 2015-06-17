package com.flowthings.client.domain.elements;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Media implements MapLike {

  private static enum MEDIA_TYPE {
    VIDEO, LINK, RICH, PHOTO
  }

  protected Map<String, Object> fields = new HashMap<>();

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
  public String getType() {
    return "media";
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
    Media other = (Media) obj;
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
    return "Media [fields=" + fields + "]";
  }

  public static class Builder {
    protected Media base;

    public Builder() {
      base = new Media();
    }

    public Builder setThumbnailHeight(Integer thumbnailHeight) {
      base.put("thumbnailHeight", thumbnailHeight);
      return this;
    }

    public Builder setThumbnailWidth(Integer thumbnailWidth) {
      base.put("thumbnailWidth", thumbnailWidth);
      return this;
    }

    public Builder setThumbnailUrl(String thumbnailUrl) {
      base.put("thumbnailUrl", thumbnailUrl);
      return this;
    }

    public Builder setCacheAge(String cacheAge) {
      base.put("cacheAge", cacheAge);
      return this;
    }

    public Builder setHtml(String html) {
      base.put("html", html);
      return this;
    }

    public Builder setAuthorUrl(String authorUrl) {
      base.put("authorUrl", authorUrl);
      return this;
    }

    public Builder setAuthorName(String authorName) {
      base.put("authorName", authorName);
      return this;
    }

    public Builder setHeight(Integer height) {
      base.put("height", height);
      return this;
    }

    public Builder setWidth(Integer width) {
      base.put("width", width);
      return this;
    }

    public Builder setProviderUrl(String providerUrl) {
      base.put("provider_url", providerUrl);
      return this;
    }

    public Builder setProviderName(String providerName) {
      base.put("provider_name", providerName);
      return this;
    }

    public Builder setMimeType(String mimeType) {
      base.put("mime_type", mimeType);
      return this;
    }

    public Builder setMediaType(MEDIA_TYPE mediaType) {
      base.put("media_type", mediaType == null ? null : mediaType.name().toLowerCase());
      return this;
    }

    public Builder setVersion(String version) {
      base.put("version", version);
      return this;
    }

    public Builder setDescription(String description) {
      base.put("description", description);
      return this;
    }

    public Builder setUrl(String url) {
      base.put("url", url);
      return this;
    }

    public Builder setDisplayName(String displayName) {
      base.put("displayName", displayName);
      return this;
    }
  }

}
