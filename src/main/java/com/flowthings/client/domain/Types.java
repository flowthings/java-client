package com.flowthings.client.domain;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.flowthings.client.response.ListResponse;
import com.flowthings.client.response.ObjectResponse;
import com.google.gson.reflect.TypeToken;

public enum Types {
  API_IMPORTER_TASK(ApiImporterTask.class, "api-task", false, new TypeToken<ObjectResponse<ApiImporterTask>>() {
  }), DEVICE(Device.class, "device", false, new TypeToken<ObjectResponse<Device>>() {
  }), DROP(Drop.class, "drop", false, new TypeToken<ObjectResponse<Drop>>() {
  }), FLOW(Flow.class, "flow", false, new TypeToken<ObjectResponse<Flow>>() {
  }), GROUP(Group.class, "group", false, new TypeToken<ObjectResponse<Group>>() {
  }), IDENTITY(Identity.class, "identity", false, new TypeToken<ObjectResponse<Identity>>() {
  }), MQTT_CONNECTION(MqttConnection.class, "mqtt", false, new TypeToken<ObjectResponse<MqttConnection>>() {
  }), RSS_TASK(RssTask.class, "rss", false, new TypeToken<ObjectResponse<RssTask>>() {
  }), SHARE(Share.class, "share", false, new TypeToken<ObjectResponse<Share>>() {
  }), TOKEN(Token.class, "token", false, new TypeToken<ObjectResponse<Token>>() {
  }), TRACK(Track.class, "track", false, new TypeToken<ObjectResponse<Track>>() {
  }), LOCAL_TRACK(LocalTrack.class, "local-track", false, new TypeToken<ObjectResponse<LocalTrack>>() {
  }),
  //
  API_IMPORTER_TASKS(ApiImporterTask.class, "api-task", true, new TypeToken<ListResponse<ApiImporterTask>>() {
  }), DEVICES(Device.class, "device", true, new TypeToken<ListResponse<Device>>() {
  }), DROPS(Drop.class, "drop", true, new TypeToken<ListResponse<Drop>>() {
  }), FLOWS(Flow.class, "flow", true, new TypeToken<ListResponse<Flow>>() {
  }), GROUPS(Group.class, "group", true, new TypeToken<ListResponse<Group>>() {
  }), IDENTITIES(Identity.class, "identity", true, new TypeToken<ListResponse<Identity>>() {
  }), MQTT_CONNECTIONS(MqttConnection.class, "mqtt", true, new TypeToken<ListResponse<MqttConnection>>() {
  }), RSS_TASKS(RssTask.class, "rss", true, new TypeToken<ListResponse<RssTask>>() {
  }), SHARES(Share.class, "share", true, new TypeToken<ListResponse<Share>>() {
  }), TOKENS(Token.class, "token", true, new TypeToken<ListResponse<Token>>() {
  }), TRACKS(Track.class, "track", true, new TypeToken<ListResponse<Track>>() {
  }), LOCAL_TRACKS(LocalTrack.class, "local-track", true, new TypeToken<ListResponse<LocalTrack>>() {
  });

  public final Class<? extends FlowDomainObject> klazz;
  public final Type tokenType;
  public final String name;
  private final TreeMap<String, TypeToken> fieldTokens = new TreeMap<>();
  private boolean listType;
  public TypeToken token;
  private static HashMap<Class, Types> fromClassMap = new HashMap<>();
  private static TreeMap<String, Types> fromNameMap = new TreeMap<>();
  private static Map<Class, Types> fromListClassMap = new HashMap<>();
  private static Map<String, Types> fromListNameMap = new TreeMap<>();
  static {
    for (Types t : Types.class.getEnumConstants()) {
      try {
        if (t.listType) {
          fromListClassMap.put(t.klazz, t);
          fromListNameMap.put(t.name, t);
        } else {
          fromClassMap.put(t.klazz, t);
          fromNameMap.put(t.name, t);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void addFieldTokens(Class c) {
    while (FlowDomainObject.class.isAssignableFrom(c)) {
      for (Field f : c.getDeclaredFields()) {
        fieldTokens.put(f.getName(), TypeToken.get(f.getGenericType()));
      }
      c = c.getSuperclass();
    }
  }

  private Types(Class<? extends FlowDomainObject> klazz, String name, boolean listType, TypeToken token) {
    this.klazz = klazz;
    this.name = name;
    this.listType = listType;
    this.tokenType = token.getType();
    this.token = token;
    addFieldTokens(klazz);
  }

  public TypeToken getFieldTypeToken(String fieldName) {
    return fieldTokens.get(fieldName);
  }

  public Map<String, TypeToken> getTokenMap() {
    return fieldTokens;
  }

  public static Types get(Class klazz) {
    return fromClassMap.get(klazz);
  }

  public static Types getListType(Class klazz) {
    return fromListClassMap.get(klazz);
  }

  public static Types fromName(String name) {
    return fromNameMap.get(name);
  }

  public static Types fromListName(String name) {
    return fromListNameMap.get(name);
  }

  public boolean isListType(){
    return listType;
  }
}
