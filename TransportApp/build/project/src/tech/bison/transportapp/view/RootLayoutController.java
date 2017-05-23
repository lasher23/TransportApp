package tech.bison.transportapp.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class RootLayoutController {
  @FXML
  private BorderPane rootPane;
  private static final KeyCombination COMBO_CONNECTIONS = new KeyCodeCombination(KeyCode.F,
      KeyCombination.CONTROL_DOWN);
  private static final KeyCombination COMBO_TABLE = new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN);
  private static final KeyCombination COMBO_SEARCH = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);

  /**
   * @return the Border Pane where the root layout is on it
   */
  public BorderPane getRootPane() {
    return rootPane;
  }

  @FXML
  private void onStationsSearch() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("stationsearch/StationSearch.fxml"));
      VBox pane = (VBox) loader.load();
      rootPane.setCenter(pane);
    } catch (IOException e) {
      // never fails
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
      // never fails
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
      // never fails
    }
  }

  @FXML
  private void keyPressed(KeyEvent event) {
    if (COMBO_CONNECTIONS.match(event)) {
      onSearchConnection();
    } else if (COMBO_SEARCH.match(event)) {
      onStationsSearch();
    } else if (COMBO_TABLE.match(event)) {
      onShowStationBoard();
    }
  }
}
