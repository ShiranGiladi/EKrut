package controller;

import java.io.IOException;
import java.text.DecimalFormat;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.Cart;
import logic.Product;

/**
 * This class represents the "Menu" window. 
 * There are 4 types of menu the user can choose from: all products, desserts, drinks and food.
 * For each product on the menu, the following details will be available: the product's name, the product's image, and the product's price, as well 
 * as a "select" button that will allow the user to add the required product to their cart. 
 * In case there is a product on sale, both the original price and the discount price will be displayed.
 * The window this class represents also include a cart.
 * For each item in the cart, the following details will be available: the product's name, the product's image, the product's price per item,
 * 																										and the selected amount of the product.
 * Also, for each item in the cart there would be 3 buttons: + to increase the amount of the product in the cart
 * 														     - to decrease the amount of the product in the cart
 * 														     X to remove the product from the cart altogether
 * After selecting at least one item, the user can proceed to the "payment" window.
 * Also, from the window this class represents, the user can go back to their home page, or exit the client side of the system altogether.
 */
public class MenuController {

    @FXML
    private Button Exitbtn;
    
    @FXML
    private Button Backbtn;
    
    @FXML
    private ComboBox<String> Combo;
    
    @FXML
    private Button DeleteOrderbtn;
    
    @FXML
    private Button GotoPaymentbtn;
    
    @FXML
    private GridPane gridPaneMenu;
    
    @FXML
    private GridPane myCartGridPane;
    
    @FXML
    private TextField txtTotalPrice;
    
    @FXML
    private Text errorMsg;
    
    @FXML
    private Text insteadOfMsg;
    
    private int discount = 0;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private ArrayList<Cart> arrCart = new ArrayList<Cart>();
    private String costumerID = ChatClient.user.getId();
    private String address, phone; //for delivery information
    private FiveMinutesTimer timer = new FiveMinutesTimer();
    
