package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import client.ChatClient;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class represents the window that the user will see in their E-Krut app if they chose to make an order that they intent to pick up at a later
 * time or date. In order to make their order they need to fill in all the mandatory fields on the screen.
 * Also, from the window this class represents, the user can go back to their home page, or exit the client side of the system altogether.
 */
public class PickUpLaterController {
    @FXML
    private Button Exitbtn;

    @FXML
    private Button Backbtn;

    @FXML
    private Button GotoMenubtn;
    
    @FXML
    private ComboBox<String> Combo;
    
    @FXML
    private Label errorMsg;
    
    @FXML
    private DatePicker selectDate;
    
    private int discount;
    
    private String costumerID = ChatClient.user.getId();
    
    /**
   	* This method closes the current window ("Pick Up Later") and opens the window "Distant Client".
   	* @param event
   	* @throws IOException
   	*/ 
    @FXML
    void getBackbtn(ActionEvent event) throws IOException {
    	((Node)event.getSource()).getScene().getWindow().hide();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/DistantClient.fxml")); 
    	Parent root = (Parent) loader.load();
    	DistantClientController control = loader.getController();
    	control.loadDetails(ChatClient.user);
    	if(discount == 1)
    		control.initDiscountMsg();
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Distant Client");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);		
		primaryStage.show();
    }
    /**
   	* This method closes the current window ("Pick Up Later") and opens the window "Menu", but only after the user filled all the mandatory fields 
   	* in the current window. If the user didn't filled one ore more of the mandatory fields, an appropriate error message will appear on the screen.
   	* @param event
   	* @throws IOException
   	*/ 
    @FXML
    void getGotoMenubtn(ActionEvent event) throws Exception {
    	LocalDate selectedDate = selectDate.getValue();
    	try {
        	String date = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    	} catch (Exception e) {errorMsg.setText("You must fill all details."); return;}
    	
    	if (Combo.getPromptText() == null) {
    		errorMsg.setText("You must fill all details.");
    	}
    	else {
	    	((Node)event.getSource()).getScene().getWindow().hide();
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/Menu.fxml"));
	    	Parent root = (Parent) loader.load();
	    	MenuController control = loader.getController();
	    	control.setDiscount(discount);
	    	control.start();
			Stage primaryStage = new Stage();
			Scene scene = new Scene(root);
			primaryStage.setTitle("Menu");
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setScene(scene);		
			primaryStage.show();
			control.setTimer(primaryStage);
    	}
    }
    /**
     * This class sets the facility that the user chose (from the combo box).
     * @param event
     * @throws IOException
     */
    @FXML
    void getComboBox(ActionEvent event) throws IOException {
    	String s = Combo.getSelectionModel().getSelectedItem().toString();
    	Combo.setPromptText(s);
    	switch(s) {
    	case "Haifa":
    		ChatClient.user.setNote("haifa");
    		break;
    	case "Karmiel":
    		ChatClient.user.setNote("karmiel");
    		break;
    	case "Eilat":
    		ChatClient.user.setNote("eilat");
    		break;
    	case "Be'er Sheva":
    		ChatClient.user.setNote("beersheva");
    		break;
    	case "Dubai":
    		ChatClient.user.setNote("dubai");
    		break;
    	case "Abu Dhabi":
    		ChatClient.user.setNote("abudhabi");
    		break;
    	default:
    		break;
    	}
    }
    /**
	* This method initializes the combo box in the window with all the facilities from which the client can make their order.
	*/
    public void initialize() {
    	ObservableList<String> list = FXCollections.observableArrayList("Haifa", "Karmiel", "Eilat", "Be'er Sheva" , "Dubai", "Abu Dhabi");
    	Combo.setStyle("-fx-font-family: Rockwell; -fx-font-size: 14;");
    	Combo.setItems(list);
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
    /**
	 * 
	 * @return the discount for clients that upgraded to subscribers, for their first purchase as subscribers
	 */
	public int getDiscount() {
		return discount;
	}
	/** 
	 * 
	 * @param discount - set the discount to this value
	 */
	public void setDiscount(int discount) {
		this.discount = discount;
	}
}
