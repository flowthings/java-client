package com.flowthings.client.api;

import com.flowthings.client.domain.ApiImporterTask;
import com.flowthings.client.domain.Device;
import com.flowthings.client.domain.Flow;
import com.flowthings.client.domain.Group;
import com.flowthings.client.domain.Identity;
import com.flowthings.client.domain.LocalTrack;
import com.flowthings.client.domain.MqttConnection;
import com.flowthings.client.domain.RssTask;
import com.flowthings.client.domain.Share;
import com.flowthings.client.domain.Token;
import com.flowthings.client.domain.Track;

/**
 * Entry point for creating requests to flowthings.io
 *
 * @author matt
 */
public class Flowthings {
  private static MutableDomainObjectApi<ApiImporterTask> apiImporter = new MutableDomainObjectApi<>(
      ApiImporterTask.class);
  private static MutableDomainObjectApi<Device> device = new MutableDomainObjectApi<>(Device.class);
  private static MutableDomainObjectApi<Flow> flow = new MutableDomainObjectApi<>(Flow.class);
  private static MutableDomainObjectApi<Group> group = new MutableDomainObjectApi<>(Group.class);
  private static MutableDomainObjectApi<Identity> identity = new MutableDomainObjectApi<>(Identity.class);
  private static MutableDomainObjectApi<MqttConnection> mqttConnection = new MutableDomainObjectApi<>(
      MqttConnection.class);
  private static MutableDomainObjectApi<RssTask> rssTask = new MutableDomainObjectApi<>(RssTask.class);
  private static DomainObjectApi<Share> share = new DomainObjectApi<>(Share.class);
  private static DomainObjectApi<Token> token = new DomainObjectApi<>(Token.class);
  private static MutableDomainObjectApi<Track> track = new MutableDomainObjectApi<>(Track.class);
  private static MutableDomainObjectApi<LocalTrack> localTrack = new MutableDomainObjectApi<>(LocalTrack.class);

  @Deprecated
  public static MutableDomainObjectApi<ApiImporterTask> apiImporter() {
    return apiImporter;
  }

  public static MutableDomainObjectApi<Device> device() {
    return device;
  }

  public static MutableDomainObjectApi<Group> group() {
    return group;
  }

  public static MutableDomainObjectApi<Identity> identity() {
    return identity;
  }

  public static MutableDomainObjectApi<MqttConnection> mqttConnection() {
    return mqttConnection;
  }

  @Deprecated
  public static MutableDomainObjectApi<RssTask> rssTask() {
    return rssTask;
  }

  public static DomainObjectApi<Share> share() {
    return share;
  }

  public static DropApi drop(String flowId) {
    if (flowId == null) {
      throw new NullPointerException("FlowId must not be null");
    }
    return new DropApi(flowId);
  }

  public static DropFromPathApi dropFromPath(String path) {
    if (path == null) {
      throw new NullPointerException("Path must not be null");
    }
    return new DropFromPathApi(path);
  }

  public static MutableDomainObjectApi<Flow> flow() {
    return flow;
  }

  public static DomainObjectApi<Token> token() {
    return token;
  }

  public static MutableDomainObjectApi<Track> track() {
    return track;
  }

  public static MutableDomainObjectApi<LocalTrack> localTrack() {
    return localTrack;
  }
}
