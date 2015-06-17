package com.flowthings.client.domain;

public abstract class Task extends FlowDomainObject {
  public enum Status {
    OK, ERROR, TIMEOUT, NO_NEW_DATA
  }

  protected Integer periodicity;
  protected String description;
  protected String displayName;
  protected String reportFlowId;
  protected String status;
  protected String reportPath;
  protected Boolean disabled;

  public Integer getPeriodicity() {
    return periodicity;
  }

  public void setPeriodicity(Integer periodicity) {
    this.periodicity = periodicity;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getReportFlowId() {
    return reportFlowId;
  }

  public void setReportFlowId(String reportFlowId) {
    this.reportFlowId = reportFlowId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getReportPath() {
    return reportPath;
  }

  public void setReportPath(String reportPath) {
    this.reportPath = reportPath;
  }

  public Boolean getDisabled() {
    return disabled;
  }

  public void setDisabled(Boolean disabled) {
    this.disabled = disabled;
  }

  public boolean isDisabled() {
    return disabled == null ? false : disabled;
  }

  public abstract String getDestination();

  public abstract void setDestination(String dest);

  @Override
  public String toString() {
    return "Task [periodicity=" + periodicity + ", description=" + description + ", displayName=" + displayName
        + ", reportFlowId=" + reportFlowId + ", status=" + status + ", reportPath=" + reportPath + ", disabled="
        + disabled + ", id=" + id + "]";
  }

  public static class Builder<TASK_TYPE extends Task> extends FlowDomainObject.Builder<TASK_TYPE, Builder<TASK_TYPE>> {

    public Builder<TASK_TYPE> setDisplayName(String displayName) {
      base.setDisplayName(displayName);
      return this;
    }

    public Builder<TASK_TYPE> setDescription(String description) {
      base.setDescription(description);
      return this;
    }

    public Builder<TASK_TYPE> setPeriodicity(int i) {
      base.setPeriodicity(i);
      return this;
    }

    public Builder<TASK_TYPE> setDestination(String destination) {
      base.setDestination(destination);
      return this;
    }
  }
}
