package room;

import hotelproject.App;
import hotelproject.Controller;
import javafx.scene.Parent;

public class DisplayAllRoomsController extends Controller {
  private RoomListController rlc = new RoomListController();
  private DisplayAllRoomsView allRoomsView = new DisplayAllRoomsView(this);
  private RoomEditController rec;

  /**
   * Controller for displaying all rooms and their details.
   */
  public DisplayAllRoomsController() {
    rlc.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal == null) {
        allRoomsView.nothingSelected();
      } else {
        rec = new RoomEditController(newVal);
        rec.toggleDisabled();
        allRoomsView.displayRoomDetails(rec.getView());
        if (newVal.isBooked() || !App.getUser().isAdmin()) { 
          allRoomsView.showDeleteButton(false);
        } else {
          allRoomsView.showDeleteButton(true);
        }
      }
    });
  }

  @Override
  protected void beforeRender() {
    rlc.getRooms();
  }

  public void editDetails() {
    rec.toggleDisabled();
  }

  public void deleteRoom() {
    rlc.deleteRoom();
  }

  /**
   * Display details about a room using a new view.
   * @param roomToDisplay The room to display details about.
   */
  public Parent getRoomDetails(RoomModel roomToDisplay) {
    rec = new RoomEditController(roomToDisplay);
    rec.toggleDisabled();
    return rec.getView();
  }

  public Parent getRoomList() {
    return rlc.getView();
  }

  @Override
  public Parent getView() {
    return allRoomsView.getView();
  }
  
}
