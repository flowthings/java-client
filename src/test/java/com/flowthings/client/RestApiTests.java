package com.flowthings.client;

import java.util.List;

import org.junit.Test;

import com.flowthings.client.api.Flowthings;
import com.flowthings.client.api.RestApi;
import com.flowthings.client.api.SubscriptionCallback;
import com.flowthings.client.domain.AvConnection;
import com.flowthings.client.domain.Drop;
import com.flowthings.client.domain.Flow;
import com.flowthings.client.domain.Identity;
import com.flowthings.client.domain.Token;
import com.flowthings.client.domain.TokenPermissions;
import com.flowthings.client.exception.AuthorizationException;
import com.flowthings.client.exception.FlowthingsException;
import com.flowthings.client.exception.NotFoundException;

import junit.framework.Assert;

@SuppressWarnings("unused")
public class RestApiTests {
  private static String accountName;
  private static RestApi api;
  private static String hostString;
  private static boolean secure;
  static {
    accountName = System.getenv("FTIO_CLIENT_TEST_USER");
    String tokenString = System.getenv("FTIO_CLIENT_TEST_TOKEN");
    hostString = System.getenv("FTIO_CLIENT_TEST_HOST");
    String secureString = System.getenv("FTIO_CLIENT_TEST_SECURE");
    Credentials credentials = new Credentials(accountName, tokenString);
    if (accountName == null || accountName.isEmpty() || tokenString == null || tokenString.isEmpty()) {
      throw new IllegalStateException("To run tests, ensure FTIO_CLIENT_TEST_USER and FTIO_CLIENT_TEST_TOKEN are "
          + "supplied (a valid username and master token, respectively)");
    }
    if (hostString != null) {
      secure = secureString != null && secureString.toLowerCase().equals("true");
      api = new RestApi(credentials, hostString, secure);
    } else {
      api = new RestApi(credentials);
    }
  }

  @Test
  public void testCreateIdentity() throws FlowthingsException {
    Credentials syscreds = new Credentials("system", "znBG0s95qWNuUcLGPXJG5fGbEmo1CWf6");
    Credentials alicecreds = new Credentials("alice", "sFIRjS5QMcJJcZp8qWSGeE5i3967QcYx");
    Identity i = new Identity.Builder().setName("foo2").get();
    i.setEmail("asb@basca.com");
    RestApi api2 = new RestApi(syscreds, "localhost:3000/v4.0", false);
    i = api2.send(Flowthings.identity().create(i));
    System.out.println(i);
  }

  @Test
  public void testAvConnections() throws FlowthingsException {
    String companyName = "company" + System.currentTimeMillis();
    String path = "/" + accountName + "/test" + System.currentTimeMillis();
    AvConnection av = new AvConnection.Builder().setCompanyId(companyName).setDestination(path).get();
    // Create
    av = api.send(Flowthings.avConnection().create(av));
    Assert.assertNotNull("Created", av);
    // Get
    AvConnection got = api.send(Flowthings.avConnection().get(av.getId()));
    Assert.assertEquals("Got", av, got);
    // Find
    List<AvConnection> results = api
        .send(Flowthings.avConnection().find(new QueryOptions().filter("destination==\"" + path + "\"")));
    Assert.assertEquals("Found", 1, results.size());
    Assert.assertEquals("Found", av, results.get(0));
    // Update
    av.setDescription("Now with all new descriptions!");
    AvConnection updated = api.send(Flowthings.avConnection().update(av.getId(), av));
    Assert.assertEquals("Updated", "Now with all new descriptions!", updated.getDescription());
    // Delete
    api.send(Flowthings.avConnection().delete(av.getId()));
    // Get again
    try {
      got = api.send(Flowthings.avConnection().get(av.getId()));
      Assert.fail();
    } catch (Exception e) {
    }
  }

  @Test
  public void testFlows() throws FlowthingsException {
    /**
     * Flows
     */
    String path = "/" + accountName + "/testf" + System.currentTimeMillis();
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
      Drop drop2 = new Drop.Builder().addElem("foo", "bar").get();
      // Create
      drop = api.send(Flowthings.drop(flowId).create(drop));
      drop2 = api.send(Flowthings.dropFromPath(path).create(drop2));
      Assert.assertNotNull("Created", drop);
      Assert.assertNotNull("Created", drop2);
      sleep(1000);
      // Get
      Drop got = api.send(Flowthings.drop(flowId).get(drop.getId()));
      Assert.assertEquals("Got", drop, got);
      got = api.send(Flowthings.drop(flowId).get(drop2.getId()));
      Assert.assertEquals("Got", drop2, got);
      // Find
      List<Drop> results = api.send(Flowthings.drop(flowId).find(new QueryOptions().filter("elems.foo==\"bar\"")));
      Assert.assertEquals("Found", 2, results.size());
      // Find by Path
      results = api.send(Flowthings.dropFromPath(path).find(new QueryOptions().filter("elems.foo==\"bar\"")));
      Assert.assertEquals("Found", 2, results.size());
      // Update
      drop.getElems().put("baz", 7);
      Drop updated = api.send(Flowthings.drop(flowId).update(drop.getId(), drop));
      Assert.assertEquals("Updated", 7l, updated.getElems().get("baz"));
      // Delete
      api.send(Flowthings.drop(flowId).delete(drop.getId()));
      api.send(Flowthings.drop(flowId).delete(drop2.getId()));
      // Get again
      try {
        got = api.send(Flowthings.drop(flowId).get(drop.getId()));
        Assert.fail();
        got = api.send(Flowthings.drop(flowId).get(drop2.getId()));
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

  private void sleep(long i) {
    try {
      Thread.sleep(i);
    } catch (InterruptedException e) {
      e.printStackTrace();
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
    RestApi api = hostString == null ? new RestApi(new Credentials("nope", "nooo"))
        : new RestApi(new Credentials("nope", "nooo"), hostString, secure);
    List<Flow> r1 = null;
    try {
      r1 = api.send(Flowthings.flow().find());
      Assert.fail("Should have thrown exception");
    } catch (AuthorizationException e) {
    }
  }

  @Test
  public void test403Async() throws FlowthingsException {
    RestApi api = hostString == null ? new RestApi(new Credentials("nope", "nooo"))
        : new RestApi(new Credentials("nope", "nooo"), hostString, secure);
    List<Flow> r1 = null;
    try {
      r1 = api.sendAsync(Flowthings.flow().find()).grab();
      Assert.fail("Should have thrown exception");
    } catch (AuthorizationException e) {
    }
  }

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
  public void test404Async() throws FlowthingsException {
    Flow r2 = null;
    try {
      // This flow doesn't exist
      r2 = api.sendAsync(Flowthings.flow().get("ff551ac39bd4c6c0000000000")).grab();
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
  public void testNoFlowIdAsync() throws FlowthingsException {
    List<Drop> r1 = null;
    try {
      r1 = api.sendAsync(Flowthings.drop(null).find()).grab();
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
      // All good!
    }
  }
}
