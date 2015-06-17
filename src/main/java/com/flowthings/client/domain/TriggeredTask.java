package com.flowthings.client.domain;

import java.util.Date;

/**
 * Represents a task that will be triggered by the platform such as @link
 * {ApiImporterTask}. Mqtt Tasks are not triggered by the platform.
 */
public abstract class TriggeredTask extends Task {
  public Integer runCt;
  public Date lastRun;
  public Date nextRun;
  public String destination;

  public int getRunCt() {
    return runCt == null ? 0 : runCt;
  }

  public void setRunCt(int runCt) {
    this.runCt = runCt;
  }

  public Date getLastRun() {
    return lastRun;
  }

  public void setLastRun(Date lastRun) {
    this.lastRun = lastRun;
  }

  public Date getNextRun() {
    return nextRun;
  }

  public void setNextRun(Date nextRun) {
    this.nextRun = nextRun;
  }

  @Override
  public String getDestination() {
    return destination;
  }

  @Override
  public void setDestination(String dest) {
    this.destination = dest;
  }
}
