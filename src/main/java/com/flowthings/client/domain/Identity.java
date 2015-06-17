package com.flowthings.client.domain;

import java.util.Map;
import java.util.Set;

public class Identity extends FlowDomainObject {
  protected String salutation;
  protected String firstName;
  protected String lastName;
  protected String middleName;
  protected String name;
  protected String email;
  protected String icon;
  protected Set<String> groupIds;
  protected Map<String, Share> shares;
  protected String masterToken;

  public String getSalutation() {
    return salutation;
  }

  public void setSalutation(String salutation) {
    this.salutation = salutation;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public Set<String> getGroupIds() {
    return groupIds;
  }

  public void setGroupIds(Set<String> groupIds) {
    this.groupIds = groupIds;
  }

  public Map<String, Share> getShares() {
    return shares;
  }

  public void setShares(Map<String, Share> shares) {
    this.shares = shares;
  }

  public String getMasterToken() {
    return masterToken;
  }

  public void setMasterToken(String masterToken) {
    this.masterToken = masterToken;
  }

  @Override
  public String toString() {
    return "Identity [name=" + name + ", toString()=" + super.toString() + "]";
  }

  public static class Builder extends FlowDomainObject.Builder<Identity, Builder> {
    public Builder setFirstName(String s) {
      base.setFirstName(s);
      return this;
    }

    public Builder setLastName(String s) {
      base.setLastName(s);
      return this;
    }

    public Builder setSalutation(String s) {
      base.setSalutation(s);
      return this;
    }

    public Builder setMiddleName(String s) {
      base.setMiddleName(s);
      return this;
    }

    public Builder setName(String s) {
      base.setName(s);
      return this;
    }

    public Builder setIcon(String icon) {
      base.setIcon(icon);
      return this;
    }
  }
}
