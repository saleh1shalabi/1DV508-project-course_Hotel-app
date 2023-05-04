package common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

public class DateRange {
  private HBox rootNode             = new HBox();
  private DatePicker startDateField = new DatePicker(LocalDate.now());
  private DatePicker endDateField   = new DatePicker(LocalDate.now().plusDays(1));
  private Label endLabel            = new Label(" To ");

  /**
   * Create a DateRange picker.
   */
  public DateRange() {
    setLabelOptions();
    setFieldOptions();
    setActions();
    setRootOptions();
  }

  private void setLabelOptions() {
    endLabel.getStyleClass().addAll("filter-text");
  }

  private void setFieldOptions() {
    startDateField.setPrefWidth(130);
    endDateField.setPrefWidth(130);
    startDateField.setEditable(false);
    endDateField.setEditable(false);
    startDateField.setConverter(getDateConverter());
    endDateField.setConverter(getDateConverter());
    startDateField.setDayCellFactory(e -> getDayCellFactory(null, 0));
    endDateField.setDayCellFactory(e -> getDayCellFactory(startDateField, 1));
  }

  private void setActions() {
    startDateField.setOnAction(e -> {
      if (startDateField.getValue().compareTo(endDateField.getValue()) >= 0) {
        endDateField.setValue(startDateField.getValue().plusDays(1));
      }
    });
  }

  private void setRootOptions() {
    rootNode.setAlignment(Pos.CENTER);
    rootNode.getChildren().addAll(startDateField, endLabel, endDateField);
  }

  /**
   * Set disabled daycells.
   * @param com The DatePicker field to compare to.
   * @param res The value for the compareTo() method.
   */
  private DateCell getDayCellFactory(DatePicker com, int res) {
    return new DateCell() {
      public void updateItem(LocalDate date, boolean empty) {
          super.updateItem(date, empty);
          setDisable(empty || date.compareTo(com == null ? LocalDate.now() : com.getValue()) < res);
      }
    };
  }

  /**
   * Return a StringConverter to get a better date format.
   */
  private StringConverter<LocalDate> getDateConverter() {
    return new StringConverter<LocalDate>() {
      String pattern = "yyyy-MM-dd";
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
      @Override public String toString(LocalDate date) {
        return (date != null) ? dateFormatter.format(date) : "";
      }

      @Override public LocalDate fromString(String string) {
        return (string != null && !string.isEmpty())
          ? LocalDate.parse(string, dateFormatter) : null;
      }
    };
  }

  public ObjectProperty<LocalDate> startDateProperty() {
    return startDateField.valueProperty();
  }

  public LocalDate getStartDate() {
    return startDateField.getValue();
  }

  public ObjectProperty<LocalDate> endDateProperty() {
    return endDateField.valueProperty();
  }

  public LocalDate getEndDate() {
    return endDateField.getValue();
  }

  public Parent getNode() {
    return rootNode;
  }
}
