package tech.bison.transport;

public class Station {
  private String id;
  private String name;
  private int score;
  private Coordinate coordinate;
  private double distance;

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getScore() {
    return score;
  }

  public Coordinate getCoordinate() {
    return coordinate;
  }

  public double getDistance() {
    return distance;
  }
}
