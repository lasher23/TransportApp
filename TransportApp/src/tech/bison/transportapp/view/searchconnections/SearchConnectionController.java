package tech.bison.transportapp.view.searchconnections;

import java.util.ArrayList;
import java.util.List;

import org.controlsfx.control.textfield.TextFields;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import tech.bison.transport.Connection;
import tech.bison.transport.PublicTransportServiceUnvailableException;
import tech.bison.transport.Station;
import tech.bison.transport.Transport;

public class SearchConnectionController {
  @FXML
  private TextField txtStart;
  @FXML
  private TextField txtDestination;
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

  @FXML
  private void initialize() {
    setAutocompleteToTextField(txtStart);
    setAutocompleteToTextField(txtDestination);
    tableView.setItems(connections);

    columnBus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFrom().getPlatform()));
  }

  @FXML
  private void onSearchClick() {
    try {
      this.connections.addAll(transport.getConnections(txtStart.getText(), txtDestination.getText()).getConnections());
      System.out.println("hep");
      for (Connection connection : connections) {
        System.out.println(connection.getDuration());
      }
      tableView.refresh();
    } catch (PublicTransportServiceUnvailableException e) {
      e.printStackTrace();
      Alert alert = new Alert(AlertType.ERROR);
      alert.setHeaderText("Fehler bei der Verbindun zum Server!");
      alert.setContentText("Pr�fen sie ihre Internetverbindung und versuchen sie es sp�ter nochmal.");
      alert.show();
    }
  }

  private void setAutocompleteToTextField(TextField textField) {
    textField.textProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
      try {
        List<Station> stations = transport.getStations(newValue).getStations();
        stations.sort((o1, o2) -> {
          if (o1.getScore() <= o2.getScore()) {
            return -1;
          } else if (o1.getScore() >= o2.getScore()) {
            return 1;
          }
          return 0;
        });
        List<String> names = new ArrayList<>();
        int i = 0;
        for (Station station : stations) {
          // only show 10
          if (i > 10) {
            break;
          }
          names.add(station.getName());
          i++;
        }
        TextFields.bindAutoCompletion(textField, names);
      } catch (PublicTransportServiceUnvailableException e1) {
        // To Many request next char it will get reloaded
        // TextFields.bindAutoCompletion(txtStart, "");
      }
    });
  }
}