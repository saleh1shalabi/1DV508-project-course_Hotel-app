package booking;

import common.Email;
import common.Util;
import customer.CustomerModel;
import hotelproject.DataHandler;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import room.RoomModel;

public class Booking {
  private final IntegerProperty id                     = new SimpleIntegerProperty();
  private final IntegerProperty roomId                 = new SimpleIntegerProperty();
  private final IntegerProperty customerId             = new SimpleIntegerProperty();
  private final ObjectProperty<LocalDate> startDate    = new SimpleObjectProperty<LocalDate>();
  private final ObjectProperty<LocalDate> endDate      = new SimpleObjectProperty<LocalDate>();
  private final BooleanProperty paid                   = new SimpleBooleanProperty();
  private final StringProperty paymentMethod           = new SimpleStringProperty();
  private final ListProperty<String> errors            = new SimpleListProperty<>();
  private final ObjectProperty<RoomModel> room         = new SimpleObjectProperty<RoomModel>();
  private final ObjectProperty<CustomerModel> customer = new SimpleObjectProperty<CustomerModel>();
  private boolean valid                                = true;


  /**
   * Creats all the booking objects with the map return from the Datahandler. 
   */

  public Booking(Map<String, Object> values) {
    setAllValues(values);
  }
  
  /**
   * Sets model for a new booking object.
   */

  public Booking(Integer id, Integer roomId, Integer customerId, LocalDate startDate,
        LocalDate endDate, boolean paid, String paymentMethod) {
    this.id.set(id);
    this.roomId.set(roomId);
    this.customerId.set(customerId);
    this.startDate.set(startDate);
    this.endDate.set(endDate);
    this.paid.set(paid);
    this.paymentMethod.set(paymentMethod);
  }
  
  public Booking() {
    clearErrors();
  }

  public final ObjectProperty<RoomModel> roomProperty() {
    return this.room;
  }

  public final void setRoom(final RoomModel room) {
    this.roomProperty().set(room);
  }

  public final RoomModel getRoom() {
    return this.roomProperty().get();
  }

  public final ObjectProperty<CustomerModel> customerProperty() {
    return this.customer;
  }

  public final CustomerModel getCustomer() {
    return this.customerProperty().get();
  }

  public final IntegerProperty idProperty() {
    return this.id;
  }

  public final void setId(int id) {
    this.idProperty().set(id);
  }

  public Integer getId() {
    return id.get();
  }

  /**
   * Room functions. 
   */
  public void isRoomIdValid() {
    if (1001 > getRoomId() && getRoomId() > 9999) {
      Util.displayAlert("Error", "Error", "Invalid roomnumber, must be between 1001 and 9999");
      valid = false;
    }
  }

  public final IntegerProperty roomIdProperty() {
    return this.roomId;
  }

  public Integer getRoomId() {
    return roomId.get();
  } 

  public final IntegerProperty customerIdProperty() {
    return this.customerId;
  }
  
  public Integer getCustomerId() {
    return customerId.get();
  }


  public final ObjectProperty<LocalDate> startDateProperty() {
    return this.startDate;
  }

  public final BooleanProperty paidProperty() {
    return paid;
  }

  public boolean isPaid() {
    return paid.get();
  }

  public String getPaymentMethod() {
    return paymentMethod.get();
  }

  public LocalDate getStartDate() {
    return startDate.get();
  }


  public final ObjectProperty<LocalDate> endDateProperty() {
    return this.endDate;
  }

  public LocalDate getEndDate() {
    return endDate.get();
  }

  public final StringProperty paymentMethodProperty() {
    return this.paymentMethod;
  }

  /**
   * Start date functions.
   */
  public void isStartDateValid() {
    if (getStartDate().isBefore(LocalDate.now())) {
      Util.displayAlert("Error", "Error", "Chosen startdate has already passed, try again!");
      valid = false;
    }
  }

  /**
   * End date functions.
   */
  public void isEndDateValid() {
    if (getEndDate().isBefore(LocalDate.now())) {
      Util.displayAlert("Error", "Error", "Chosen enddate has already passed, try again!");
      valid = false;
    }
  }

  /**
   * Payment functions.
   */
  public void isPaymentMethodValid() {
    if (!getPaymentMethod().equalsIgnoreCase("cash")
        || !getPaymentMethod().equalsIgnoreCase("card")
        || !getPaymentMethod().equalsIgnoreCase("invoice")) {
      Util.displayAlert("Error", "Error", "Invalid payment method! (cash, card, invoice)");
      valid = false;
    }
  }

