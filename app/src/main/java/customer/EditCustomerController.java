package customer;

import javafx.scene.Parent;

public class EditCustomerController {
  private EditCustomerView view;
  private CustomerModel customer;

  public EditCustomerController(CustomerModel customer) {
    view = new EditCustomerView(this, customer);
    this.customer = customer;
  }

  public void editCustomer(CustomerModel customer) {
    customer.updateCustomer();
  }

  public Parent getView(CustomerModel customer) {
    return view.getView(customer);
  }

  public CustomerModel getCustomer() {
    return customer;
  }
}
