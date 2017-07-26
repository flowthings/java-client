package com.flowthings.client;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.flowthings.client.domain.ApiImporterTask;
import com.flowthings.client.domain.Device;
import com.flowthings.client.domain.Drop;
import com.flowthings.client.domain.Flow;
import com.flowthings.client.domain.FlowDomainObject;
import com.flowthings.client.domain.Group;
import com.flowthings.client.domain.Identity;
import com.flowthings.client.domain.LocalTrack;
import com.flowthings.client.domain.MqttConnection;
import com.flowthings.client.domain.RssTask;
import com.flowthings.client.domain.Share;
import com.flowthings.client.domain.Token;
import com.flowthings.client.domain.Track;

public class DomainObjectSerializationTest extends FileBasedSerializationTest {
  @Test
  public void testApiImporterTask() {
    testSerialize("/json/ApiImporterTask-Full.json", ApiImporterTask.class);
    testSerialize("/json/ApiImporterTask-Minimal.json", ApiImporterTask.class);
  }

  @Test
  public void testDevice() {
    testSerialize("/json/Device-Full.json", Device.class);
    testSerialize("/json/Device-Minimal.json", Device.class);
  }

  @Test
  public void testDrop() {
    dropCheck(Serializer.fromJson(readFromFile("/json/Drop-Full.json"), Drop.class), false);
    testSerialize("/json/Drop-Full.json", Drop.class);
    testSerialize("/json/Drop-Minimal.json", Drop.class);
  }

  @Test
  public void testFlow() {
    testSerialize("/json/Flow-Full.json", Flow.class);
    testSerialize("/json/Flow-Minimal.json", Flow.class);
  }

  @Test
  public void testGroup() {
    testSerialize("/json/Group-Full.json", Group.class);
    testSerialize("/json/Group-Minimal.json", Group.class);
  }

  @Test
  public void testIdentity() {
    testSerialize("/json/Identity-Full.json", Identity.class);
    testSerialize("/json/Identity-Minimal.json", Identity.class);
  }

  @Test
  public void testMqtt() {
    testSerialize("/json/MqttConnection-Full.json", MqttConnection.class);
    testSerialize("/json/MqttConnection-Minimal.json", MqttConnection.class);
  }

  @Test
  public void testRssTask() {
    testSerialize("/json/RssTask-Full.json", RssTask.class);
    testSerialize("/json/RssTask-Minimal.json", RssTask.class);
  }

  @Test
  public void testShare() {
    testSerialize("/json/Share-Full.json", Share.class);
    testSerialize("/json/Share-Minimal.json", Share.class);
  }

  @Test
  public void testToken() {
    testSerialize("/json/Token-Full.json", Token.class);
    testSerialize("/json/Token-Minimal.json", Token.class);
  }

  @Test
  public void testTrack() {
    testSerialize("/json/Track-Full.json", Track.class);
    testSerialize("/json/Track-Minimal.json", Track.class);
  }

  @Test
  public void testLocalTrack() {
    testSerialize("/json/LocalTrack-Full.json", LocalTrack.class);
    testSerialize("/json/LocalTrack-Minimal.json", LocalTrack.class);
  }

  private static void testSerialize(String fileName, Class<? extends FlowDomainObject> klazz) {
    String json = readFromFile(fileName);
    Object o = Serializer.fromJson(json, klazz);
    compare(o, Serializer.fromJson(Serializer.toJson(o), klazz));
  }

  private static <T> void compare(T a, T b) {
    Class c = a.getClass();
    for (Method m : c.getMethods()) {
      if (m.getName().startsWith("get")) {
        try {
          Object o1 = m.invoke(a, new Object[0]);
          Object o2 = m.invoke(b, new Object[0]);
          Assert.assertEquals(a.getClass() + ":" + m.getName(), o1, o2);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * It's not good enough just to serialize then deserialize, and check the
   * results. We need to do a field-by-field check
   */
  @SuppressWarnings("unchecked")
  private void dropCheck(Drop fromString, boolean minimal) {
    Assert.assertEquals("d0000000000018cc6666f6f00", fromString.getId());
    Assert.assertEquals("f55523257adc66a5ff36e731c", fromString.getFlowId());
    Assert.assertEquals("/alice/apples", fromString.getPath());
    if (!minimal) {
      Assert.assertEquals("f55523257adc66a5ff36e731c", fromString.getParentDropId().getFlowId());
      Assert.assertEquals("d55523257adc66a5ff36e731c", fromString.getParentDropId().getDropId());
      Assert.assertEquals(new Date(1433800947425l), fromString.getLastEditDate());
      Assert.assertEquals(0.06031791922725516, fromString.getLocation().getLatitude(), 0.000001);
      Assert.assertEquals(0.6709274265862697, fromString.getLocation().getLongitude(), 0.000001);
      Assert.assertEquals("foo", fromString.getFhash());
    }
    Assert.assertEquals("beepboop", fromString.getElems().get("astring"));
    Assert.assertEquals(true, fromString.getElems().get("abool"));
    // TODO - fix
    Assert.assertEquals(1433800947425l, fromString.getElems().get("along"));
    Assert.assertEquals(5.87, fromString.getElems().get("adouble"));
    Assert.assertEquals("bar", ((Map<String, Object>) fromString.getElems().get("anobject")).get("foo"));
    Assert.assertEquals(7.3, ((Map<String, Object>) fromString.getElems().get("anobject")).get("baz"));
    // TODO - fix
    Assert.assertEquals(1l, ((List<Object>) fromString.getElems().get("anarray")).get(0));
    Assert.assertEquals(2l, ((List<Object>) fromString.getElems().get("anarray")).get(1));
    Assert.assertEquals(3.4, ((List<Object>) fromString.getElems().get("anarray")).get(2));
    Assert.assertEquals(true, ((List<Object>) fromString.getElems().get("anarray")).get(3));
    Assert.assertEquals(false, ((List<Object>) fromString.getElems().get("anarray")).get(4));
  }
}
