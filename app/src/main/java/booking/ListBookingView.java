package booking;

import common.Util;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleListProperty;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


public class ListBookingView {
  private BorderPane rootNode  = new BorderPane();
  private VBox vbox            = new VBox(5);
  private HBox hboxForDp       = new HBox(5);
  private HBox hboxForButtons  = new HBox(5);
  private Label noBooking      = new Label("Select a booking to the left to view it's details.");
  private Label title          = new Label();
  private TextField tfSearch   = new TextField();
  private CheckBox oldBookings = new CheckBox("Old bookings");
  private DatePicker calender  = new DatePicker();
  private Button resetButton   = new Button("Reset fields");
  private Button deleteButton   = new Button("Delete");
  private ListView<Booking> bookingNodes;

 
  /**
   * Creats the view for the bookings.
   */
  
  public ListBookingView(ListBookingController ctrl, SimpleListProperty<Booking> bookings) {
    bookingNodes = new ListView<Booking>(bookings);
    rootNode.setCenter(new Pane(noBooking));
    title.textProperty().bind(
        Bindings.concat("Displaying ", bookings.sizeProperty().asString(), " bookings."));
    bookingNodes.setPrefWidth(330);
    bookingNodes.setMinWidth(Control.USE_PREF_SIZE);
    bookingNodes.setMaxWidth(Control.USE_PREF_SIZE);
    bookingNodes.setCellFactory(param -> new ListViewBookingCell());
    bookingNodes.getSelectionModel().selectedItemProperty().addListener((e, o, n) -> {
      if (n != null) {
        rootNode.setCenter(ctrl.editBooking(n));
        deleteButton.setVisible(true);
      } else {
        deleteButton.setVisible(false);
      }
    });

    tfSearch.setOnKeyPressed(event -> {
      if (event.getCode() != KeyCode.BACK_SPACE) {
        ctrl.getSearchedBookings(tfSearch.getText() + event.getText(), oldBookings.isSelected());
      } else {
        ctrl.getSearchedBookings(tfSearch.getText(), oldBookings.isSelected());
      }
    });

    oldBookings.setOnAction(event -> {
      if (oldBookings.isSelected()) {
        ctrl.getAllBookings();
        ctrl.getSearchedBookings(tfSearch.getText(), oldBookings.isSelected());
      } else {
        ctrl.getCurrentBookings();
        ctrl.getSearchedBookings(tfSearch.getText(), oldBookings.isSelected());
      }
    });

    calender.setOnAction(event -> {
      ctrl.getSearchedBookings(tfSearch.getText(), oldBookings.isSelected(), calender.getValue());
    });

    resetButton.setOnAction(event -> {
      tfSearch.clear();
      calender.getEditor().clear();
      oldBookings.setSelected(false);
      ctrl.beforeRender();
    });

    deleteButton.setOnAction(event -> {
      Booking b = bookingNodes.getSelectionModel().selectedItemProperty().get();
      if (b != null) {
        Util.displayAlert("CONFIRMATION", "Delete booking",
            "Are you sure you want to delete the booking?"
        ).filter(res -> res == ButtonType.OK).ifPresent(res -> {
          if (b.deleteBooking()) {
            bookingNodes.getItems().remove(b);
            Util.displayAlert("Information", "Booking Deleted", "The booking has been deleted!");
          } else {
            Util.displayAlert("error", "Error", "Could not delete the booking. Try again later.");
          }
        });
      } else {
        Util.displayAlert("error", "Error", "You need to select a booking.");
      }
    });

    
    deleteButton.getStyleClass().add("delete-button");
    deleteButton.setVisible(false);
    tfSearch.setPromptText("Search for booking");
    oldBookings.setSelected(false);
    hboxForDp.getChildren().addAll(calender, oldBookings);
    hboxForButtons.getChildren().addAll(resetButton, deleteButton);
    vbox.getChildren().addAll(title, tfSearch, hboxForDp, hboxForButtons);
    vbox.setPadding(new Insets(0, 0, 5, 0));
    BorderPane bp = new BorderPane();
    bp.setTop(vbox);
    bp.setCenter(bookingNodes);
    rootNode.setLeft(bp);
  }

  public Parent getView() {
    return rootNode;
  }
}
