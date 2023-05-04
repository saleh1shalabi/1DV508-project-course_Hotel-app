package login;

import common.Util;
import hotelproject.App;
import hotelproject.DataHandler;
import hotelproject.MainView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;
import user.User;
import user.User.Role;


public class LoginModel {

  private final StringProperty username = new SimpleStringProperty();
  private final StringProperty password = new SimpleStringProperty();
  private Stage primaryStage;

  public LoginModel(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  public StringProperty passwordProperty() {
    return this.password;
  }

  public String getPassword() {
    return passwordProperty().get();
  }

  public void setPassword(String password) {
    passwordProperty().set(password);
  }

  public StringProperty usernameProperty() {
    return this.username;
  }

  public String getUsername() {
    return usernameProperty().get();
  }

  public void setUsername(String username) {
    usernameProperty().set(username);
  }

  /**
   * Happens when login button is pressed.
   * Opens main view if user is authorized.
   * @param username Input from username field.
   * @param password Input from password field.
   */
  public void loginCallback(String username, String password) {
    if (authorize(username, password)) {
      showMainView();
    }
  }

  private void showMainView() {
    MainView mainView = new MainView();
    primaryStage.setScene(mainView.getScene());
    primaryStage.setTitle(username.get() + " - Silicone Hotel");
    primaryStage.centerOnScreen();
    primaryStage.setMaximized(true);
  }

  private boolean authorize(String username, String password) {
    List<String> query = new ArrayList<>();
    query.add("SELECT * FROM user WHERE username=?");
    query.add(username);
    List<Map<String,Object>> result = DataHandler.getData(query);
    if (result.isEmpty()) {
      Util.displayAlert("Error", "Invalid login details", "User " + username + " doesn't exist.");
    } else {
      String dbPassword = (String) result.get(0).get("password");
      if (dbPassword.equals(DataHandler.hashString(password))) {
        Role role = result.get(0).get("role").equals("admin") ? Role.ADMIN : Role.RECEPTIONIST;
        App.setUser(new User(username, role));
        return true;
      } else {
        Util.displayAlert("Error", "Invalid login details", "Incorrect password.");
      }
    }
    return false;
  }

}
