package tech.bison.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class FXUtils {
  private FXUtils() {

  }

  public static void onlyAllowNumbers(int lowerBound, int upperBound, TextField textfield) {
    textfield.textProperty().addListener((observable, oldValue, newValue) -> {
      try {
        if (newValue.isEmpty()) {
          return;
        }
        int val = Integer.parseInt(newValue);
        if (val < lowerBound || val > upperBound) {
          textfield.setText(oldValue);
        }
      } catch (NumberFormatException e) {
        textfield.setText(oldValue);
      }
    });
  }

  public static void showErrorAlert(String headerText, String contentText) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setHeaderText(headerText);
    alert.setContentText(contentText);
    alert.show();
  }
}
