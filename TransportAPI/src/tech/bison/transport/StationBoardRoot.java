package tech.bison.transport;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class StationBoardRoot {
  private Station station;
  @SerializedName("stationboard")
  private List<StationBoard> entries;

  public Station getStation() {
    return station;
  }

  public List<StationBoard> getEntries() {
    return entries;
  }
}
