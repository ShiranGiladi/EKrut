package controller;

import java.io.IOException;
import java.util.ArrayList;
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
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.Cart;

/**
 * This class allows the user to make payment for their order.
 * There is only one option of payment for regular client: payment with credit card.
 * There are there options of payment for a subscribed client: payment with credit card, payment with E-Krut app, a deferred payment.
 * If the payment was successful, the client will get back to their home page. 
 * The client can go back from the window this class represents to the window "Menu".
 * Also from the window this class represents, the client can exit the client side of the system altogether.
 */
public class PaymentController {

    @FXML
    private Button Exitbtn;

    @FXML
    private Button Backbtn;
    
    @FXML
    private Button PayEKT;

    @FXML
    private Button PayNowtbn;
    
    @FXML
    private Button MakePaymentbtn;
    
    @FXML
    private TextField txtCVV;

    @FXML
    private TextField txtCardNum;

    @FXML
    private TextField txtNameOnCard;
    
    @FXML
    private Text txtErrorMsg1;

    @FXML
    private Text txtErrorMsg2;

    @FXML
    private Text txtErrorMsg3;
    
    @FXML
    private ArrayList<Cart> arrCart;
    
    @FXML
    private ComboBox<String> Month;

    @FXML
    private ComboBox<String> Year;
    
    private String[] totalPrice;
    private String costumerID = ChatClient.user.getId();
    private String address, phone; //for delivery information
    private FiveMinutesTimer timer = new FiveMinutesTimer();
    
