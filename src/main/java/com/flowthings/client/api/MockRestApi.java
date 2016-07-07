package com.flowthings.client.api;

import com.flowthings.client.Credentials;
import com.flowthings.client.domain.Drop;
import com.flowthings.client.domain.Types;
import com.flowthings.client.exception.ConnectionRefusedException;
import com.flowthings.client.exception.FlowthingsException;
import com.google.common.util.concurrent.SettableFuture;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A utility class for testing the behaviour of a REST connection.
 * No actual connection is made - it is faked
 * This class has two purposes:
 *
 * 1) You can supply responses to requests you make
 * 2) You can simulate disconnections
 *
 * Created by matt on 7/6/16.
 */
public class MockRestApi extends RestApi {

  private boolean canConnect = true;
  private ConcurrentHashMap<Request, Callable> answers = new ConcurrentHashMap<>();
  private ConcurrentHashMap<Request, AtomicInteger> counters = new ConcurrentHashMap<>();

  private ExecutorService pool = Executors.newCachedThreadPool();

  public MockRestApi() throws FlowthingsException {
    super(new Credentials("a","b"));
  }

  public MockRestApi setAnswer(Request request, Callable response){
    answers.put(request, response);
    return this;
  }
  public MockRestApi setAnswers(Request request, Object... responses){
    final AtomicInteger counter = new AtomicInteger();
    answers.put(request, () -> {
      int i = counter.getAndIncrement();
      i = Math.min(responses.length - 1, i);

      Object response = responses[i];
      if (response instanceof FlowthingsException){
        throw (FlowthingsException) response;
      }

      return response;
    });
    return this;
  }
  public MockRestApi setException(Request request, FlowthingsException response){
    answers.put(request, () -> {throw response;});
    return this;
  }
  public MockRestApi setAnswerImmediately(Request request, Object response){
    answers.put(request, () -> response);
    return this;
  }

  @Override
  public <S> FlowthingsFuture<S> sendAsync(Request<S> request) {
    Request.Action action = request.action;
    Types type = request.type;
    final Callable provider = answers.get(request);


    // Increment counter
    counters.compute(request, (k, v) -> {
      if (v == null){
        return new AtomicInteger(0);
      }
      v.incrementAndGet();
      return v;
    });

    if (provider != null){
      Future<S> future = pool.submit(provider);
      return new FlowthingsFuture<>(future);
    }

    // Block forever
    return new FlowthingsFuture<>(SettableFuture.<S>create());
  }

  public int getCounter(Request request){
    return counters.getOrDefault(request, new AtomicInteger(0)).get();
  }
}

