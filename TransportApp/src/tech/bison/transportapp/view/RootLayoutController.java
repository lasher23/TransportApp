package tech.bison.transportapp.view;

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
  private void onSearchConnection() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("searchconnections/SearchConnection.fxml"));
      VBox pane = (VBox) loader.load();
      rootPane.setCenter(pane);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onShowStationBoard() {

  }
}
