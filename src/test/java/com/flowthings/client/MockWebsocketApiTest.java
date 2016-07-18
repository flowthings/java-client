package com.flowthings.client;

import com.flowthings.client.api.Flowthings;
import com.flowthings.client.api.MockWebsocketApi;
import com.flowthings.client.api.SubscriptionCallback;
import com.flowthings.client.domain.Device;
import com.flowthings.client.domain.Drop;
import com.flowthings.client.exception.ConnectionRefusedException;
import com.flowthings.client.exception.FlowthingsException;
import com.flowthings.client.exception.NotFoundException;
import junit.framework.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by matt on 7/6/16.
 */
public class MockWebsocketApiTest {

  @Test
  public void testBasicFunctions() throws Exception {

    Device d = new Device.Builder().setId("abc").setDisplayName("boop").get();

    /**
     * Test: Start connected
     */
    final MockWebsocketApi api = new MockWebsocketApi(true);
    api.retryDelayMs = 500;
    api.setAnswerImmediately(Flowthings.device().get("v5758799c9e5273467374d820"), d);
    api.start();

    // Device returns
    Device d2 = api.sendAsync(Flowthings.device().get("v5758799c9e5273467374d820")).grab();
    Assert.assertEquals(d.getDisplayName(), d2.getDisplayName());

    // Unknown answer
    try {
      api.sendAsync(Flowthings.device().find()).get(200, TimeUnit.MILLISECONDS);
      Assert.fail();
    } catch (TimeoutException e) {
      // Pass
    }

    // An example of not-found
    api.setException(Flowthings.flow().get("f5758799c9e5273467374d820"), new NotFoundException("boop"));

    try {
      api.sendAsync(Flowthings.flow().get("f5758799c9e5273467374d820")).grab();
      Assert.fail();
    } catch (NotFoundException e) {
      // Pass
    }

    /**
     * Test: Disconnection!
     */
    api.setCanConnect(false);

    Thread.sleep(200);

    // Now requests should block
    final AtomicReference<Device> i = new AtomicReference<>();
    new Thread(new Runnable(){

      @Override
      public void run() {
        try {
          Device d3 = api.sendAsync(Flowthings.device().get("v5758799c9e5273467374d820")).grab();
          i.set(d3);
        } catch (FlowthingsException e) {
          System.out.println("FAIL!");
        }
      }
    }).start();

    Thread.sleep(200);
    Assert.assertEquals("Hasn't made the request yet", null, i.get());

    api.setCanConnect(true);

    Thread.sleep(800);
    Assert.assertNotNull("Request has been made!", i.get());
    Assert.assertEquals("Returns the original device still", d.getDisplayName(), i.get().getDisplayName());
  }

  @Test
  public void testExceptionIsThrownIfCannotConnectInitially() throws Exception {

    /**
     * Test: Start disconnected
     */
    final MockWebsocketApi api = new MockWebsocketApi(false);
    try {
      api.start();
      Assert.fail();
    } catch (ConnectionRefusedException e) {
      // Pass
    }
  }

  @Test
  public void testDropSubscribe() throws Exception {
    final MockWebsocketApi api = new MockWebsocketApi(true);
    api.start();

    final CountDownLatch l = new CountDownLatch(1);
    api.sendAsync(Flowthings.drop("f5758799c9e5273467374d820").subscribe(
        new SubscriptionCallback<Drop>() {
          @Override
          public void onMessage(Drop drop) {
            l.countDown();
          }
        }
    ));

    Drop d1 = new Drop.Builder().addElem("foo", "bar").get();
    api.supplyIncomingDrop("f5758799c9e5273467374d820", d1);


    l.await(200, TimeUnit.MILLISECONDS);
  }
}

