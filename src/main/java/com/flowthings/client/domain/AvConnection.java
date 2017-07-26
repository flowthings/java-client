package com.flowthings.client.domain;

public class AvConnection extends FlowDomainObject {
  protected String company;
  protected String description;
  protected String destination;
  protected String displayName;
  protected Integer periodicity;
  protected Integer reportFrequency;
  protected String status;
  protected Boolean disabled;

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public Integer getPeriodicity() {
    return periodicity;
  }

  public void setPeriodicity(Integer periodicity) {
    this.periodicity = periodicity;
  }

  public Integer getReportFrequency() {
    return reportFrequency;
  }

  public void setReportFrequency(Integer reportFrequency) {
    this.reportFrequency = reportFrequency;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Boolean getDisabled() {
    return disabled;
  }

  public void setDisabled(Boolean disabled) {
    this.disabled = disabled;
  }

  @Override
  public String toString() {
    return "AvConnection [company=" + company + ", toString()=" + super.toString() + "]";
  }

  public static class Builder extends FlowDomainObject.Builder<AvConnection, Builder> {
    public Builder setCompany(String s) {
      base.setCompany(s);
      return this;
    }

    public Builder setDescription(String s) {
      base.setDescription(s);
      return this;
    }

    public Builder setDestination(String s) {
      base.setDestination(s);
      return this;
    }

    public Builder setDisplayName(String s) {
      base.setDisplayName(s);
      return this;
    }

    public Builder setPeriodicity(Integer s) {
      base.setPeriodicity(s);
      return this;
    }

    public Builder setReportFrequency(Integer s) {
      base.setReportFrequency(s);
      return this;
    }

    public Builder setStatus(String s) {
      base.setStatus(s);
      return this;
    }

    public Builder setDisabled(Boolean s) {
      base.setDisabled(s);
      return this;
    }
  }
}
