package tech.bison.transportapp.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class RootLayoutController {
  @FXML
  private BorderPane rootPane;

  public BorderPane getRootPane() {
    return rootPane;
  }

  @FXML
  private void onStationsSearch() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("stationsearch/StationSearch.fxml"));
      VBox pane = (VBox) loader.load();
      rootPane.getScene().getWindow().setHeight(pane.getMinHeight() + 200);
      rootPane.getScene().getWindow().setWidth(pane.getMinWidth() + 50);
      rootPane.setCenter(pane);
    } catch (IOException e) {
    }
  }

  @FXML
  private void onSearchConnection() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("searchconnections/SearchConnection.fxml"));
      VBox pane = (VBox) loader.load();
      rootPane.setCenter(pane);
    } catch (IOException e) {
    }
  }

  @FXML
  private void onShowStationBoard() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("tableboard/TableBoard.fxml"));
      VBox pane = (VBox) loader.load();
      rootPane.setCenter(pane);
    } catch (IOException e) {
    }
  }
}
