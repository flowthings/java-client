package com.flowthings.client;

import com.flowthings.client.domain.elements.Location;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

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
    return toPrimitive(o);
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
        Number number = p.getAsNumber();
        if (number.doubleValue() % 1 == 0){
          return number.longValue();
        } else {
          return number.doubleValue();
        }
      } else {
        return p.getAsString();
      }
    }
    if (e instanceof JsonArray){
      return DropElementSerializer.COLLECTION.from("list", e);
    }
    if (e instanceof JsonObject) {
      return DropElementSerializer.MAP.from("map", e);
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
    return o.has(key) ? o.getAsJsonPrimitive(key).getAsInt() : 0;
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
  }
}
