package com.flowthings.client;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import com.flowthings.client.Serializer;
import com.flowthings.client.domain.Flow;
import com.flowthings.client.response.ObjectResponse;
import com.flowthings.client.response.ListResponse;
import com.flowthings.client.response.Response.Head;
import com.google.gson.reflect.TypeToken;

public class SearchJsonSerializationTest extends FileBasedSerializationTest {

  protected String headJson = readFromFile("/json/partials/Head-Success.json");
  protected String headFailJson = readFromFile("/json/partials/Head-Error.json");

  @Test
  public void testFindSuccessResponse() {
    String json1 = readFromFile("/json/Flow-Full.json");
    String json2 = readFromFile("/json/Flow-Minimal.json");

    String fullJson = new StringBuffer("{ \"head\" : ").append(headJson).append(",\"body\" : [").append(json1)
        .append(",").append(json2).append("]}").toString();

    ListResponse<Flow> r = Serializer.fromJson(fullJson, new TypeToken<ListResponse<Flow>>() {
    });
    Assert.assertEquals(2, r.getBody().size());
  }

  @Test
  public void testGetSuccessResponse() {
    String json1 = readFromFile("/json/Flow-Minimal.json");

    String fullJson = new StringBuffer("{ \"head\" : ").append(headJson).append(",\"body\" : ").append(json1)
        .append("}").toString();

    System.out.println(fullJson);

    ObjectResponse<Flow> r = Serializer.fromJson(fullJson, new TypeToken<ObjectResponse<Flow>>() {
    });
    Flow flow = r.get();
    Assert.assertEquals("f5575f4e02b738dfc9af7314c", flow.getId());
    Assert.assertEquals(new Integer(500), flow.getCapacity());
    Assert.assertEquals(new Date(1433793760545l), flow.getCreationDate());
    Assert.assertEquals("i551ac39ad4c6c02e55b74632", flow.getCreatorId());
    Assert.assertEquals("/alice/pears/test1", flow.getPath());

  }

  @Test
  public void testGetErrorResponse() {
    String fullJson = new StringBuffer("{ \"head\" : ").append(headFailJson).append(",\"body\" : {} }").toString();

    System.out.println(fullJson);

    ObjectResponse<Flow> r = Serializer.fromJson(fullJson, new TypeToken<ObjectResponse<Flow>>() {
    });
    Head head = r.getHead();
    Assert.assertEquals(1, head.getErrors().size());
    Assert.assertEquals("The requested resource could not be located at this address.", head.getErrors().get(0));

  }

}
