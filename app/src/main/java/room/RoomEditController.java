package room;

public class RoomEditController extends RoomController {
  // private final RoomModel room;
  // private final RoomView view;

  /**
   * Constructor for RoomEditController.
   */
  public RoomEditController(RoomModel room) {
    this.room = room;
    this.view = new RoomView(this, room);
  }

  /**
   * Decides what happens on `saveButton` press:
   * Room gets updated if entries are valid. 
   */
  public void saveRoom() {
    room.clearErrors();
    if (room.isValid()) {
      if (room.update()) {
        view.success("updated");
      }
    }
  }

  public void toggleDisabled() {
    view.toggleDisabled();
  }
}
