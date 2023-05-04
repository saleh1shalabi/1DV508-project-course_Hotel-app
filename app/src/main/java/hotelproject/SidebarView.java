package hotelproject;

import booking.BookingAddController;
import booking.ListBookingController;
import customer.AddCustomerController;
import customer.ListCustomerController;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import room.DisplayAllRoomsController;
import room.RoomController;
import user.CreateUser;
import user.UserChanges;

public class SidebarView {
  private Accordion sidebarButtons  = new Accordion();
  private ToggleGroup menuGroup     = new ToggleGroup();
  private VBox bookingButtons       = new VBox();
  private VBox roomButtons          = new VBox();
  private VBox customerButtons      = new VBox();
  private VBox userButtons          = new VBox();
  private TitledPane bookings       = new TitledPane("Bookings", bookingButtons);
  private TitledPane rooms          = new TitledPane("Rooms", roomButtons);
  private TitledPane customers      = new TitledPane("Customers", customerButtons);
  private TitledPane users          = new TitledPane("Users", userButtons);
  private BorderPane mainPane;
  private RadioButton addRoomButton;

  /**
   * Create all menu buttons.
   */
  public SidebarView(BorderPane mainPane) {
    this.mainPane = mainPane;
    bookingButtons.setPadding(Insets.EMPTY);
    roomButtons.setPadding(Insets.EMPTY);
    customerButtons.setPadding(Insets.EMPTY);
    userButtons.setPadding(Insets.EMPTY);

    bookingButtons.getChildren().addAll(
        createButton("Add booking", new BookingAddController()),
        createButton("View bookings", new ListBookingController())
    );
    roomButtons.getChildren().addAll(
        addRoomButton = createButton("Add room", new RoomController()),
        createButton("View rooms", new DisplayAllRoomsController())
    );
    customerButtons.getChildren().addAll(
        createButton("Add customer", new AddCustomerController()),
        createButton("View customers", new ListCustomerController())
    );
    userButtons.getChildren().addAll(
        createButton("Add user", new CreateUser()),
        createButton("Edit user", new UserChanges(App.getUser().getUsername()))
       
    );
    sidebarButtons.getPanes().addAll(bookings, rooms, customers, users);
    App.adminFeature(List.of(users, addRoomButton));
  }


  /**
   * Create a radio button that looks like a toggle button.
   * Easy way to see which button is selected.
   * Might need to change the setOnAction, depending on how we want to handle button clicks.
   */
  private RadioButton createButton(String buttonText, Controller controller) {
    RadioButton button = new RadioButton(buttonText);
    button.setToggleGroup(menuGroup);
    button.getStyleClass().remove("radio-button");
    button.getStyleClass().addAll("toggle-button", "sb-button");
    button.setMaxWidth(Double.MAX_VALUE);
    button.setAlignment(Pos.CENTER_LEFT);
    button.setOnAction(e -> controller.renderView(controller.getView(), mainPane));
    return button;
  }

  public Accordion getView() {
    return sidebarButtons;
  }

  public ToggleGroup getGroup() {
    return menuGroup;
  }
}
