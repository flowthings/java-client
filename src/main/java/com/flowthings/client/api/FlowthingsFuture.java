package com.flowthings.client.api;

import com.flowthings.client.exception.AsyncException;
import com.flowthings.client.exception.FlowthingsException;
import com.flowthings.client.response.Response;
import com.google.common.util.concurrent.Futures;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by matt on 7/5/16.
 */
public class FlowthingsFuture<T> implements Future<T> {

  private final Future<T> inner;

  public FlowthingsFuture(Future<T> inner){
    this.inner = inner;
  }

  public static <S> FlowthingsFuture<S> fromResult(S result){
    return new FlowthingsFuture<>(Futures.immediateFuture(result));
  }
  public static <S> FlowthingsFuture<S> fromException(FlowthingsException e){
    return new FlowthingsFuture<>(Futures.<S>immediateFailedFuture(e));
  }

  @Override
  public boolean cancel(boolean mayInterruptIfRunning) {
    return inner.cancel(mayInterruptIfRunning);
  }

  @Override
  public boolean isCancelled() {
    return inner.isCancelled();
  }

  @Override
  public boolean isDone() {
    return inner.isDone();
  }

  @Override
  public T get() throws InterruptedException, ExecutionException {
    return inner.get();
  }

  @Override
  public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    return inner.get(timeout, unit);
  }

  /**
   * Works like "get", but returns a sane exception type
   */
  public T grab() throws FlowthingsException {
    try {
      return this.get();
    } catch (InterruptedException e) {
      throw new AsyncException(e);
    } catch (ExecutionException e) {
      Throwable cause = e.getCause();
      if (cause instanceof FlowthingsException){
        throw (FlowthingsException) cause;
      } else {
        throw new FlowthingsException(cause);
      }
    }
  }
}
