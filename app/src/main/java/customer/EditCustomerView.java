package customer;

import common.Util;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class EditCustomerView {
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
  private Button btnEdit;

  /**
   * View for add customer.
   */
  public EditCustomerView(EditCustomerController controller, CustomerModel customer) {
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

    btnEdit = new Button("Edit Customer");
    btnEdit.setOnAction(e -> {
      btnEditCallback(controller, customer);
    });
    VBox vbEditCustomer = new VBox();
    vbEditCustomer.setAlignment(Pos.CENTER);
    vbEditCustomer.getChildren().addAll(lblFirstName, tfFirstName, lblLastName, tfLastName,
        lblEmail, tfEmail, lblPhone, tfPhone, lblStreetAddress, tfStreetAddress,
        lblCity, tfCity, lblZipCode, tfZipcode, btnEdit);
    vbEditCustomer.setSpacing(5);
    vbEditCustomer.setPadding(new Insets(30));
    this.view.getChildren().addAll(vbEditCustomer);
  }

  /**
   * Returns view with a customer filled in the text fields.
   */
  public Parent getView(CustomerModel customer) {
    tfFirstName.setText(customer.getFirstName());
    tfLastName.setText(customer.getLastName());
    tfEmail.setText(customer.getEmail());
    tfPhone.setText(customer.getPhone());
    tfStreetAddress.setText(customer.getStreetAddress());
    tfCity.setText(customer.getCity());
    tfZipcode.setText(customer.getZipCode().toString());
    return view;
  }

  private void btnEditCallback(EditCustomerController controller, CustomerModel customer) {
    try {
      customer.setAllValues(tfFirstName.getText(), tfLastName.getText(), tfEmail.getText(), 
          tfPhone.getText(), tfStreetAddress.getText(), tfCity.getText(), 
          Integer.parseInt(tfZipcode.getText()));
      controller.editCustomer(customer);
    } catch (NumberFormatException e) {
      Util.displayAlert("Error", "Error", "Invalid zip code");
    }
  }
}
