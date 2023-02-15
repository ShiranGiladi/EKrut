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
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class allows the area managers to set a threshold level to the products in the facilities in their area.
 * Also from the window this class represents, the area manager can go back to their home page, or exit the client side of the system altogether.
 */
public class ThresholdLevelController {
    @FXML
    private Button Exitbtn;

    @FXML
    private Button Backbtn;
    
    @FXML
    private ComboBox<String> SelectProduct;

    @FXML
    private Button UpdateValuebtn;

    @FXML
    private ComboBox<String> facilityComboBox;

    @FXML
    private TextField txtNewValue;

    @FXML
    private TextField txtOldValue;
    
    @FXML
    private Text errorMsg;

    private String costumerID = ChatClient.user.getId();
    
    /**
	* This method closes the current window ("Threshold Level") and opens the window "Area Manager".
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

    @FXML
    void getUpdateValuebtn(ActionEvent event) {
    	if(txtNewValue.getText().trim().isEmpty())
    		errorMsg.setText("You must enter new value");
    	else if(!isNumeric(txtNewValue.getText()))
			errorMsg.setText("You must enter a integer positive number");
    	else if(Double.parseDouble(txtNewValue.getText())%1 != 0)
    		errorMsg.setText("You must enter an integer value");
    	else if(Integer.parseInt(txtNewValue.getText()) < 0)
    		errorMsg.setText("Threshold can not be negative");
    	else {
        	ClientUI.chat.accept("updateThreshold " + SelectProduct.getSelectionModel().getSelectedItem().toString() + " " + getFcility() + " " + txtNewValue.getText());
        	if (ChatClient.answerMsg.equals("Error")){
        		errorMsg.setText("Failed to update new threshold.");
        	}
        	else {
        		popUpWindow();
        	}
    	}
    }
    /**
	* This method opens a pop up window, that lets the area manager know that a new threshold was set successfully.
	*/
	private void popUpWindow() {
		Scene popUp = new Scene(initPane(), 370, 155);
		Stage popUpStage = new Stage();
		popUpStage.setTitle("Set successfully");
		popUpStage.initStyle(StageStyle.UNDECORATED);
		popUpStage.setScene(popUp);
		popUpStage.show();
	}
	/**
	* This method sets the pop up window that lets the area manager know that a new threshold was set successfully.
	* @return the pop up window
	*/	
    private Pane initPane() {
		Pane pane = new Pane();
		pane.setStyle("-fx-background-color:  #94b5fe; -fx-border-color:  #000000;");
		
		Label label = new Label("A new threshold was set successfully.");
		label.setStyle("-fx-font-family: Rockwell; -fx-font-size: 18;");
		label.setLayoutX(32);
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
    
    private boolean isNumeric(String str) {
        for (char c : str.toCharArray())
            if (!Character.isDigit(c)) return false;
        return true;
    }
    
    @FXML
    void getSelectProduct(ActionEvent event) {
    	String location = getFcility();
    	String s = SelectProduct.getSelectionModel().getSelectedItem().toString();
    	SelectProduct.setPromptText(s);
    	SelectProduct.setStyle("-fx-font-family: Rockwell");
    	
    	ClientUI.chat.accept("getCurrentThreshold " + s + " " + location);
    	if(!ChatClient.answerMsg.equals("Error"))
    		txtOldValue.setText(ChatClient.answerMsg);
    	txtOldValue.setDisable(true);
    }
    
    @FXML
    void getFacilityComboBox(ActionEvent event) {
    	String location = getFcility();
    	ClientUI.chat.accept("getProductNameForFacility " + location);
    	if(!ChatClient.answerMsg.equals("Empty")) {
			ObservableList<String> productsNames = FXCollections.observableArrayList();
			String[] productList = ChatClient.answerMsg.split("\\,");
			for(int i=0; i<productList.length; i++)
				productsNames.add(productList[i]);
			SelectProduct.setItems(productsNames);
		}
    	SelectProduct.setDisable(false);
    }
    /**
     * This method sets which facilities will appear in the "facility" combo box, according to the area that the area manager is in charge of.
     */
    public void initialize() {
    	SelectProduct.setDisable(true);
    	ObservableList<String> facilities = FXCollections.observableArrayList();
    	if(ChatClient.user.getNote().equals("north")){
    		facilities.add("Haifa");
    		facilities.add("Karmiel");
    	}
    	else if(ChatClient.user.getNote().equals("south")){
    		facilities.add("Eilat");
    		facilities.add("Be'er_Sheva");
    	}
    	else {
    		facilities.add("Dubai");
    		facilities.add("Abu_Dhabi");
    	}
    	facilityComboBox.setItems(facilities);
    	facilityComboBox.setStyle("-fx-font-family: Rockwell");
    }
    /**
     * This method gets the facility in which the area manager wants to update the threshold level of a certain product.
     * @return
     */
    private String getFcility() {
    	String facility = facilityComboBox.getSelectionModel().getSelectedItem().toString();
    	String location = "";
    	switch (facility) {
    	case "Haifa":
    		location = "haifa";
    		break;
    	case "Karmiel":
    		location = "karmiel";
    		break;
    	case "Eilat":
    		location = "eilat";
    		break;
    	case "Be'er_Sheva":
    		location = "beersheva";
    		break;
    	case "Dubai":
    		location = "dubai";
    		break;
    	case "Abu_Dhabi":
    		location = "abudhabi";
    		break;
    	default:
    		break;
    	}
    	return location;
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
