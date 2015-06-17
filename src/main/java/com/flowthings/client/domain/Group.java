package com.flowthings.client.domain;

import java.util.HashSet;

public class Group extends FlowDomainObject {
  protected String displayName;
  protected String description;
  protected HashSet<String> memberIds;

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public HashSet<String> getMemberIds() {
    return memberIds;
  }

  public void setMemberIds(HashSet<String> memberIds) {
    this.memberIds = memberIds;
  }

  @Override
  public String toString() {
    return "Group [displayName=" + displayName + ", memberIds=" + memberIds + ", toString()=" + super.toString() + "]";
  }

  public static class Builder extends FlowDomainObject.Builder<Group, Builder> {
    public Builder setDisplayName(String displayName) {
      base.setDisplayName(displayName);
      return this;
    }

    public Builder setDescription(String description) {
      base.setDescription(description);
      return this;
    }

    public Builder setMemberIds(HashSet<String> memberIds) {
      base.setMemberIds(memberIds);
      return this;
    }

  }
}
