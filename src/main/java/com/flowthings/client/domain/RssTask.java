package com.flowthings.client.domain;

public class RssTask extends TriggeredTask {
  public String url;
  public String favicon;
  public String icon;
  public Boolean isFeatured;
  public String category;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getFavicon() {
    return favicon;
  }

  public void setFavicon(String favicon) {
    this.favicon = favicon;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public Boolean getIsFeatured() {
    return isFeatured;
  }

  public void setIsFeatured(Boolean isFeatured) {
    this.isFeatured = isFeatured;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  @Override
  public String toString() {
    return "RssTask [url=" + url + ", toString()=" + super.toString() + "]";
  }

  // -------------------------------
  // BUILDER
  // -------------------------------
  public static class Builder extends Task.Builder<RssTask> {
    public Builder setCategory(String o) {
      base.category = o;
      return this;
    }

    public Builder setFavicon(String o) {
      base.icon = o;
      return this;
    }

    public Builder setIcon(String o) {
      base.icon = o;
      return this;
    }

    public Builder setUrl(String o) {
      base.url = o;
      return this;
    }
  }
}
