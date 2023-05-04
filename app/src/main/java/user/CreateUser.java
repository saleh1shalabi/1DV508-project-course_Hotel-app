package user;

import common.Util;
import hotelproject.Controller;
import hotelproject.DataHandler;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class CreateUser extends Controller {

  TextField usernamField = new TextField();
  TextField emailField = new TextField();
  PasswordField passField = new PasswordField();
  PasswordField confPassField = new PasswordField();
  CheckBox adminBox = new CheckBox("Admin?");

  /**
   * Opens a window to add new user.
   */
  @Override
  public VBox getView() {
    VBox all = new VBox(15);
    all.setAlignment(Pos.TOP_CENTER);
    all.setPadding(new Insets(10,0,0,0));
  
    usernamField.setPromptText("Username");
    emailField.setPromptText("Email");
    passField.setPromptText("Password");
    confPassField.setPromptText("Confirm password");

    Button btnCreate = new Button("Create User");
    
    btnCreate.setOnAction(e -> {
    
      List<String> values = getValues();
      if (values != null) {
        try { 
          DataHandler.setData(getValues());
          Util.displayAlert("INFORMATION", "Done", "New User Have Been Created!");
          
        } catch (SQLIntegrityConstraintViolationException ee) { 
          Util.displayAlert("Error", "Faild", "Username is already in use!");
        } catch (SQLException ex) {
          // ex.printStackTrace();
          System.out.println(ex);
        }
      }
    });
    usernamField.setMaxWidth(200);
    emailField.setMaxWidth(200);
    passField.setMaxWidth(200);
    confPassField.setMaxWidth(200);
    
    Label lblUsername = new Label("Username: ");
    Label lblEmail = new Label("Email");
    Label lblPassword = new Label("Password:  ");
    Label lblPwConf = new Label("Confirm:   ");
    
    all.getChildren().addAll(lblUsername, usernamField, lblEmail, emailField, lblPassword,
        passField, lblPwConf, confPassField, adminBox, btnCreate
    );
    return all;
  }

  private List<String> getValues() {

    List<String> query = new ArrayList<String>();

    query.add("INSERT INTO user(username, email, password, role) "
        + "VALUES(?, ?, ?, ?);");

    if (usernamField.getText().isEmpty()) {
      Util.displayAlert("Error", "Faild", "Username is empty!");
    
    } else if (!(checkEmail())) { 
      Util.displayAlert("Error", "Faild", "the mail is invalid");
    } else if (!(checkPassword())) {
      Util.displayAlert("Error", "Faild", "Passwords does not match");

    } else if (passField.getText().isEmpty() || confPassField.getText().isEmpty()) {
      Util.displayAlert("Error", "Faild", "Passwords is empty!");
    
    } else if (passField.getText().length() < 6) {
      Util.displayAlert("Error", "Faild", "Passwords is Short, Must be longer than 6 Char!");

    } else {
      query.add(usernamField.getText());
      query.add(emailField.getText());
      query.add(DataHandler.hashString(passField.getText()));
      if (adminBox.isSelected()) {
        query.add("admin");
      } else {
        query.add("staff");
      }
      return query;
    }
    return null;
  }

  private Boolean checkPassword() {
    return passField.getText().equals(confPassField.getText());
  }
   
  private Boolean checkEmail() {
    String mail = emailField.getText();

    if (mail.isBlank() || mail.isEmpty()) {
      return false;
    } else if (!(mail.contains("@"))) {
      return false;
    }
    return true;
  }
}
