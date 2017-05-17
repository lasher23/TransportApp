package tech.bison.transport;

import com.google.gson.annotations.SerializedName;

public class Coordinate {
  private String type;
  @SerializedName("x")
  private double xCoordinate;
  @SerializedName("y")
  private double yCoordinate;

  /**
   * Trainstation, address, point of adress
   */
  public String getType() {
    return type;
  }

  public double getxCoordinate() {
    return xCoordinate;
  }

  public double getyCoordinate() {
    return yCoordinate;
  }
}
