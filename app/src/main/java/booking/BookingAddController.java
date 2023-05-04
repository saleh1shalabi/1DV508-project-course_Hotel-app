package booking;

import customer.ListCustomerController;
import hotelproject.Controller;
import java.time.LocalDate;
import javafx.beans.property.SimpleListProperty;
import javafx.scene.Parent;
import room.RoomListController;
import room.RoomModel;

public class BookingAddController extends Controller {
  private RoomListController rlc = new RoomListController();
  private ListCustomerController lcc = new ListCustomerController();
  private BookingAddView addView;
  private Booking booking;

  /**
   * Constructor for adding a booking.
   */
  public BookingAddController() {
    this.booking = new Booking();
    addView = new BookingAddView(this, booking);
    booking.startDateProperty().set(LocalDate.now());
    booking.endDateProperty().set(LocalDate.now().plusDays(1));
  }

  /**
   * Constructor for updating a booking.
   */
  public BookingAddController(Booking booking) {
    this.booking = booking;
    addView = new BookingAddView(this, booking);
  }

  public void newBooking() {
    BookingAddController bac = new BookingAddController();
    bac.renderView(bac.getView(), root);
  }

  /**
   * Populate the list of rooms and try to select the previous selected room.
   */
  public void selectRoom() {
    getRooms();
    if (booking.roomProperty().get() != null) {
      rlc.getRoomList().removeIf(e -> e.getId() == booking.roomProperty().get().getId());
      rlc.getRoomList().add(0, booking.roomProperty().get());
      rlc.getSelectionModel().select(booking.roomProperty().get());
    }
    booking.roomProperty().bind(rlc.getSelectionModel().selectedItemProperty());
  }

  public SimpleListProperty<RoomModel> getRooms() {
    rlc.getRooms(addView.getStartDate(), addView.getEndDate(), booking.getId());
    return rlc.getRoomList();
  }

  /**
   * Populate the list of customers and try to select the previous selected customer.
   */
  public void selectCustomer() {
    getCustomers();
    if (booking.customerProperty().get() != null) {
      lcc.getCustomerList().removeIf(e ->
          e.getId().intValue() == booking.customerProperty().get().getId().intValue());
      lcc.getCustomerList().add(0, booking.customerProperty().get());
      lcc.getSelectionModel().select(booking.customerProperty().get());
    }
    booking.customerProperty().bind(lcc.getSelectionModel().selectedItemProperty());
  }

  public void getCustomers() {
    lcc.getAllCustomers();
  }

  public Parent getRoomListView() {
    return rlc.getView();
  }

  public Parent getCustomerListView() {
    return lcc.getView();
  }

  @Override
  public Parent getView() {
    return addView.getView();
  }
}
