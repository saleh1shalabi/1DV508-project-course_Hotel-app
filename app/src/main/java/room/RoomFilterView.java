package room;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

public class RoomFilterView {
  private VBox rootNode       = new VBox(5);
  private Label bedsLabel     = new Label("Number of beds");
  private Slider bedsSlider   = new Slider(0, 10, 0);

  /**
   * Create a RoomFilterView.
   */
  public RoomFilterView() {
    setupSlider();
    rootNode.getChildren().addAll(bedsLabel, bedsSlider);
  }

  private void setupSlider() {
    Popup p = new Popup();
    Label pval = new Label("0");
    p.getContent().addAll(pval);
    p.setAutoHide(true);
    pval.setPadding(new Insets(5));
    pval.setPrefWidth(40);
    pval.setAlignment(Pos.CENTER);
    pval.setMaxWidth(Control.USE_PREF_SIZE);
    pval.setMinWidth(Control.USE_PREF_SIZE);
    bedsSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
      pval.setText(Math.round(newVal.doubleValue()) < 10
          ? Math.round(newVal.doubleValue()) == 0 ? "All" :
          String.valueOf(Math.round(newVal.doubleValue())) : "10+");
      if (!p.isShowing()) {
        p.show(bedsSlider.getScene().getWindow());
      }
      Bounds b = bedsSlider.localToScreen(bedsSlider.lookup(".thumb").getBoundsInParent());
      p.setX(b.getMinX() + (b.getWidth() / 2) - (p.getWidth() / 2));
      p.setY(b.getMaxY() - 10);
      if (!bedsSlider.isValueChanging() || newVal.doubleValue() == bedsSlider.getMax()
          || newVal.doubleValue() == bedsSlider.getMin()) {
        p.hide();
      }
    });
    bedsSlider.setOnDragExited(e -> p.hide());
    bedsSlider.setShowTickMarks(true);
    bedsSlider.setShowTickLabels(true);
    bedsSlider.setBlockIncrement(1);
    bedsSlider.setMajorTickUnit(5);
    bedsSlider.setMinorTickCount(4);
    bedsSlider.setSnapToTicks(true);
  }

  public Slider getSlider() {
    return bedsSlider;
  }

  public Parent getView() {
    return rootNode;
  }
}
