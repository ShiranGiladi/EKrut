package controller;

import java.io.IOException;
import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class allows the user to collect an order they made earlier through the E-Krut application.
 * Also, from the window this class represents, the user can go back to their home page, or exit the client side of the system altogether.
 * @author Debbie
 *
 */
public class CollectYourOrderController {

    @FXML
    private Button Exitbtn;

    @FXML
    private AnchorPane anchorPaneOrder;

    @FXML
    private Button backBtn;

    @FXML
    private Button collectBtn;

    @FXML
    private GridPane gridPaneOrder;

    @FXML
    private Label labelTotalPrice;

    @FXML
    private Button okBtn;

    @FXML
    private ScrollPane scrollOrder;

    @FXML
    private TextField txtOrderCode;

    @FXML
    private TextField txtTotalPrice;

    @FXML
    private Text errorMsg;
    
    private String costumerID = ChatClient.user.getId();

    /**
	* This method closes the current window ("Collect Your Order") and opens the window "On Site Client".
	* @param event
	* @throws IOException
	*/ 
    @FXML
    void getBackBtn(ActionEvent event) throws IOException {
    	((Node)event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/OnSiteClient.fxml"));
    	Parent root = (Parent) loader.load();
    	OnSiteClientController control = loader.getController();
    	control.loadDetails(ChatClient.user);
		Scene scene = new Scene(root);
		primaryStage.setTitle("On Site Client");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.show();
    }
    /**
	 * This method is for a button that the client clicks on to collect their order.
	 * If the order already have been collected, an appropriate error message will appear on the screen.
	 * Otherwise, a pop up window will appear on the screen to let the client know that they successfully collected their order.
	 * @param event
	 */
    @FXML
    void getCollectBtn(ActionEvent event) {
    	ClientUI.chat.accept("setPickUpOrderToDone " + txtOrderCode.getText());
    	switch(ChatClient.answerMsg) {
    	case "Error":
    		errorMsg.setText("Error, order already collected");
    		break;
    	case "Done":
    		popUpWindow();
    		break;
    	default:
    		break;
    	}
    }
    /**
	* This method opens a pop up window, that lets the client know that they successfully collected their order.
	*/
    private void popUpWindow() {
		Scene popUp = new Scene(initPane(), 370, 155);
		Stage popUpStage = new Stage();
		popUpStage.setTitle("Order collected");
		popUpStage.initStyle(StageStyle.UNDECORATED);
		popUpStage.setScene(popUp);
		popUpStage.show();
	}
    /**
   	* This method sets the pop up window that lets the client know that they successfully collected their order.
   	* @return the pop up window
   	*/
    private Pane initPane() {
		Pane pane = new Pane();
		pane.setStyle("-fx-background-color:  #94b5fe; -fx-border-color:  #000000;");
		
		Label label = new Label("Order is collected successfully.");
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
	 * This method is for a button that the client clicks on after entering their order code.
	 * In case the client didn't enter a code or any other error, an appropriate error message will appear on the screen.
	 * In case of success, the details of the client's order will appear on the screen.
	 * @param event
	 */
    @FXML
    void getOkBtn(ActionEvent event) {
    	errorMsg.setText(null);
    	if(txtOrderCode.getText().trim().isEmpty())
    		errorMsg.setText("You must enter your order code");
    	else {
    		ClientUI.chat.accept("getOrderDetailsForUser " + ChatClient.user.getId() + " pick_up " + txtOrderCode.getText() + " " + ChatClient.user.getNote());
    		if(ChatClient.answerMsg.equals("Empty")) {
    			errorMsg.setText("Order Code Not Found!");
    			start();
    		}
    		else {
    			collectBtn.setDisable(false);
    	    	anchorPaneOrder.setVisible(true);
    	    	gridPaneOrder.setVisible(true);
    	    	labelTotalPrice.setVisible(true);
    	    	scrollOrder.setVisible(true);
    	    	txtTotalPrice.setVisible(true);
    	    	showOrderDetails(ChatClient.answerMsg);
    		}
    	}
    }
    /**
     * This method shows the details of the order to the client.
     * For each item in the order it displays: the name of the product and the selected quantity 
     * @param details
     */
    private void showOrderDetails(String details) {
    	String[] result = details.split("\\&");
    	txtTotalPrice.setText(result[0] + "$");
    	String[] productsAndQuantity = result[1].split("\\,");
    	int row = 0;
    	Text titleName = new Text("  Product Name");
    	Text titlePrice = new Text("Quantity");
    	gridPaneOrder.add(titleName, 0, row);
    	gridPaneOrder.add(titlePrice, 2, row);
    	row++;
    	for(int i=0; i<productsAndQuantity.length; i+=2) {
    		Text txtProductName = new Text("  " + productsAndQuantity[i]);
    		Text dots = new Text("..............................");
    		Text txtProductPrice = new Text(productsAndQuantity[i+1]);
    		gridPaneOrder.add(txtProductName, 0, row);
    		gridPaneOrder.add(dots, 1, row);
    		gridPaneOrder.add(txtProductPrice, 2, row);
    		row++;
    	}
    }
    /**
     * This method initiates the state of the different components in the window once the window opens.
     */
    public void start() {
    	collectBtn.setDisable(true);
    	anchorPaneOrder.setVisible(false);
    	gridPaneOrder.setVisible(false);
    	labelTotalPrice.setVisible(false);
    	scrollOrder.setVisible(false);
    	txtTotalPrice.setVisible(false);
    }
}
