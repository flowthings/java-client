package com.flowthings.client;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.flowthings.client.exception.AuthorizationException;
import com.flowthings.client.exception.NotFoundException;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

import com.flowthings.client.api.Flowthings;
import com.flowthings.client.api.SubscriptionCallback;
import com.flowthings.client.api.WebsocketApi;
import com.flowthings.client.domain.Drop;
import com.flowthings.client.domain.Flow;
import com.flowthings.client.exception.BadRequestException;
import com.flowthings.client.exception.FlowthingsException;

import junit.framework.Assert;

@SuppressWarnings("unused")
public class WebsocketsApiTests {
  private static String accountName;
  private static String tokenString;
  private static Credentials credentials;
  private static WebsocketApi api;
  static {
    try {
      accountName = System.getenv("FTIO_CLIENT_TEST_USER");
      tokenString = System.getenv("FTIO_CLIENT_TEST_TOKEN");
      String host = System.getenv("FTIO_CLIENT_TEST_HOST");
      String secure = System.getenv("FTIO_CLIENT_TEST_SECURE");

      credentials = new Credentials(accountName, tokenString);

      if (accountName == null || accountName.isEmpty() ||
          tokenString == null || tokenString.isEmpty()){
        throw new IllegalStateException("To run tests, ensure FTIO_CLIENT_TEST_USER and FTIO_CLIENT_TEST_TOKEN are " +
            "supplied (a valid username and master token, respectively)");
      }

      if (host != null && !host.isEmpty() && secure != null && !secure.isEmpty()){
        boolean sec = Boolean.parseBoolean(secure);
        api = new WebsocketApi(credentials, host, sec).start();
      } else {
        api = new WebsocketApi(credentials).start();
      }

    } catch (FlowthingsException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testSubscribe() throws FlowthingsException, InterruptedException, ExecutionException {
    final CountDownLatch latch = new CountDownLatch(1);
    final AtomicInteger count = new AtomicInteger(0);
    /**
     * Create a test flow
     */
    String path = "/" + accountName + "/test3887";
    Flow flow = new Flow.Builder().setPath(path).get();
    // Create
    Future<Flow> response = api.send(Flowthings.flow().create(flow));
    flow = response.get(); // immediately deref to ensure it is created
    System.out.println("Created Flow");
    try {
      // Subscribe
      Drop subRes = api.send(Flowthings.drop(flow.getId()).subscribe(new SubscriptionCallback<Drop>() {
        @Override
        public void onMessage(Drop t) {
          System.out.println("I got the drop: " + t);
          latch.countDown();
          count.incrementAndGet();
        }
      })).get();
      System.out.println("Sub response: " + subRes);
      Drop drop = new Drop.Builder().addElem("Foo", "bar").get();
      Drop dropCreateResponse = api.send(Flowthings.drop(flow.getId()).create(drop)).get();
      System.out.println("Drop create res: " + dropCreateResponse);
      latch.await(5000, TimeUnit.MILLISECONDS);
      // Unsubscribe
      api.send(Flowthings.drop(flow.getId()).unsubscribe()).get();
      // Send another drop and hope we don't receive it!
      api.send(Flowthings.drop(flow.getId()).create(drop));
      Thread.sleep(500);
      Assert.assertEquals("drop count", 1, count.get());
    } finally {
      System.out.println("Removing flow");
      Flow flowRemoveResponse = api.send(Flowthings.flow().delete(flow.getId())).get();
      System.out.println("Removed flow: " + flowRemoveResponse);
    }
  }

  @Test
  public void test403() throws FlowthingsException, ExecutionException, InterruptedException {
    List<Flow> r1 = null;
    try {
      WebsocketApi api = new WebsocketApi(new Credentials("nope", "nooo")).start();
      api.send(Flowthings.flow().find()).get();
      Assert.fail("Should have thrown exception");
    } catch (AuthorizationException e) {
    }
  }

  @Test
  public void test404() throws FlowthingsException, InterruptedException, ExecutionException {
    Flow r2 = null;
    try {
      // This flow doesn't exist
      r2 = api.send(Flowthings.flow().get("ff551ac39bd4c6c0000000000")).grab();
      System.out.println("R2 is: " + r2);
      Assert.fail("Should have thrown exception");
    } catch (NotFoundException e) {
    }
  }

  @Test
  public void testBadSubscription() throws FlowthingsException, InterruptedException, ExecutionException {
    Drop r2 = null;
    try {
      // This flow doesn't exist
      r2 = api.send(Flowthings.drop("ff551ac39bd4c6c0000000000").subscribe(new SubscriptionCallback<Drop>() {
        @Override
        public void onMessage(Drop t) {
        }
      })).get();
      System.out.println("R2 is: " + r2);
      Assert.fail("Should have thrown exception");
    } catch (ExecutionException e) {
    }
  }
}
