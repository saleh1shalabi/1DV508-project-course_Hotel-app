package booking;

import common.DateRange;
import customer.AddCustomerController;
import customer.ListCustomerController;
import java.time.LocalDate;
import javafx.beans.binding.Bindings;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

public class BookingAddView {
  private GridPane rootNode     = new GridPane();
  private VBox dateBox          = new VBox(10);
  private Label dateTitle       = new Label("Booking period");
  private DateRange dateRange   = new DateRange();
  private VBox roomBox          = new VBox(10);
  private Label roomTitle       = new Label("No room selected");
  private Label bedsValue       = new Label("");
  private Label sizeValue       = new Label("");
  private Label bedsLabel       = new Label("Beds:");
  private Label sizeLabel       = new Label("Size:");
  private VBox roomValueColumn  = new VBox(5, bedsValue, sizeValue);
  private VBox roomLabelColumn  = new VBox(5, bedsLabel, sizeLabel);
  private HBox roomRow          = new HBox(10, roomLabelColumn, roomValueColumn);
  private Button selectRoom     = new Button("Select room");
  private Popup roomsPopup      = new Popup();
  private VBox roomsWrapper     = new VBox(10);
  private VBox customerBox      = new VBox(10);
  private Label customerTitle   = new Label("No customer selected");
  private Label emailValue      = new Label("");
  private Label phoneValue      = new Label("");
  private Label emailLabel      = new Label("Email:");
  private Label phoneLabel      = new Label("Phone:");
  private VBox customerValueColumn = new VBox(5, emailValue, phoneValue);
  private VBox customerLabelColumn = new VBox(5, emailLabel, phoneLabel);
  private HBox customerRow      = new HBox(10, customerLabelColumn, customerValueColumn);
  private Button selectCustomer = new Button("Select customer");
  private Button newCustomer    = new Button("New customer");
  private Popup customersPopup  = new Popup();
  private VBox customersWrapper = new VBox(10);
  private VBox paidBox          = new VBox(10);
  private Label paidTitle       = new Label("Paid?");
  private ToggleGroup paidGroup = new ToggleGroup();
  private RadioButton paidYes   = new RadioButton("Yes");
  private RadioButton paidNo    = new RadioButton("No");
  private VBox paymentBox       = new VBox(10);
  private Label paymentTitle    = new Label("Payment method");
  private Button saveButton     = new Button("Save");
  private ComboBox<String> paymentMethod = new ComboBox<>();
  private BookingAddController bac;
  private Booking booking;

  /**
   * View for adding a booking.
   */
  public BookingAddView(BookingAddController bac, Booking booking) {
    this.bac = bac;
    this.booking = booking;
    setupBindings();
    setupDateSection();
    setupRoomSection();
    setupCustomerSection();
    setupPaidSection();
    setupPaymentSection();
    ColumnConstraints column1 = new ColumnConstraints();
    ColumnConstraints column2 = new ColumnConstraints();
    column1.setPercentWidth(50);
    column2.setPercentWidth(50);
    rootNode.getColumnConstraints().addAll(column1, column2);
    rootNode.setVgap(10);
    rootNode.setHgap(10);
    rootNode.add(dateBox, 0, 0, 2, 1);
    rootNode.addRow(1, roomBox, customerBox);
    rootNode.addRow(2, paidBox, paymentBox);
    rootNode.add(saveButton, 0, 3, 2, 1);
    saveButton.getStyleClass().add("big-button");
    GridPane.setHalignment(saveButton, HPos.CENTER);
    saveButton.setOnAction(e -> {
      if (booking.getId() > 0) {
        booking.updateBooking();
      } else {
        if (booking.addBooking()) {
          bac.newBooking();
        }
      }
    });
  }

  private void setupBindings() {
    dateRange.startDateProperty().bindBidirectional(booking.startDateProperty());
    dateRange.endDateProperty().bindBidirectional(booking.endDateProperty());
    dateRange.startDateProperty().addListener((e, o, n) -> booking.roomProperty().set(null));
    dateRange.endDateProperty().addListener((e, o, n) -> booking.roomProperty().set(null));
    roomTitle.textProperty().bind(
        Bindings.createStringBinding(() ->
          booking.roomProperty().isNull().get()
            ? "No room selected"
            : "Room #" + String.valueOf(booking.roomProperty().get().idProperty().get()),
          booking.roomProperty()));
    bedsValue.textProperty().bind(
        Bindings.createStringBinding(() ->
          booking.roomProperty().isNull().get()
            ? ""
            : String.valueOf(booking.roomProperty().get().bedsProperty().get()),
          booking.roomProperty()));
    sizeValue.textProperty().bind(
        Bindings.createStringBinding(() ->
          booking.roomProperty().isNull().get()
            ? ""
            : String.valueOf(booking.roomProperty().get().sizeProperty().get()),
          booking.roomProperty()));

    customerTitle.textProperty().bind(
        Bindings.createStringBinding(() ->
          booking.customerProperty().isNull().get()
            ? "No customer selected"
            : booking.customerProperty().get().getFirstName() + " "
            + booking.customerProperty().get().getLastName(),
          booking.customerProperty()));
    emailValue.textProperty().bind(
        Bindings.createStringBinding(() ->
          booking.customerProperty().isNull().get()
            ? ""
            : booking.customerProperty().get().getEmail(),
          booking.customerProperty()));
    phoneValue.textProperty().bind(
        Bindings.createStringBinding(() ->
          booking.customerProperty().isNull().get()
            ? ""
            : booking.customerProperty().get().getPhone(),
          booking.customerProperty()));
    paidNo.selectedProperty().set(!booking.paidProperty().get());
    paidYes.selectedProperty().bindBidirectional(booking.paidProperty());
    paymentMethod.valueProperty().bindBidirectional(booking.paymentMethodProperty());
    saveButton.disableProperty().bind(
        Bindings.or(booking.roomProperty().isNull(), booking.customerProperty().isNull()));
    customerRow.visibleProperty().bind(booking.customerProperty().isNotNull());
    roomRow.visibleProperty().bind(booking.roomProperty().isNotNull());
  }

