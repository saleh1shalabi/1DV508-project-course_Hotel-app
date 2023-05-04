package login;

import common.Email;
import common.Util;
import hotelproject.DataHandler;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ForgotPassword {
  private String pin;
  private String username;
  private Stage root;
  private Scene scene;

  private void generatePin() {
    Random rdm = new Random();
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < 4; i++) {
      sb.append(rdm.nextInt(10));
    }
    pin = sb.toString();
  }

  private void setPassword(String password, String confirm) {
    if (password.equals(confirm)) {
      List<String> query = new ArrayList<String>();
      query.add("UPDATE user SET password = ? WHERE username = ?");
      query.add(DataHandler.hashString(password));
      query.add(username);
      try {
        DataHandler.setData(query);
        Util.displayAlert("Information", "", "Password updated");
        root.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      Util.displayAlert("Error", "Error", "Passwords didn't match");
    }
  }

  private void setPasswordView() {
    Label lblPassword = new Label("New password: ");
    Label lblConfirm = new Label("Confirm password:");
    VBox vboxOne = new VBox();
    vboxOne.setAlignment(Pos.CENTER);
    vboxOne.setSpacing(10);
    vboxOne.getChildren().addAll(lblPassword, lblConfirm);

    PasswordField pfPassword = new PasswordField();
    PasswordField pfConfirm = new PasswordField();
    pfPassword.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER) {
        setPassword(pfPassword.getText(), pfConfirm.getText());
      }
    });
    pfConfirm.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER) {
        setPassword(pfPassword.getText(), pfConfirm.getText());
      }
    });
    VBox vboxTwo = new VBox();
    vboxTwo.setAlignment(Pos.CENTER);
    vboxTwo.setSpacing(10);
    vboxTwo.getChildren().addAll(pfPassword, pfConfirm);

    HBox hbox = new HBox();
    hbox.setAlignment(Pos.CENTER);
    hbox.setPadding(new Insets(10));
    hbox.setSpacing(10);
    hbox.getChildren().addAll(vboxOne, vboxTwo);

    VBox vboxThree = new VBox();
    vboxThree.setAlignment(Pos.CENTER);
    vboxThree.setSpacing(10);
    vboxThree.setPadding(new Insets(10));
    Button btnOk = new Button("OK");
    btnOk.setOnAction(e -> { 
      setPassword(pfPassword.getText(), pfConfirm.getText()); 
    });

    vboxThree.getChildren().addAll(hbox, btnOk);
    scene = new Scene(vboxThree);
    scene.getStylesheets().add(getClass().getClassLoader().getResource("app.css").toExternalForm());
    root.setScene(scene);
  }

  private void popup(String email) {
    root = new Stage();
    root.setTitle("Reset Password");
    VBox vbox = new VBox();
    vbox.setPadding(new Insets(10));
    vbox.setSpacing(10);
    vbox.setAlignment(Pos.CENTER);
    scene = new Scene(vbox);
    scene.getStylesheets().add(getClass().getClassLoader().getResource("app.css").toExternalForm());
    TextField tfPin = new TextField();
    tfPin.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER) {
        if (tfPin.getText().equals(pin)) {
          setPasswordView();
        } else {
          Util.displayAlert("Error", "Error", "Wrong PIN");
          root.close();
        }
      }
    });
    tfPin.setPromptText("PIN");
    Button btnOk = new Button("OK");
    btnOk.setOnAction(e -> {
      if (tfPin.getText().equals(pin)) {
        setPasswordView();
      } else {
        Util.displayAlert("Error", "Error", "Wrong PIN");
        root.close();
      }
    });
    
    Label lblInfo = new Label("We have sent a PIN to your E-mail."
        + System.lineSeparator() + "Enter it to reset your password.");
    vbox.getChildren().addAll(lblInfo, tfPin, btnOk);
    root.setScene(scene);
    Email e = new Email();
    e.sendPinToUser(pin, email);
    root.showAndWait();
  }

  /**
   * Handles user interface for updating password.
   */
  public void getNewPassword(String username) {
    generatePin();
    this.username = username;
    List<String> query = new ArrayList<String>();
    query.add("SELECT email from user WHERE username = ?");
    query.add(username);
    List<Map<String, Object>> email = DataHandler.getData(query);

    if (!email.isEmpty()) {
      popup(email.get(0).get("email").toString());
    } else if (username.equals("")) {
      Util.displayAlert("Error", "Error", "Enter username");
    } else {
      Util.displayAlert("Error", "Error", "Invalid username");
    }
  }
}