    /** 
	 * @param discount - set the discount to this value
	 */
    public void setDiscount(int discount) {
    	this.discount = discount;
    }
    /**
     * @return the array that represents the cart
     */
    public ArrayList<Cart> getArrCart() {
		return arrCart;
	}
    /**
     * @param arrCart - set the array that represents the cart to this value
     */
	public void setArrCart(ArrayList<Cart> arrCart) {
		this.arrCart = arrCart;
	}
	/**
	  * This method closes the current window ("Menu") and opens a window according to the user that open the current window:
	  * 										"On Site Client", "Pick Up Later" (distant client) or "For Delivery" (distant client).
	  * (There are there windows from which the window "Menu" is reachable: "On Site Client", "Pick Up Later" and "For Delivery".
	  * If, for instance, the window "Menu" was opened from the window "On Site Client", then this method will open back the 
	  * "On Site Client" window. Same goes with the "Pick Up Later" and "For Delivery" windows).
	  * @param event
	  * @throws IOException
	  */ 
	@FXML
    void getBackbtn(ActionEvent event) throws IOException {		
		((Node)event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		if(ChatClient.user.getSupplyMethod().equals("pick_up") || ChatClient.user.getSupplyMethod().equals("delivery")) {
			if(ChatClient.user.getSupplyMethod().equals("pick_up")) {
		    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/PickUpLater.fxml"));
		    	Parent root = (Parent) loader.load();
		    	PickUpLaterController control = loader.getController();
		    	control.setDiscount(discount);
		    	ChatClient.user.setSupplyMethod("pick_up");
				Scene scene = new Scene(root);
				primaryStage.setTitle("Pick Up Later");
				primaryStage.initStyle(StageStyle.UNDECORATED);
				primaryStage.setScene(scene);		
				primaryStage.show();
			}
			else {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/ForDelivery.fxml"));
		    	Parent root = (Parent) loader.load();
		    	ForDeliveryController control = loader.getController();
		    	control.setDiscount(discount);
		    	ChatClient.user.setSupplyMethod("delivery");
				Scene scene = new Scene(root);
				primaryStage.setTitle("For Delivery");
				primaryStage.initStyle(StageStyle.UNDECORATED);
				primaryStage.setScene(scene);		
				primaryStage.show();
			}
		}
		else {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/OnSiteClient.fxml"));
	    	Parent root = (Parent) loader.load();
	    	OnSiteClientController control = loader.getController();
	    	control.loadDetails(ChatClient.user);
	    	control.saveCart(arrCart);
			Scene scene = new Scene(root);
			primaryStage.setTitle("On Site Client");
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setScene(scene);
		}
		primaryStage.show();
    }
	
    @FXML
    void getComboBox(ActionEvent event) throws IOException, InterruptedException {
    	timer.resetTimer();
    	timer.startTimer();
    	String s = Combo.getSelectionModel().getSelectedItem().toString();
    	ClientUI.chat.accept("showProductInLocation " + s + " " +  ChatClient.user.getNote() + " " + ChatClient.user.getStatus() + " " + ChatClient.user.getSupplyMethod());
		if(ChatClient.arrProduct.get(0).getProductName().equals("Error")) {
			System.out.println("No product found");
		}
		else
			getProducts(ChatClient.arrProduct);
    }
    /**
	* This method initializes the combo box in the window with the type of menu that the client's choosing.
	*/
    public void initialize() {
    	ObservableList<String> list = FXCollections.observableArrayList("Show-All", "Food", "Desserts", "Drinks");
    	Combo.setStyle("-fx-font-family: Rockwell; -fx-font-size: 14;");
    	Combo.setItems(list);
    }
    /**
     * This method is in charge of showing the products in the menu according to the information about them in the data base.
     * For each product the following details will be displayed: the product's name, the product's image, the product's price. 
     * In case there is a product on sale, both the original price and the discount price will be displayed.
     * Also, there would be a "select" button for each product. Once the user clicks on this button, the selected product will be added to the cart.
     * Every time the user clicks on that button the quantity of the selected product in the cart will increase by 1, as long as there is enough 
     * of the product in the facility's inventory. Once the user select all the available items of a certain product in the inventory,
     * an appropriate error message will appear on the screen.
     * @param arrProduct
     * @return
     */
	public ObservableList<Product> getProducts(ArrayList<Product> arrProduct){
    	ObservableList<Product> product = FXCollections.observableArrayList();
    	int i=0, j=0;
    	gridPaneMenu.getChildren().clear();
    	for(Product p : arrProduct) {
    		HBox hbox = new HBox();
    		String[] scrImage = p.getImgSrc().split("/"); ///////////////////////////////////////////////////////////////////////////////////////////////////
    		String finalSrcImg = scrImage[1] + "/" + scrImage[2];
    		Image img = new Image(finalSrcImg);
    		ImageView imgView = new ImageView(img);
    		imgView.setFitHeight(78);
    		imgView.setFitWidth(90);
    		TextField productName = new TextField();
    		
    		VBox vbox = new VBox();
    		productName.setStyle("-fx-font-size: 14; -fx-font-family: Rockwell; -fx-background-color: #f4f4f4; -fx-min-height: 16; -fx-min-width: 86");
    		productName.setText(p.getProductName());
    		vbox.getChildren().add(productName);
    		
    		TextField productPrice = new TextField();
    		productPrice.setStyle("-fx-font-size: 14; -fx-font-family: Rockwell; -fx-background-color: #f4f4f4; -fx-min-height: 16; -fx-min-width: 86");
    		if(ChatClient.user.getStatus().equals("Subscriber_Client") && p.getNewPriceForSubscriberInSale() != null) {
    			productPrice.setText(p.getNewPriceForSubscriberInSale() + "$");
    			Text oldPrice = new Text();
    			oldPrice.setText(p.getProductPrice() + "$");
    			oldPrice.setStyle("-fx-font-family: Rockwell");
    			oldPrice.setStrikethrough(true);
    			Text saleText = new Text();
    			saleText.setText("  SALE");
    			saleText.setStyle("-fx-font-family: Rockwell");
    			HBox saleHbox = new HBox();
    			saleHbox.getChildren().add(oldPrice);
    			saleHbox.getChildren().add(saleText);
    			vbox.getChildren().add(productPrice);
    			vbox.getChildren().add(saleHbox);
    		}
    		else {
    			productPrice.setText(p.getProductPrice() + "$");
    			vbox.getChildren().add(productPrice);
    		}
    		
    		Button btn = new Button("Select");
    		btn.setStyle("-fx-background-color: #94b5fe; -fx-font-family: Rockwell; -fx-font-size: 14; -fx-min-height: 26; -fx-min-width: 58");
    		btn.setOnAction(new EventHandler<ActionEvent>() {
    		    public void handle(ActionEvent event) {
        	    	timer.resetTimer();
        	    	timer.startTimer();
    		    	errorMsg.setText(null);
    		    	Cart c;
    		    	if(ChatClient.user.getStatus().equals("Subscriber_Client") && p.getNewPriceForSubscriberInSale() != null) {
    		    		c = new Cart(p.getProductName(), p.getNewPriceForSubscriberInSale(), p.getNewPriceForSubscriberInSale(), "1", p.getImgSrc());
    		    	}
    		    	else
    		    		c = new Cart(p.getProductName(), p.getProductPrice(), p.getProductPrice(), "1", p.getImgSrc());
    		    	addToArrayCart(c);
    		    }
    		});
    		
    		ClientUI.chat.accept("CheckQuantity " + ChatClient.user.getNote() + " " + p.getProductName());
    		if(ChatClient.answerMsg.equals("0")) {
    			TextField notAvailableMsg = new TextField();
    			notAvailableMsg.setStyle("-fx-font-size: 14; -fx-font-family: Rockwell; -fx-background-color: #f4f4f4; -fx-min-height: 16; -fx-min-width: 86");
    			notAvailableMsg.setText("Not Available");
        		vbox.getChildren().add(notAvailableMsg);
        		btn.setDisable(true);
    		}
    		
    		vbox.setStyle("-fx-min-height: 78; -fx-min-width: 100");
    		vbox.getChildren().add(btn);
    		hbox.getChildren().add(imgView);
    		hbox.getChildren().add(vbox);
    		gridPaneMenu.add(hbox, j++, i++/3);
    		if(j==3) {j=0;}
    	}
		return product;
    }
	/**
	 * This method adds the products that the user chooses to the cart.
	 * If the user tries to add to cart more items of a certain product than available in inventory - an appropriate error message 
	 * 																									  will appear on the screen. 
	 * @param c
	 */
	public void addToArrayCart(Cart c) {
		int flag = 0, flagHasQauntity = 0;;
    	for(Cart c1: arrCart) {
    		if(c1.getProductName().equals(c.getProductName())) {
    			flag = 1;
		    	if(ChatClient.user.getSupplyMethod().equals("delivery")) { ////////////////////////////////?????????????????//////////////////////
		    		flagHasQauntity=1;
		    	}
		    	else {
		    		ClientUI.chat.accept("CheckQuantity " + ChatClient.user.getNote() + " " + c1.getProductName());
		    		if(Integer.parseInt(ChatClient.answerMsg) > Integer.parseInt(c1.getQauntity())) {
		    			flagHasQauntity=1;
		    		}
		    	}
		    	if(flagHasQauntity == 1) {
		    		String newQauntity = String.valueOf(Integer.parseInt(c1.getQauntity()) + Integer.parseInt("1"));
	    			String newTotalPrice = String.valueOf(Double.parseDouble(c1.getTotalPerProduct()) + Double.parseDouble(c.getProductPrice()));
	    			c1.setTotalPerProduct(newTotalPrice);
	    			c1.setQauntity(newQauntity);
        			errorMsg.setText(null);
		    	}
		    	else {
		    		errorMsg.setText("You reach the maximum qauntity for this product.");
		    	}
    			break;
    		}
    	}
    	if(flag != 1)
    		arrCart.add(c);
    	initCart(arrCart);
	}
	/**
	 * This method represents the cart.
	 * For each product in the cart the following details will be displayed: the product's name, the product's image, the product's price per item,
	 * 																										and the selected amount of the product.
	 * For each item in the cart there would be 3 buttons: + to increase the amount of the product in the cart
	 * 													   - to decrease the amount of the product in the cart
	 * 													   X to remove the product from the cart altogether
	 * The user can increase the amount of a certain product in the cart as long as there is enough of that product in the facility's inventory.
	 * Once the user select all the available items of a certain product in the inventory, an appropriate error message will appear on the screen.
	 * @param arr
	 */
	public void initCart(ArrayList<Cart> arr) {
		arrCart = arr;
		myCartGridPane.getChildren().clear();
		int row = 0;
		for(Cart c : arrCart) {
	    	HBox carthbox = new HBox();
	    	String[] scrImage = c.getImgSrc().split("/"); ///////////////////////////////////////////////////////////////////////////////////////////////////
    		String finalSrcImg = scrImage[1] + "/" + scrImage[2];
    		Image img = new Image(finalSrcImg);
	    	ImageView CartimgView = new ImageView(img);
	    	CartimgView.setFitHeight(35);
	    	CartimgView.setFitWidth(35);
	    	carthbox.getChildren().add(CartimgView);
	    	
	    	TextField CartproductName = new TextField();
	    	CartproductName.setStyle("-fx-background-color: #f4f4f4; -fx-font-size: 14; -fx-font-family: Rockwell; -fx-max-height: 5; -fx-max-width: 110");
	    	CartproductName.setText(c.getProductName());
	    	//CartproductName.setDisable(true);
	    	TextField CartproductPrice = new TextField();
	    	CartproductPrice.setStyle("-fx-background-color: #f4f4f4; -fx-font-size: 14; -fx-font-family: Rockwell; -fx-max-height: 5; -fx-max-width: 110; -fx-min-width: 110");
	    	CartproductPrice.setText(c.getProductPrice() + "$ per item");
	    	//CartproductPrice.setDisable(true);
	    	VBox cartvbox = new VBox();
	    	cartvbox.getChildren().add(CartproductName);
	    	cartvbox.getChildren().add(CartproductPrice);
	    	carthbox.getChildren().add(cartvbox);
	    	
	    	Button Minusbtn = new Button("-");
	    	Minusbtn.setStyle("-fx-font-size: 14; -fx-background-color: #94b5fe; -fx-font-family: Rockwell; -fx-min-width: 30; -fx-max-height: 8");
	    	Minusbtn.setOnAction(new EventHandler<ActionEvent>() {
    		    public void handle(ActionEvent event) {
        	    	timer.resetTimer();
        	    	timer.startTimer();
    		    	if(Integer.parseInt(c.getQauntity()) > 1) {
    		    		String newQauntity = String.valueOf(Integer.parseInt(c.getQauntity()) - Integer.parseInt("1"));
            			String newTotalPrice = String.valueOf(Double.parseDouble(c.getTotalPerProduct()) - Double.parseDouble(c.getProductPrice()));
            			c.setTotalPerProduct(newTotalPrice);
            			c.setQauntity(newQauntity);
            			//calculatorTotalPrice();
            			initCart(arrCart);
            			errorMsg.setText(null);
    		    	}
    		    	else
    		    		errorMsg.setText("Minimum qauntity is 1.");
    		    }
    		});
	    	carthbox.getChildren().add(Minusbtn);
	    	
	    	TextField txtAmount = new TextField();
	    	txtAmount.setText(c.getQauntity());
	    	txtAmount.setStyle("-fx-font-size: 14; -fx-font-family: Rockwell; -fx-min-height: 8; -fx-max-width: 32; -fx-min-width: 32");
	    	carthbox.getChildren().add(txtAmount);
	    	
	    	Button Plusbtn = new Button("+");
	    	Plusbtn.setStyle("-fx-font-size: 14; -fx-background-color: #94b5fe; -fx-font-family: Rockwell; -fx-min-width: 30; -fx-max-height: 8");
	    	Plusbtn.setOnAction(new EventHandler<ActionEvent>() {
    		    public void handle(ActionEvent event) {
        	    	timer.resetTimer();
        	    	timer.startTimer();
    		    	int flag = 0;
    		    	if(ChatClient.user.getSupplyMethod().equals("delivery")) { ////////////////////////////////?????????????????//////////////////////
    		    		flag=1;
    		    	}
    		    	else {
    		    		ClientUI.chat.accept("CheckQuantity " + ChatClient.user.getNote() + " " + c.getProductName());
    		    		if(Integer.parseInt(ChatClient.answerMsg) > Integer.parseInt(c.getQauntity())) {
    		    			flag=1;
    		    		}
    		    	}
    		    	if(flag == 1) {
    		    		String newQauntity = String.valueOf(Integer.parseInt(c.getQauntity()) + Integer.parseInt("1"));
            			String newTotalPrice = String.valueOf(Double.parseDouble(c.getTotalPerProduct()) + Double.parseDouble(c.getProductPrice()));
            			c.setTotalPerProduct(newTotalPrice);
            			c.setQauntity(newQauntity);
            			//calculatorTotalPrice();
            			initCart(arrCart);
            			errorMsg.setText(null);
    		    	}
    		    	else {
    		    		errorMsg.setText("You reach the maximum qauntity for this product.");
    		    	}
    		    }
    		});
	    	carthbox.getChildren().add(Plusbtn);
	    	
	    	Button deletebtn = new Button("X");
	    	deletebtn.setStyle("-fx-background-color: #122753; -fx-text-fill: #ffffff; -fx-min-width: 30; -fx-min-height: 10");
	    	deletebtn.setOnAction(new EventHandler<ActionEvent>() {
    		    public void handle(ActionEvent event) {
        	    	timer.resetTimer();
        	    	timer.startTimer();
    		    	errorMsg.setText(null);
    		    	deleteProductFromCart(c);
    		    	System.out.println(arrCart);
        			initCart(arrCart);
    		    }
    		});
	    	carthbox.getChildren().add(deletebtn);
	    	
	    	myCartGridPane.addRow(row, carthbox);
	    	row++;
		}
		calculatorTotalPrice();
	}
	/**
	 * This method deletes a specific product from the cart.
	 * @param c
	 */
	public void deleteProductFromCart(Cart c) {
    	int flag = 0;
    	Cart cToRemove = null;
    	for(Cart c1: arrCart) {
    		if(c1.getProductName().equals(c.getProductName())) {
    			cToRemove = c1;
    			flag = 1;
    			break;
    		}
    	}
    	if(flag == 1)
    		arrCart.remove(cToRemove);
	}
	/**
	 * This method calculates the total price of all the chosen products that are currently in the cart.
	 * In case this is the first order of a client after upgrading their account to subscriber, the total price of the order will be 
	 * calculated with 20% discount and the client will see the price before the discount, as well as the price they will actually pay.
	 */
    public void calculatorTotalPrice() {
    	double total = 0;
    	for (Cart c : arrCart)
    		total = total + Double.parseDouble(c.getTotalPerProduct());
    	if(discount == 1) {
    		insteadOfMsg.setText("instead of: " + df.format(total) + "$");
    		total = total * 0.8;
    	}
    	if(total == 0) {
    		txtTotalPrice.setText(null);
    		insteadOfMsg.setText(null);
    	}
    	else
    		txtTotalPrice.setText(String.valueOf(df.format(total)) + "$");
    }
    /**
     * This method initiates the menu once the user opens the "Menu" window
     * @throws InterruptedException 
     */
    public void start() throws InterruptedException {
		ClientUI.chat.accept("showProductInLocation Show-All " + ChatClient.user.getNote() + " " + ChatClient.user.getStatus() + " " + ChatClient.user.getSupplyMethod());
		Thread.sleep(10);
		if(ChatClient.arrProduct.get(0).getProductName().equals("Error")) {
			System.out.println("No products found.");
		}
		else
			getProducts(ChatClient.arrProduct);
    }
    /**
     * This method deletes all the product the user chose so far, from the cart.
     * @param event
     */
    @FXML
    void getDeleteOrderbtn(ActionEvent event) {
    	timer.resetTimer();
    	timer.startTimer();
    	popUpWindow();
    	/*myCartGridPane.getChildren().clear();
    	txtTotalPrice.setText(null);
    	insteadOfMsg.setText(null);
    	arrCart = new ArrayList<Cart>();*/
    }
    
    private void popUpWindow() {
		Scene popUp = new Scene(initPane(), 300, 155);
		Stage popUpStage = new Stage();
		popUpStage.setTitle("Delete Order?");
		popUpStage.initStyle(StageStyle.UNDECORATED);
		popUpStage.setScene(popUp);
		popUpStage.show();
	}
    
    private Pane initPane() {
		Pane pane = new Pane();
		pane.setStyle("-fx-background-color:  #ffffff; -fx-border-color:  #000000;");
		
		Label label = new Label("Are you sure?");
		label.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16;");
		label.setLayoutX(95);
		label.setLayoutY(48);
		
		Button yes = new Button("Yes, Delete");
		yes.setStyle("-fx-background-color: #94b5fe; -fx-font-family: Rockwell; -fx-font-size: 14;");
		yes.setLayoutX(53);
		yes.setLayoutY(94);
		yes.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
    	    	timer.resetTimer();
    	    	timer.startTimer();
				myCartGridPane.getChildren().clear();
		    	txtTotalPrice.setText(null);
		    	insteadOfMsg.setText(null);
		    	arrCart = new ArrayList<Cart>();
				((Node)event.getSource()).getScene().getWindow().hide();
			}
		});
		
		Button no = new Button("No, Keep It");
		no.setStyle("-fx-background-color: #94b5fe; -fx-font-family: Rockwell; -fx-font-size: 14;");
		no.setLayoutX(155);
		no.setLayoutY(94);
		no.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
    	    	timer.resetTimer();
    	    	timer.startTimer();
				((Node)event.getSource()).getScene().getWindow().hide();
			}
		});
		pane.getChildren().addAll(label, yes, no);
		return pane;
	}
    
    /**
     * This method closes the current window ("Menu") and opens the window "Payment".
     * In case of an error, or if the user haven't chose a single product, an appropriate error message will appear on the screen
     * 							(and the user will stay on the "Menu" screen and will not be able to pass to the "Payment" screen).
     * @param event
     * @throws IOException
     */
    @FXML
    void getGotoPaymentbtn(ActionEvent event) throws IOException {
    	timer.resetTimer();
    	timer.startTimer();
    	if(arrCart.isEmpty() || arrCart == null)
    		errorMsg.setText("You have to select at least one product first!");
    	else {
    		ClientUI.chat.accept("getDetailsForPaymentFields " + ChatClient.user.getStatus() + " " + ChatClient.user.getId());
        	if(ChatClient.answerMsg.equals("Error")) {
        		errorMsg.setText("Somthing went worng :(");
    			return;
        	}
        	String[] result = ChatClient.answerMsg.split("\\,");
    		((Node)event.getSource()).getScene().getWindow().hide();
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/Payment.fxml"));
        	Parent root = (Parent) loader.load();
        	PaymentController control = loader.getController();
        	control.initArrCart(arrCart);
        	control.setTotalPrice(txtTotalPrice.getText().split("\\$"));
        	control.setFields(result[0], result[1], result[2], result[3]);
        	control.setDetailsUserForDelivery(address, phone);
    		Stage primaryStage = new Stage();
    		Scene scene = new Scene(root);
    		primaryStage.setTitle("Payment");
    		primaryStage.initStyle(StageStyle.UNDECORATED);
    		primaryStage.setScene(scene);		
    		primaryStage.show();
    		control.setTimer(primaryStage);
    	}
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
     * This method sets the client's details in case of a distant order for delivery.
     * @param fullAddress - the address the client entered (where to send the order to).
     * @param phoneNumber - the client's phone number
     */
	public void setDetailsUserForDelivery(String fullAddress, String phoneNumber) {
		address = fullAddress;
		phone = phoneNumber;
	}
	
	public void setTimer(Stage primaryStage) {
		timer.setTimer(primaryStage, ChatClient.user.getId());
		timer.startTimer();
	}
}
