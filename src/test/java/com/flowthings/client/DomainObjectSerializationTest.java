package com.flowthings.client;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;

import com.flowthings.client.Serializer;
import com.flowthings.client.domain.ApiImporterTask;
import com.flowthings.client.domain.Device;
import com.flowthings.client.domain.Drop;
import com.flowthings.client.domain.Flow;
import com.flowthings.client.domain.FlowDomainObject;
import com.flowthings.client.domain.Group;
import com.flowthings.client.domain.Identity;
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

  // @Test
  // public void testFindBucketByKeywordDeserialization() {
  // String json = readFromFile("/json/FindBucketByKeywordResponse.json");
  // if (json == "") {
  // // just skip if we can't find the file. Configure the files in maven
  // // later.
  // Assert.assertTrue(true);
  // return;
  // }
  // Response<List<Flow>> fsr = Serializer.fromJson(json, new
  // TypeToken<Response<List<Flow>>>() {
  // });
  // List<? extends FlowDomainObject> l = fsr.get();
  // Assert.assertEquals(l.size(), 10);
  // for (FlowDomainObject d : l) {
  // Assert.assertNotNull(d);
  // }
  // }
}
