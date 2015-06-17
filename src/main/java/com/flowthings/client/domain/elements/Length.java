package com.flowthings.client.domain.elements;

public class Length {

  public enum Units {
    MILES, KILOMETERS, METERS, FEET;
  }

  public final Units units;
  public final double magnitude;

  public Length(Units units, double magnitude) {
    this.units = units;
    this.magnitude = magnitude;
  }

  public Units getUnits() {
    return units;
  }

  public double getMagnitude() {
    return magnitude;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(magnitude);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((units == null) ? 0 : units.hashCode());
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
    Length other = (Length) obj;
    if (Double.doubleToLongBits(magnitude) != Double.doubleToLongBits(other.magnitude)) {
      return false;
    }
    if (units != other.units) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Length [units=" + units + ", magnitude=" + magnitude + "]";
  }

}
