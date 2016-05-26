package com.flowthings.client;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.flowthings.client.api.Flowthings;
import com.flowthings.client.api.RestApi;
import com.flowthings.client.api.SubscriptionCallback;
import com.flowthings.client.domain.Drop;
import com.flowthings.client.domain.Flow;
import com.flowthings.client.domain.Token;
import com.flowthings.client.domain.TokenPermissions;
import com.flowthings.client.exception.AuthorizationException;
import com.flowthings.client.exception.FlowthingsException;
import com.flowthings.client.exception.NotFoundException;

@SuppressWarnings("unused")
public class RestApiTests {
  private static String accountName = "matt";
  private static String tokenString = "fWNhEOEJ2RdqoKiqOfLfYlDyNrCWCTBU";
  private static Credentials credentials = new Credentials(accountName, tokenString);
  private static RestApi api = new RestApi(credentials);

  @Test
  public void testFlows() throws FlowthingsException {
    /**
     * Flows
     */
    String path = "/" + accountName + "/test123456";
    Flow flow = new Flow.Builder().setPath(path).get();
    // Create
    flow = api.send(Flowthings.flow().create(flow));
    Assert.assertNotNull("Created", flow);
    // Get
    Flow got = api.send(Flowthings.flow().get(flow.getId()));
    Assert.assertEquals("Got", flow, got);
    // Find
    List<Flow> results = api.send(Flowthings.flow().find(new QueryOptions().filter("path==\"" + path + "\"")));
    Assert.assertEquals("Found", 1, results.size());
    Assert.assertEquals("Found", flow, results.get(0));
    // Update
    flow.setDescription("Now with all new descriptions!");
    Flow updated = api.send(Flowthings.flow().update(flow.getId(), flow));
    Assert.assertEquals("Updated", "Now with all new descriptions!", updated.getDescription());
    // Delete
    api.send(Flowthings.flow().delete(flow.getId()));
    // Get again
    try {
      got = api.send(Flowthings.flow().get(flow.getId()));
      Assert.fail();
    } catch (Exception e) {
    }
  }

  @Test
  public void testDrops() throws FlowthingsException {
    // (Create a test flow)
    String path = "/" + accountName + "/test123";
    Flow flow = new Flow.Builder().setPath(path).get();
    flow = api.send(Flowthings.flow().create(flow));
    Assert.assertNotNull("Created", flow);
    String flowId = flow.getId();
    /**
     * Drops
     */
    try {
      Drop drop = new Drop.Builder().addElem("foo", "bar").get();
      // Create
      drop = api.send(Flowthings.drop(flowId).create(drop));
      Assert.assertNotNull("Created", drop);
      // Get
      Drop got = api.send(Flowthings.drop(flowId).get(drop.getId()));
      Assert.assertEquals("Got", drop, got);
      // Find
      List<Drop> results = api.send(Flowthings.drop(flowId).find(new QueryOptions().filter("elems.foo==\"bar\"")));
      Assert.assertEquals("Found", 1, results.size());
      Assert.assertEquals("Found", drop, results.get(0));
      // Update
      drop.getElems().put("baz", 7);
      Drop updated = api.send(Flowthings.drop(flowId).update(drop.getId(), drop));
      Assert.assertEquals("Updated", 7, updated.getElems().get("baz"));
      // Delete
      api.send(Flowthings.drop(flowId).delete(drop.getId()));
      // Get again
      try {
        got = api.send(Flowthings.drop(flowId).get(drop.getId()));
        Assert.fail();
      } catch (Exception e) {
        // Good
      }
      // Delete all drops
      api.send(Flowthings.drop(flowId).deleteAll());
    } finally {
      // (delete test flow)
      api.send(Flowthings.flow().delete(flowId));
    }
  }

  @Test
  public void testTokens() throws FlowthingsException {
    /**
     * Tokens (read only, not entirely symetrical)
     */
    // (Create a test flow)
    String path = "/" + accountName + "/test8998";
    Flow flow = new Flow.Builder().setPath(path).get();
    flow = api.send(Flowthings.flow().create(flow));
    Assert.assertNotNull("Created", flow);
    String flowId = flow.getId();
    try {
      Token token = new Token.Builder().addPath(flow.getPath(), new TokenPermissions(true, false))
          .setDescription("description1").setDuration(120000l).get();
      // Create
      token = api.send(Flowthings.token().create(token));
      Assert.assertNotNull("Created", token);
      // Get
      Token got = api.send(Flowthings.token().get(token.getId()));
      Assert.assertEquals("Got", token, got);
      Assert.assertNotNull("Got expire", got.getExpiresInMs());
      // Find
      List<Token> results = api
          .send(Flowthings.token().find(new QueryOptions().filter("description==\"description1\"")));
      Assert.assertEquals("Found", 1, results.size());
      Assert.assertEquals("Found", token, results.get(0));
      // Update not allowed
      // Delete
      api.send(Flowthings.token().delete(token.getId()));
      // Get again
      try {
        got = api.send(Flowthings.token().get(token.getId()));
        Assert.fail();
      } catch (Exception e) {
      }
    } finally {
      // (delete test flow)
      api.send(Flowthings.flow().delete(flowId));
    }
  }

  @Test
  public void test403() throws FlowthingsException {
    RestApi api = new RestApi(new Credentials("nope", "nooo"));
    List<Flow> r1 = null;
    try {
      r1 = api.send(Flowthings.flow().find());
      Assert.fail("Should have thrown exception");
    } catch (AuthorizationException e) {
    }
  }

  // if (flowId == null) {
  // throw new NullPointerException("FlowId must not be null");
  // }
  @Test
  public void test404() throws FlowthingsException {
    Flow r2 = null;
    try {
      // This flow doesn't exist
      r2 = api.send(Flowthings.flow().get("ff551ac39bd4c6c0000000000"));
      Assert.fail("Should have thrown exception");
    } catch (NotFoundException e) {
    }
  }

  @Test
  public void testNoFlowId() throws FlowthingsException {
    List<Drop> r1 = null;
    try {
      r1 = api.send(Flowthings.drop(null).find());
      Assert.fail("Should have thrown exception");
    } catch (NullPointerException e) {
    }
  }

  @Test
  public void testIncorrectFlowId() throws FlowthingsException {
    List<Drop> r1 = null;
    try {
      r1 = api.send(Flowthings.drop("f123").find());
      Assert.fail("Should have thrown exception");
    } catch (IllegalArgumentException e) {
    }
  }

  @Test
  public void testNoId() throws FlowthingsException {
    Flow r1 = null;
    try {
      r1 = api.send(Flowthings.flow().get(null));
      Assert.fail("Should have thrown exception");
    } catch (NullPointerException e) {
    }
  }

  @Test
  public void testIncorrectId() throws FlowthingsException {
    Flow r1 = null;
    try {
      r1 = api.send(Flowthings.flow().get("f123"));
      Assert.fail("Should have thrown exception");
    } catch (IllegalArgumentException e) {
    }
  }

  @Test
  public void testSendSubscribeThroughHttp() throws FlowthingsException {
    Drop r1 = null;
    try {
      r1 = api.send(Flowthings.drop("ff551ac39bd4c6c0000000000").subscribe(new SubscriptionCallback<Drop>() {
        @Override
        public void onMessage(Drop t) {
        }
      }));
      Assert.fail("Should have thrown exception");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
