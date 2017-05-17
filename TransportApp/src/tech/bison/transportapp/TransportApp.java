package tech.bison.transportapp;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tech.bison.transportapp.view.RootLayoutController;

public class TransportApp extends Application {
  /**
   * Start method is always called first, never can be null
   */
  private Stage primaryStage;
  private RootLayoutController controller;

  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    initRootLayout();
    primaryStage.setScene(new Scene(controller.getRootPane()));
    primaryStage.show();

  }

  private void initRootLayout() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("view/RootLayout.fxml"));
      loader.load();
      controller = (RootLayoutController) loader.getController();
    } catch (IOException e) {
      // never happens, I ensure that correct Path is set
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
