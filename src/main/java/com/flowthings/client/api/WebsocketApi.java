package com.flowthings.client.api;

/**
 * Send requests over HTTP. Responses are synchronous.
 *
 * @author matt
 */
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

import com.google.common.util.concurrent.Futures;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import com.flowthings.client.Credentials;
import com.flowthings.client.Serializer;
import com.flowthings.client.api.Request.Action;
import com.flowthings.client.domain.Drop;
import com.flowthings.client.domain.Types;
import com.flowthings.client.exception.AuthorizationException;
import com.flowthings.client.exception.BadRequestException;
import com.flowthings.client.exception.FlowthingsException;
import com.flowthings.client.exception.NotFoundException;
import com.flowthings.client.response.Response;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.reflect.TypeToken;

@SuppressWarnings("unchecked")
public class WebsocketApi extends Api {
  private static Map<Request.Action, String> methods = new HashMap<>();
  private String host;
  private boolean secure;
  private SimpleSocket socket;
  private RestApi restApi;
  private Random random;
  private int retryDelay = 5;
  boolean reconnect = true;
  ConcurrentHashMap<String, WSCallback> callbacks = new ConcurrentHashMap<>();
  ConcurrentHashMap<String, SubscriptionCallback<Drop>> subscriptions = new ConcurrentHashMap<>();
  ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  static {
    methods.put(Request.Action.CREATE, "create");
    methods.put(Request.Action.DELETE, "delete");
    methods.put(Request.Action.FIND, "findmany");
    methods.put(Request.Action.UPDATE, "update");
    methods.put(Request.Action.GET, "find");
    methods.put(Request.Action.SUBSCRIBE, "subscribe");
    methods.put(Request.Action.UNSUBSCRIBE, "unsubscribe");
  }

