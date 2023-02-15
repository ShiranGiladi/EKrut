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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.Product;

/**
 * This class allows the area manager to send an executive order to the operations worker in case a certain product in one of the facilities in
 * the area manager's area reached below the threshold level and needs to be re-stocked.
 * The area manager have to allocate the re-stock task to a specific operations worker.
 * Also from the window this class represents, the area manager can go back to their home page, or exit the client side of the system altogether.
 */
public class SendExecutiveOrderController {
    @FXML
    private Button Backbtn;

    @FXML
    private Button sendBtn;
    
    @FXML
    private Button Exitbtn;
    
    @FXML
    private Text errorMsg;

    @FXML
    private TableView<Product> tableProdutUnderTreshold;
    
    @FXML
    private TableColumn<Product, String> amount;

    @FXML
    private TableColumn<Product, String> productID;

    @FXML
    private TableColumn<Product, String> productName;

    @FXML
    private TableColumn<Product, String> thresholdLevel;

    @FXML
    private TableColumn<Product, String> facility;

    @FXML
    private ComboBox<String> selectEmployeeComboBox;
    
	private String costumerID = ChatClient.user.getId();
    
	/**
     * This method initializes the table that shows on the screen.
     */
    public void initialize() {
    	productID.setCellValueFactory(new PropertyValueFactory<Product, String>("productID"));
    	productName.setCellValueFactory(new PropertyValueFactory<Product, String>("productName"));
    	facility.setCellValueFactory(new PropertyValueFactory<Product, String>("facility"));
    	amount.setCellValueFactory(new PropertyValueFactory<Product, String>("amount"));
    	thresholdLevel.setCellValueFactory(new PropertyValueFactory<Product, String>("thresholdLevel"));

		ObservableList<Product> returnList = getProduct();
		if (!returnList.isEmpty()) {
			tableProdutUnderTreshold.setItems(returnList);
			tableProdutUnderTreshold.refresh();
		}
    }
    /**
     * This method fills the table with the information about the products that the area manager can send an executive order for to the
     * operations worker, with data from the data base.
     * @return
     */
    public ObservableList<Product> getProduct(){
    	ObservableList<Product> products = FXCollections.observableArrayList();
		ClientUI.chat.accept("getProdutUnderTreshold " + ChatClient.user.getNote());
		if (!ChatClient.answerMsg.equals("Empty")) {
			String[] result = ChatClient.answerMsg.split("\\,");
			for (int i = 0; i < result.length; i += 5) {
				products.add(new Product(result[i], result[i + 1], result[i + 2], result[i + 3], result[i + 4]));
			}		
		}
		ClientUI.chat.accept("getOprationWorkerNames");
		if(!ChatClient.answerMsg.equals("Empty")) {
			ObservableList<String> employeeList = FXCollections.observableArrayList();
			String[] owe = ChatClient.answerMsg.split("\\,");
			for(int i=0; i<owe.length; i+=2)
				employeeList.add(owe[i] + "_" + owe[i+1]);
			selectEmployeeComboBox.setItems(employeeList);
		}
		return products;
    }
    /**
     * This method allows the area manager to send an executive order to the operations worker.
     * In any case of an error, an appropriate error message will appear on the screen.
     * In case of success a pop up window will appear on the screen (a simulation) //////////////////////////////////////
     * @param event
     */
    @FXML
    void getSendBtn(ActionEvent event) {
    	Product p = tableProdutUnderTreshold.getSelectionModel().getSelectedItem();
		if (p == null) {
			errorMsg.setText("Please choose a product.");
			return;
		}		
		if(selectEmployeeComboBox.getValue() == null) { 
			errorMsg.setText("Please choose an employee.");
			return;
		}
		String e = selectEmployeeComboBox.getSelectionModel().getSelectedItem().toString();
		ClientUI.chat.accept("sendToOprationWorker " + p.getProductID() + " " + p.getFacility() + " " + e);
		switch (ChatClient.answerMsg) {
		case "Error":
			errorMsg.setText("Failed to send executive order.");
			break;
		case "Send":
			ObservableList<Product> returnList = getProduct();
			if (returnList.isEmpty()) {
				tableProdutUnderTreshold.getItems().clear();
			} else {
				tableProdutUnderTreshold.setItems(returnList);
			}
			tableProdutUnderTreshold.refresh();
			popUpWindow();
			break;
		}
    }
    /**
	* This method opens a pop up window, that lets the area manager know that an email and a SMS has been sent to the operations worker (simulation).
	*/
	private void popUpWindow() {
		Scene popUp = new Scene(initPane(), 390, 155);
		Stage popUpStage = new Stage();
		popUpStage.setTitle("A message has been sent");
		popUpStage.initStyle(StageStyle.UNDECORATED);
		popUpStage.setScene(popUp);
		popUpStage.show();
	}
	/**
	* This method sets the pop up window that lets the area manager know that an email and a SMS has been sent to the operations worker (simulation).
	* @return the pop up window
	*/	
    private Pane initPane() {
		Pane pane = new Pane();
		pane.setStyle("-fx-background-color:  #ffffff; -fx-border-color:  #000000;");
		
		Label label = new Label("S I M U L A T I O N");
		label.setStyle("-fx-font-family: Rockwell; -fx-font-size: 14; -fx-font-weight: bold;");
		label.setLayoutX(123);
		label.setLayoutY(39);
		
		Label label2 = new Label("An email and a SMS sent to the operation worker.");
		label2.setStyle("-fx-font-family: Rockwell; -fx-font-size: 15;");
		label2.setLayoutX(28);
		label2.setLayoutY(74);
		
		Button ok = new Button("OK");
		ok.setStyle("-fx-background-color: #94b5fe; -fx-font-family: Rockwell; -fx-font-size: 14;");
		ok.setLayoutX(167);
		ok.setLayoutY(110);
		ok.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				((Node)event.getSource()).getScene().getWindow().hide();
			}
		});
		pane.getChildren().addAll(label, label2, ok);
		return pane;
	}
    /**
     * This method gets the name of the operations worker that the area manager chose to allocate the task of refilling the inventory to.
     * @param event
     */
    @FXML
    void getComboBox(ActionEvent event) {
    	String s = selectEmployeeComboBox.getSelectionModel().getSelectedItem();
    	selectEmployeeComboBox.setStyle("-fx-font-family: Rockwell; -fx-font-size: 14;");
    	selectEmployeeComboBox.setPromptText(s);
    }
    /**
	* This method closes the current window ("Send Executive Order") and opens the window "Area Manager".
	* @param event
	* @throws IOException
	*/ 
    @FXML
    void getBackbtn(ActionEvent event) throws IOException {
    	((Node)event.getSource()).getScene().getWindow().hide();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/AreaManager.fxml"));
    	Parent root = (Parent) loader.load();
    	AreaManagerController control = loader.getController();
    	control.loadDetails(ChatClient.user);
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Area Manager");
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
