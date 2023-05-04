package room;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class RoomListViewCell extends ListCell<RoomModel> {
  HBox hbox           = new HBox();
  Label roomNumber    = new Label("");
  Label beds          = new Label("");
  Pane pane           = new Pane();

  /**
   * Custom ListCell for listing rooms.
   */
  public RoomListViewCell() {
    super();
    hbox.getChildren().addAll(roomNumber, pane, beds);
    HBox.setHgrow(pane, Priority.ALWAYS);
  }

  @Override
  protected void updateItem(RoomModel item, boolean empty) {
    super.updateItem(item, empty);
    setText(null);
    setGraphic(null);
    if (item != null && !empty) {
      roomNumber.setText("#" + String.valueOf(item.getId()));
      beds.setText("Beds: " + (item.getBeds() < 10 ? "  " : "") + String.valueOf(item.getBeds()));
      setGraphic(hbox);
    }
  }
}
