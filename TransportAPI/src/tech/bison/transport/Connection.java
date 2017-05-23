package tech.bison.transport;

import java.util.List;

public class Connection {
  private ConnectionPoint from;
  private ConnectionPoint to;
  private String duration;
  private List<Section> sections;

  public ConnectionPoint getFrom() {
    return from;
  }

  public ConnectionPoint getTo() {
    return to;
  }

  public String getDuration() {
    return duration;
  }

  public List<Section> getSections() {
    return sections;
  }
}
