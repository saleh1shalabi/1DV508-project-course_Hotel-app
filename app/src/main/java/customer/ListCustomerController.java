package customer;

import hotelproject.Controller;
import hotelproject.DataHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.control.MultipleSelectionModel;

public class ListCustomerController extends Controller {
  private ListCustomersView view;
  private SimpleListProperty<CustomerModel> customers;

  public ListCustomerController() {
    customers = new SimpleListProperty<>(FXCollections.observableArrayList());
    this.view = new ListCustomersView(this, customers);
  }

  public SimpleListProperty<CustomerModel> getCustomerList() {
    return customers;
  }

  public MultipleSelectionModel<CustomerModel> getSelectionModel() {
    return view.getSelectionModel();
  }

  @Override
  protected void beforeRender() {
    getAllCustomers();
  }

  /**
   * Return view for edit a customer.
   */
  public Parent editCustomer(CustomerModel customer) {
    EditCustomerController ctrl = new EditCustomerController(customer);
    return ctrl.getView(customer);
  }

  /**
   * Add all customers from database to list of customermodels.
   */
  public void getAllCustomers() {
    List<String> query = new ArrayList<String>();
    query.add("SELECT * FROM customer;");
    List<Map<String, Object>> result = DataHandler.getData(query);
    customers.clear();
    result.forEach(e -> customers.add(new CustomerModel(e)));
  }

  /**
   * Add customer from database to list of CustomerModels with a searched input.
   */
  public void getSearchedCustomers(String input) {
    List<String> query = new ArrayList<String>();
    query.add("SELECT * FROM customer WHERE CONCAT_WS(' ',first_name, last_name) LIKE ?"
        + "OR id LIKE ?"
        + "OR email LIKE ?"
        + "OR REPLACE(REPLACE(REPLACE(REPLACE(REPLACE("
        + "phone,'+',''),'(',''),')',''),'.',''),'-','') LIKE ?"
        + "OR street_address LIKE ?"
        + "OR city LIKE ?"
        + "OR zip_code LIKE ?;");

    StringBuilder sb = new StringBuilder();
    sb.append("%");
    sb.append(input);
    sb.append("%");
    input = sb.toString();

    for (int i = 0; i < 7; i++) {
      query.add(input);
    }
    List<Map<String, Object>> result = DataHandler.getData(query);
    customers.clear();
    result.forEach(e -> customers.add(new CustomerModel(e)));
  }

  public Parent getView() {
    return view.getView();
  }
}
