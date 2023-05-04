package login;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LoginView {
  
  private LoginModel login;
  private LoginController controller;
  private VBox view;
  private HBox rowOne;
  private HBox rowTwo;
  private HBox rowThree;
  private TextField username;
  private PasswordField password;
  private Button btnLogin;
  private Button btnReset;
  private Label usernameLabel;
  private Label passwordLabel;
  private Scene scene;

  /**
   * Creates view for Login.
   */
  public LoginView(LoginModel login, LoginController controller) {
    this.login = login;
    this.controller = controller;

    createPane();

    createControls();

    createScene();

    updateControllersFromListeners();

    observeModelUpdateControls();
  }

  private void createScene() {
    scene = new Scene(view, 400, 200);
    scene.getStylesheets().add(getClass().getClassLoader().getResource("app.css").toExternalForm());
  }

  private void observeModelUpdateControls() {
    login.usernameProperty().addListener((obs, oldUn, newUn) ->
        updateIfNeeded(newUn, username));

    login.passwordProperty().addListener((obs, oldPw, newPw) ->
        updateIfNeeded(newPw, password));
  }

  private void updateIfNeeded(String newValue, TextField field) {
    if (!field.toString().equals(newValue)) {
      field.setText(newValue);
    }
  }

  private void updateControllersFromListeners() {
    username.textProperty().addListener((obs, oldText, newText) -> 
        controller.updateUsername((newText)));
    password.textProperty().addListener((obs, oldText, newText) -> 
        controller.updatePassword((newText)));
  }

  private void createControls() {
    Double labelWidth = 90.0;
    usernameLabel = new Label("Username:");
    usernameLabel.setPrefWidth(labelWidth);
    username = new TextField();
    username.setPromptText("Username");
    username.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        btnLogin.fire();
      }
    });

    passwordLabel = new Label("Password:");
    passwordLabel.setPrefWidth(labelWidth);
    password = new PasswordField();
    password.setPromptText("Password");
    password.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        btnLogin.fire();
      }
    });

    btnLogin = new Button("Login");
    btnLogin.setOnAction(e -> login.loginCallback(username.getText(), password.getText()));

    btnReset = new Button("Reset Password");
    btnReset.setOnAction(e -> {
      new ForgotPassword().getNewPassword(username.getText());
    });
    
    rowOne.getChildren().addAll(usernameLabel, username);
    rowTwo.getChildren().addAll(passwordLabel, password);
    rowThree.getChildren().addAll(btnLogin, btnReset);
    view.getChildren().addAll(rowOne, rowTwo, rowThree);
  }

  private void createPane() {
    rowOne = new HBox();
    rowOne.setSpacing(5);
    rowOne.setAlignment(Pos.CENTER);
    
    rowTwo = new HBox();
    rowTwo.setSpacing(5);
    rowTwo.setAlignment(Pos.CENTER);

    rowThree = new HBox();
    rowThree.setSpacing(5);
    rowThree.setAlignment(Pos.CENTER);

    view = new VBox();
    view.setSpacing(15);
    view.setAlignment(Pos.CENTER);
  }

  public Scene getScene() {
    return this.scene;
  }

  public Parent getView() {
    return view;
  }
  
}
