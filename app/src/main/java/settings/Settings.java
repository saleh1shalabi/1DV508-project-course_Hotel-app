package settings;

import common.Util;
import hotelproject.App;
import hotelproject.Controller;

import java.sql.SQLException;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import user.User;

public class Settings extends Controller {
  private VBox view;
  private HBox hbOne;
  private HBox hbTwo;
  private HBox hbThree;
  private HBox hbFour;
  private ComboBox<String> cbTheme;
  private Label lblTheme;
  private Label lblUsername;
  private TextField tfUsername;
  private Button btnUsername;
  private Label lblPassword;
  private PasswordField pfPassword;
  private PasswordField pfConfirm;
  private VBox vbPassword;
  private Button btnPassword;
  private Label lblEmail;
  private TextField tfEmail;
  private Button btnEmail;

  /**
   * Set up view.
   */
  public Settings() {
    User user = App.getUser();
    lblUsername = new Label("Username: " + user.getUsername() + " ");
    lblPassword = new Label("Password: ********");
    lblEmail = new Label("Email: " + user.getEmail() + " ");

    view = new VBox();
    view.setSpacing(10);
    
    tfUsername = new TextField();
    pfPassword = new PasswordField();
    pfPassword.setPromptText("Password");
    pfConfirm = new PasswordField();
    pfConfirm.setPromptText("Confirm password");
    tfEmail = new TextField();
    hbOne = new HBox();
    btnUsername = new Button("Edit");
    hbOne.getChildren().addAll(lblUsername, btnUsername);
    hbOne.setSpacing(10);
    hbTwo = new HBox();
    btnPassword = new Button("Edit");
    hbTwo.getChildren().addAll(lblPassword, btnPassword);
    hbTwo.setSpacing(10);
    hbThree = new HBox();
    btnEmail = new Button("Edit");
    hbThree.getChildren().addAll(lblEmail, btnEmail);
    hbThree.setSpacing(10);

    vbPassword = new VBox();
    vbPassword.getChildren().addAll(pfPassword, pfConfirm);
    vbPassword.setSpacing(10);

    hbFour = new HBox();
    hbFour.setSpacing(10);
    lblTheme = new Label("Theme:  ");
    cbTheme = new ComboBox<String>();
    cbTheme.getStyleClass().add("combo");
    cbTheme.getItems().setAll("Light", "Dark");
    if (App.getUser().getTheme().equals("light")) {
      cbTheme.setPromptText("Light");
    } else {
      cbTheme.setPromptText("Dark");
    }
    hbFour.getChildren().addAll(lblTheme, cbTheme);

    btnUsername.getStyleClass().add("small-button");
    btnPassword.getStyleClass().add("small-button");
    btnEmail.getStyleClass().add("small-button");

    btnUsername.setOnAction(e -> {
      if (btnUsername.getText().equals("Edit")) {
        hbOne.getChildren().setAll(tfUsername, btnUsername);
        tfUsername.setText(user.getUsername());
        btnUsername.setText("Save");
      } else {
        try {
          user.setUsername(tfUsername.getText());
          Util.displayAlert("Information", "Information", "Username updated.");
        } catch (SQLException ex) {
          Util.displayAlert("Error", "Error", "Couldn't update username, try again later.");
        }
        hbOne.getChildren().setAll(lblUsername, btnUsername);
        lblUsername.setText("Username: " + user.getUsername() + " ");
        btnUsername.setText("Edit");
      }
    });
    btnPassword.setOnAction(e -> {
      if (btnPassword.getText().equals("Edit")) {
        hbTwo.getChildren().setAll(vbPassword, btnPassword);
        btnPassword.setText("Save");
      } else {
        if (pfPassword.getText().equals(pfConfirm.getText()) && pfConfirm.getText().length() > 5) {
          try {
            user.setPassword(pfConfirm.getText());
            Util.displayAlert("Information", "Information", "Password updated.");
            hbTwo.getChildren().setAll(lblPassword, btnPassword);
            lblPassword.setText("Password: ********");
            btnPassword.setText("Edit");
          } catch (SQLException ex) {
            Util.displayAlert("Error", "Error", "Couldn't update password, try again later.");
          }
        } else if (!pfPassword.getText().equals(pfConfirm.getText())) {
          Util.displayAlert("Error", "Error", "Password didn't match.");
        } else if (pfConfirm.getText().length() < 6) {
          Util.displayAlert("Error", "Error", "Password too short.");
        }
      }
    });
    btnEmail.setOnAction(e -> {
      if (btnEmail.getText().equals("Edit")) {
        hbThree.getChildren().setAll(tfEmail, btnEmail);
        tfEmail.setText(user.getEmail());
        btnEmail.setText("Save");
      } else {
        try {
          user.setEmail(tfEmail.getText());
          Util.displayAlert("Information", "Information", "E-mail updated.");
          hbThree.getChildren().setAll(lblEmail, btnEmail);
          btnEmail.setText("Edit");
          lblEmail.setText("Email: " + user.getEmail() + " ");
        } catch (SQLException ex) {
          Util.displayAlert("Error", "Error", "Couldn't update E-mail, try again later.");
        }
      }
    });
    cbTheme.setOnAction(e -> {
      if (cbTheme.getSelectionModel().getSelectedItem().toLowerCase().equals("light")) {
        App.getUser().setTheme("light");
        view.getScene().getStylesheets().setAll(getClass().getClassLoader()
            .getResource("app.css").toExternalForm());
      } else {
        App.getUser().setTheme("dark");
        view.getScene().getStylesheets().setAll(getClass().getClassLoader()
            .getResource("dark.css").toExternalForm());
      }
    });

    view.getChildren().addAll(hbOne, hbTwo, hbThree, hbFour);
  }

  public Parent getView() {
    return view;
  }
}
