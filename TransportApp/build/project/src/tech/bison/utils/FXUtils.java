package tech.bison.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class FXUtils {
  /**
   * nobody should instantiate this class, because it only has static methods
   */
  private FXUtils() {

  }

  /**
   * @param lowerBound
   *          min value that should be allowed to enter
   * @param upperBound
   *          max value that should be allowed to enter
   * @param textfield
   *          on which {@link javafx.scene.control.TextField} to register this listener
   */
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
