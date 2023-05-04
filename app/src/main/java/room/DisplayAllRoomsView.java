package room;

import common.Util;

import hotelproject.App;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DisplayAllRoomsView {
  private HBox rootNode       = new HBox(20);
  private Label noRoom        = new Label("Select a room to the left to view it's details.");
  private BorderPane details  = new BorderPane(noRoom);
  private Button editButton   = new Button("Edit");
  private Button deleteButton = new Button("Delete");
  private HBox buttonWrapper  = new HBox(10, editButton, deleteButton);
  private DisplayAllRoomsController dac;

  /**
   * View for displaying all rooms and their details.
   */
  public DisplayAllRoomsView(DisplayAllRoomsController dac) {
    this.dac = dac;
    configureButtons();
    rootNode.getChildren().addAll(dac.getRoomList(), details);
  }

  private void configureButtons() {
    buttonWrapper.setAlignment(Pos.CENTER);
    buttonWrapper.setPadding(new Insets(10, 0, 0, 0));
    deleteButton.getStyleClass().add("delete-button");
    deleteButton.setOnAction(e -> confirmDelete());
    editButton.setOnAction(e -> dac.editDetails());
    App.adminFeature(List.of(deleteButton, editButton));
  }

  public void showDeleteButton(boolean b) {
    deleteButton.setVisible(b);
  }

  public void nothingSelected() {
    details.setCenter(noRoom);
    details.setBottom(null);
  }

  public void displayRoomDetails(Parent node) {
    details.setCenter(node);
    details.setBottom(new VBox(10, new Separator(), buttonWrapper));
  }

  /**
   * Show confirmation dialog for deleting a room. Only continue if the OK button is pressed.
   */
  public void confirmDelete() {
    Util.displayAlert("CONFIRMATION", "Delete room",
      "Are you sure you want to delete the room?"
    ).filter(res -> res == ButtonType.OK).ifPresent(res -> dac.deleteRoom());
  }

  public Parent getView() {
    return rootNode;
  }
}
