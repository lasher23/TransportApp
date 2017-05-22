package tech.bison.transportapp.view.stationsearch;

import java.util.Comparator;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import tech.bison.transport.PublicTransportServiceUnvailableException;
import tech.bison.transport.Station;
import tech.bison.transport.Transport;
import tech.bison.utils.AutoCompleteTextField;
import tech.bison.utils.FXUtils;

public class StationSearchController {
  @FXML
  private WebView webView;
  @FXML
  private AutoCompleteTextField txtStation;
  private Transport transport = new Transport();

  @FXML
  private void onSearch() {
    List<Station> stations;
    try {
      stations = transport.getStations(txtStation.getText()).getStations();
      if (stations.size() > 1) {
        FXUtils.showErrorAlert("Zu viele Stationen!", "Geben sie eine einmalige Station ein");
      } else {

        WebEngine engine = webView.getEngine();
        engine.load("https://www.google.ch/maps?q=loc:" + stations.get(0).getCoordinate().getxCoordinate() + "+"
            + stations.get(0).getCoordinate().getyCoordinate());
      }
    } catch (PublicTransportServiceUnvailableException e) {
      FXUtils.showErrorAlert("Keine Verbindung!", "Es konnte keine Verbindung mit dem Server hergestellt werden.");
    }
  }

  @FXML
  private void onTxtStationKeyPressed() {
    try {
      List<Station> stations = transport.getStations(txtStation.getText()).getStations();
      stations.sort(getComparator());
      txtStation.getEntries().clear();
      for (Station station : stations) {
        txtStation.getEntries().add(station.getName());
      }
    } catch (PublicTransportServiceUnvailableException e) {
      // TODO: handle exception
    }
  }

  private Comparator<Station> getComparator() {
    return (o1, o2) -> {
      if (o1.getScore() > o2.getScore()) {
        return 1;
      } else if (o1.getScore() < o2.getScore()) {
        return -1;
      }
      return 0;
    };
  }
}
