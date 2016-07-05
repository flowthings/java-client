package com.flowthings.client;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.flowthings.client.exception.NotFoundException;
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
  private static String accountName = "matt";
  private static String tokenString = "fWNhEOEJ2RdqoKiqOfLfYlDyNrCWCTBU";
  private static Credentials credentials = new Credentials(accountName, tokenString);
  private static WebsocketApi api;
  static {
    try {
      api = new WebsocketApi(credentials);
    } catch (FlowthingsException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testDisconnection() throws Exception {
    WebsocketApi api = new WebsocketApi(new Credentials("alice", "2R5xwhmlmb9r6z90XCUYqOu1ScJJuPMr"), "localhost:17890", false);
    api.send(Flowthings.drop("f55ef2e3ad515290ae135fde0").subscribe(new SubscriptionCallback<Drop>() {
      @Override
      public void onMessage(Drop drop) {
        System.out.println("One");
      }
    }));
    api.send(Flowthings.drop("f55ef2e3ad515290ae135fde2").subscribe(new SubscriptionCallback<Drop>() {
      @Override
      public void onMessage(Drop drop) {
        System.out.println("Two");
      }
    }));
    while (true){
      Thread.sleep(1000);
      try {
        List<Flow> results = api.send(Flowthings.flow().find()).grab();
        System.out.println("Got " + results.size() + " results");
      } catch (FlowthingsException e) {
        e.printStackTrace();
      }
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
      WebsocketApi api = new WebsocketApi(new Credentials("nope", "nooo"));
      r1 = api.send(Flowthings.flow().find()).get();
      Assert.fail("Should have thrown exception");
    } catch (BadRequestException e) {
    }
  }

  @Test
  public void test404() throws FlowthingsException, InterruptedException, ExecutionException {
    Drop r2 = null;
    try {
      // This flow doesn't exist
      r2 = api.send(Flowthings.drop("ff551ac39bd4c6c0000000000").get("df551ac39bd4c6c0000000000")).grab();
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

  // @Test
  public void testLoad() throws FlowthingsException, InterruptedException, ExecutionException {
    Drop r2 = null;
    try {
      // This flow doesn't exist
      api = new WebsocketApi(new Credentials("alice", "N6KwpHHJHbURic5PcmIGrKAO2Nr73erH"), "localhost:17890", false);
      int i = 0;
      while (true) {
        r2 = api
            .send(Flowthings.drop("f55de25e0f23d91563c4e31fb").create(new Drop.Builder().addElem("foo", "bar").get()))
            .get();
        System.out.print(".");
        if (i % 30 == 0) System.out.println("");
        // Thread.sleep(10);
        i++;
      }
    } catch (ExecutionException e) {
    }
  }
}
