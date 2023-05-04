package hotelproject;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class SettingsWindow extends Controller {
  private VBox rootNode = new VBox();
  // private User user;

  /**
   * Display settings window.
   */
  public SettingsWindow() {
    // this.user = user;
  
    rootNode.setPadding(new Insets(10, 0, 0, 15));
    rootNode.setSpacing(10);
    
    Label topLabel = new Label("Settings");
    topLabel.setFont(new Font(15));
    
    Button changeUn = new Button("Change Username");
    changeUn.setOnAction(e -> {
      // ChangeUsername changeName = new ChangeUsername(user,this);
      try {
        // changeName.start(primaryStage);
      } catch (Exception err) {
        err.printStackTrace();
      }
    });

    Button changePw = new Button("Change Password");
    changePw.setOnAction(e -> {
      // ChangePassword changePass = new ChangePassword(user,this);
      try {
        // changePass.start(primaryStage);
      } catch (Exception err) {
        err.printStackTrace();
      }
    });
    rootNode.getChildren().addAll(topLabel,changeUn,changePw);
  }


  @Override
  public Parent getView() {
    return rootNode;
  }
  
}
