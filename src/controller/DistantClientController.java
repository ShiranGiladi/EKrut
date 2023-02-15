package controller;

import java.io.IOException;
import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.User;

/**
 * This class is the "home page" of the user "Distant Client" (client).
 * The "Distant Client" can go from the window that this class represents to the following windows: - pick up later
 * 																									- for delivery
 * 																									- approve your delivery
 * "pick up later" and "for delivery" these are the two available options for distant orders.
 * "approve your delivery" is to confirm that the client got their order delivered to them.
 * The "Distant Client" can also log out and get back to the "welcome to E-Krut - login" window, or exit the system altogether.
 */
public class DistantClientController {

    @FXML
    private Button Exitbtn;
    
	@FXML
    private Button ForDeliverybtn;

    @FXML
    private Button Logoutbtn;

    @FXML
    private Button PickUpLaterbtn;
    
    @FXML
    private Button approveYourDeliveryBtn;
    
    @FXML
    private TextField txtName;

    @FXML
    private TextField txtStatus;
    
    @FXML
    private Text errorMsg;
    
    @FXML
    private Text discountMsg;
    
    private int discount = 0;
    
    private String costumerID = ChatClient.user.getId();
    
    /**
	 * This method logs out the user (Distant Client) from the system.
	 * Once the user clicks on the "logout" button, the current window, "Distant Client", will close, and instead, the window 
	 * "Welcome To E-Krut Login" will open. 
	 * @param event
	 * @throws IOException
	 */
    @FXML
    void getLogoutbtn(ActionEvent event) throws IOException {
    	ClientUI.chat.accept("logout " + costumerID);
		((Node)event.getSource()).getScene().getWindow().hide();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/WelcomeToEKrutLogin.fxml"));
    	Parent root = (Parent) loader.load();
    	WelcomeToEKrutLoginController control = loader.getController();
    	control.setFormat(ChatClient.user.getSystemFormat());
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Welcome To E-Krut");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);		
		primaryStage.show();
    }
    /**
     * This method closes the current window ("Distant Client") and opens the window "Pick Up Later", where the client enters the necessary info 
     * for starting a distant order, that they intent to pick up at a later time or date.
     * @param event
     * @throws IOException
     */
    @FXML
    void getPickUpLaterbtn(ActionEvent event) throws IOException {
    	((Node)event.getSource()).getScene().getWindow().hide();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/PickUpLater.fxml"));
    	Parent root = (Parent) loader.load();
    	PickUpLaterController control = loader.getController();
    	control.setDiscount(discount);
    	ChatClient.user.setSupplyMethod("pick_up");
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Pick Up Later");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);		
		primaryStage.show();
    }
    /**
     * This method closes the current window ("Distant Client") and opens the window "For Delivery", where the client enters the necessary info 
     * for starting a distant order, that they want to get delivered to them.
     * @param event
     * @throws IOException
     */
    @FXML
    void getForDeliverybtn(ActionEvent event) throws IOException {
    	ClientUI.chat.accept("getInfoForDeliveryFields " + ChatClient.user.getStatus() + " " + ChatClient.user.getId());
    	if(ChatClient.answerMsg.equals("Error")) {
    		errorMsg.setText("Somthing went worng :(");
			return;
    	}
    	String[] result = ChatClient.answerMsg.split("\\,");
    	((Node)event.getSource()).getScene().getWindow().hide();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/ForDelivery.fxml"));
    	Parent root = (Parent) loader.load();
    	ForDeliveryController control = loader.getController();
    	control.setDiscount(discount);
    	control.setFields(result[0], result[1]);
    	ChatClient.user.setSupplyMethod("delivery");
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("For Delivery");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);		
		primaryStage.show();
    }
    /**
     * This method closes the current window ("Distant Client") and opens the window "Approve Your Delivery", where the client can approve that 
     * they have got their order delivered to them.
     * @param event
     * @throws IOException
     */
    @FXML
    void getApproveYourDeliveryBtn(ActionEvent event) throws IOException {
    	((Node)event.getSource()).getScene().getWindow().hide();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/ApproveYourDelivery.fxml"));
    	Parent root = (Parent) loader.load();
    	ApproveYourDeliveryController control = loader.getController();
    	control.start();
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Approve Your Delivery");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);		
		primaryStage.show();
    }
    /**
    * This method enters the user's name and status (regular client or subscriber) in the appropriate fields in the user's home page.
    * @param user
    */
	public void loadDetails(User user) {
		String fullName = user.getFirstName() + " " + user.getLastName();
    	txtName.setText(fullName);
    	txtStatus.setText(user.getStatus());
	}
	/**
	 * This method will inform the client if they have a 20% discount on their next order.
	 * A client will have this discount on their first order after upgrading their account from regular client to subscriber.
	 */
	public void initDiscountMsg() {
		discountMsg.setText("Walcome! \nThank you for upgrading your account to a subscriber!\nYou have a 20% discount on your first order!");
		discount = 1;
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
