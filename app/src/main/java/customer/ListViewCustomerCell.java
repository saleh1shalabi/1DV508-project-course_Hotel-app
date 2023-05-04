package customer;

import common.Util;
import hotelproject.DataHandler;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ListViewCustomerCell extends ListCell<CustomerModel> {
  HBox hbox           = new HBox(5);
  Label label         = new Label("");
  Pane pane           = new Pane();
  Button btnEdit = new Button("Edit");
  Button deletButton = new Button("Delete");
  


  /**
   * Custom ListCell for listing rooms.
   */
  public ListViewCustomerCell() {
    super();
    
    btnEdit.getStyleClass().add("edit-button");
    deletButton.getStyleClass().add("delete-button");

    hbox.getChildren().addAll(label, pane, btnEdit, deletButton);
    HBox.setHgrow(pane, Priority.ALWAYS);
    btnEdit.setOnAction(event -> getListView().getSelectionModel().select(getItem()));

    deletButton.setOnAction(event -> {
      getListView().getSelectionModel().select(getItem());
      int index = getListView().getSelectionModel().getSelectedItem().getId();
      deleteCustomer(index);   
    });
  }


  @Override
  protected void updateItem(CustomerModel item, boolean empty) {
    super.updateItem(item, empty);
    setText(null);
    setGraphic(null);
    if (item != null && !empty) {
      label.setText(String.valueOf(item.getId() + " " + item.getFirstName() 
          + " " + item.getLastName()));
      setGraphic(hbox);
    }
  }

  private void deleteCustomer(int index) {
    
    Stage confirmDelete = new Stage();
    confirmDelete.setTitle("Delete this User!");

    Button yes = new Button("Yes");
    Button no = new Button("No");

    yes.setOnAction(e -> {
      List<String> query = new ArrayList<String>();

      query.add("Delete from customer "
          + "Where id = '" + index + "';");
      
      try {
        
        DataHandler.setData(query);
        getListView().getItems().remove(getItem());
        Util.displayAlert("INFORMATION", "Done", "The customer has been deleted!");
              
        confirmDelete.close();
       


      } catch (SQLException e1) {
        e1.printStackTrace();
      } catch (Exception sq) {
        System.out.println(sq);
      }

    });

    no.setOnAction(e -> confirmDelete.close());
        
    HBox yesOrNo = new HBox(100);
    yesOrNo.setAlignment(Pos.CENTER);
    yesOrNo.getChildren().addAll(yes, no);

    Label qus = new Label("Do you really want to delete this customer?");

    VBox popup = new VBox(10);
    popup.getChildren().addAll(qus, yesOrNo);
    popup.setAlignment(Pos.CENTER);
          
    Scene popupScene = new Scene(popup, 300, 100);
          
    confirmDelete.setScene(popupScene);   
    confirmDelete.showAndWait();

  }

}
