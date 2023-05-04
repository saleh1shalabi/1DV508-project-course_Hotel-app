package customer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class AddCustomerView {
  private VBox view = new VBox();
  private Label lblFirstName;
  private TextField tfFirstName;
  private Label lblLastName;
  private TextField tfLastName;
  private Label lblEmail;
  private TextField tfEmail;
  private Label lblPhone;
  private TextField tfPhone;
  private Label lblStreetAddress;
  private TextField tfStreetAddress;
  private Label lblCity;
  private TextField tfCity;
  private Label lblZipCode;
  private TextField tfZipcode;
  private Button btnAdd;

  /**
   * View for add customer.
   */
  public AddCustomerView(AddCustomerController controller) {
    lblFirstName = new Label("First name:");
    tfFirstName = new TextField();
    lblLastName = new Label("Last name:");
    tfLastName = new TextField();
    lblEmail = new Label("E-mail:");
    tfEmail = new TextField();
    lblPhone = new Label("Phone number:");
    tfPhone = new TextField();
    lblStreetAddress = new Label("Street address:");
    tfStreetAddress = new TextField();
    lblCity = new Label("City:");
    tfCity = new TextField();
    lblZipCode = new Label("Zip code:");
    tfZipcode = new TextField();

    btnAdd = new Button("Add Customer");
    btnAdd.setOnAction(e -> {
      btnAddCallback(controller);
    });
    VBox vbAddCustomer = new VBox();
    vbAddCustomer.setAlignment(Pos.CENTER);
    vbAddCustomer.getChildren().addAll(lblFirstName, tfFirstName, lblLastName, tfLastName,
        lblEmail, tfEmail, lblPhone, tfPhone, lblStreetAddress, tfStreetAddress,
        lblCity, tfCity, lblZipCode, tfZipcode, btnAdd);
    vbAddCustomer.setSpacing(5);
    vbAddCustomer.setPadding(new Insets(30));
    this.view.getChildren().addAll(vbAddCustomer);
  }

  public VBox getView() {
    return view;
  }

  private void btnAddCallback(AddCustomerController controller) {
    controller.addCustomer(tfFirstName, tfLastName, 
        tfEmail, tfPhone, tfStreetAddress, tfCity, tfZipcode);
  }
}
