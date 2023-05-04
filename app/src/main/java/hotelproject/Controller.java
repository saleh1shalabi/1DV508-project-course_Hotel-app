package hotelproject;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public abstract class Controller {
  protected BorderPane root;

  /**
   * Render the given view.
   * @param content The content to render in the main node. Can be any type of javafx layout class.
   * @param root The root BorderPane where we switch the center content for new views.
   */
  public void renderView(Parent content, BorderPane root) {
    this.root = root;
    beforeRender();
    root.setCenter(content);
  }

  /**
   * Optional init method to ovveride.
   */
  protected void beforeRender() {}

  public abstract Parent getView();
}
