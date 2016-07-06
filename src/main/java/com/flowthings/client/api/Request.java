package com.flowthings.client.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.flowthings.client.Credentials;
import com.flowthings.client.QueryOptions;
import com.flowthings.client.Serializer;
import com.flowthings.client.domain.Device;
import com.flowthings.client.domain.Types;
import com.flowthings.client.response.Response;
import com.google.gson.reflect.TypeToken;

/**
 * A Request object represents a command that can be sent to flowthings.io, e.g
 * to Create, Get, Find, Update or Delete and object. These apply to the common
 * flowthings.io objects, such as Flows, Tracks, and Drops.
 * 
 * <p>
 * In most cases, requests can be sent over any of the APIs (REST, WebSockets).
 * If the request is naturally idempotent, such as Find or Get, they can be
 * reused multiple times.
 * </p>
 *
 * <pre>
 * {@code
 *  
 *  Credentials credentials = new Credentials(accountName, tokenString);
 *  RestApi api = new RestApi(credentials);
 *  
 *  List<Flow> results = api.send(Flowthings.flow().find(new QueryOptions().limit(5)));
 * }
 * </pre>
 *
 * @author matt
 * @param <S>
 *          the object type
 */
public class Request<S> {

  private static final Pattern ID_PATTERN = Pattern.compile("[a-z][0-9a-f]{24}");

  protected static Logger logger = Logger.getLogger("com.flow.client.Request");

  public enum Action {
    CREATE, UPDATE, DELETE, GET, FIND, SUBSCRIBE, UNSUBSCRIBE
  }

  protected String id;
  protected QueryOptions queryOptions = new QueryOptions();
  protected String body;
  protected TypeToken<? extends Response<S>> typeToken;
  protected Action action;
  protected Types type;
  protected String member;
  protected String flowId;
  protected boolean listResponse;
  protected S bodyObject;
  protected Map<String, Object> otherData = new HashMap<>();

  @SuppressWarnings("unchecked")
  public static <S> Request<S> createObjectRequest(Class<S> domainObjectType, Action action) {
    Types type = Types.get(domainObjectType);
    return new Request<>(action, type, type.token, false);
  }

  @SuppressWarnings("unchecked")
  public static <S> Request<List<S>> createListRequest(Class<S> domainObjectType, Action action) {
    Types type = Types.getListType(domainObjectType);
    return new Request<>(action, type, type.token, true);
  }

  public Request(Action action, Types type, TypeToken<? extends Response<S>> typeToken, boolean listResponse) {
    this.action = action;
    this.type = type;
    this.typeToken = typeToken;
    this.listResponse = listResponse;
  }

  public Request<S> member(String member) {
    this.member = member;
    return this;
  }

  public Request<S> id(String id) {
    if (!ID_PATTERN.matcher(id).matches()) {
      throw new IllegalArgumentException(String.format("\"%s\" is not a valid id", id));
    }
    this.id = id;
    return this;
  }

  public Request<S> flowId(String flowId) {
    this.flowId = flowId;
    return this;
  }

  public Request<S> params(QueryOptions queryOptions) {
    this.queryOptions = queryOptions;
    return this;
  }

  public Request<S> body(S s) {
    this.bodyObject = s;
    this.body = Serializer.toJson(s);
    return this;
  }

  public Request<S> addData(String k, Object o) {
    this.otherData.put(k, o);
    return this;
  }

  public String buildHttpPath(Credentials credentials, String url) {
    StringBuffer sb = new StringBuffer(url);
    sb.append("/").append(credentials.account).append("/").append(type.name);

    if (flowId != null) {
      sb.append("/").append(flowId);
    }

    if (id != null) {
      sb.append("/").append(id);
      if (member != null) {
        sb.append("/").append(member);
      }
    }

    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Request<?> request = (Request<?>) o;

    if (listResponse != request.listResponse) return false;
    if (id != null ? !id.equals(request.id) : request.id != null) return false;
    if (queryOptions != null ? !queryOptions.equals(request.queryOptions) : request.queryOptions != null)
      return false;
    if (body != null ? !body.equals(request.body) : request.body != null)
      return false;
    if (typeToken != null ? !typeToken.equals(request.typeToken) : request.typeToken != null)
      return false;
    if (action != request.action) return false;
    if (type != request.type) return false;
    if (member != null ? !member.equals(request.member) : request.member != null)
      return false;
    if (flowId != null ? !flowId.equals(request.flowId) : request.flowId != null)
      return false;
    if (bodyObject != null ? !bodyObject.equals(request.bodyObject) : request.bodyObject != null)
      return false;
    return !(otherData != null ? !otherData.equals(request.otherData) : request.otherData != null);

  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (queryOptions != null ? queryOptions.hashCode() : 0);
    result = 31 * result + (body != null ? body.hashCode() : 0);
    result = 31 * result + (typeToken != null ? typeToken.hashCode() : 0);
    result = 31 * result + (action != null ? action.hashCode() : 0);
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + (member != null ? member.hashCode() : 0);
    result = 31 * result + (flowId != null ? flowId.hashCode() : 0);
    result = 31 * result + (listResponse ? 1 : 0);
    result = 31 * result + (bodyObject != null ? bodyObject.hashCode() : 0);
    result = 31 * result + (otherData != null ? otherData.hashCode() : 0);
    return result;
  }
}
