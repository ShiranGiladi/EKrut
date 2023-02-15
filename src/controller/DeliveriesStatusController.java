package controller;

import java.io.IOException;
import client.ChatClient;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.Deliveries;

/**
 * This class allows the deliveries operator to change the status of a certain delivery to "Done".
 * Also from the window this class represents, the deliveries operator can go back to their home page, or exit the client side of the
 * 																													system altogether.
 */
public class DeliveriesStatusController {

    @FXML
    private Button Exitbtn;

    @FXML
    private Button Backbtn;
    
    @FXML
    private Button doneBtn;
    
    @FXML
    private Text errorMsg;
    
    @FXML
    private TableView<Deliveries> DeliveriesStatusTable;

    @FXML
    private TableColumn<Deliveries, String> costumerIDCol;
    
    @FXML
    private TableColumn<Deliveries, String> orderNumber;

    @FXML
    private TableColumn<Deliveries, String> estimateDeliveryDate;
    
    @FXML
    private TableColumn<Deliveries, String> facility;
    
    private String costumerID = ChatClient.user.getId();

    /**
     * This method initializes the table that shows on the screen.
     */
    public void initialize() {
    	facility.setCellValueFactory(new PropertyValueFactory<Deliveries,String>("facility"));
    	orderNumber.setCellValueFactory(new PropertyValueFactory<Deliveries,String>("orderNumber"));
    	costumerIDCol.setCellValueFactory(new PropertyValueFactory<Deliveries,String>("costumerID"));
    	estimateDeliveryDate.setCellValueFactory(new PropertyValueFactory<Deliveries,String>("estimateDeliveryDate"));
    	ObservableList<Deliveries> returnList = getDeliveries();
		if (!returnList.isEmpty()) {
			DeliveriesStatusTable.setItems(returnList);
			DeliveriesStatusTable.refresh();
		}    	
    }
    /**
     * This method fills the table with the deliveries data from the data base.
     * @return
     */
    public ObservableList<Deliveries> getDeliveries(){
    	ObservableList<Deliveries> deliveries = FXCollections.observableArrayList();
		ClientUI.chat.accept("getDeliverirsToClose " + ChatClient.user.getNote());
		if (!ChatClient.answerMsg.equals("Empty")) {
			String[] result = ChatClient.answerMsg.split("\\,");
			for (int i = 0; i < result.length; i += 4) {
				deliveries.add(new Deliveries(result[i], result[i + 1], result[i + 2], result[i + 3]));
			}	
		}
		return deliveries;
    } 
    /**
     * This method changes the status of a certain delivery to "Done".
     * In case of any error, an appropriate error message will appear on the screen.
     * In case of success, a pop up window will appear on the screen to lets the deliveries operator know that the delivery status 
     * successfully changed to 'Done'.
     * @param event
     */
    @FXML
    void getDoneBtn(ActionEvent event) {
    	Deliveries d = DeliveriesStatusTable.getSelectionModel().getSelectedItem();
		if (d == null) {
			errorMsg.setText("Please choose a delivery.");
			return;
		}
		ClientUI.chat.accept("changeStatusToDoneForDelivery " + DeliveriesStatusTable.getSelectionModel().getSelectedItem().getOrderNumber());
		switch (ChatClient.answerMsg) {
		case "Error":
			errorMsg.setText("Failed to change status to 'Done'.");
			break;
		case "Change":
			ObservableList<Deliveries> returnList = getDeliveries();
			if (returnList.isEmpty()) {
				DeliveriesStatusTable.getItems().clear();
			} else {
				DeliveriesStatusTable.setItems(returnList);
			}
			DeliveriesStatusTable.refresh();
			popUpWindow();
			break;
		}
    }
    /**
	* This method opens a pop up window, that lets the deliveries operator know that the delivery status successfully changed to 'Done'.
	*/
	private void popUpWindow() {
		Scene popUp = new Scene(initPane(), 370, 155);
		Stage popUpStage = new Stage();
		popUpStage.setTitle("Successful status change");
		popUpStage.initStyle(StageStyle.UNDECORATED);
		popUpStage.setScene(popUp);
		popUpStage.show();
	}
	/**
	* This method sets the pop up window that lets the deliveries operator know that the delivery status successfully changed to 'Done'.
	* @return the pop up window
	*/	
    private Pane initPane() {
		Pane pane = new Pane();
		pane.setStyle("-fx-background-color:  #94b5fe; -fx-border-color:  #000000;");
		
		Label label = new Label("Delivery status is 'Done'.");
		label.setStyle("-fx-font-family: Rockwell; -fx-font-size: 18;");
		label.setLayoutX(40);
		label.setLayoutY(48);
		
		Button ok = new Button("OK");
		ok.setStyle("-fx-background-color: #122753; -fx-text-fill: #ffffff; -fx-font-family: Rockwell; -fx-font-size: 14;");
		ok.setLayoutX(165);
		ok.setLayoutY(102);
		ok.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				((Node)event.getSource()).getScene().getWindow().hide();
			}
		});
		pane.getChildren().addAll(label, ok);;
		return pane;
	}
    /**
   	* This method closes the current window ("Deliveries Status") and opens the window "Deliveries Operator".
   	* @param event
   	* @throws IOException
   	*/
    @FXML
    void getBackbtn(ActionEvent event) throws IOException {
    	((Node)event.getSource()).getScene().getWindow().hide();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/DeliveriesOperator.fxml"));
    	Parent root = (Parent) loader.load();
    	DeliveriesOperatorController control = loader.getController();
    	control.loadDetails(ChatClient.user);
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Deliveries Operator");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);		
		primaryStage.show();
    }
    /**
     * This method closes entirely the client side of the system and logs out the user that was logged in.
     * @param event
     */
    @FXML
    void getExitbtn(ActionEvent event) {
    	ClientUI.chat.accept("logout " + costumerID);
		((Node) event.getSource()).getScene().getWindow().hide();
    }
}