  /**

   * Sets all values to the model with a map.
   */
  public void setAllValues(Map<String, Object> values) {
    id.set((Integer) values.get("id"));
    roomId.set((Integer) values.get("room_id"));
    startDate.set(((Date) values.get("start_date")).toLocalDate());
    endDate.set(((Date) values.get("end_date")).toLocalDate());
    paid.set((Boolean) values.get("paid"));
    paymentMethod.set(values.get("payment_method").toString());
    roomProperty().set(new RoomModel((int) values.get("room_id")));
    if ((Integer) values.get("customer_id") != null) {
      customerProperty().set(new CustomerModel((Integer) values.get("customer_id")));
    } else {
      customerProperty().set(null);
    }
  }

  /**
   * Sets all values to the model with variable. 
   */
  public void setAllValues(Integer id, Integer roomId, Integer customerId, LocalDate startDate,
      LocalDate endDate, Boolean paid, String paymentMethod) {
    this.id.set(id);
    this.roomId.set(roomId);
    this.customerId.set(customerId);
    this.startDate.set(startDate);
    this.endDate.set(endDate);
    this.paid.set(paid);
    this.paymentMethod.set(paymentMethod);
  }

  /**
   * Add a new booking to the database.
   */
  public boolean addBooking() {
    List<String> query = new ArrayList<>();
    Collections.addAll(query,
        "INSERT INTO booking (customer_id, room_id, start_date, end_date, paid, payment_method)"
        + "VALUES (?, ?, ?, ?, ?, ?)", String.valueOf(getCustomer().getId()),
        String.valueOf(getRoom().getId()), getStartDate().toString(),
        getEndDate().toString(), paid.get() ? "1" : "0", paymentMethod.get());
    try {
      DataHandler.setData(query);
      List<String> lastInsertId = new ArrayList<>();
      lastInsertId.add("SELECT LAST_INSERT_ID() AS id;");
      BigInteger lastId = (BigInteger) DataHandler.getData(lastInsertId).get(0).get("id");
      this.setId(lastId.intValue());
      Util.displayAlert(
          "information", "New booking created",
          "A new booking has been created for room #" + getRoom().getId() + "."
          + System.lineSeparator() + "An email has been sent to the customer."
      );
      Email email = new Email();
      email.sendBooking(this);
      return true;
    } catch (SQLException e) {
      Util.displayAlert("error", "Error", "Could not add the booking. Try again later.");
      return false;
    }
  }

  /**
   * Update the booking object in the database.
   */
  public void updateBooking() {
    List<String> query = new ArrayList<>();
    Collections.addAll(query,
        "UPDATE booking SET customer_id=?, room_id=?, start_date=?, end_date=?, paid=?," 
        + " payment_method=? WHERE id=?",
        String.valueOf(getCustomer().getId()), String.valueOf(getRoom().getId()),
        getStartDate().toString(), getEndDate().toString(), 
        isPaid() ? "1" : "0", getPaymentMethod(),
        String.valueOf(getId()));
    try {
      DataHandler.setData(query);
      Util.displayAlert(
          "information", "Booking updated",
          "Booking #" + getId() + " has been updated."
          + System.lineSeparator() + "An email has been sent to the customer."
      );
      Email email = new Email();
      email.sendBooking(this);
    } catch (SQLException e) {
      Util.displayAlert("error", "Error", "Could not update the booking. Try again later.");
    }
  }

  /**
   * Deletes the booking object in the database.
   */
  public boolean deleteBooking() {
    List<String> query = new ArrayList<String>();
    query.add("DELETE FROM booking WHERE id = ?;");
    query.add(this.getId().toString());
    try {
      DataHandler.setData(query);
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  /**
   *  Returns true if the booking object is valid.
   */
  public boolean isValid() {
    valid = true;
    isRoomIdValid();
    isStartDateValid();
    isEndDateValid();
    isPaymentMethodValid();
    return valid;
  }

  // Methods for errors
  public final ObservableList<String> getErrors() {
    return this.errorsProperty().get();
  }


  public final void setErrors(final ObservableList<String> errors) {
    this.errorsProperty().set(errors);
  }

  public final ListProperty<String> errorsProperty() {
    return this.errors;
  }

  public void clearErrors() {
    setErrors(FXCollections.observableArrayList(new ArrayList<String>()));
  }
}