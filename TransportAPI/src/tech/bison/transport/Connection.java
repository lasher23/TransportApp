package tech.bison.transport;

public class Connection {
  private ConnectionPoint from;
  private ConnectionPoint to;
  private String duration;

  public ConnectionPoint getFrom() {
    return from;
  }

  public ConnectionPoint getTo() {
    return to;
  }

  public String getDuration() {
    return duration;
  }
}
