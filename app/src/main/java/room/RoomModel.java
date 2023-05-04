package room;

import hotelproject.DataHandler;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RoomModel {
  private final IntegerProperty id          = new SimpleIntegerProperty();
  private final IntegerProperty beds        = new SimpleIntegerProperty();
  private final IntegerProperty size        = new SimpleIntegerProperty();
  private final StringProperty location     = new SimpleStringProperty();
  private final StringProperty information  = new SimpleStringProperty();
  private final BooleanProperty inUse       = new SimpleBooleanProperty();
  private final ListProperty<String> errors = new SimpleListProperty<>();
  private boolean valid                     = true;

  public RoomModel(Map<String, Object> values) {
    setRoomValues(values);
  }

  /**
   * Creates room based on roomId.
   * @param roomId Primary key for room in which you want to create.
   */
  public RoomModel(int roomId) {
    getRoom(roomId);
  }

  public RoomModel() {
    clearErrors();
  }

  // Methods for id field
  public final IntegerProperty idProperty() {
    return this.id;
  }

  public final int getId() {
    return this.idProperty().get();
  }

  public final void setId(final int id) {
    this.idProperty().set(id);
  }

  // Methods for beds field
  public final IntegerProperty bedsProperty() {
    return this.beds;
  }

  public final int getBeds() {
    return this.bedsProperty().get();
  }

  public final void setBeds(final int beds) {
    this.bedsProperty().set(beds);
  }

  // Methods for location field
  public final StringProperty locationProperty() {
    return this.location;
  }

  public final String getLocation() {
    return this.locationProperty().get();
  }

  public final void setLocation(final String location) {
    this.locationProperty().set(location);
  }

  // Methods for information field
  public final StringProperty informationProperty() {
    return this.information;
  }

  public final String getInformation() {
    return this.informationProperty().get();
  }

  public final void setInformation(final String information) {
    this.informationProperty().set(information);
  }

  public final BooleanProperty inUseProperty() {
    return this.inUse;
  }

  public final Boolean getInUse() {
    return this.inUseProperty().get();
  }

  public final void setInUse(final Boolean inUse) {
    this.inUseProperty().set(inUse);
  }

  // Methods for size field
  public final IntegerProperty sizeProperty() {
    return this.size;
  }

  public final int getSize() {
    return this.sizeProperty().get();
  }

  public final void setSize(final int size) {
    this.sizeProperty().set(size);
  }

  // Methods for errors
  public final ListProperty<String> errorsProperty() {
    return this.errors;
  }

  public final ObservableList<String> getErrors() {
    return this.errorsProperty().get();
  }

  public final void setErrors(final ObservableList<String> errors) {
    this.errorsProperty().set(errors);
  }

  /**
   * Return true if the model is valid.
   */
  public boolean isValid() {
    valid = true;
    validId();
    validBeds();
    validSize();
    return valid;
  }

  public boolean isIdValid() {
    return getId() > 1000;
  }

  private void validId() {
    if (!isIdValid()) {
      getErrors().add("Room number can not be empty or below 1000!");
      valid = false;
    }
  }

  private void validBeds() {
    if (getBeds() < 1) {
      getErrors().add("Number of beds can not be empty!");
      valid = false;
    }
  }

  private void validSize() {
    if (getSize() < 1) {
      getErrors().add("Room size can not be empty!");
      valid = false;
    }
  }

  public void clearErrors() {
    setErrors(FXCollections.observableArrayList(new ArrayList<String>()));
  }

  /**
   * Set all values of the model with database values.
   * @param values The database values as a Map.
   */
  private void setRoomValues(Map<String, Object> values) {
    setId((int) values.get("id"));
    setBeds((int) values.get("beds"));
    setSize((int) values.get("size"));
    setLocation(values.get("location") == null ? "" : values.get("location").toString());
    setInformation(values.get("information") == null ? "" : values.get("information").toString());
  }

  /**
   * Add a room.
   */
  public boolean addRoom() {
    List<String> query = new ArrayList<>();
    Collections.addAll(query,
        "INSERT INTO room (id, beds, location, information, size) VALUES (?, ?, ?, ?, ?)",
        String.valueOf(getId()), String.valueOf(getBeds()), getLocation(),
        getInformation(), String.valueOf(getSize()));
    try {
      DataHandler.setData(query);
      return true;
    } catch (SQLIntegrityConstraintViolationException e) {
      getErrors().add("The room number already exists!");
    } catch (SQLException e) {
      getErrors().add("Something went wrong.");
    }
    return false;
  }

  /**
   * Updates room in database with values from `room`.
   * @return true if success, false, otherwise.
   */
  public boolean update() {
    if (!this.exists()) {
      getErrors().add("There's no room with this room number.");
      return false;
    }
    List<String> query = new ArrayList<>();
    Collections.addAll(query,
        "UPDATE room SET beds=?, size=?, location=?, information=? WHERE id=?",
        String.valueOf(getBeds()), String.valueOf(getSize()), getLocation(),
        getInformation(), String.valueOf(getId()));
    try {
      DataHandler.setData(query);
      clearErrors();
    } catch (SQLException e) {
      getErrors().add("Something went wrong.");
      return false;
    }
    return true;
  }

  /**
   * Mark room as not in use.
   * @throws SQLException if sql error.
   */
  public void delete() throws SQLException {
    List<String> query = new ArrayList<>();
    if (isBooked()) {
      query.add("UPDATE room SET in_use=0 WHERE id=?");
    } else {
      query.add("DELETE FROM room WHERE id=?");
    }
    query.add(String.valueOf(getId()));
    DataHandler.setData(query);
  }

  /**
   * Check to see if the Room is booked.
   * @return True if the room is booked, else false
   */
  public boolean isBooked() {
    List<String> query = new ArrayList<>();
    query.add("SELECT 1 FROM booking WHERE room_id = ? LIMIT 1");
    query.add(String.valueOf(getId()));
    return !DataHandler.getData(query).isEmpty();
  }

  /**
   * Check to see if Room exists in database.
   * @return True if exists, false if not.
   */
  private boolean exists() {
    List<String> currentRoom = new ArrayList<>();
    currentRoom.add("SELECT * FROM room WHERE id=?");
    currentRoom.add(String.valueOf(getId()));
    return !DataHandler.getData(currentRoom).isEmpty();
  }

  /**
   * Function to getRoom for `Edit room`. 
   * @param roomNumber The primary key for the room in which to edit.
   * @return True if room exists, false if not. 
   */
  public boolean getRoom(Integer roomNumber) {
    List<String> query = new ArrayList<>();
    query.add("SELECT * FROM room WHERE id = " + roomNumber.toString());
    List<Map<String, Object>> resultList = DataHandler.getData(query);
    if (!resultList.isEmpty()) {
      setRoomValues(resultList.get(0));
      return true;
    } else {
      return false;
    }
  }
}
