package tech.bison.transportapp.view.searchconnections;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import tech.bison.transport.Connection;
import tech.bison.transport.PublicTransportServiceUnvailableException;
import tech.bison.transport.Section;
import tech.bison.transport.Station;
import tech.bison.transport.Transport;
import tech.bison.utils.AutoCompleteTextField;
import tech.bison.utils.FXUtils;

public class SearchConnectionController {
  @FXML
  private AutoCompleteTextField txtStart;
  @FXML
  private AutoCompleteTextField txtDestination;
  @FXML
  private TextField txtHours;
  @FXML
  private TextField txtMinutes;
  @FXML
  private DatePicker datePicker;
  @FXML
  private TableView<Connection> tableView;
  @FXML
  private TableColumn<Connection, String> columnBus;
  @FXML
  private TableColumn<Connection, String> columnDestination;
  @FXML
  private TableColumn<Connection, String> columnDepartureTime;
  @FXML
  private TableColumn<Connection, String> columnArrivalTime;
  private Transport transport = new Transport();
  private ObservableList<Connection> connections = FXCollections.observableArrayList();
  private DateFormat dateFormat = new SimpleDateFormat("HH:mm dd:MM:yyyy");

  @FXML
  private void initialize() {
    tableView.setPlaceholder(new Label(""));
    FXUtils.onlyAllowNumbers(0, 23, txtHours);
    FXUtils.onlyAllowNumbers(0, 59, txtMinutes);

    txtHours.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH")));
    txtMinutes.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("mm")));
    datePicker.setValue(LocalDate.now());
    columnBus.setCellValueFactory(cellData -> {
      StringBuilder sb = new StringBuilder();
      for (Section section : cellData.getValue().getSections()) {
        try {
          sb.append(section.getJourney().getName() + "\n");
        } catch (NullPointerException e) {
          // somtimes the api returns null
        }
      }
      return new SimpleStringProperty(sb.toString().substring(0, sb.length() - 2));
    });
    columnDestination.setCellValueFactory(cellData -> {
      return new SimpleStringProperty(cellData.getValue().getTo().getStation().getName());
    });
    columnDepartureTime.setCellValueFactory(
        cellData -> new SimpleStringProperty(getFromattedDate(cellData.getValue().getFrom().getDeparture())));
    columnArrivalTime.setCellValueFactory(
        cellData -> new SimpleStringProperty(getFromattedDate(cellData.getValue().getTo().getArrival())));

    tableView.setItems(connections);
  }

  @FXML
  private void onSearchClick() {
    try {
      List<Connection> connections;
      if (dateIsValid()) {
        connections = transport.getConnections(txtStart.getText(), txtDestination.getText(),
            LocalDateTime.of(datePicker.getValue().getYear(), datePicker.getValue().getMonth(),
                datePicker.getValue().getDayOfMonth(), Integer.parseInt(txtHours.getText()),
                Integer.parseInt(txtMinutes.getText())))
            .getConnections();
      } else {
        connections = transport.getConnections(txtStart.getText(), txtDestination.getText()).getConnections();
      }
      tableView.getItems().clear();
      this.connections.clear();
      this.connections.addAll(connections);
      if (connections.size() == 0) {
        tableView.setPlaceholder(new Label("No results"));
      }
      tableView.refresh();
    } catch (PublicTransportServiceUnvailableException e) {
      FXUtils.showErrorAlert("Fehler bei der Verbindun zum Server!",
          "Prüfen sie ihre Internetverbindung und versuchen sie es später nochmal.");
      e.printStackTrace();
    }
  }

  private boolean dateIsValid() {
    try {
      // throws exception if not valid
      LocalDate.of(datePicker.getValue().getYear(), datePicker.getValue().getMonth(),
          datePicker.getValue().getDayOfMonth());
      return true;
    } catch (DateTimeException e) {
      return false;
    }
  }

  private String getFromattedDate(Date date) {
    return dateFormat.format(date);
  }

  @FXML
  private void onTxtStartKeyPressed() {
    try {
      List<Station> stations = transport.getStations(txtStart.getText()).getStations();
      txtStart.getEntries().clear();
      for (Station station : stations) {
        txtStart.getEntries().add(station.getName());
      }
    } catch (PublicTransportServiceUnvailableException e) {
      // Not refresh when api is done or just don't start to show
    }
  }

  @FXML
  private void onTxtDestinationKeyPressed() {
    try {
      List<Station> stations = transport.getStations(txtDestination.getText()).getStations();
      txtDestination.getEntries().clear();
      for (Station station : stations) {
        txtDestination.getEntries().add(station.getName());
      }
    } catch (PublicTransportServiceUnvailableException e) {
      // Not refresh when api is done or just don't start to show
    }
  }
}
