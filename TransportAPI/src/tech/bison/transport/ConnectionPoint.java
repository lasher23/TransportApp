package tech.bison.transport;

public class ConnectionPoint {
  private Station station;
  private String arrival;
  private String arrivalTimestamp;
  private String departure;
  private String departureTimestamp;
  private int delay;
  private String platform;
  private String realtimeAvailability;

  public Station getStation() {
    return station;
  }

  public String getArrival() {
    return arrival;
  }

  public String getArrivalTimestamp() {
    return arrivalTimestamp;
  }

  public String getDeparture() {
    return departure;
  }

  public String getDepartureTimestamp() {
    return departureTimestamp;
  }

  public int getDelay() {
    return delay;
  }

  public String getPlatform() {
    return platform;
  }

  public String getRealtimeAvailability() {
    return realtimeAvailability;
  }
}
