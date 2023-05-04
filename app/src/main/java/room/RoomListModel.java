package room;

import hotelproject.DataHandler;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

public class RoomListModel {
  private SimpleListProperty<RoomModel> rooms;
  private SimpleIntegerProperty beds     = new SimpleIntegerProperty();
  private SimpleIntegerProperty updateId = new SimpleIntegerProperty();
  private LocalDate startDate            = null;
  private LocalDate endDate              = null;

  /**
   * Create a RoomListModel.
   */
  public RoomListModel() {
    this.rooms = new SimpleListProperty<>(FXCollections.observableArrayList());
    setBeds(0);
    setUpdateId(0);
  }

  public SimpleListProperty<RoomModel> getRoomList() {
    return rooms;
  }

  public IntegerProperty bedsProperty() {
    return this.beds;
  }

  public int getBeds() {
    return this.bedsProperty().get();
  }

  public void setBeds(int beds) {
    this.bedsProperty().set(beds);
  }

  public IntegerProperty updateIdProperty() {
    return this.updateId;
  }

  public int getUpdateId() {
    return this.updateIdProperty().get();
  }

  public void setUpdateId(int id) {
    this.updateIdProperty().set(id);
  }

  public void setStartDate(LocalDate start) {
    this.startDate = start;
  }

  public void setEndDate(LocalDate end) {
    this.endDate = end;
  }

  private List<String> buildQuery() {
    List<String> queryList = new ArrayList<>();
    String start = startDate == null ? null : startDate.toString();
    String end = endDate == null ? null : endDate.toString();
    Collections.addAll(queryList, start, start, start, end);

    StringBuilder sb = new StringBuilder();
    sb.append("SELECT * FROM room WHERE id NOT IN ");
    sb.append("(SELECT room_id as id FROM booking WHERE ");
    if (getUpdateId() != 0) {
      sb.append("id != ? AND ");
      queryList.add(0, String.valueOf(getUpdateId()));
    }
    sb.append("(? >= start_date AND ? < end_date) OR (? < start_date AND ? > start_date)) ");
    if (getBeds() != 0) {
      sb.append(String.format("AND beds %s ?", getBeds() >= 10 ? ">=" : "="));
      queryList.add(String.valueOf(getBeds()));
    }
    queryList.add(0, sb.toString());
    return queryList;
  }

  /**
   * Get the rooms from the database and add to the list.
   */
  public void getRooms() {
    clear();
    DataHandler.getData(buildQuery()).forEach(e -> add(new RoomModel(e)));
  }

  public void add(RoomModel roomModel) {
    rooms.add(roomModel);
  }

  public void remove(RoomModel room) throws SQLException {
    room.delete();
    rooms.remove(room);
  }

  public void clear() {
    rooms.clear();
  }
}
