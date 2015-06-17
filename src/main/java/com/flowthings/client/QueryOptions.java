package com.flowthings.client;

import java.util.HashMap;
import java.util.Map;

public class QueryOptions {
  public static final int DEFAULT_BATCH_SIZE = 20;
  public static final int DEFAULT_LIMIT_SIZE = 20;
  public static final int NO_LIMIT = Integer.MAX_VALUE;
  protected Map<String, SortType> sorts = new HashMap<>();
  protected Integer skip;
  protected Integer limit;
  protected String filter;

  public QueryOptions sort(String sort, SortType sortType) {
    sorts.clear();
    sorts.put(sort, sortType);
    return this;
  }

  public QueryOptions skip(int ct) {
    this.skip = ct;
    return this;
  }

  public QueryOptions limit(int ct) {
    this.limit = ct;
    return this;
  }

  public QueryOptions filter(String filter) {
    this.filter = filter;
    return this;
  }

  public Map<String, String> toMap() {
    Map<String, String> results = new HashMap<>();
    if (skip != null) {
      results.put("skip", skip.toString());
    }
    if (limit != null) {
      results.put("limit", limit.toString());
    }
    if (filter != null) {
      results.put("filter", filter.toString());
    }
    if (!sorts.isEmpty()) {
      results.put("sort", sorts.keySet().iterator().next());
      results.put("order", sorts.values().iterator().next().toString());
    }

    return results;
  }

}