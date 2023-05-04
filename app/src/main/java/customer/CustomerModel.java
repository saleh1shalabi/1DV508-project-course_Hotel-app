package customer;

import common.Email;
import common.Util;
import hotelproject.DataHandler;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;



public class CustomerModel {
  private IntegerProperty id = new SimpleIntegerProperty();
  private StringProperty firstName = new SimpleStringProperty();
  private StringProperty lastName = new SimpleStringProperty();
  private StringProperty email = new SimpleStringProperty();
  private StringProperty phone = new SimpleStringProperty();
  private StringProperty streetAddress = new SimpleStringProperty();
  private StringProperty city = new SimpleStringProperty();
  private IntegerProperty zipCode = new SimpleIntegerProperty();

  public CustomerModel() {

  }

  public CustomerModel(Map<String, Object> values) {
    setAllValues(values);
  }

  /**
   * Sets model to a customer from the database.
   */
  public CustomerModel(Integer id) {
    List<String> query = new ArrayList<String>();
    query.add("SELECT * FROM customer WHERE id = ?;");
    query.add(id.toString());

    List<Map<String, Object>> customer = DataHandler.getData(query);
    this.id.set(id);
    firstName.set(customer.get(0).get("first_name").toString());
    lastName.set(customer.get(0).get("last_name").toString());
    email.set(customer.get(0).get("email").toString());
    phone.set(customer.get(0).get("phone").toString());
    streetAddress.set(customer.get(0).get("street_address").toString());
    city.set(customer.get(0).get("city").toString());
    zipCode.set(Integer.parseInt(customer.get(0).get("zip_code").toString()));
  }

  /**
   * Sets model to new customer.
   */
  public CustomerModel(String firstName, String lastName, String email, String phone, 
      String streetAddress, String city, Integer zipCode) {
    this.firstName.set(firstName);
    this.lastName.set(lastName);
    this.email.set(email);
    this.phone.set(phone);
    this.streetAddress.set(streetAddress);
    this.city.set(city);
    this.zipCode.set(zipCode);
  }

  public Integer getId() {
    return id.get();
  }

  public String getFirstName() {
    return firstName.get();
  }

  public String getLastName() {
    return lastName.get();
  }

  public String getEmail() {
    return email.get();
  }

  public String getPhone() {
    return phone.get();
  }

  public String getStreetAddress() {
    return streetAddress.get();
  }

  public String getCity() {
    return city.get();
  }

  public Integer getZipCode() {
    return zipCode.get();
  }

  /**
   * Sets all values to the model with a map.
   */
  public void setAllValues(Map<String, Object> values) {
    id.set((int) values.get("id"));
    firstName.set(values.get("first_name").toString());
    lastName.set(values.get("last_name").toString());
    email.set(values.get("email").toString());
    phone.set(values.get("phone").toString());
    streetAddress.set(values.get("street_address").toString());
    city.set(values.get("city").toString());
    zipCode.set((int) values.get("zip_code"));
  }

  /**
   * Sets all values to the model with variables.
   */
  public void setAllValues(String firstName, String lastName, String email, 
      String phone, String streetAddress, String city, Integer zipCode) {
    this.firstName.set(firstName);
    this.lastName.set(lastName);
    this.email.set(email);
    this.phone.set(phone);
    this.streetAddress.set(streetAddress);
    this.city.set(city);
    this.zipCode.set(zipCode);
  }

  /**
   * Update existing customer.
   */
  public void updateCustomer() {
    if (isValid()) {
      List<String> query = new ArrayList<String>();
      query.add("UPDATE customer SET first_name = ?, last_name = ?, email = ?, "
          + "phone = ?, street_address = ?, city = ?, zip_code = ? " 
          + "WHERE id = ?");
      query.add(firstName.get());
      query.add(lastName.get());
      query.add(email.get());
      query.add(phone.get());
      query.add(streetAddress.get());
      query.add(city.get());
      query.add(String.valueOf(zipCode.get()));
      query.add(String.valueOf(id.get()));
      try {
        DataHandler.setData(query);   
        Util.displayAlert("Information", "Succes", "Customer updated!");
        Email email = new Email();
        email.sendUpdate(this);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Checks if the customer is valid.
   */
  public boolean isValid() {
    if (!(validFirstName() && validLastName() && validEmail() && validPhone() 
        && validStreetAddress() && validCity() && validZipCode())) {
      return false;
    }
    return true;
  }

  private boolean validFirstName() {
    if (firstName.get().length() < 2) {
      Util.displayAlert("Error", "Error", "Invalid first name");
      return false;
    }
    return true;
  }

  private boolean validLastName() {
    if (lastName.get().length() < 2) {
      Util.displayAlert("Error", "Error", "Invalid last name");
      return false;
    }
    return true;
  }

  private boolean validEmail() {
    if (!email.get().contains("@") || email.get().length() < 5) {
      Util.displayAlert("Error", "Error", "Invalid E-mail");
      return false;
    }
    return true;
  }

  private boolean validPhone() {
    if (phone.get().length() < 4 || phone.get().length() > 18) {
      Util.displayAlert("Error", "Error", "Invalid phone number");
      return false;
    }
    return true;
  }

  private boolean validStreetAddress() {
    if (streetAddress.get().length() < 5) {
      Util.displayAlert("Error", "Error", "Invalid Street address");
      return false;
    }
    return true;
  }

  private boolean validCity() {
    if (city.get().length() < 2) {
      Util.displayAlert("Error", "Error", "Invalid City");
      return false;
    }
    return true;
  }

  private boolean validZipCode() {
    if (zipCode.get() < 99 || zipCode.get() > 9999999) {
      Util.displayAlert("Error", "Error", "Invalid zip code");
      return false;
    }
    return true;
  }
}
