package room;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleListProperty;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class RoomListView {
  private VBox rootNode        = new VBox(10);
  private VBox boxOverList     = new VBox(10);
  private Label title          = new Label();
  private ListView<RoomModel> roomNodes;

  /**
   * View for list of Rooms.
   */
  public RoomListView(SimpleListProperty<RoomModel> rooms, Parent filter) {
    roomNodes = new ListView<RoomModel>(rooms);
    title.textProperty().bind(
        Bindings.concat("Displaying ", rooms.sizeProperty(), " rooms"));
    roomNodes.setCellFactory(param -> new RoomListViewCell());

    VBox.setVgrow(roomNodes, Priority.ALWAYS);
    roomNodes.setPrefWidth(300);
    roomNodes.setMinWidth(Control.USE_PREF_SIZE);
    roomNodes.setMaxWidth(Control.USE_PREF_SIZE);
    boxOverList.getChildren().addAll(filter, title);
    rootNode.getChildren().addAll(boxOverList, roomNodes);
  }

  public MultipleSelectionModel<RoomModel> getSelectionModel() {
    return roomNodes.getSelectionModel();
  }

  public Parent getView() {
    return rootNode;
  }
}