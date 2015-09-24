package com.flowthings.client.domain;

public class MqttConnection extends Task {
  /** Correspond to Mqtt QOS levels. */
  public static final int QOS_AT_MOST_ONCE = 0;
  public static final int QOS_AT_LEAST_ONCE = 1;
  public static final int QOS_EXACTLY_ONCE = 2;

  protected String uri;
  protected Integer reportFrequency;
  protected String topic;
  protected String username;
  protected String password;
  protected String destination;
  protected String clientId;
  protected String source;
  protected String version;
  protected Integer qos;
  protected String outputFormat;
  protected String inputFormat;

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public Integer getReportFrequency() {
    return reportFrequency;
  }

  public void setReportFrequency(Integer reportFrequency) {
    this.reportFrequency = reportFrequency;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String getDestination() {
    return destination;
  }

  @Override
  public void setDestination(String destination) {
    this.destination = destination;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public Integer getQos() {
    return qos;
  }

  public void setQos(Integer qos) {
    this.qos = qos;
  }

  public String getOutputFormat() {
    return outputFormat;
  }

  public void setOutputFormat(String outputFormat) {
    this.outputFormat = outputFormat;
  }

  public String getInputFormat() {
    return inputFormat;
  }

  public void setInputFormat(String inputFormat) {
    this.inputFormat = inputFormat;
  }

  @Override
  public String toString() {
    return "MqttConnection [uri=" + uri + ", source=" + source + ", toString()=" + super.toString() + "]";
  }

  public static class Builder extends Task.Builder<MqttConnection> {

    public Builder setSource(String source) {
      base.setSource(source);
      return this;
    }

    public Builder setReportFrequency(Integer reportFrequency) {
      base.setReportFrequency(reportFrequency);
      return this;
    }
  }

}
