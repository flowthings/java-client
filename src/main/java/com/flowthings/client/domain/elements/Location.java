package com.flowthings.client.domain.elements;

import java.util.HashMap;

public class Location {
  protected double latitude;
  protected double longitude;
  /** Allowable values are city, street, state,zip. */
  protected HashMap<String, Object> specifiers = new HashMap<>();

  public Location() {
  }

  public Location(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public HashMap<String, Object> getSpecifiers() {
    return specifiers;
  }

  public void setSpecifiers(HashMap<String, Object> specifiers) {
    this.specifiers = specifiers;
  }

  @Override
  public String toString() {
    return "Location [latitude=" + latitude + ", longitude=" + longitude + ", specifiers=" + specifiers + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(latitude);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(longitude);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((specifiers == null) ? 0 : specifiers.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Location other = (Location) obj;
    if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude)) {
      return false;
    }
    if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude)) {
      return false;
    }
    if (specifiers == null) {
      if (other.specifiers != null) {
        return false;
      }
    } else if (!specifiers.equals(other.specifiers)) {
      return false;
    }
    return true;
  }

}
