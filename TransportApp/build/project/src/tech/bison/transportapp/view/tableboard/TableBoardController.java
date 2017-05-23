package tech.bison.transportapp.view.tableboard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import tech.bison.transport.PublicTransportServiceUnvailableException;
import tech.bison.transport.Station;
import tech.bison.transport.StationBoard;
import tech.bison.transport.Transport;
import tech.bison.utils.AutoCompleteTextField;
import tech.bison.utils.FXUtils;

public class TableBoardController {
  @FXML
  private TableView<StationBoard> table;
  @FXML
  private TableColumn<StationBoard, String> columnName;
  @FXML
  private TableColumn<StationBoard, String> columnDeparture;
  @FXML
  private TableColumn<StationBoard, String> columnDestination;
  @FXML
  private AutoCompleteTextField txtStationName;
  private Transport transport = new Transport();
  private DateFormat dateFormat = new SimpleDateFormat("HH:mm");
  private ObservableList<StationBoard> stationBoardEntries = FXCollections.observableArrayList();

  @FXML
  private void initialize() {
    columnName
        .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
    columnDeparture.setCellValueFactory(cellData -> new SimpleStringProperty(
        dateFormat.format(cellData.getValue().getStop().getDate())));
    columnDestination
        .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTo()));
    table.setItems(stationBoardEntries);
    Platform.runLater(txtStationName::requestFocus);
  }

  @FXML
  private void onTxtStationNameKeyPressed() {
    try {
      List<Station> stations = transport.getStations(txtStationName.getText()).getStations();
      stations.sort(getComparator());
      txtStationName.getEntries().clear();
      for (Station station : stations) {
        txtStationName.getEntries().add(station.getName());
      }
    } catch (PublicTransportServiceUnvailableException e) {
      // dont update Autocomplete on Server Error
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
  private void onSearchClick() {
    List<Station> stations;
    try {
      stations = transport.getStations(txtStationName.getText()).getStations();
      if (stations.isEmpty()) {
        FXUtils.showErrorAlert("Station nicht vorhanden!",
            "Es wurde keine Station mit diesem Namen gefunden.");
      } else if (stations.size() > 1) {
        FXUtils.showErrorAlert("Zu viele Stationen",
            "Es wurde keine Eindeutige Station gefunden, geben sie einen eindeutigen Namen ein!");
      } else {
        printStationBoard(stations.get(0));
      }
    } catch (Exception e) {
      FXUtils.showErrorAlert("Fehler bei der Verbindun zum Server!",
          "Prüfen sie ihre Internetverbindung und versuchen sie es später nochmal.");
    }
  }

  private void printStationBoard(Station station) throws PublicTransportServiceUnvailableException {
    List<StationBoard> stationBoards = transport.getStationBoard(station.getName(), station.getId())
        .getEntries();
    if (stationBoards.isEmpty()) {
      FXUtils.showErrorAlert("Keine Station gefunden!", "");
    } else {
      for (StationBoard stationBoard : stationBoards) {

        stationBoardEntries.add(stationBoard);
      }
    }
  }

  @FXML
  private void keyPressed(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER) {
      onSearchClick();
    }
  }
}
