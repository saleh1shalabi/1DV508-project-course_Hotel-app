package user;

import common.Util;
import hotelproject.Controller;
import hotelproject.DataHandler;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserChanges extends Controller {
  private String user;

  private VBox all = new VBox(10);

  private HBox searchBox = new HBox(20);
  private HBox usernameBox = new HBox(20);
  private HBox passwordBox = new HBox(20); 
  private HBox roleBox = new HBox(20);
  private HBox buttonBox = new HBox(20); 
  private HBox usersView = new HBox();

  private boolean usernameBoolean = false;
  private boolean passwordBoolean = false;

  private String oldUsername;

  Label allUsersLabel = new Label("All Users:");
  ObservableList<Label> names = FXCollections.observableArrayList();
  ListView<Label> nameView = getAllUsers();

  CheckBox adminCheckBox = new CheckBox("Admin");

  TextField searchField = new TextField();
  Button searchButton = new Button("Search");

  Label passwordLabel = new Label("Password: ");
  PasswordField passwordField = new PasswordField();
  Button passwordEditButton = new Button("Edit");

  Label usernameLabel = new Label("Username: ");
  TextField usernameField = new TextField();
  Button usernameEditButton = new Button("Edit");

  Button deleteButton = new Button("Delete");
  Button saveButton = new Button("Save");
  
  /** 
   * conatructor for the view.
   * @param user is a string of the username from user.
  */
  public UserChanges(String user) {
    this.user = user;
    getAllUsers();
    createView();
  }
  
  private ListView<Label> getAllUsers() {
    
    
    List<String> query = new ArrayList<String>();
    query.add("Select username, role "
        +  "From user;");

    List<Map<String, Object>> result = DataHandler.getData(query);
    names.clear();

    for (Map<String, Object> s : result) { 
      String username = (String) s.get("username");
      String roll = (String) s.get("role");
      // u would not be able to delete urself
      if (username.equals(user)) {
        continue;
      } else {
        names.add(new Label("Username : " + username));
        names.add(new Label("Role : " + roll));
        names.add(new Label("ـــــــــــــ"));
      }
    }
    ListView<Label> users = new ListView<Label>(names);
    
    users.setOnMouseClicked(e -> {
      if (!(users.getSelectionModel().getSelectedItem() == null)) {      
        int index = users.getSelectionModel().getSelectedIndex();
        Label chosidUserLabel = users.getSelectionModel().getSelectedItem();
        String chosidUser = chosidUserLabel.getText();
        if (chosidUser.startsWith("Username : ")) {
          chosidUser = chosidUser.substring(11);
          
        } else if (chosidUser.startsWith("ـــــــــــــ")) {
          users.getSelectionModel().clearSelection();
          chosidUser = users.getItems().get(index - 2).getText().substring(11);

        } else {
          users.getSelectionModel().clearSelection();

          chosidUser = users.getItems().get(index - 1).getText().substring(11);
        }
        search(chosidUser);
      }
    });
      
    return users;
  }

  private void createView() {
    all.setPadding(new Insets(0,0,0,0));
    all.setAlignment(Pos.TOP_CENTER);
    
    searchField.setMaxWidth(200);
    searchField.setMaxWidth(200);
    passwordField.setMaxWidth(200);
    usernameField.setMaxWidth(200);

    searchBox.getChildren().addAll(searchField, searchButton);
    searchBox.setAlignment(Pos.CENTER);
    
    usernameBox.getChildren().addAll(usernameLabel, usernameField);
    usernameBox.setAlignment(Pos.CENTER);
 
    passwordBox.getChildren().addAll(passwordLabel, passwordField);
    passwordBox.setAlignment(Pos.CENTER);

    buttonBox.getChildren().addAll(saveButton);
    buttonBox.setAlignment(Pos.CENTER);
  
    usersView.getChildren().addAll(getAllUsers());
    usersView.setAlignment(Pos.CENTER);
    usersView.setPadding(new Insets(0,0,10,0));
  
    adminCheckBox.setDisable(true);
    roleBox.getChildren().addAll(adminCheckBox);
    roleBox.setAlignment(Pos.CENTER);
  

    all.getChildren().addAll(searchBox, usernameBox, passwordBox, roleBox, 
        buttonBox, allUsersLabel, usersView);

    searchField.setPromptText("Search user");
    searchButton.setOnAction(e -> search(searchField.getText()));
    
    usernameField.setPromptText("Username");
    usernameField.setEditable(false);
    usernameEditButton.setOnAction(e -> editUsername());

    passwordField.setEditable(false);
    passwordField.setPromptText("New Password");
    passwordEditButton.setOnAction(e -> editPassword());
    
    saveButton.setOnAction(e -> saveChanges());
    deleteButton.getStyleClass().add("delete-button");
    deleteButton.setOnAction(e -> deleteUser());
  }

  private void saveChanges() {
    
    String update = "";
    String setUserName = "username = '" + usernameField.getText() + "'";
    String setPassword = "password = '" + DataHandler.hashString(passwordField.getText()) + "'";
    String role = "";

    if (adminCheckBox.isSelected() == true) {
      role += "admin";
    } else {
      role += "staff";
    }
    update += "role = '" + role + "'";
    if (usernameBoolean == true && passwordBoolean == true) {
      update += ", " + setUserName + ", " + setPassword;
      usernameBoolean = false;
      passwordBoolean = false;
    } else if (usernameBoolean == true) {
      update += ", " + setUserName;
      
    } else if (passwordBoolean == true) {
      update += ", " +  setPassword;

    }

    List<String> query = new ArrayList<String>();

    query.add("update user "
        + "set " + update
        + "Where username = '" + oldUsername + "';");

    if (usernameField.getText().isEmpty() && usernameBoolean == true) {
      usernameBoolean = false;
      Util.displayAlert("Error", "Faild", "Username is empty!");

    } else if (passwordBoolean == true && passwordField.getText().isEmpty()) {
      passwordBoolean = false;
      Util.displayAlert("Error", "Faild", "Password is empty!");

    } else if (passwordField.getText().length() < 6) {
      Util.displayAlert("Error", "Faild", "Password is Short, Must be longer than 6 Char!");

    } else {

      try {
        DataHandler.setData(query);
       
        all.getChildren().clear();
        all.getChildren().add(new UserChanges(user).getView());
      } catch (SQLIntegrityConstraintViolationException e) {
        Util.displayAlert("Error", "Faild", "The new user name is alrady in use!");
        
      } catch (SQLException ee) {
        Util.displayAlert("Error", "Faild", ee.getMessage());

      } catch (Exception eee) {
        System.out.println(eee);
      }    
    }
  }

  private void editPassword() {
    passwordBoolean = true;
    passwordField.setText("");
    passwordBox.getChildren().remove(passwordEditButton);
    passwordField.setEditable(true); 
  }

  private void editUsername() {
    usernameBoolean = true;
    oldUsername = usernameField.getText();
    usernameBox.getChildren().remove(usernameEditButton);
    usernameField.setEditable(true);    
  }

  private void search(String userToSearch) {
    
    List<String> query = new ArrayList<String>();

    query.add("Select username, role "
        + "From user "
        + "Where username = '" + userToSearch + "';");

    List<Map<String, Object>> result = DataHandler.getData(query);
    
    if (userToSearch.isEmpty()) {
      System.out.println("you search for nothing :) !");
    } else if (result.size() == 0) {
      Util.displayAlert("Error", "Faild", "The user " + userToSearch + " does not excist");

      
    } else if (userToSearch.equals(user)) {
      Util.displayAlert("Error", "Faild", "You cant Change your details here, "
          + "please go to setting if you want to do changes!");
    
    } else {
      String role = (String) result.get(0).get("role");
      if (role.equals("admin")) {
     
        adminCheckBox.setSelected(true);
      } else if (role.equals("staff")) {
        adminCheckBox.setSelected(false);
      }
      oldUsername = (String) result.get(0).get("username");
      usernameField.setText((String) result.get(0).get("username"));
      passwordField.setText("***********");
      
      // to avoid a dublicate error
      usernameBox.getChildren().remove(usernameEditButton);
      passwordBox.getChildren().remove(passwordEditButton);
      buttonBox.getChildren().remove(deleteButton);

      usernameBox.getChildren().add(usernameEditButton);
      passwordBox.getChildren().add(passwordEditButton);
      buttonBox.getChildren().add(deleteButton);
    }
    oldUsername = usernameField.getText();
    adminCheckBox.setDisable(false);
    nameView = getAllUsers();
  }

  private void deleteUser() {
    
    Stage confirmDelete = new Stage();
    confirmDelete.setTitle("Delete this User!");

    Button yes = new Button("yes");
    Button no = new Button("no");

    yes.setOnAction(e -> {
      List<String> query = new ArrayList<String>();

      query.add("Delete from user "
          + "Where username = '" + oldUsername + "';");

      
      try {
        DataHandler.setData(query);
        
        all.getChildren().clear();
        all.getChildren().add(new UserChanges(user).getView());
        Util.displayAlert("INFORMATION", "Done", "The user has been deleted!");

        confirmDelete.close();

      } catch (SQLException e1) {
        e1.printStackTrace();
      } catch (Exception sq) {
        System.out.println(sq);
      }

    });

    no.setOnAction(e -> confirmDelete.close());
        
    HBox yesOrNo = new HBox(100);
    yesOrNo.setAlignment(Pos.CENTER);
    yesOrNo.getChildren().addAll(yes, no);

    Label qus = new Label("Do you really want to delete this User?");

    VBox popup = new VBox(10);
    popup.getChildren().addAll(qus, yesOrNo);
    popup.setAlignment(Pos.CENTER);
          
    Scene popupScene = new Scene(popup, 300, 100);
          
    confirmDelete.setScene(popupScene);   
    confirmDelete.showAndWait();

  }

  @Override
  public VBox getView() {  
    return all;
  }

}
