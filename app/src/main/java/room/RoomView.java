package room;

import common.Util;
import hotelproject.App;
import java.util.function.UnaryOperator;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

public class RoomView {
  private GridPane rootNode          = new GridPane();
  private Label errorLabel           = new Label();
  private Label idLabel              = new Label("Room number");
  private Label bedsLabel            = new Label("Number of beds");
  private Label sizeLabel            = new Label("Room Size");
  private Label locationLabel        = new Label("Room location");
  private Label informationLabel     = new Label("Other information");
  private TextField idField          = new TextField();
  private TextField bedsField        = new TextField();
  private TextField sizeField        = new TextField();
  private TextField locationField    = new TextField();
  private TextArea informationField  = new TextArea();
  private Button saveButton          = new Button("Save");


  /**
   * Create a room view.
   */
  public RoomView(RoomController controller, RoomModel room) {
    filterInteger(idField, 4);
    filterInteger(bedsField, 2);
    filterInteger(sizeField, 3);
    bindToModel(room);
    App.adminFeature(saveButton);

    rootNode.addEventHandler(ActionEvent.ACTION, e -> controller.saveRoom());
    saveButton.getStyleClass().add("big-button");
    errorLabel.setId("errorLabel");
    informationField.setWrapText(true);
    informationField.setPrefRowCount(5);

    idField.setPromptText("Enter room number...");
    bedsField.setPromptText("Enter number of beds...");
    sizeField.setPromptText("Enter room size...");
    locationField.setPromptText("Enter room location...");
    informationField.setPromptText("Enter extra information...");

    idField.getStyleClass().add("short-field");
    bedsField.getStyleClass().add("short-field");
    sizeField.getStyleClass().add("short-field");
    locationField.getStyleClass().add("short-field");
    informationField.getStyleClass().add("short-field");

    ColumnConstraints column1 = new ColumnConstraints();
    ColumnConstraints column2 = new ColumnConstraints();
    ColumnConstraints column3 = new ColumnConstraints();
    column1.setPercentWidth(50);
    column2.setPercentWidth(50);
    column3.setPercentWidth(50);
    rootNode.getColumnConstraints().addAll(column1, column2, column3);
    rootNode.setVgap(10);
    rootNode.setHgap(10);
    VBox idBox = new VBox(10);
    idBox.getStyleClass().add("card");
    idBox.getChildren().addAll(idLabel, idField);
    idBox.setAlignment(Pos.CENTER);
    VBox bedsBox = new VBox(10);
    bedsBox.getStyleClass().add("card");
    bedsBox.getChildren().addAll(bedsLabel, bedsField);
    bedsBox.setAlignment(Pos.CENTER);
    VBox sizeBox = new VBox(10);
    sizeBox.getStyleClass().add("card");
    sizeBox.getChildren().addAll(sizeLabel, sizeField);
    sizeBox.setAlignment(Pos.CENTER);
    VBox locationBox = new VBox(10);
    locationBox.getStyleClass().add("card");
    locationBox.getChildren().addAll(locationLabel, locationField);
    locationBox.setAlignment(Pos.CENTER);
    VBox informationBox = new VBox(10);
    informationBox.getStyleClass().add("card");
    informationBox.getChildren().addAll(informationLabel, informationField);
    informationBox.setAlignment(Pos.CENTER);
    rootNode.addRow(0, idBox, bedsBox, sizeBox);
    rootNode.add(locationBox, 0, 1, 3, 1);
    rootNode.add(informationBox, 0, 2, 3, 1);
    rootNode.add(saveButton, 0, 3, 3, 1);
    GridPane.setHalignment(saveButton, HPos.CENTER);
  }

  /**
   * Toggle the disabled value of the fields, except idField as it should not be editable.
   */
  public void toggleDisabled() {
    idField.setDisable(true);
    bedsField.setDisable(!bedsField.disableProperty().get());
    sizeField.setDisable(!sizeField.disableProperty().get());
    locationField.setDisable(!locationField.disableProperty().get());
    informationField.setDisable(!informationField.disableProperty().get());
    saveButton.setDisable(!saveButton.disableProperty().get());
  }

  /**
   * Display a success message when added a room.
   */
  public void success(String task) {
    Util.displayAlert(
        "information",
        "Room " + task,
        "Room " + idField.getText() + " was " + task + "."
    );
  }

  public void error() {
    Util.displayAlert("error", "Incorrect values", errorLabel);
  }

  public Parent asParent() {
    return rootNode;
  }

  /**
   * Bind the fields to the model values.
   */
  private void bindToModel(RoomModel room) {
    idField.textProperty().bindBidirectional(room.idProperty(), new NumberStringConverter("#"));
    bedsField.textProperty().bindBidirectional(room.bedsProperty(), new NumberStringConverter("#"));
    sizeField.textProperty().bindBidirectional(room.sizeProperty(), new NumberStringConverter("#"));
    locationField.textProperty().bindBidirectional(room.locationProperty());
    informationField.textProperty().bindBidirectional(room.informationProperty());
    room.errorsProperty().addListener((obs, oldErrors, newErrors) ->
        errorLabel.setText(String.join(System.lineSeparator(), newErrors)));
  }

  /**
   * Clear all text fields.
   */
  public void clearFields() {
    idField.clear();
    bedsField.clear(); 
    sizeField.clear();
    locationField.clear();
    informationField.clear();
    idField.requestFocus();
  }

  /**
   * Make the field only accept integers using TextFormatter.
   */
  private void filterInteger(TextField field, Integer max) {
    UnaryOperator<TextFormatter.Change> integerFilter = change -> {
      String newText = change.getControlNewText();
      return (newText.matches("([1-9][0-9]*)?") && newText.length() <= max) ? change : null;
    };
    field.setTextFormatter(
        new TextFormatter<Integer>(new IntegerStringConverter(), null, integerFilter));
  }
}
