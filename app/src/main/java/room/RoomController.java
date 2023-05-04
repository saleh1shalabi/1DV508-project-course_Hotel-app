package room;

import hotelproject.Controller;
import javafx.scene.Parent;

public class RoomController extends Controller {
  protected RoomModel room = new RoomModel();
  protected RoomView view = new RoomView(this, room);

  /**
   * Try to save the room when the save button is clicked.
   */
  public void saveRoom() {
    room.clearErrors();
    if (room.isValid()) {
      if (room.addRoom()) {
        view.success("added");
        view.clearFields();
      }
    } else {
      view.error();
    }
  }

  public Parent getView() {
    return view.asParent();
  }
}