  public WebsocketApi(final Credentials credentials, final String host, boolean secure) throws FlowthingsException {
    this.host = host;
    this.secure = secure;
    final String transport = secure ? "https://" : "http://";
    this.restApi = new RestApi(credentials, host, true) {
      @Override
      protected void setRequestProperties(URLConnection connection, Map<String, Object> headers) {
        connection.setRequestProperty("X-Auth-Account", credentials.account);
        connection.setRequestProperty("X-Auth-Token", credentials.token);
      }

      @Override
      protected String toQueryString(String urlString, Map<String, String> parameters)
          throws UnsupportedEncodingException {
        return transport + host + "/session";
      }
    };
    this.socket = connect();
    this.random = new Random();

    new Thread(new Runnable() {
      @Override
      public void run() {

        while (reconnect) {
          try {
            socket.join();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          System.out.printf("Lost WS Connection\n");
          socket.close();

          while (reconnect) {
            try {
              socket = connect();
              resubscribeAll();
              break;
            } catch (FlowthingsException e) {
              System.out.println("WS Connection failed. Retry in " + retryDelay + "s");

              try {
                Thread.sleep(retryDelay * 1000);
              } catch (InterruptedException e1) {
              }
            }
          }
        }
      }

    }).start();
  }

  public WebsocketApi(Credentials credentials) throws FlowthingsException {
    this(credentials, "ws.flowthings.io", true);
  }

  public void close() {
    this.reconnect = false;
    socket.close();
  }

  protected SimpleSocket connect() throws FlowthingsException {
    String sessionId = connectHttp();
    return connectWs(sessionId);
  }

  protected String connectHttp() throws FlowthingsException {
    Request<Drop> sessionRequest = new Request<>(Action.CREATE, Types.DROP, Types.DROP.token, false);
    Drop response = restApi.send(sessionRequest);
    return response.getId();
  }

  protected SimpleSocket connectWs(String sessionId) throws FlowthingsException {
    String transport = secure ? "wss://" : "ws://";
    String url = transport + host + "/session/" + sessionId + "/ws";
    SslContextFactory ssl = new SslContextFactory();
    WebSocketClient client = secure ? new WebSocketClient(ssl) : new WebSocketClient();
    try {
      client.start();
      URI serverUrl = new URI(url);
      ClientUpgradeRequest request = new ClientUpgradeRequest();
      socket = new SimpleSocket();
      Future<Session> connect = client.connect(socket, serverUrl, request);
      System.out.printf("Connecting to : %s\n", serverUrl);
      connect.get();
      System.out.printf("Connected\n");
      return socket;
    } catch (Throwable t) {
      throw new FlowthingsException(t);
    }
  }

  public <S> FlowthingsFuture<S> send(Request<S> request) {
    WebsocketRequest<S> wsr = new WebsocketRequest<>();
    wsr.setType(methods.get(request.action));
    wsr.setMsgId("" + random.nextInt());
    wsr.setObject(request.type.name);
    wsr.setFlowId(request.flowId);
    wsr.setValue(request.bodyObject);
    String value = Serializer.toJson(wsr);
    if (request.action == Action.SUBSCRIBE) {
      SubscriptionCallback<Drop> callback = (SubscriptionCallback<Drop>) request.otherData.get("callback");
      subscriptions.put(request.flowId, callback);
    } else if (request.action == Action.UNSUBSCRIBE) {
      subscriptions.remove(request.flowId);
    }
    // Send
    try {
      socket.send(value);
    } catch (FlowthingsException e) {
      return new FlowthingsFuture<S>(Futures.<S>immediateFailedFuture(e));
    }
    // Register a callback
    SettableFuture<S> future = SettableFuture.create();
    WSCallback callback = new WSCallback(future, request.type);
    callbacks.put(wsr.getMsgId(), callback);
    return new FlowthingsFuture<>(future);
  }

  @Override
  public <S> FlowthingsFuture<S> sendAsync(final Request<S> request){
    return send(request);
  }

  private void resubscribeAll() {
    for (String flowId : subscriptions.keySet()){
      SubscriptionCallback<Drop> dropSubscriptionCallback = subscriptions.get(flowId);
      try {
        resubscribe(flowId, dropSubscriptionCallback);
        System.out.println("Resubscribed to " + flowId);
      } catch (FlowthingsException e) {
        System.out.println("Could not resubscribe to flow: " + flowId);
        e.printStackTrace();
      }
    }
  }

  private void resubscribe(String flowId, SubscriptionCallback<Drop> dropSubscriptionCallback) throws FlowthingsException {
    send(Flowthings.drop(flowId).subscribe(dropSubscriptionCallback));
  }

  @WebSocket(maxTextMessageSize = 1024 * 1024)
  public class SimpleSocket {
    protected static final String heartbeat = "{\"type\" : \"heartbeat\"}";
    private final CountDownLatch closeLatch;
    @SuppressWarnings("unused")
    private Session session;
    private ScheduledFuture<?> heartbeatHandle;

    public SimpleSocket() {
      this.closeLatch = new CountDownLatch(1);
    }

    public void close() {
      if (session != null) {
        try {
          session.close();
        } catch (Exception e){
          e.printStackTrace();
        }
      }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
      this.session = null;
      this.closeLatch.countDown();
      heartbeatHandle.cancel(false);
    }

    public synchronized void send(String message) throws FlowthingsException {
      try {
        session.getRemote().sendString(message);
      } catch (Throwable t) {
        throw new FlowthingsException(t);
      }
    }

    public void join() throws InterruptedException {
      this.closeLatch.await();
    }

    @OnWebSocketConnect
    public void onConnect(final Session session) {
      this.session = session;
      this.heartbeatHandle = scheduler.scheduleAtFixedRate(new Runnable() {
        @Override
        public void run() {
          try {
            send(heartbeat);
          } catch (FlowthingsException e) {
            e.printStackTrace();
          }
        }
      }, 10, 10, TimeUnit.SECONDS);
    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
      // Deserialize
      // TODO - greatly improve this
      WebsocketsResponse r = null;
      try {
        r = Serializer.fromJson(msg, new TypeToken<WebsocketsResponse>() {
        });
        // Must be a drop message
        if (r.getHead() == null) {
          try {
            WebsocketsDropResponse r1 = Serializer.fromJson(msg, new TypeToken<WebsocketsDropResponse>() {
            });
            if (r1.getResource() != null) {
              SubscriptionCallback<Drop> callback = subscriptions.get(r1.getResource());
              if (callback != null) {
                callback.onMessage(r1.getValue());
                return;
              }
            }
            System.out.println("Received message but didn't know what to do with it: " + msg);
          } catch (Exception e1) {
            e1.printStackTrace();
          }
          return;
        }
        String msgId = r.getHead().getMsgId();
        if (msgId != null) {
          WSCallback wsCallback = callbacks.get(msgId);
          callbacks.remove(msgId);
          if (wsCallback != null) {
            SettableFuture future = wsCallback.future;
            Object response = Serializer.fromJson(msg, wsCallback.type.token);
            int status = ((Response) response).getHead().getStatus();
            if (((Response) response).getHead().isOk()) {
              future.set(((Response) response).getBody());
            } else if (status == 404) {
              future.setException(new NotFoundException(((Response) response).getHead().getErrors().get(0)));
            } else if (status == 403) {
              future.setException(new AuthorizationException(((Response) response).getHead().getErrors().get(0)));
            } else {
              future.setException(new BadRequestException(((Response) response).getHead().getErrors().get(0)));
            }
            callbacks.remove(msgId);
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private static class WSCallback {
    public SettableFuture future;
    public Types type;

    public WSCallback(SettableFuture future, Types type) {
      super();
      this.future = future;
      this.type = type;
    }
  }
}
