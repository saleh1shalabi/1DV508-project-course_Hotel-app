package room;

import common.Util;
import hotelproject.Controller;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import javafx.beans.property.SimpleListProperty;
import javafx.scene.Parent;
import javafx.scene.control.MultipleSelectionModel;

public class RoomListController extends Controller {
  private RoomListModel roomListModel;
  private RoomFilterView filterView;
  private RoomListView view;

  /**
   * Create a RoomListController.
   */
  public RoomListController() {
    roomListModel = new RoomListModel();
    filterView = new RoomFilterView();
    view       = new RoomListView(roomListModel.getRoomList(), filterView.getView());
    filterView.getSlider().valueProperty().addListener((obs, o, n) -> {
      if (!filterView.getSlider().isValueChanging()
          || n.doubleValue() == filterView.getSlider().getMax()
          || n.doubleValue() == filterView.getSlider().getMin()) {
        getRooms((int)Math.round(n.doubleValue()));
      }
    });
  }

  public MultipleSelectionModel<RoomModel> getSelectionModel() {
    return view.getSelectionModel();
  }

  public SimpleListProperty<RoomModel> getRoomList() {
    return roomListModel.getRoomList();
  }

  /**
   * Get the Rooms that are available for the given date range, ignore the updateId booking.
   * @param start The start date of the booking period.
   * @param end The end date of the booking period.
   * @param updateId The id of the booking we are updating.
   */
  public void getRooms(LocalDate start, LocalDate end, Integer updateId) {
    roomListModel.setUpdateId(updateId);
    getRooms(start, end);
  }

  /**
   * Get the Rooms that are available for the given date range.
   * @param start The start date of the booking period.
   * @param end The end date of the booking period.
   */
  public void getRooms(LocalDate start, LocalDate end) {
    roomListModel.setStartDate(start);
    roomListModel.setEndDate(end);
    roomListModel.getRooms();
  }

  /**
   * Get the rooms with the matching number of beds.
   * @param beds The number of beds in the room.
   */
  public void getRooms(int beds) {
    roomListModel.setBeds(beds);
    getRooms();
  }

  /**
   * Get all the rooms from the database.
   */
  public void getRooms() {
    roomListModel.getRooms();
  }

  /**
   * Delete a room from the database.
   */
  public void deleteRoom() {
    RoomModel roomToDelete = getSelectionModel().getSelectedItem();
    try {
      roomListModel.remove(roomToDelete);
    } catch (SQLIntegrityConstraintViolationException e) {
      Util.displayAlert("information", null,
          "Room " + roomToDelete.getId() + " is booked and can't be deleted.");
    } catch (SQLException er) {
      Util.displayAlert("information", null, "Something went wrong.");
    }
  }

  @Override
  public Parent getView() {
    return view.getView();
  }
}
