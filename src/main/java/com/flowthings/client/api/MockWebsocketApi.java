package com.flowthings.client.api;

import com.flowthings.client.Credentials;
import com.flowthings.client.domain.Drop;
import com.flowthings.client.domain.Types;
import com.flowthings.client.exception.ConnectionRefusedException;
import com.flowthings.client.exception.FlowthingsException;
import com.flowthings.client.response.Response;
import com.google.common.util.concurrent.SettableFuture;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * A utility class for testing the behaviour of a Websockets connection.
 * No actual connection is made - it is faked
 * This class has two purposes:
 *
 * 1) You can supply responses to requests you make
 * 2) You can simulate disconnections
 *
 * Created by matt on 7/6/16.
 */
public class MockWebsocketApi extends WebsocketApi {

  private boolean canConnect = true;
  private Map<Request, FlowthingsFuture> answers = new HashMap<>();

  public MockWebsocketApi(boolean canConnect) throws FlowthingsException {
    super(new Credentials("a","b"));
    this.canConnect = canConnect;
  }

  public void setCanConnect(boolean canConnect){
    this.canConnect = canConnect;
    if (!canConnect){
      if (socket != null){
        socket.close();
      }
    }
  }

  public void setAnswer(Request request, Object response){
    answers.put(request, FlowthingsFuture.fromResult(response));
  }
  public void setException(Request request, FlowthingsException response){
    answers.put(request, FlowthingsFuture.fromException(response));
  }
  public void supplyIncomingDrop(String flowId, Drop drop){
    WebsocketsDropResponse r1 = new WebsocketsDropResponse();
    r1.setResource(flowId);
    r1.setType("drop");
    r1.setValue(drop);
    this.onWebsocketsDropResponse(r1);
  }

  @Override
  protected <S> FlowthingsFuture<S> sendRequest(Request<S> request) {
    Request.Action action = request.action;
    Types type = request.type;
    FlowthingsFuture answer = answers.get(request);

    // Copied from the main implementation
    if (request.action == Request.Action.SUBSCRIBE) {
      SubscriptionCallback<Drop> callback = (SubscriptionCallback<Drop>) request.otherData.get("callback");
      subscriptions.put(request.flowId, callback);
    } else if (request.action == Request.Action.UNSUBSCRIBE) {
      subscriptions.remove(request.flowId);
    }

    if (answer != null){
      return answer;
    }

    // Block forever
    return new FlowthingsFuture<>(SettableFuture.<S>create());
  }

  @Override
  protected Socket connectWs(String sessionId) throws FlowthingsException {
    if (canConnect){
      return new MockSocket();
    } else {
      throw new ConnectionRefusedException("Mock Connection - cannot connect");
    }
  }

  @Override
  protected String connectHttp() throws FlowthingsException {
    return "beepboop";
  }

  public class MockSocket implements WebsocketApi.Socket {

    private CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void close() {
      latch.countDown();
    }

    @Override
    public void send(String message) throws FlowthingsException {}

    @Override
    public void join() throws InterruptedException {
      latch.await();
    }
  }
}

