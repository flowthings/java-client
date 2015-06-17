package com.flowthings.client;

// ------------------------------------------------
// Sort Obj
// ------------------------------------------------
public enum SortType {
  ASCENDING(1, "asc"), DESCENDING(-1, "desc"), GEOSPATIAL("2d", "geo");
  public final Object specifier;
  public final String value;

  private SortType(Object specifier, String value) {
    this.specifier = specifier;
    this.value = value;
  }

  public static SortType fromString(String s) {
    if (s != null && s.toLowerCase().startsWith("asc")) return ASCENDING;
    return DESCENDING;
  }

  @Override
  public String toString() {
    return value;
  }
}