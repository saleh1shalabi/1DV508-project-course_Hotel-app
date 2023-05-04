package hotelproject;

import common.ScriptRunner;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.stage.Stage;
import login.LoginController;
import login.LoginModel;
import login.LoginView;
import user.User;

public class App extends Application {
  private static User user;
  private static Stage stage;
  private static LoginModel login;
  private static LoginView loginView;

  /**
   * Set scene and show initial view.
   */
  @Override
  public void start(Stage primaryStage) {
    initializeDatabase();
    setStage(primaryStage);
    login();
  }

  /**
   * Try to use "silicone_hotel" as DB. If it does not exist, init the db with the SQL file.
   */
  private void initializeDatabase() {
    try (Connection conn = DataSource.getConnection();
        Statement stmt = conn.createStatement();) {
      stmt.execute("USE silicone_hotel;");
      DataSource.setDatabase();
    } catch (SQLException e) {
      try {
        Connection conn = DataSource.getConnection();
        ScriptRunner runner = new ScriptRunner(conn, false, false);
        runner.runScript("../database/hotel.sql");
        DataSource.setDatabase();
      } catch (SQLException f) {
        f.printStackTrace();
      } catch (IOException i) {
        i.printStackTrace();
      }
    }
  }

  /**
   * Makes a node visible and managed to admins only.
   * Bind makes it so that node.setVisible(Boolean) also sets node.setMangaged(Boolean).
   * @param node that only admins should be able to access.
   */
  public static void adminFeature(Node node) {
    node.managedProperty().bind(node.visibleProperty());
    if (!App.getUser().isAdmin()) {
      node.setVisible(false);
    }
  }

  /**
   * Makes a list of nodes visible and managed to admins only.
   * Bind makes it so that node.setVisible(Boolean) also sets node.setMangaged(Boolean).
   * @param nodes that only admins should be able to access.
   */
  public static void adminFeature(List<Node> nodes) {
    if (!nodes.isEmpty()) {
      for (Node node : nodes) {
        node.managedProperty().bind(node.visibleProperty());
        if (!App.getUser().isAdmin()) {
          node.setVisible(false);
        } 
      }
    }
  }

  private static void setStage(Stage stage) {
    App.stage = stage;
  }

  public static void setUser(User user) {
    App.user = user;
  }

  /**
   * Builds and opens the login window.
   */
  public static void login() {
    login = new LoginModel(stage);
    loginView = new LoginView(login, new LoginController(login));
    
    stage.setScene(loginView.getScene());
    stage.centerOnScreen();
    stage.setTitle("Login");
    stage.show();
  }

  public static User getUser() {
    return user;
  }
}
