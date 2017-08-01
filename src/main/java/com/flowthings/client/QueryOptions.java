package com.flowthings.client;

import java.util.HashMap;
import java.util.Map;

import com.flowthings.client.api.DomainObjectApi;

/**
 * <p>
 * When making a {@link DomainObjectApi#find(QueryOptions)} request, use
 * QueryOptions to specify:
 * </p>
 * 
 * <ul>
 * <li>The <a
 * href="https://flowthings.io/docs/flowthings-filter-language">Filter
 * String</a>, for restricting the result set returned</li>
 * <li>The number of records to skip</li>
 * <li>A limit to the number of records returned</li>
 * <li>The order in which to sort the results</li>
 * </ul>
 * 
 * <p>
 * For examples, see <a href="https://flowthings.io/docs/http-flow-find">HTTP
 * Flow Find</a>
 * </p>
 * 
 * @author matt
 */
public class QueryOptions {
  public static final int DEFAULT_BATCH_SIZE = 20;
  public static final int DEFAULT_LIMIT_SIZE = 20;
  public static final int NO_LIMIT = Integer.MAX_VALUE;
  protected Map<String, SortType> sorts = new HashMap<>();
  protected Integer skip;
  protected Integer limit;
  protected String filter;
  protected Map<String,String> other = new HashMap<>();

  /**
   * The order in which to sort the results
   * 
   * @param sort
   *          - a field to sort by
   * @param sortType
   *          - order of the sort
   * @return - this
   */
  public QueryOptions sort(String sort, SortType sortType) {
    sorts.clear();
    sorts.put(sort, sortType);
    return this;
  }

  /**
   * The number of records from the beginning which we shall skip
   * 
   * @param ct
   *          - number of records to skip
   * @return - this
   */
  public QueryOptions skip(int ct) {
    this.skip = ct;
    return this;
  }

  /**
   * To limit the number of results returned
   * 
   * @param ct
   *          - the number of results returned (default 20)
   * @return - this
   */
  public QueryOptions limit(int ct) {
    this.limit = ct;
    return this;
  }

  /**
   * For defining the parameters of the search
   * 
   * @param filterStr
   *          - flowthings filter language
   * @return - this
   */
  public QueryOptions filter(String filterStr) {
    this.filter = filterStr;
    return this;
  }

  /**
   * For adding additional query parameters to the request.
   * Not guaranteed to have any affect!
   *
   */
  public void addOtherOption(String key, String value){
    this.other.put(key, value);
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
    for (String key : other.keySet()){
      results.put(key, other.get(key));
    }

    return results;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    QueryOptions that = (QueryOptions) o;

    if (sorts != null ? !sorts.equals(that.sorts) : that.sorts != null)
      return false;
    if (skip != null ? !skip.equals(that.skip) : that.skip != null)
      return false;
    if (limit != null ? !limit.equals(that.limit) : that.limit != null)
      return false;
    return !(filter != null ? !filter.equals(that.filter) : that.filter != null);

  }

  @Override
  public int hashCode() {
    int result = sorts != null ? sorts.hashCode() : 0;
    result = 31 * result + (skip != null ? skip.hashCode() : 0);
    result = 31 * result + (limit != null ? limit.hashCode() : 0);
    result = 31 * result + (filter != null ? filter.hashCode() : 0);
    return result;
  }
}