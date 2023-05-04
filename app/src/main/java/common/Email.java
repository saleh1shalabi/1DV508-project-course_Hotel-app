package common;

import booking.Booking;
import customer.CustomerModel;
import room.RoomModel;


public class Email {

  /**
   * Sends welcome email to customer.
   */
  public void sendWelcome(CustomerModel customer) {
    String subject = "Welcome to Silicone Hotel";
    String body = "Welcome to Silicone Hotel!" + System.lineSeparator() + System.lineSeparator()
        + "You have been registered as a customer at Silicone Hotel." + System.lineSeparator()
        + System.lineSeparator() + "Your personal information: " + System.lineSeparator()
        + "Name: " + customer.getFirstName() + " " + customer.getLastName() + System.lineSeparator()
        + "E-mail: " + customer.getEmail() + System.lineSeparator() + "Phone nr: " 
        + customer.getPhone() + System.lineSeparator() + "Address: " + customer.getStreetAddress() 
        + ", " + customer.getZipCode() + " " + customer.getCity() + System.lineSeparator() 
        + System.lineSeparator() + "Best Regards" + System.lineSeparator() + "Silicone Hotel";
    String[] receivers = {customer.getEmail()};
    SendEmail email = new SendEmail(receivers, subject, body);
    new Thread(email).start();
  }

  /**
   * Send email with info update to customer.
   */
  public void sendUpdate(CustomerModel customer) {
    String subject = "Updated information";
    String body = "Your peronal information has been updated." + System.lineSeparator()
        + "Your new information is:" + System.lineSeparator()
        + "Name: " + customer.getFirstName() + " " + customer.getLastName() + System.lineSeparator()
        + "E-mail: " + customer.getEmail() + System.lineSeparator() + "Phone nr: " 
        + customer.getPhone() + System.lineSeparator() + "Address: " + customer.getStreetAddress() 
        + ", " + customer.getZipCode() + " " + customer.getCity() + System.lineSeparator() 
        + System.lineSeparator() + "Best Regards" + System.lineSeparator() + "Silicone Hotel";
    String[] receivers = {customer.getEmail()};
    SendEmail email = new SendEmail(receivers, subject, body);
    new Thread(email).start();
  }

  /**
   * Send email with booking info to customer.
   */
  public void sendBooking(Booking booking) {
    String subject = "Booking at Silicone Hotel";
    String body = "This is a confirmation of your booking at Silicone Hotel." 
        + System.lineSeparator() + System.lineSeparator() + "Booking ID: " + booking.getId() 
        + System.lineSeparator() + "Dates: " + booking.getStartDate() + " to " 
        + booking.getEndDate() + System.lineSeparator() + "Paid for: " 
        + (booking.isPaid() ? "Yes" : "No") + System.lineSeparator() 
        + System.lineSeparator() + "Best Regards" + System.lineSeparator() + "Silicone Hotel";

    String[] receiver = {booking.getCustomer().getEmail()};
    SendEmail email = new SendEmail(receiver, subject, body);
    new Thread(email).start();
  }

  /**
   * Sends invoice to customer.
   */
  public void sendInvoice(Booking booking) {
    CustomerModel customer = new CustomerModel(booking.getCustomerId());
    RoomModel room = new RoomModel(booking.getRoomId());
    String subject = "Invoice for Silicone Hotel";
    String body = room.getInformation();

    String[] receiver = {customer.getEmail()};
    SendEmail email = new SendEmail(receiver, subject, body);
    new Thread(email).start();
  }

  /**
   * Sends pin to user to get new password.
   */
  public void sendPinToUser(String pin, String email) {
    String subject = "Reset password";
    String body = "Enter the PIN below to reset your password:" + System.lineSeparator()
        + System.lineSeparator() + pin + System.lineSeparator() + System.lineSeparator() 
        + "Best Regards" + System.lineSeparator() + "Silicone Hotel";
    String[] receiver = {email};
    SendEmail e = new SendEmail(receiver, subject, body);
    new Thread(e).start();;
  }
}
