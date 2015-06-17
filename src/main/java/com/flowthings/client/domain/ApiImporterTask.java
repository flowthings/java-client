package com.flowthings.client.domain;

public class ApiImporterTask extends TriggeredTask {
  public String js;

  public String getJs() {
    return js;
  }

  public void setJs(String js) {
    this.js = js;
  }

  // -------------------------------
  // BUILDER
  // -------------------------------
  public static class Builder extends Task.Builder<ApiImporterTask> {
    public Builder setJs(String o) {
      base.js = o;
      return this;
    }
  }
}
