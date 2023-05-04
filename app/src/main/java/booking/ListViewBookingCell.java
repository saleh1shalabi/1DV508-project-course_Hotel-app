package booking;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class ListViewBookingCell extends ListCell<Booking> {
  HBox hbox         = new HBox();
  Label label       = new Label("");
  Pane pane         = new Pane();

  /**
   * Custom ListCell for listing bookings.
   */

  public ListViewBookingCell() {
    super();
    hbox.getChildren().addAll(label,pane);
    HBox.setHgrow(pane, Priority.ALWAYS);
  }

  @Override
  protected void updateItem(Booking item, boolean empty) {
    super.updateItem(item,empty);
    setText(null);
    setGraphic(null);
    if (item != null && !empty) {
      String id = String.valueOf(item.getId());
      String date = item.getStartDate().toString();
      String name = "Deleted customer";
      if (item.getCustomer() != null) {
        name = item.getCustomer().getFirstName() + " " + item.getCustomer().getLastName();
      }
      label.setText(id + " | " + name + " | " + date);
      setGraphic(hbox);
    }
  }
}
