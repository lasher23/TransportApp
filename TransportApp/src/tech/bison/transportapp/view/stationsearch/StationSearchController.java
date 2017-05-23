package tech.bison.transportapp.view.stationsearch;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Comparator;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import tech.bison.transport.Coordinate;
import tech.bison.transport.PublicTransportServiceUnvailableException;
import tech.bison.transport.Station;
import tech.bison.transport.Transport;
import tech.bison.utils.AutoCompleteTextField;
import tech.bison.utils.FXUtils;

public class StationSearchController {
  @FXML
  private AutoCompleteTextField txtStation;
  @FXML
  private VBox panelResult;
  private TableView<Station> tableResults = new TableView<>();
  private TableColumn<Station, String> columnName = new TableColumn<>("Station");
  private TableColumn<Station, String> columnDistance = new TableColumn<>("Distanz");
  private ImageView imgMap = new ImageView();
  private Transport transport = new Transport();
  private static final String BASE_URL = "https://maps.googleapis.com/maps/api/staticmap";
  private static final int VERTICAL_PIXELS = 400;
  private static final int HORIZONTAL_PIXELS = 500;
  private static final int ZOOM_LEVEL = 14;
  private static final String GOOGLE_MAPS_API_KEY = "AIzaSyAfROinP2rwqQ17ps-G9G1ERmWraOIYrAE";

  @FXML
  private void initialize() {
    tableResults.getColumns().add(columnName);
    tableResults.getColumns().add(columnDistance);
    tableResults.setOnMouseClicked(event -> {
      if (event.getClickCount() == 2) {
        txtStation.setText(tableResults.getSelectionModel().getSelectedItem().getName());
        onSearch();
      }
    });
    columnName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
    columnDistance.setCellValueFactory(
        param -> new SimpleStringProperty(String.valueOf(param.getValue().getDistance())));
    Platform.runLater(txtStation::requestFocus);
  }

  @FXML
  private void onSearch() {
    panelResult.getChildren().clear();
    panelResult.getChildren().add(imgMap);
    List<Station> stations;
    try {
      stations = transport.getStations(txtStation.getText()).getStations();
      if (stations.size() > 1) {
        FXUtils.showErrorAlert("Zu viele Stationen!", "Geben sie eine einmalige Station ein");
      } else {
        double pointX = stations.get(0).getCoordinate().getxCoordinate();
        double pointY = stations.get(0).getCoordinate().getyCoordinate();
        URL url = new URL(BASE_URL + "?=center" + pointX + "," + pointY + "&size="
            + HORIZONTAL_PIXELS + "x" + VERTICAL_PIXELS + "&markers=" + pointX + "," + pointY
            + "&zoom=" + ZOOM_LEVEL + "&" + GOOGLE_MAPS_API_KEY);
        imgMap.setImage(getImageFromURL(url));
      }
    } catch (PublicTransportServiceUnvailableException | IOException e) {
      FXUtils.showErrorAlert("Keine Verbindung!",
          "Es konnte keine Verbindung mit dem Server hergestellt werden.");
    }
  }

  private Image getImageFromURL(URL url) throws IOException {
    URLConnection connection = url.openConnection();
    return new Image(connection.getInputStream(), HORIZONTAL_PIXELS, VERTICAL_PIXELS, false, false);
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
      // ignore if doesn't refresh
    }
  }

  @FXML
  private void onSearchNearBy() {
    try {
      panelResult.getChildren().clear();
      Coordinate coordinate = transport.getStations(txtStation.getText()).getStations().get(0)
          .getCoordinate();
      ObservableList<Station> items = FXCollections.observableArrayList(transport
          .getStations(coordinate.getxCoordinate(), coordinate.getyCoordinate()).getStations());
      tableResults.setItems(items);
      panelResult.getChildren().add(tableResults);
    } catch (PublicTransportServiceUnvailableException e) {
      FXUtils.showErrorAlert("Verbindung Fehlgeschlagen!",
          "Konnte nicht mit dem Server verbinden.");
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

  @FXML
  private void keyPressed(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER) {
      onSearch();
    }
  }
}
