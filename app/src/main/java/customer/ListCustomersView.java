package customer;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleListProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ListCustomersView {
  private BorderPane rootNode = new BorderPane();
  private VBox vbox           = new VBox();
  private TextField tfSearch  = new TextField();
  private Label noCustomer    = new Label("Select a customer to the left to view it's details.");
  private Label title         = new Label();
  private ListView<CustomerModel> customerNodes;
  
  /**
   * View of a list of customer.
   */
  public ListCustomersView(ListCustomerController ctrl, 
      SimpleListProperty<CustomerModel> customers) {
    customerNodes = new ListView<CustomerModel>(customers);
    rootNode.setCenter(new Pane(noCustomer));
    title.textProperty().bind(
        Bindings.concat("Displaying ", customers.sizeProperty().asString(), " customers"));
    customerNodes.setPrefWidth(330);
    customerNodes.setMinWidth(Control.USE_PREF_SIZE);
    customerNodes.setMaxWidth(Control.USE_PREF_SIZE);
    customerNodes.setCellFactory(param -> new ListViewCustomerCell());
    customerNodes.addEventHandler(
        ActionEvent.ACTION, e -> {
          rootNode.setCenter(ctrl.editCustomer(
              customerNodes.getSelectionModel().getSelectedItem()));
        });
    customerNodes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) ->
        rootNode.setCenter(
        newVal == null ? noCustomer : getCustomerDetails(newVal)));
    
    tfSearch.setOnKeyPressed(event -> {
      if (event.getCode() != KeyCode.BACK_SPACE) {
        ctrl.getSearchedCustomers(tfSearch.getText() + event.getText());
      } else {
        ctrl.getSearchedCustomers(tfSearch.getText());
      }
    });
    tfSearch.setPromptText("Search for customer(s)...");
    BorderPane bp = new BorderPane();
    vbox.setSpacing(5);
    vbox.getChildren().addAll(title,tfSearch);
    bp.setTop(vbox);
    bp.setCenter(customerNodes);
    bp.setPadding(new Insets(10));
    rootNode.setLeft(bp);
  }

  private Parent getCustomerDetails(CustomerModel customer) {
    VBox name = new VBox(new Label("Name: "
        + customer.getFirstName() + " " + customer.getLastName()));
    name.getStyleClass().add("card");
    VBox email = new VBox(new Label("Email: "
        + (customer.getEmail().isBlank() ? "No email set for this customer" : customer.getEmail())
    ));
    email.getStyleClass().add("card");
    VBox phone = new VBox(new Label("Phone: " + customer.getPhone()));
    phone.getStyleClass().add("card");
    VBox address = new VBox(new Label("Address:" + System.lineSeparator()
         + customer.getStreetAddress() + System.lineSeparator()
         + customer.getZipCode() + ", " + customer.getCity()));
    address.getStyleClass().add("card");
    VBox v = new VBox(10, name, email, phone, address);
    v.setFillWidth(true);
    return v;
  }

  public MultipleSelectionModel<CustomerModel> getSelectionModel() {
    return customerNodes.getSelectionModel();
  }

  public Parent getView() {
    return rootNode;
  }
}
