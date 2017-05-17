package tech.bison.transport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.Gson;

public class Transport {
  public Stations getStations(String query) throws PublicTransportServiceUnvailableException {
    try {
      URL url = new URL("http://transport.opendata.ch/v1/locations?query=" + query);

      String output = getStringFromUrl(url);

      return new Gson().fromJson(output, Stations.class);
    } catch (Exception e) {
      throw new PublicTransportServiceUnvailableException(e);
    }
  }

  public StationBoardRoot getStationBoard(String station, String id) throws PublicTransportServiceUnvailableException {
    try {
      URL url = new URL("http://transport.opendata.ch/v1/stationboard?Station=" + station + "&id=" + id);

      String output = getStringFromUrl(url);

      return new Gson().fromJson(output, StationBoardRoot.class);
    } catch (Exception e) {
      throw new PublicTransportServiceUnvailableException(e);
    }
  }

  public Connections getConnections(String fromStation, String toStation)
      throws PublicTransportServiceUnvailableException {
    try {
      URL url = new URL("http://transport.opendata.ch/v1/connections?from=" + fromStation + "&to=" + toStation);

      String output = getStringFromUrl(url);

      return new Gson().fromJson(output, Connections.class);
    } catch (Exception e) {
      throw new PublicTransportServiceUnvailableException(e);
    }
  }

  private String getStringFromUrl(URL url) throws IOException {
    URLConnection connection = url.openConnection();
    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    StringBuilder output = new StringBuilder();
    String tempOutput;
    while ((tempOutput = reader.readLine()) != null) {
      output.append(tempOutput);
    }
    return output.toString();
  }

}
