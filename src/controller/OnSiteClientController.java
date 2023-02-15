package controller;

import java.io.IOException;
import java.util.ArrayList;
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
import logic.Cart;
import logic.User;

/**
 * This class is the "home page" of the user "On Site Client" (client).
 * The "On Site Client" can go from the window that this class represents to the windows: - menu - to start their order
 * 																						  - collect order - to pick up an order they made earlier.
 * The "On Site Client" can also log out and get back to the "welcome to E-Krut - login" window, or exit the system altogether.
 */
public class OnSiteClientController {
    @FXML
    private Button Exitbtn;
    
	@FXML
	private Button CollectOrderbtn;
	
    @FXML
    private Button Logoutbtn;

    @FXML
    private Button StartOrderbtn;
    
    @FXML
    private TextField txtName;

    @FXML
    private TextField txtStatus;
    
    @FXML
    private Text discountMsg;
    
    @FXML
    private Text errorMsg;
    
    private ArrayList<Cart> arrCart = null;
    
    private int discount = 0;
    private String costumerID = ChatClient.user.getId();

    /**
	 * This method logs out the user (On Site Client) from the system.
	 * Once the user clicks on the "logout" button, the current window, "On Site Client", will close, and instead, the window 
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
     * This method closes the current window ("On Site Client") and opens the window "Menu", where the client can start their order.
     * @param event
     * @throws IOException
     */
    @FXML
    void getStartOrderbtn(ActionEvent event) throws Exception {
    	((Node)event.getSource()).getScene().getWindow().hide();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/Menu.fxml"));
    	Parent root = (Parent) loader.load();
    	MenuController control = loader.getController();
    	control.setDiscount(discount);
    	control.start();
    	if(arrCart != null) {
    		control.setArrCart(arrCart);
        	control.initCart(arrCart);
    	}
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Menu");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);		
		primaryStage.show();
		control.setTimer(primaryStage);
    }
    /**
     * This method closes the current window ("On Site Client") and opens the window "Collect Your Order", where the client can enter a code
     * to pick up an order they made earlier (through the E-Krut application).
     * @param event
     * @throws IOException
     */
    @FXML
    void getCollectOrderbtn(ActionEvent event) throws IOException {
    	((Node)event.getSource()).getScene().getWindow().hide();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/CollectYourOrder.fxml"));
    	Parent root = (Parent) loader.load();
    	CollectYourOrderController control = loader.getController();
    	control.start();
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Collect Your Order");
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
	 * This method saves the products that the client added to their cart, during the entire order process, regardless of the screen the client
	 * opens (the client can move freely between the different screens, and their cart will still be saved).
	 * @param arr
	 */
	public void saveCart(ArrayList<Cart> arr) {
		arrCart = arr;
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
