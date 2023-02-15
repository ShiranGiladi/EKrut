package controller;

import java.io.IOException;

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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class represents the window that the user will see in their E-Krut app if they chose to make an order that they want to be delivered for them.
 * In order to make their order they need to fill in all the mandatory fields on the screen.
 * Also, from the window this class represents, the user can go back to their home page, or exit the client side of the system altogether.
 */
public class ForDeliveryController {
    @FXML
    private Button Exitbtn;

    @FXML
    private Button Backbtn;

    @FXML
    private Button GotoMenubtn;
    
    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtCity;

    @FXML
    private TextField txtPhoneNumber;
    
    @FXML
    private Label errorMsg;
    
    @FXML
    private ComboBox<String> selectAreaComboBox;
    
    private int discount;

    private String costumerID = ChatClient.user.getId();
    
    /**
   	* This method closes the current window ("For Delivery") and opens the window "Distant Client".
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
   	* This method closes the current window ("For Delivery") and opens the window "Menu", but only after the user filled all the mandatory fields 
   	* in the current window. If the user didn't filled one ore more of the mandatory fields, an appropriate error message will appear on the screen.
   	* @param event
   	* @throws IOException
   	*/ 
    @FXML
    void getGotoMenubtn(ActionEvent event) throws Exception {
    	String address = txtAddress.getText(), city = txtCity.getText(), phone = txtPhoneNumber.getText();
    	if(address.trim().isEmpty() || city.trim().isEmpty() || phone.trim().isEmpty() || selectAreaComboBox.getPromptText() == null) {
    		errorMsg.setText("You must fill all mandatory fields.");
    	}
    	else {
	    	((Node)event.getSource()).getScene().getWindow().hide();
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/Menu.fxml"));
	    	Parent root = (Parent) loader.load();
	    	MenuController control = loader.getController();
	    	setFacility();
	    	control.setDiscount(discount);
	    	control.setDetailsUserForDelivery(getFullAddress(), txtPhoneNumber.getText());
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
    
    private void setFacility() {
    	if(selectAreaComboBox.getPromptText().equals("North"))
    		ChatClient.user.setNote("haifa");
    	else if(selectAreaComboBox.getPromptText().equals("South"))
    		ChatClient.user.setNote("eilat");
    	else
    		ChatClient.user.setNote("dubai");
    }
    
    public void initialize() {
    	ObservableList<String> list = FXCollections.observableArrayList("North", "South", "UAE");
    	selectAreaComboBox.setStyle("-fx-font-family: Rockwell; -fx-font-size: 14;");
    	selectAreaComboBox.setItems(list);
    }
    
    @FXML
    void getSelectAreaComboBox(ActionEvent event) {
    	String s = selectAreaComboBox.getSelectionModel().getSelectedItem().toString();
    	selectAreaComboBox.setPromptText(s);
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

	public void setFields(String address, String phoneNumber) {
		String[] a = address.split("\\_");
		txtCity.setText(a[0]);
		txtAddress.setText(a[1] + " " + a[2]);
		txtPhoneNumber.setText(phoneNumber);
		String area = "";
		if(ChatClient.user.getNote().equals("haifa") || ChatClient.user.getNote().equals("karmiel"))
			area = "North";
		else if(ChatClient.user.getNote().equals("eilat") || ChatClient.user.getNote().equals("beersheva"))
			area = "South";
		else
			area = "United Arab Emirates";
		selectAreaComboBox.setPromptText(area);
	}
	
	private String getFullAddress() {
		return txtCity.getText() + "_" + txtAddress.getText().replace(" ", "_");
	}
}
