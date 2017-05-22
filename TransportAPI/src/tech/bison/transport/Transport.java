package tech.bison.transport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.common.net.UrlEscapers;
import com.google.gson.Gson;

public class Transport {
  public Stations getStations(String query) throws PublicTransportServiceUnvailableException {
    try {
      URL url = new URL(
          "http://transport.opendata.ch/v1/locations?query=" + UrlEscapers.urlFormParameterEscaper().escape(query));

      String output = getStringFromUrl(url);

      return new Gson().fromJson(output, Stations.class);
    } catch (Exception e) {
      throw new PublicTransportServiceUnvailableException(e);
    }
  }

  public StationBoardRoot getStationBoard(String station, String id) throws PublicTransportServiceUnvailableException {
    try {
      URL url = new URL("http://transport.opendata.ch/v1/stationboard?Station="
          + UrlEscapers.urlFormParameterEscaper().escape(station) + "&id="
          + UrlEscapers.urlFormParameterEscaper().escape(id));
      String output = getStringFromUrl(url);

      return new Gson().fromJson(output, StationBoardRoot.class);
    } catch (Exception e) {
      throw new PublicTransportServiceUnvailableException(e);
    }
  }

  public Connections getConnections(String fromStation, String toStation)
      throws PublicTransportServiceUnvailableException {
    return getConnections(fromStation, toStation, LocalDateTime.now());
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

  public Connections getConnections(String fromStation, String toStation, LocalDateTime dateTime)
      throws PublicTransportServiceUnvailableException {
    try {
      DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
      URL url = new URL("http://transport.opendata.ch/v1/connections?from="
          + UrlEscapers.urlFormParameterEscaper().escape(fromStation) + "&to="
          + UrlEscapers.urlFormParameterEscaper().escape(toStation) + "&date=" + dateTime.format(formatterDate)
          + "&time=" + dateTime.format(formatterTime));

      String output = getStringFromUrl(url);
      return new Gson().fromJson(output, Connections.class);
    } catch (Exception e) {
      throw new PublicTransportServiceUnvailableException(e);
    }
  }

}