  private void setupDateSection() {
    dateBox.getStyleClass().add("card");
    dateBox.setAlignment(Pos.CENTER);
    dateBox.getChildren().addAll(dateTitle, dateRange.getNode());
  }

  private void setupRoomSection() {
    roomBox.getStyleClass().add("card");
    roomBox.setAlignment(Pos.CENTER);
    roomRow.setAlignment(Pos.CENTER);
    roomBox.getChildren().addAll(roomTitle, roomRow, selectRoom);
    selectRoom.setOnAction(e -> {
      bac.selectRoom();
      Bounds b = selectRoom.localToScreen(selectRoom.getBoundsInLocal());
      roomsPopup.show(selectRoom, b.getMaxX() + 10, b.getMinY() - 200);
      roomsWrapper.requestFocus();
    });
    roomsWrapper.setPadding(new Insets(10));
    roomsWrapper.getChildren().addAll(
        new Label("Available rooms for the booking period"), bac.getRoomListView());
    roomsPopup.getContent().addAll(roomsWrapper);
    roomsPopup.setAutoHide(true);
    roomsPopup.setOnHiding(e -> {
      booking.roomProperty().unbind();
      rootNode.requestFocus();
    });
  }

  private void setupCustomerSection() {
    customerBox.getStyleClass().add("card");
    customerBox.setAlignment(Pos.CENTER);
    customerRow.setAlignment(Pos.CENTER);
    customerBox.getChildren().addAll(customerTitle, customerRow, selectCustomer);
    selectCustomer.setOnAction(e -> {
      bac.selectCustomer();
      Bounds b = rootNode.localToScreen(rootNode.getBoundsInLocal());
      customersPopup.show(selectCustomer, b.getMinX() - 200, b.getMinY());
      customersWrapper.requestFocus();
    });
    customersWrapper.setMinWidth(750);
    customersWrapper.setPadding(new Insets(10));
    ListCustomerController lcc = new ListCustomerController();
    lcc.getAllCustomers();
    HBox titleHbox = new HBox(10, new Label("Choose a customer"), newCustomer);
    titleHbox.setAlignment(Pos.CENTER_LEFT);
    customersWrapper.getChildren().addAll(titleHbox, bac.getCustomerListView());
    customersPopup.getContent().addAll(customersWrapper);
    customersPopup.setAutoHide(true);
    customersPopup.setOnHiding(e -> {
      booking.customerProperty().unbind();
      rootNode.requestFocus();
    });
    newCustomer.setOnAction(e -> newCustomerPopup());
  }

  private void newCustomerPopup() {
    Popup newCustomerPopup  = new Popup();
    VBox newCustomerWrapper = new VBox(10);
    newCustomerWrapper.setPadding(new Insets(10));
    AddCustomerController adc = new AddCustomerController();
    newCustomerWrapper.getChildren().addAll(new Label("Add new customer"),adc.getView());
    newCustomerPopup.getContent().add(newCustomerWrapper);
    newCustomerPopup.setAutoHide(true);
    newCustomerPopup.setOnHiding(e -> {
      bac.getCustomers();
      customersPopup.requestFocus();
    });
    Bounds b = customersWrapper.localToScreen(customersWrapper.getBoundsInLocal());
    newCustomerPopup.show(newCustomer, b.getMaxX(), b.getMinY() - 4);
    newCustomerWrapper.requestFocus();
  }

  private void setupPaidSection() {
    paidBox.getStyleClass().add("card");
    paidBox.setAlignment(Pos.CENTER);
    paidYes.setToggleGroup(paidGroup);
    paidNo.setToggleGroup(paidGroup);
    paidYes.getStyleClass().remove("radio-button");
    paidNo.getStyleClass().remove("radio-button");
    paidYes.getStyleClass().addAll("toggle-button", "left-toggle");
    paidNo.getStyleClass().addAll("toggle-button", "right-toggle");
    paidYes.setPrefWidth(80);
    paidNo.setPrefWidth(80);
    HBox paidButtonBox = new HBox(paidYes, paidNo);
    paidButtonBox.setAlignment(Pos.CENTER);
    paidBox.getChildren().addAll(paidTitle, paidButtonBox);
  }

  private void setupPaymentSection() {
    paymentBox.getStyleClass().add("card");
    paymentMethod.getStyleClass().add("combo");
    paymentMethod.getItems().setAll("Card", "Cash", "Invoice");
    if (paymentMethod.getValue() == null) {
      paymentMethod.getSelectionModel().select("Card");
    } else {
      paymentMethod.getSelectionModel().select(
          paymentMethod.valueProperty().get().substring(0, 1).toUpperCase()
          + paymentMethod.valueProperty().get().substring(1));
    }
    paymentBox.setAlignment(Pos.CENTER);
    paymentBox.getChildren().addAll(paymentTitle, paymentMethod);
  }

  public LocalDate getStartDate() {
    return dateRange.getStartDate();
  }

  public LocalDate getEndDate() {
    return dateRange.getEndDate();
  }

  public Parent getView() {
    return rootNode;
  }
}
