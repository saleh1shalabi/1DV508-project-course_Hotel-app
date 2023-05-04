package hotelproject;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import settings.Settings;
import user.User;

public class MainView {
  private BorderPane root          = new BorderPane();
  private Scene scene              = new Scene(root);
  private BorderPane mainPane      = new BorderPane();
  private BorderPane sidebar       = new BorderPane();
  private Label welcomeLabel       = new Label("Welcome " + App.getUser().getUsername() + "!");
  private HBox usernameBox         = new HBox();
  private VBox bottomButtons       = new VBox();
  private Button settingsButton    = new Button("SETTINGS");
  private Button signoutButton     = new Button("SIGN OUT");
  private Accordion sidebarButtons;
  private ToggleGroup menuGroup;
  private Label username;
  private Label role;

  /**
   * Setup the main view.
   */
  public MainView() {
    User user = App.getUser();
    username      = new Label(user.getUsername());
    role          = new Label(user.getRole().toString().toUpperCase());

    username.textProperty().bind(App.getUser().usernameProperty());

    if (user.getTheme().equals("dark")) {
      scene.getStylesheets().add(getClass().getClassLoader()
          .getResource("dark.css").toExternalForm());
    } else {
      scene.getStylesheets().add(getClass().getClassLoader()
          .getResource("app.css").toExternalForm());
    }
    createSidebar();
    welcomeLabel.setStyle("-fx-font-size: 3em");
    mainPane.setPadding(new Insets(10));
    mainPane.setCenter(welcomeLabel);
    root.setCenter(mainPane);
    root.setLeft(sidebar);
  }

  private void createSidebar() {
    SidebarView sbView = new SidebarView(mainPane);
    sidebarButtons     = sbView.getView();
    menuGroup          = sbView.getGroup();

    role.getStyleClass().add("role-label");
    username.getStyleClass().add("username-label");
    usernameBox.getChildren().addAll(role, username);
    usernameBox.setPadding(new Insets(10));
    usernameBox.setAlignment(Pos.CENTER_LEFT);

    bottomButtons.getChildren().addAll(settingsButton, signoutButton);
    VBox.setMargin(signoutButton, new Insets(10));
    bottomButtons.setAlignment(Pos.CENTER);

    Settings sw = new Settings();
    settingsButton.setOnAction(e -> {
      Toggle toggle = menuGroup.getSelectedToggle();
      if (toggle != null) {
        toggle.setSelected(false);
      }
      sw.renderView(sw.getView(), mainPane);
    });

    signoutButton.setOnAction(e -> App.login());

    sidebar.setMinWidth(200);
    sidebar.getStyleClass().add("sidebar");
    sidebar.prefHeightProperty().bind(scene.heightProperty());
    sidebar.setTop(usernameBox);
    sidebar.setCenter(new VBox(sidebarButtons));
    sidebar.setBottom(bottomButtons);
  }

  public Scene getScene() {
    return scene;
  }
}
