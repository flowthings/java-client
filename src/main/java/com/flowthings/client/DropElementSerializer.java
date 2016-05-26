package com.flowthings.client;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.codec.binary.Base64;

import com.flowthings.client.domain.elements.Duration;
import com.flowthings.client.domain.elements.Length;
import com.flowthings.client.domain.elements.Location;
import com.flowthings.client.domain.elements.MapLike;
import com.flowthings.client.domain.elements.Media;
import com.flowthings.client.domain.elements.Text;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public abstract class DropElementSerializer<T> {
  protected String[] typeKeys;

  public DropElementSerializer(String... typeKeys) {
    this.typeKeys = typeKeys;
  }

  // --------------------------------------
  // Abstract
  // --------------------------------------
  public abstract T from(String type, JsonElement e);

  @SuppressWarnings("unused")
  protected String getTypeKey(T o) {
    return typeKeys[0];
  }

  public JsonElement to(T o) {
    JsonObject jo = new JsonObject();
    jo.add("type", new JsonPrimitive(getTypeKey(o)));
    jo.add("value", toPrimitive(o));
    return jo;
  }

  public abstract JsonElement toPrimitive(T o);

  // --------------------------------------
  // Static
  // --------------------------------------
  public static Object fromJsonElement(JsonElement e) {
    if (e == null) {
      return null;
    }
    if (e instanceof JsonPrimitive) {
      JsonPrimitive p = (JsonPrimitive) e;
      if (p.isBoolean()) {
        return p.getAsBoolean();
      } else if (p.isNumber()) {
        return p.getAsNumber();
      } else {
        return p.getAsString();
      }
    }
    if (e instanceof JsonObject) {
      JsonObject o = (JsonObject) e;
      String type = o.get("type").getAsString();
      DropElementSerializer s = serializerMap.get(type);
      if (s == null) {
        throw new UnsupportedOperationException("Type " + type + " not supported");
      }
      return s.from(type, o.get("value"));
    }
    return e;
  }

  @SuppressWarnings("unchecked")
  public static JsonElement toJsonElement(Object o) {
    if (o == null) {
      return null;
    }
    Class type = o.getClass();
    DropElementSerializer s = serializerMap.get(type);
    if (s == null) {
      if (o instanceof Map) {
        return MAP.to((Map) o);
      }
      if (o instanceof Collection) {
        return COLLECTION.to((Collection) o);
      }
    }
    if (s == null) {
      throw new UnsupportedOperationException(
          "Don't know how to create json from Object " + o + " of type " + o.getClass());
    }
    return s.to(o);
  }

  protected static int extractIntMember(JsonObject o, String key) {
    return o.has(key) ? o.getAsJsonObject(key).getAsJsonPrimitive("value").getAsInt() : 0;
  }

  // --------------------------------------
  // Serializers
  // --------------------------------------
  //
  // ---------------- map -----------------
  //
  protected static DropElementSerializer<Map<String, Object>> MAP = new DropElementSerializer<Map<String, Object>>(
      "map", "sortedMap") {
    private Map<String, Object> newInstance(String type) {
      if ("sortedMap".equals(type)) {
        return new TreeMap<>();
      }
      return new HashMap<>();
    }

    @Override
    protected String getTypeKey(Map<String, Object> o) {
      if (o instanceof SortedMap) {
        return "sortedMap";
      }
      return "map";
    }

    @Override
    public Map<String, Object> from(String type, JsonElement elem) {
      Map<String, Object> m = newInstance(type);
      JsonObject c = elem.getAsJsonObject();
      for (Map.Entry<String, JsonElement> e : c.entrySet()) {
        m.put(e.getKey(), DropElementSerializer.fromJsonElement(e.getValue()));
      }
      return m;
    }

    @Override
    public JsonObject toPrimitive(Map<String, Object> m) {
      JsonObject o = new JsonObject();
      for (Map.Entry<String, Object> e : m.entrySet()) {
        o.add(e.getKey(), toJsonElement(e.getValue()));
      }
      return o;
    }
  };
  protected static DropElementSerializer<MapLike> MAPLIKE = new DropElementSerializer<MapLike>("text", "media") {
    @Override
    protected String getTypeKey(MapLike e) {
      return e.getType();
    }

    @Override
    public MapLike from(String type, JsonElement elem) {
      MapLike m = "media".equals(type) ? new Media() : new Text();
      JsonObject c = elem.getAsJsonObject();
      for (Map.Entry<String, JsonElement> e : c.entrySet()) {
        m.put(e.getKey(), DropElementSerializer.fromJsonElement(e.getValue()));
      }
      return m;
    }

    @Override
    public JsonElement toPrimitive(MapLike o) {
      JsonObject jo = new JsonObject();
      for (String s : o) {
        jo.add(s, toJsonElement(o.get(s)));
      }
      return jo;
    }
  };
  protected static DropElementSerializer<Length> LENGTH = new DropElementSerializer<Length>("length") {
    @Override
    protected String getTypeKey(Length e) {
      return "length";
    }

    @Override
    public Length from(String type, JsonElement elem) {
      JsonObject c = elem.getAsJsonObject();
      JsonElement unitsJ = c.get("unit");
      JsonElement magnitudeJ = c.get("magnitude");
      String units = unitsJ == null ? "METERS" : unitsJ.getAsString();
      Double magnitude = magnitudeJ == null ? 0.0 : magnitudeJ.getAsDouble();
      Length l = new Length(Length.Units.valueOf(units.toUpperCase()), magnitude);
      return l;
    }

    @Override
    public JsonElement toPrimitive(Length o) {
      JsonObject jo = new JsonObject();
      jo.addProperty("unit", o.getUnits().name().toLowerCase());
      jo.addProperty("magnitude", o.getMagnitude());
      return jo;
    }
  };
  protected static DropElementSerializer<Duration> DURATION = new DropElementSerializer<Duration>("duration") {
    @Override
    protected String getTypeKey(Duration e) {
      return "duration";
    }

    @Override
    public Duration from(String type, JsonElement elem) {
      JsonObject c = elem.getAsJsonObject();
      JsonElement unitsJ = c.get("unit");
      JsonElement magnitudeJ = c.get("magnitude");
      String units = unitsJ == null ? "MILLISECONDS" : unitsJ.getAsString();
      Double magnitude = magnitudeJ == null ? 0.0 : magnitudeJ.getAsDouble();
      Duration l = new Duration(Duration.Units.valueOf(units.toUpperCase()), magnitude);
      return l;
    }

    @Override
    public JsonElement toPrimitive(Duration o) {
      JsonObject jo = new JsonObject();
      jo.addProperty("unit", o.getUnits().name().toLowerCase());
      jo.addProperty("magnitude", o.getMagnitude());
      return jo;
    }
  };
  //
  // ---------------- list -----------------
  //
  protected static DropElementSerializer<Collection<Object>> COLLECTION = new DropElementSerializer<Collection<Object>>(
      "list", "set", "sortedSet") {
    private Collection<Object> newInstance(String type) {
      if ("set".equals(type)) {
        return new HashSet<>();
      }
      if ("sortedSet".equals(type)) {
        return new TreeSet<>();
      }
      return new ArrayList<>();
    }

    @Override
    protected String getTypeKey(Collection<Object> o) {
      if (o instanceof SortedSet) {
        return "sortedSet";
      }
      if (o instanceof Set) {
        return "set";
      }
      return "list";
    }

    @Override
    public Collection<Object> from(String type, JsonElement elem) {
      Collection<Object> m = newInstance(type);
      for (JsonElement o : elem.getAsJsonArray()) {
        m.add(fromJsonElement(o));
      }
      return m;
    }

    @Override
    public JsonElement toPrimitive(Collection<Object> m) {
      JsonArray ja = new JsonArray();
      for (Object o : m) {
        ja.add(toJsonElement(o));
      }
      return ja;
    }
  };
  protected static DropElementSerializer<String> STRING = new DropElementSerializer<String>("string") {
    @Override
    public JsonElement toPrimitive(String b) {
      return new JsonPrimitive(b);
    }

    @Override
    public String from(String type, JsonElement e) {
      return e.getAsString();
    }
  };
  protected static DropElementSerializer<Boolean> BOOLEAN = new DropElementSerializer<Boolean>("boolean") {
    @Override
    public Boolean from(String type, JsonElement e) {
      return e.getAsBoolean();
    }

    @Override
    public JsonElement toPrimitive(Boolean b) {
      return new JsonPrimitive(b);
    }
  };
  protected static DropElementSerializer<Double> DOUBLE = new DropElementSerializer<Double>("float") {
    @Override
    public Double from(String type, JsonElement e) {
      return e.getAsDouble();
    }

    @Override
    public JsonElement toPrimitive(Double d) {
      return new JsonPrimitive(d);
    }
  };
  protected static DropElementSerializer<Integer> INTEGER = new DropElementSerializer<Integer>("integer") {
    @Override
    public Integer from(String type, JsonElement e) {
      return e.getAsInt();
    }

    @Override
    public JsonElement toPrimitive(Integer b) {
      return new JsonPrimitive(b);
    }
  };
  protected static DropElementSerializer<Long> LONG = new DropElementSerializer<Long>("long") {
    @Override
    public Long from(String type, JsonElement e) {
      return e.getAsLong();
    }

    @Override
    public JsonElement toPrimitive(Long b) {
      return new JsonPrimitive(b);
    }
  };
  protected static DropElementSerializer<Date> DATE = new DropElementSerializer<Date>("date") {
    @Override
    public Date from(String type, JsonElement e) {
      return new Date(e.getAsLong());
    }

    @Override
    public JsonElement toPrimitive(Date d) {
      return new JsonPrimitive(d.getTime());
    }
  };
  protected static DropElementSerializer<byte[]> BYTES = new DropElementSerializer<byte[]>("bytes") {
    protected Base64 base64 = new Base64();

    @Override
    public byte[] from(String type, JsonElement e) {
      return base64.decode(e.getAsString());
    }

    @Override
    public JsonElement toPrimitive(byte[] b) {
      return new JsonPrimitive(base64.encodeAsString(b));
    }
  };
  protected static DropElementSerializer<Location> LOCATION = new DropElementSerializer<Location>("location") {
    @Override
    public Location from(String type, JsonElement elem) {
      JsonObject jo = (JsonObject) elem;
      Location loc = new Location();
      loc.setLatitude(jo.get("lat").getAsDouble());
      loc.setLongitude(jo.get("lon").getAsDouble());
      if (jo.has("specifiers")) {
        loc.setSpecifiers((HashMap<String, Object>) MAP.from("map", jo.getAsJsonObject("specifiers")));
      }
      return loc;
    }

    @Override
    public JsonObject toPrimitive(Location m) {
      JsonObject o = new JsonObject();
      JsonObject specMap = new JsonObject();
      for (String s : m.getSpecifiers().keySet()) {
        Object spec = m.getSpecifiers().get(s);
        specMap.add(s, toJsonElement(spec));
      }
      o.add("specifiers", specMap);
      o.addProperty("lat", m.getLatitude());
      o.addProperty("lon", m.getLongitude());
      return o;
    }
  };
  static final HashMap<Object, DropElementSerializer<?>> serializerMap = new HashMap<>();
  static {
    for (Field f : DropElementSerializer.class.getDeclaredFields()) {
      if (DropElementSerializer.class.isAssignableFrom(f.getType())) {
        try {
          DropElementSerializer s = (DropElementSerializer) f.get(null);
          for (String key : s.typeKeys) {
            serializerMap.put(key, s);
          }
          Type serializerType = ((ParameterizedType) s.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
          if (serializerType instanceof Class) {
            serializerMap.put(serializerType, s);
          } else if (serializerType instanceof ParameterizedType) {
            serializerMap.put(((ParameterizedType) serializerType).getRawType(), s);
          }
        } catch (IllegalArgumentException e) {
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }
    serializerMap.put(byte[].class, BYTES);
    serializerMap.put(Text.class, MAPLIKE);
    serializerMap.put(Media.class, MAPLIKE);
    serializerMap.put(Duration.class, DURATION);
    serializerMap.put(Length.class, LENGTH);
  }
}
