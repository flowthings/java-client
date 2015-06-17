package com.flowthings.client;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.flowthings.client.domain.DropElementsMap;
import com.flowthings.client.domain.FlowDomainObject;
import com.flowthings.client.domain.Permissions;
import com.flowthings.client.domain.TokenPermissions;
import com.flowthings.client.domain.Types;
import com.flowthings.client.response.ListResponse;
import com.flowthings.client.response.MapResponse;
import com.flowthings.client.response.ObjectResponse;
import com.flowthings.client.response.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

public class Serializer {
  protected static Logger logger = Logger.getLogger("com.flow.client.Serializer");
  public static Gson gson = createGsonSerializer();
  private static TypeToken<ParseToken> innerParseToken = new TypeToken<ParseToken>() {
  };

  /** Helper class for returning serialized portions from gson. */
  public static class ParseToken {
    public String value;
    public MapResponse.Head head;
  }

  public static String toJson(Object o) {
    return o == null ? "" : gson.toJson(o, o.getClass());
  }

  public static String toJson(String key, Object o) {
    return o == null ? "" : String.format("{\"%s\":%s}", key, gson.toJson(o, o.getClass()));
  }

  public static <T extends FlowDomainObject> ObjectResponse<T> fromJsonResponse(String s, Class<T> klazz) {
    logger.log(Level.INFO, "Parsing type: " + klazz);
    logger.log(Level.INFO, s);
    return gson.fromJson(s, Types.get(klazz).tokenType);
  }

  public static MapResponse fromJsonMapResponse(String json, String key, Type t) {
    logger.log(Level.INFO, json);
    ParseToken p = gson.fromJson(json, innerParseToken.getType());
    Object inner = gson.fromJson(p.value, t);
    return new MapResponse(p.head, key, inner);
  }

  public static <T> T fromJson(String s, Class<T> klazz) {
    return gson.fromJson(s, klazz);
  }

  public static <T> T fromJson(String s, TypeToken<T> t) {
    return gson.fromJson(s, t.getType());
  }

  @SuppressWarnings("rawtypes")
  private static Gson createGsonSerializer() {
    GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
    builder.registerTypeAdapter(ListResponse.ERROR.class, new JsonDeserializer<ListResponse.ERROR>() {
      @SuppressWarnings("rawtypes")
      @Override
      public ListResponse.ERROR deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
          throws JsonParseException {
        ListResponse.ERROR r = new ListResponse.ERROR();
        JsonObject jo = (JsonObject) json;
        JsonElement head = jo.remove("head");
        r.setHead((Response.Head) context.deserialize(head, Response.Head.class));
        return r;
      }
    });
    builder.registerTypeAdapter(ObjectResponse.ERROR.class, new JsonDeserializer<ObjectResponse.ERROR>() {
      @SuppressWarnings("rawtypes")
      @Override
      public ObjectResponse.ERROR deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
          throws JsonParseException {
        ObjectResponse.ERROR r = new ObjectResponse.ERROR();
        JsonObject jo = (JsonObject) json;
        JsonElement head = jo.remove("head");
        r.setHead((Response.Head) context.deserialize(head, Response.Head.class));
        return r;
      }
    });
    /* Type adapter for key-value put response. */
    builder.registerTypeAdapter(ParseToken.class, new JsonDeserializer<ParseToken>() {
      @Override
      public ParseToken deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
          throws JsonParseException {
        ParseToken r = new ParseToken();
        JsonObject jo = (JsonObject) json;
        JsonElement head = jo.remove("head");
        r.head = context.deserialize(head, MapResponse.Head.class);
        JsonElement je = jo.get("body");
        if (je instanceof JsonObject) {
          JsonObject body = (JsonObject) je;
          Iterator<Map.Entry<String, JsonElement>> iter = body.entrySet().iterator();
          if (iter.hasNext()) {
            Map.Entry<String, JsonElement> first = iter.next();
            r.value = (first.getValue().toString());
          }
        }
        return r;
      }
    });
    builder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
      @Override
      public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getTime());
      }
    });
    builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
      @Override
      public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
          throws JsonParseException {
        Date d = new Date();
        d.setTime(json.getAsLong());
        return d;
      }
    });
    builder.registerTypeAdapter(Permissions.class, new JsonDeserializer<Permissions>() {
      @Override
      public Permissions deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
          throws JsonParseException {
        try {
          JsonObject map = (JsonObject) json;
          JsonElement readObj = map.get("read");
          JsonElement writeObj = map.get("write");
          JsonElement dropReadObj = map.get("dropRead");
          JsonElement dropWriteObj = map.get("dropWrite");

          boolean read = readObj == null ? false : readObj.getAsBoolean();
          boolean write = writeObj == null ? false : writeObj.getAsBoolean();
          boolean dropRead = dropReadObj == null ? false : dropReadObj.getAsBoolean();
          boolean dropWrite = dropWriteObj == null ? false : dropWriteObj.getAsBoolean();

          return new Permissions(read, write, dropRead, dropWrite);
        } catch (Exception e) {
          logger.log(Level.FINE, "Couldn't deserialize " + json + " as permissions");
          return null;
        }
      }
    });
    builder.registerTypeAdapter(TokenPermissions.class, new JsonDeserializer<TokenPermissions>() {
      @Override
      public TokenPermissions deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
          throws JsonParseException {
        try {
          JsonObject map = (JsonObject) json;
          JsonElement dropReadObj = map.get("dropRead");
          JsonElement dropWriteObj = map.get("dropWrite");

          boolean dropRead = dropReadObj == null ? false : dropReadObj.getAsBoolean();
          boolean dropWrite = dropWriteObj == null ? false : dropWriteObj.getAsBoolean();

          return new TokenPermissions(dropRead, dropWrite);
        } catch (Exception e) {
          logger.log(Level.FINE, "Couldn't deserialize " + json + " as token permissions");
          return null;
        }
      }
    });
    builder.registerTypeAdapter(DropElementsMap.class, new JsonSerializer<DropElementsMap>() {
      @Override
      public JsonElement serialize(DropElementsMap src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject o = new JsonObject();
        for (Map.Entry<String, Object> entry : src.entrySet()) {
          o.add(entry.getKey(), DropElementSerializer.toJsonElement(entry.getValue()));
        }
        return o;
      }
    });
    builder.registerTypeAdapter(DropElementsMap.class, new JsonDeserializer<DropElementsMap>() {
      @Override
      public DropElementsMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
          throws JsonParseException {
        DropElementsMap d = new DropElementsMap();
        JsonObject map = (JsonObject) json;
        for (Entry<String, JsonElement> e : map.entrySet()) {
          d.put(e.getKey(), DropElementSerializer.fromJsonElement(e.getValue()));
        }
        return d;
      }
    });
    return builder.create();
  }
}
