package common;

import hotelproject.App;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class Util {
  /**
   * Display an alert with a string as message.
  */
  public static Optional<ButtonType> displayAlert(String type, String title, String msg) {
    Alert alert = getAlert(type, title);
    alert.setContentText(msg);
    return alert.showAndWait();
  }

  /**
   * Display an alert with a label as message.
  */
  public static Optional<ButtonType> displayAlert(String type, String title, Label msg) {
    Alert alert = getAlert(type, title);
    alert.getDialogPane().setContent(msg);
    return alert.showAndWait();
  }

  /**
   * Create the alert.
   */
  private static Alert getAlert(String type, String title) {
    Alert alert = new Alert(AlertType.valueOf(type.toUpperCase()));
    if (App.getUser().getTheme().equals("dark")) {
      alert.getDialogPane().getStylesheets().setAll(Util.class
          .getClassLoader().getResource("dark.css").toExternalForm());
    } else {
      alert.getDialogPane().getStylesheets().setAll(Util.class
          .getClassLoader().getResource("app.css").toExternalForm());
    }
    alert.setTitle(title);
    alert.setHeaderText(null);
    return alert;
  }
}
