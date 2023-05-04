package customer;

import common.Email;
import common.Util;
import hotelproject.Controller;
import hotelproject.DataHandler;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

public class AddCustomerController extends Controller {
  private CustomerModel customer;
  private AddCustomerView view;

  public AddCustomerController() {
    this.customer = new CustomerModel();
    this.view = new AddCustomerView(this);
  }

  /**
   * Add new Customer.
   */
  public void addCustomer(TextField tfFirstName, TextField tfLastName, TextField tfEmail, 
      TextField tfPhone, TextField tfStreetAddress, TextField tfCity, TextField tfZipCode) {
    
    try {
      customer = new CustomerModel(tfFirstName.getText(), tfLastName.getText(), tfEmail.getText(), 
          tfPhone.getText(), tfStreetAddress.getText(), tfCity.getText(), 
          Integer.parseInt(tfZipCode.getText()));

      if (customer.isValid()) {
        List<String> query = new ArrayList<String>();
        query.add("INSERT INTO customer "
            + "(first_name, last_name, email, phone, street_address, city, zip_code) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?);");
        query.add(tfFirstName.getText());
        query.add(tfLastName.getText());
        query.add(tfEmail.getText());
        query.add(tfPhone.getText());
        query.add(tfStreetAddress.getText());
        query.add(tfCity.getText());
        query.add(tfZipCode.getText());
        
        try {
          DataHandler.setData(query);
          Util.displayAlert("Information", "Succesful", "Customer added succesfully");
          Email email = new Email();
          email.sendWelcome(customer);
          tfFirstName.setText("");
          tfLastName.setText("");
          tfEmail.setText("");
          tfPhone.setText("");
          tfStreetAddress.setText("");
          tfCity.setText("");
          tfZipCode.setText("");
        } catch (SQLIntegrityConstraintViolationException e) {
          Util.displayAlert("Error", "Error", "Customer already exists");
        } catch (SQLException e) {
          Util.displayAlert("Error", "Error", "Could not add customer");
          e.printStackTrace();
        }
      }

    } catch (NumberFormatException e) {
      Util.displayAlert("Error", "Error", "Invalid zip code");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Parent getView() {
    return view.getView();
  }
}
