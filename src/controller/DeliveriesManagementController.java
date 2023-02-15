package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
 * This class allows the deliveries operator to approve a delivery and send to the client an email and a SMS with the estimated 
 * 																										delivery time (simulation).
 * Also from the window this class represents, the deliveries operator can go back to their home page, or exit the client side of the 
 * 																													system altogether.
 */
public class DeliveriesManagementController {
	
    @FXML
    private Button Exitbtn;

	@FXML
    private Button Backbtn;
	
    @FXML
    private Button approveBtn;
    
    @FXML
    private Text errorMsg;
	
	@FXML
    private TableView<Deliveries> DeliveriesTable;
	
    @FXML
    private TableColumn<Deliveries, String> address;

    @FXML
    private TableColumn<Deliveries, String> orderNumber;

    @FXML
    private TableColumn<Deliveries, String> costumerIDCol;

    @FXML
    private TableColumn<Deliveries, String> receiverPhoneNumber;

    @FXML
    private TableColumn<Deliveries, String> facility;
    
    private String costumerID = ChatClient.user.getId();
    String estimateDeliveryDate = null; //estimateDeliveryDate = 3 days after delivery operator approve the order
    
    /**
     * This method initializes the table that shows on the screen.
     */
    public void initialize() {
    	facility.setCellValueFactory(new PropertyValueFactory<Deliveries,String>("facility"));
    	orderNumber.setCellValueFactory(new PropertyValueFactory<Deliveries,String>("orderNumber"));
    	costumerIDCol.setCellValueFactory(new PropertyValueFactory<Deliveries,String>("costumerID"));
    	address.setCellValueFactory(new PropertyValueFactory<Deliveries,String>("address"));
    	receiverPhoneNumber.setCellValueFactory(new PropertyValueFactory<Deliveries,String>("receiverPhoneNumber"));
    	ObservableList<Deliveries> returnList = getDeliveries();
		if (!returnList.isEmpty()) {
			DeliveriesTable.setItems(returnList);
			DeliveriesTable.refresh();
		}
    }
    /**
     * This method fills the table with the deliveries data from the data base.
     * @return
     */
    public ObservableList<Deliveries> getDeliveries(){
    	ObservableList<Deliveries> deliveries = FXCollections.observableArrayList();
		ClientUI.chat.accept("getDeliverirsToApprove " + ChatClient.user.getNote());
		if (!ChatClient.answerMsg.equals("Empty")) {
			String[] result = ChatClient.answerMsg.split("\\,");
			for (int i = 0; i < result.length; i += 5) {
				deliveries.add(new Deliveries(result[i], result[i + 1], result[i + 2], result[i + 3].replace("_", " "), result[i + 4]));
			}
		}
		return deliveries;
    } 
    /**
     * This method allows the deliverers operator to approve a delivery and send to client an email and a SMS with the estimated 
     * delivery time (simulation). In any case of an error, an appropriate error message will appear on the screen.
     * In case of success, a pop up window will appear on the screen to lets the deliveries operator know that the delivery was 
     * successfully approved and an email and a SMS has been sent to the costumer with the estimate delivery time (simulation).
     * @param event
     */
    @FXML
    void getApproveBtn(ActionEvent event) {
    	Deliveries d = DeliveriesTable.getSelectionModel().getSelectedItem();
		if (d == null) {
			errorMsg.setText("Please choose a delivery.");
			return;
		}
		LocalDate myDateObj = LocalDate.now().plusDays(3); 
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		estimateDeliveryDate = myDateObj.format(myFormatObj);
		ClientUI.chat.accept("approveDelivery " + DeliveriesTable.getSelectionModel().getSelectedItem().getOrderNumber() + " " + estimateDeliveryDate);
		switch (ChatClient.answerMsg) {
		case "Error":
			errorMsg.setText("Failed to approve delivery.");
			break;
		case "Approve":
			ObservableList<Deliveries> returnList = getDeliveries();
			if (returnList.isEmpty()) {
				DeliveriesTable.getItems().clear();
			} else {
				DeliveriesTable.setItems(returnList);
			}
			DeliveriesTable.refresh();
			popUpWindow();
			break;
		}
    }
    /**
	* This method opens a pop up window, that lets the deliveries operator know that the delivery was successfully approved and 
	* an email and a SMS has been sent to the costumer with the estimate delivery time (simulation).
	*/
	private void popUpWindow() {
		Scene popUp = new Scene(initPane(), 390, 175);
		Stage popUpStage = new Stage();
		popUpStage.setTitle("A message has been sent");
		popUpStage.initStyle(StageStyle.UNDECORATED);
		popUpStage.setScene(popUp);
		popUpStage.show();
	}
	/**
	* This method sets the pop up window that lets the deliveries operator know that the delivery was successfully approved and 
	* an email and a SMS has been sent to the costumer with the estimate delivery time (simulation).
	* @return the pop up window
	*/
    private Pane initPane() {
		Pane pane = new Pane();
		pane.setStyle("-fx-background-color:  #ffffff; -fx-border-color:  #000000;");
		
		Label label = new Label("S I M U L A T I O N");
		label.setStyle("-fx-font-family: Rockwell; -fx-font-size: 14; -fx-font-weight: bold;");
		label.setLayoutX(123);
		label.setLayoutY(39);
		
		Label label2 = new Label("Delivery approved successfully.");
		label2.setStyle("-fx-font-family: Rockwell; -fx-font-size: 15;");
		label2.setLayoutX(83);
		label2.setLayoutY(72);
		
		Label label3 = new Label("An email and a SMS sent to the costumer.");
		label3.setStyle("-fx-font-family: Rockwell; -fx-font-size: 15;");
		label3.setLayoutX(53);
		label3.setLayoutY(92);
		
		Label label4 = new Label("The estimate delivery time is: " + estimateDeliveryDate);
		label4.setStyle("-fx-font-family: Rockwell; -fx-font-size: 15;");
		label4.setLayoutX(51);
		label4.setLayoutY(112);
		
		Button ok = new Button("OK");
		ok.setStyle("-fx-background-color:  #94b5fe; -fx-font-family: Rockwell; -fx-font-size: 14;");
		ok.setLayoutX(166);
		ok.setLayoutY(132);
		ok.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				((Node)event.getSource()).getScene().getWindow().hide();
			}
		});
		pane.getChildren().addAll(label, label2, label3, label4, ok);;
		return pane;
	}
    /**
	* This method closes the current window ("Deliveries Management") and opens the window "Deliveries Operator".
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

