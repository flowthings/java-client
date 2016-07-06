package com.flowthings.client.api;

import com.flowthings.client.Credentials;
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
  private Map<RequestPair, FlowthingsFuture> answers = new HashMap<>();

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

  public void setAnswer(Types domainObjectType, Request.Action action, Object response){
    answers.put(new RequestPair(domainObjectType, action), FlowthingsFuture.fromResult(response));
  }
  public void setException(Types domainObjectType, Request.Action action, FlowthingsException response){
    answers.put(new RequestPair(domainObjectType, action), FlowthingsFuture.fromException(response));
  }

  @Override
  protected <S> FlowthingsFuture<S> sendRequest(Request<S> request) {
    Request.Action action = request.action;
    Types type = request.type;
    FlowthingsFuture answer = answers.get(new RequestPair(type, action));

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

class RequestPair {
  private final Types type;
  private final Request.Action action;

  public RequestPair(Types type, Request.Action action) {
    this.type = type;
    this.action = action;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RequestPair that = (RequestPair) o;

    if (type != that.type) return false;
    return action == that.action;

  }

  @Override
  public int hashCode() {
    int result = type.hashCode();
    result = 31 * result + action.hashCode();
    return result;
  }
}
