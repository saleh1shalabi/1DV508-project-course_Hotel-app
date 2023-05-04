package user;

import hotelproject.DataHandler;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Interface to implement for all types of users.
 */
public class User {
  public enum Role {
    ADMIN,
    RECEPTIONIST;
  }

  private StringProperty username = new SimpleStringProperty();
  private Role role;

  public User(String username, Role role) {
    this.username.set(username);
    this.role = role;
  }

  public StringProperty usernameProperty() {
    return username;
  }

  public String getUsername() {
    return username.get();
  }

  public Role getRole() {
    return role;
  }

  /**
   * Updates username.
   */
  public void setUsername(String username) throws SQLException {
    List<String> query = new ArrayList<String>();
    query.add("UPDATE user SET username = ? WHERE username = ?");
    query.add(username);
    query.add(getUsername());
    
    DataHandler.setData(query);
   
    this.username.set(username);
  }

  /**
   * Updates password.
   */
  public void setPassword(String password) throws SQLException {
    List<String> query = new ArrayList<String>();
    query.add("UPDATE user SET password = ? WHERE username = ?");
    query.add(DataHandler.hashString(password));
    query.add(getUsername());
    DataHandler.setData(query);
  }

  /**
   * Gets email.
   */
  public String getEmail() {
    List<String> query = new ArrayList<String>();
    query.add("SELECT email FROM user WHERE username = ?");
    query.add(getUsername());
    Object result = DataHandler.getData(query).get(0).get("email");
    if (result != null) {
      return result.toString();
    } else {
      return "";
    }
  }

  /**
   * Sets email.
   */
  public void setEmail(String email) throws SQLException {
    List<String> query = new ArrayList<String>();
    query.add("UPDATE user SET email = ? WHERE username = ?");
    query.add(email);
    query.add(getUsername());
    DataHandler.setData(query);
  }

  public Boolean isAdmin() {
    return this.role.equals(Role.ADMIN);
  }

  /**
   * Set role.
   */
  public void setRole(Role role) {
    List<String> query = new ArrayList<String>();
    query.add("UPDATE user SET role = ? WHERE username = ?");
    query.add(role.toString());
    query.add(getUsername());
    try {
      DataHandler.setData(query);
      this.role = role;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Get theme.
   */
  public String getTheme() {
    List<String> query = new ArrayList<String>();
    query.add("SELECT theme FROM user WHERE username = ?");
    query.add(getUsername());
    return DataHandler.getData(query).get(0).get("theme").toString();
  }

  /**
   * Set theme.
   */
  public void setTheme(String theme) {
    List<String> query = new ArrayList<String>();
    query.add("UPDATE user SET theme = ? WHERE username = ?");
    query.add(theme);
    query.add(getUsername());
    try {
      DataHandler.setData(query);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}

