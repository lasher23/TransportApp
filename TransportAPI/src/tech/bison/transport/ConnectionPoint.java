package tech.bison.transport;

import java.util.Date;

public class ConnectionPoint {
  private Station station;
  private Date arrival;
  private String arrivalTimestamp;
  private Date departure;
  private String departureTimestamp;
  private int delay;
  private String platform;
  private String realtimeAvailability;

  public Station getStation() {
    return station;
  }

  public Date getArrival() {
    return arrival;
  }

  public String getArrivalTimestamp() {
    return arrivalTimestamp;
  }

  public Date getDeparture() {
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
