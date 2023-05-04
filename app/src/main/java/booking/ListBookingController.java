package booking;

import hotelproject.Controller;
import hotelproject.DataHandler;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.scene.Parent;

public class ListBookingController extends Controller {
  private ListBookingView view;
  private SimpleListProperty<Booking> bookings;
  
  public ListBookingController() {
    bookings = new SimpleListProperty<>(FXCollections.observableArrayList());
    this.view = new ListBookingView(this, bookings);
  }

  @Override
  protected void beforeRender() {
    this.view = new ListBookingView(this, bookings);
    getCurrentBookings();
  }
  
  /**
   * Returns all booking in a list with all the bookings as booking objects.
   */
  public void getAllBookings() {
    List<String> query = new ArrayList<String>();
    query.add("SELECT * FROM booking ORDER BY id;");
    bookings.clear();
    DataHandler.getData(query).forEach(e -> bookings.add(new Booking(e)));
  }

  /**
   * Returns the upcoming and  list with all the bookings as booking objects.
   */
  public void getCurrentBookings() {
    List<String> query = new ArrayList<String>();
    query.add("SELECT * FROM booking WHERE end_date > ? ORDER BY id;");
    query.add(LocalDate.now().toString());
    bookings.clear();
    DataHandler.getData(query).forEach(e -> bookings.add(new Booking(e)));
  }
  
  /**
   * Returns the query as a string.
   */
  public String searchCurrentBookings() {
    String query = ("SELECT * FROM search WHERE (CONCAT_WS(' ',first_name, last_name) LIKE ?"
        + "OR id LIKE ?"
        + "OR email LIKE ?"
        + "OR customer_id LIKE ?"
        + "OR REPLACE(REPLACE(REPLACE(REPLACE(REPLACE("
        + "phone,'+',''),'(',''),')',''),'.',''),'-','') LIKE ?"
        + "OR street_address LIKE ?)"
        + "AND end_date > ?"
        + "ORDER BY id;"
    );
    return query;
  }

  /**
   * Returns the query for searching all bookings as a string.
   */
  public String searchAllBookings() {
    String query = ("SELECT * FROM search WHERE CONCAT_WS(' ',first_name, last_name) LIKE ?"
        + "OR id LIKE ?"
        + "OR email LIKE ?"
        + "OR customer_id LIKE ?"
        + "OR REPLACE(REPLACE(REPLACE(REPLACE(REPLACE("
        + "phone,'+',''),'(',''),')',''),'.',''),'-','') LIKE ?"
        + "OR street_address LIKE ?"
        + "ORDER BY id;"
      );
    return query;
  }

  /**
   * Returns the query for searching booking for specific date as a string.
   */
  public String getBookingForDate() {
    String query = ("SELECT * FROM search WHERE (CONCAT_WS(' ',first_name, last_name) LIKE ?"
        + " OR id LIKE ?"
        + " OR email LIKE ?"
        + " OR customer_id LIKE ?"
        + " OR REPLACE(REPLACE(REPLACE(REPLACE(REPLACE("
        + "phone,'+',''),'(',''),')',''),'.',''),'-','') LIKE ? "
        + " OR street_address LIKE ?)"
        + " AND start_date <= ? AND end_date >= ?"
    );
    return query;
  }

  /**
   * Query to filter the bookings in listview.
   */
  public void getSearchedBookings(String search, boolean oldBookings) {
    List<String> query = new ArrayList<String>();

    if (!oldBookings) {
      query.add(searchCurrentBookings());
    } else {
      query.add(searchAllBookings());
    }

    StringBuilder sb = new StringBuilder();
    sb.append("%"); 
    sb.append(search);
    sb.append("%"); 
    search = sb.toString();
  
    if (!oldBookings) {
      for (int i = 0; i <= 5; i++) {
        query.add(search);
      } 
      query.add(LocalDate.now().toString());
    } else {
      for (int i = 0; i <= 5; i++) {
        query.add(search);
      }
    }
  
    bookings.clear();
    DataHandler.getData(query).forEach(e -> bookings.add(new Booking(e)));
  }

  /**
   * Query to filter the bookings in listview with the date picker.
   */
  public void getSearchedBookings(String search, boolean oldBookings, LocalDate date) {
    List<String> query = new ArrayList<String>();
    query.add(getBookingForDate());

    StringBuilder sb = new StringBuilder();
    sb.append("%"); //0-fler chars innan
    sb.append(search);
    sb.append("%"); //0-fler chars efter
    search = sb.toString(); //Setting searchvariable to SQLfiendly syntax
  
    for (int i = 0; i <= 5; i++) {
      query.add(search);
    }
    query.add(date.toString());
    query.add(date.toString());
    
    bookings.clear();
    DataHandler.getData(query).forEach(e -> bookings.add(new Booking(e)));
  }

  /**
   * Delete booking.
   */
  public void deleteBooking(Integer bookingId) {
    List<String> query = new ArrayList<>();
    String strBookingId = bookingId.toString();
    query.add("DELETE booking WHERE id = ? ");
    query.add(strBookingId);
    try {
      DataHandler.setData(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Parent editBooking(Booking booking) {
    BookingAddController bac = new BookingAddController(booking);
    return bac.getView();
  }

  /**
   * Return content.
   */
  @Override
  public Parent getView() {
    return view.getView();
  }

}