    /**
   	* This method closes the current window ("Payment") and opens the window "Menu".
   	* @param event
   	* @throws IOException
     * @throws InterruptedException 
   	*/ 
    @FXML
    void getBackbtn(ActionEvent event) throws IOException, InterruptedException {
    	((Node)event.getSource()).getScene().getWindow().hide();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/Menu.fxml"));
    	Parent root = (Parent) loader.load();
    	MenuController control = loader.getController();
    	control.setArrCart(arrCart);
    	control.start();
    	control.initCart(arrCart);
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Menu");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);		
		primaryStage.show();
		control.setTimer(primaryStage);
    }
    /**
     * This method allows the user make payment for their purchase with a credit card.
     * This option is available for regular clients as well as subscribers.
     * When this window opens, the client's credit card data from the data base will appear automatically. The client can change it if they like to.
     * Once the payment was done successfully, a pop up window will appear on the screen to let the client know that the payment was successful, 
     * and also the current window ("Payment") will close and the home page of the client will open.
     * @param event
     * @throws IOException
     */
    @FXML
    void getPayNowtbn(ActionEvent event) throws IOException {
    	timer.resetTimer();
    	timer.startTimer();
    	String cvv = txtCVV.getText(), cardNum = txtCardNum.getText(), nameOnCard = txtNameOnCard.getText();
    	if (cvv.trim().isEmpty() || cardNum.trim().isEmpty() || nameOnCard.isEmpty() || Month.getPromptText() == null || Year.getPromptText() == null) {
    		txtErrorMsg1.setText("You must enter all the details listed above.");
    	}
    	else {
    		if(!ChatClient.user.getSupplyMethod().equals("delivery"))
    			updateInvatory(arrCart);
    		addOrderDetailsToDB("craditcard");
	    	((Node)event.getSource()).getScene().getWindow().hide();
	    	returnToHomePage();
    	}
    }
    /**
     * This method allows the user make payment for their purchase via the E-Krut app.
     * This option is available only for subscribed clients.
     * In case a regular client tries to use this option, an appropriate error message will appear on the screen.
     * Once the payment was done successfully, a pop up window will appear on the screen to let the client know that the payment was successful, 
     * and also the current window ("Payment") will close and the home page of the client will open.
     * @param event
     * @throws IOException
     */
    @FXML
    void getPayEKT(ActionEvent event) throws IOException {
    	timer.resetTimer();
    	timer.startTimer();
    	if(!ChatClient.user.getStatus().equals("Subscriber_Client")) {
    		txtErrorMsg2.setText("You have to be a subscriber to get this option.");
    		return;
    	}
    	if(!ChatClient.user.getSupplyMethod().equals("delivery"))
    		updateInvatory(arrCart);
    	addOrderDetailsToDB("EKT");
    	((Node)event.getSource()).getScene().getWindow().hide();
    	returnToHomePage();
    }
    /**
     * This method allows the user make a deferred payment for their purchase.
     * This option is available only for subscribed clients.
     * In case a regular client tries to use this option, an appropriate error message will appear on the screen.
     * Once the was payment done successfully, a pop up window will appear on the screen to let the client know that the payment was successful, and
     * also the current window ("Payment") will close and the home page of the client will open.
     * @param event
     * @throws IOException
     */
    @FXML
    void getMakePaymentbtn(ActionEvent event) throws IOException {
    	timer.resetTimer();
    	timer.startTimer();
    	if(!ChatClient.user.getStatus().equals("Subscriber_Client")) {
    		txtErrorMsg3.setText("You have to be a subscriber to get this option.");
    		return;
    	}
    	if(!ChatClient.user.getSupplyMethod().equals("delivery"))
    		updateInvatory(arrCart);
    	addOrderDetailsToDB("deferredPayment");
    	
    	((Node)event.getSource()).getScene().getWindow().hide();
    	returnToHomePage();
    }
    /**
     * This method return the client to their home page after the order is done.
     * If the client was on site, the current window ("Payment") will close, and the window "On Site Client" will open.
     * If it's a distant client, the current window ("Payment") will close, and the window "Distant Client" will open.
     * @throws IOException
     */
    private void returnToHomePage() throws IOException {  
    	//take client to "On Site Client" window
    	if(ChatClient.user.getSupplyMethod().equals("onSite")) {
    		FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/GUI/OnSiteClient.fxml"));
        	Parent root2 = (Parent) loader2.load();
        	OnSiteClientController control = loader2.getController();
        	control.loadDetails(ChatClient.user);
    		Stage primaryStage2 = new Stage();
    		Scene scene2 = new Scene(root2);
    		primaryStage2.setTitle("On Site Client");
    		primaryStage2.initStyle(StageStyle.UNDECORATED);
    		primaryStage2.setScene(scene2);		
    		primaryStage2.show(); 
    	}
    	//take client to "Distant Client" window
    	else {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/DistantClient.fxml")); 
        	Parent root = (Parent) loader.load();
        	DistantClientController control = loader.getController();
        	control.loadDetails(ChatClient.user);
    		Stage primaryStage = new Stage();
    		Scene scene = new Scene(root);
    		primaryStage.setTitle("Distant Client");
    		primaryStage.initStyle(StageStyle.UNDECORATED);
    		primaryStage.setScene(scene);		
    		primaryStage.show();
    	}
    	popUpWindow();
    }
    /**
	* This method opens a pop up window, that lets the client know that they completed successfully their purchase and their order is placed.
	*/
    private void popUpWindow() {
		Scene popUp = new Scene(initPane(), 370, 155);
		Stage popUpStage = new Stage();
		popUpStage.setTitle("Order is Placed!");
		popUpStage.initStyle(StageStyle.UNDECORATED);
		popUpStage.setScene(popUp);
		popUpStage.show();
	}
    /**
	* This method sets the pop up window that lets the client know that they completed successfully their purchase and their order is placed.
	* @return the pop up window
	*/
    private Pane initPane() {
		Pane pane = new Pane();
		pane.setStyle("-fx-background-color:  #94b5fe; -fx-border-color:  #000000;");
		
		Label labe1 = new Label("Yey, your order has been placed!");
		labe1.setStyle("-fx-font-family: Rockwell; -fx-font-size: 18;");
		labe1.setLayoutX(51);
		labe1.setLayoutY(35);
		
		Label labe2 = new Label("Thank you for choosing E-Krut! See you soon :)");
		labe2.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16;");
		labe2.setLayoutX(18);
		labe2.setLayoutY(68);
		
		Button thanks = new Button("Thank You!");
		thanks.setStyle("-fx-background-color: #122753; -fx-text-fill: #ffffff; -fx-font-family: Rockwell; -fx-font-size: 14;");
		thanks.setLayoutX(143);
		thanks.setLayoutY(102);
		thanks.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				((Node)event.getSource()).getScene().getWindow().hide();
			}
		});
		
		pane.getChildren().addAll(labe1, labe2, thanks);;
		return pane;
	}
    /**
     * This method updates the inventory of the facility from which the client made their order according to the order - reduces the products
     * purchased from the inventory.
     * @param arrCart
     */
    public void updateInvatory(ArrayList<Cart> arrCart) {
    	for(Cart c : arrCart) {
    		ClientUI.chat.accept("UpdateQuantity " + ChatClient.user.getNote() + " " + c.getProductName() + " " + c.getQauntity() + " Minus");
    	}
    }
    /**
     * This method adds all the details about the order to the "orders" table in the data base.
     * These details include: the products purchased, the total price of the order, the type of payment that was made, 
     * the type of order (on site / pick up late / delivery), the facility from which the order was made, etc. 
     * @param payment
     */
    public void addOrderDetailsToDB(String payment) { 	
    	String orderContent = "";
    	int productInOrder = 0;
    	for(int i=0; i<arrCart.size(); i++) {
    		productInOrder = productInOrder + Integer.parseInt(arrCart.get(i).getQauntity());
    		if(i == arrCart.size()-1)
    			orderContent = orderContent + arrCart.get(i).getProductName() + "," + arrCart.get(i).getQauntity() ;
    		else
    			orderContent = orderContent + arrCart.get(i).getProductName() + "," + arrCart.get(i).getQauntity() + ",";
    	}
    	ClientUI.chat.accept("addOrderDetailsToDB " + totalPrice[0] + " " + ChatClient.user.getNote() + " " + ChatClient.user.getId() + " " + payment 
    			+ " " + String.valueOf(productInOrder) + " " + ChatClient.user.getSupplyMethod() + " " + orderContent + " " + address + " " + phone);
    }
    /**
     * This method gets the chosen month (represents the credit card expiration month) from the "Month" combo box.
     * @param event
     */
    @FXML
    void getMonth(ActionEvent event) {
    	String s = Month.getSelectionModel().getSelectedItem().toString();
    	Month.setPromptText(s);
    }
    /**
     * This method gets the chosen year (represents the credit card expiration year) from the "Year" combo box.
     * @param event
     */
    @FXML
    void getYear(ActionEvent event) {
    	String s = Year.getSelectionModel().getSelectedItem().toString();
    	Year.setPromptText(s);	
    }
    /**
   	* This method initializes the combo boxes in the window, from which the user can choose the expiration date on their credit card.
   	*/
    public void initialize() {
    	ObservableList<String> m = FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", 
    															"July", "August", "September", "October", "November", "December");
    	Month.setItems(m);
    	Month.setStyle("-fx-font-family: Rockwell; -fx-font-size: 12;");
    	
    	ObservableList<String> y = FXCollections.observableArrayList("2023", "2024", "2025", "2026", "2027", "2028", "2029");
    	Year.setItems(y);
    	Year.setStyle("-fx-font-family: Rockwell; -fx-font-size: 12;");
    }
    /**
     * 
     * @param arr - set the array that represents the cart to this value
     */
    public void initArrCart(ArrayList<Cart> arr) {
    	arrCart = arr;
    }
    /**
     * @return the total price of the entire order
     */
	public String[] getTotalPrice() {
		return totalPrice;
	}
	/**
	 * 
	 * @param totalPrice - set the total price of the entire order to this value
	 */
	public void setTotalPrice(String[] totalPrice) {
		this.totalPrice = totalPrice;
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
     * This method sets automatically the fields in the "payment with credit card" section to the values that stored in the data base for the client.
     * The client can change the values set automatically if they want to pay with a different card.
     * @param nameOnCard - the name of the card holder
     * @param creditCard - the credit card number
     * @param expirationDate - the expiration date of the credit card
     * @param cvv - the security number at the back of the card
     */
	public void setFields(String nameOnCard, String creditCard, String expirationDate, String cvv) { ////////////////////////////////////////////////////
		String[] resultNameOneCard = nameOnCard.split("\\-");
		String[] resultExpirationDate = expirationDate.split("\\-");
		String monthtxt = ""; 
		switch(resultExpirationDate[2]) {
		case "01":
			monthtxt = "January";
			break;
		case "02":
			monthtxt = "February";
			break;
		case "03":
			monthtxt = "March";
			break;
		case "04":
			monthtxt = "April";
			break;
		case "05":
			monthtxt = "May";
			break;
		case "06":
			monthtxt = "June";
			break;
		case "107":
			monthtxt = "July";
			break;
		case "08":
			monthtxt = "August";
			break;
		case "09":
			monthtxt = "September";
			break;
		case "10":
			monthtxt = "October";
			break;
		case "11":
			monthtxt = "November";
			break;
		case "12":
			monthtxt = "December";
			break;
		}
		txtNameOnCard.setText(resultNameOneCard[0] + " " + resultNameOneCard[1]);
		txtCardNum.setText(creditCard);
		Year.setPromptText(resultExpirationDate[0]); /////////////////////////////////////////////////////////////////////////////
		Month.setPromptText(monthtxt); //////////////////////////////////////////////////////////////////////////
		txtCVV.setText(cvv);
	}
	/**
	 * This method sets the details needed for an order for delivery
	 * @param address - the address that the client entered (where to send the order to)
	 * @param phone - the clients phone number
	 */
	public void setDetailsUserForDelivery(String address, String phone) {
		this.address = address;
		this.phone = phone;
	}
	/**
	 * This method sets a timer so that if the user doesn't use the system for more than 5 minutes, it will automatically close
	 * @param primaryStage
	 */
	public void setTimer(Stage primaryStage) {
		timer.setTimer(primaryStage, ChatClient.user.getId());
		timer.startTimer();
	}
}
