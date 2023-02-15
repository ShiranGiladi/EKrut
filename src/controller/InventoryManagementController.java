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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.Product;

/**
 * This class allows the operations worker to update the quantity of a certain product in its facility's inventory.
 * The operations worker will only see the products that the area manager sent an executive order about.
 * Also from the window this class represents, the operations worker can go back to their home page, or exit the system altogether.
 */
public class InventoryManagementController {
    @FXML
    private Button Exitbtn;

    @FXML
    private Button Backbtn;

    @FXML
    private Button UpdateValuebtn;
    
    @FXML
    private Text errorMsg;
    
    @FXML
    private TextField txtAmount;

    @FXML
    private TextField txtNewAmount;

    @FXML
    private TextField txtTreshold;
    
    @FXML
    private TableView<Product> tableProdutToUpdate;

    @FXML
    private TableColumn<Product, String> facility;

    @FXML
    private TableColumn<Product, String> productID;

    @FXML
    private TableColumn<Product, String> productName;
        
    private String costumerID = ChatClient.user.getId();
    
    /**
     * This method initializes the table that shows on the screen.
     */
    public void initialize() {
    	productID.setCellValueFactory(new PropertyValueFactory<Product, String>("productID"));
    	productName.setCellValueFactory(new PropertyValueFactory<Product, String>("productName"));
    	facility.setCellValueFactory(new PropertyValueFactory<Product, String>("facility"));
		ObservableList<Product> returnList = getProduct();
		if (!returnList.isEmpty()) {
			tableProdutToUpdate.setItems(returnList);
			tableProdutToUpdate.refresh();
		}
    }
    /**
     * This method fills the table with data from the data base about the products that the operations worker need to refill
     * 															(according to the executive order sent from the area manager).
     * @return
     */
    public ObservableList<Product> getProduct(){
    	ObservableList<Product> products = FXCollections.observableArrayList();
		ClientUI.chat.accept("getProdutToFillForOprationWorker " + ChatClient.user.getFirstName()+"_"+ChatClient.user.getLastName());
		if (!ChatClient.answerMsg.equals("Empty")) {
			String[] result = ChatClient.answerMsg.split("\\,");
			for (int i = 0; i < result.length; i += 3) {
				products.add(new Product(result[i], result[i + 1], result[i + 2]));
			}
		}
		return products;
    }
    
    @FXML
    void getRelatedFields(MouseEvent event) {
    	Product p = tableProdutToUpdate.getSelectionModel().getSelectedItem();
    	ClientUI.chat.accept("getRelatadFieldsForProduct " + p.getProductID() + " " + p.getFacility());
    	if (!ChatClient.answerMsg.equals("Empty")) {
    		String[] result = ChatClient.answerMsg.split("\\,");
    		txtTreshold.setText(result[0]);
    		txtAmount.setText(result[1]);
    	}
    }

    @FXML
    void getUpdateValuebtn(ActionEvent event) {
    	Product p = tableProdutToUpdate.getSelectionModel().getSelectedItem();
		if (p == null) {
			errorMsg.setText("Please choose a product");
			return;
		}
		if(txtNewAmount.getText().trim().isEmpty()) {
			errorMsg.setText("You must enter a integer number");
			return;
		}
		if(!isNumeric(txtNewAmount.getText())) {
			errorMsg.setText("You must enter a integer number");
			return;
		}
		if(Integer.parseInt(txtNewAmount.getText()) < Integer.parseInt(txtTreshold.getText())) {
			errorMsg.setText("New amount can not be smaller than threshold");
			return;
		}
		ClientUI.chat.accept("updateInventory " + p.getFacility() + " " + p.getProductName() + " " + txtNewAmount.getText());
		switch (ChatClient.answerMsg) {
		case "Error":
			errorMsg.setText("Failed to update quantity.");
			break;
		case "Update":
			ObservableList<Product> returnList = getProduct();
			if (returnList.isEmpty()) {
				tableProdutToUpdate.getItems().clear();
			} else {
				tableProdutToUpdate.setItems(returnList);
			}
			tableProdutToUpdate.refresh();
			popUpWindow();
			break;
		}
    }
    /**
	* This method opens a pop up window, that lets the operations worker know that the inventory update was successful, and that
	* an email and a SMS has been sent to the area manager (simulation).
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
	* This method sets the pop up window that lets the operations worker know that the inventory update was successful, and that
	* an email and a SMS has been sent to the area manager (simulation).
	* @return the pop up window
	*/	
    private Pane initPane() {
		Pane pane = new Pane();
		pane.setStyle("-fx-background-color:  #ffffff; -fx-border-color:  #000000;");
		
		Label label = new Label("S I M U L A T I O N");
		label.setStyle("-fx-font-family: Rockwell; -fx-font-size: 14; -fx-font-weight: bold;");
		label.setLayoutX(123);
		label.setLayoutY(39);
		
		Label label2 = new Label("New amount has been set.");
		label2.setStyle("-fx-font-family: Rockwell; -fx-font-size: 15;");
		label2.setLayoutX(92);
		label2.setLayoutY(67);
		
		Label label3 = new Label("An email and a SMS sent to the area manager.");
		label3.setStyle("-fx-font-family: Rockwell; -fx-font-size: 15;");
		label3.setLayoutX(36);
		label3.setLayoutY(87);
		
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
		pane.getChildren().addAll(label, label2, label3, ok);;
		return pane;
	}
    private boolean isNumeric(String str) {
        for (char c : str.toCharArray())
            if (!Character.isDigit(c)) return false;
        return true;
    }
    /**
	* This method closes the current window ("Inventory Management") and opens the window "Operation Worker".
	* @param event
	* @throws IOException
	*/
    @FXML
    void getBackbtn(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/OperationWorker.fxml"));
    	Parent root = (Parent) loader.load();
    	OperationWorkerController control = loader.getController();
    	control.loadDetails(ChatClient.user);
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Operation Worker");
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
