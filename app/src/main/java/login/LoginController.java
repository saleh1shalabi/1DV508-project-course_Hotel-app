package login;

import hotelproject.Controller;
import javafx.scene.Parent;

public class LoginController extends Controller {

  LoginModel login;

  public LoginController(LoginModel login) {
    this.login = login;
  }

  public void updateUsername(String username) {
    login.setUsername(username);
  }

  public void updatePassword(String password) {
    login.setPassword(password);
  }

  @Override
  public Parent getView() {
    return null;
  }
  
}
