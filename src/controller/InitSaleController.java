package controller;

import java.io.IOException;
import java.util.ArrayList;
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
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.Product;

/**
 * This class allows the marketing manager to initiate a new sale in their area on a certain product.
 * Also from the window this class represents, the marketing manager can go back to their home page, or exit the client side of the system altogether.
 */
public class InitSaleController {
	
	@FXML
	private DatePicker EndDatePicker;
	
	@FXML
	private DatePicker StartDatePicker;
	
    @FXML
    private Button Exitbtn;
    
    @FXML
    private Button Backbtn;
    
    @FXML
    private Button InitSalebtn;

    @FXML
    private ComboBox<String> SelectFacility;

    @FXML
    private ComboBox<String> SelectProduct;
    
    @FXML
    private Text errorMsg;

    @FXML
    private TextField txtCurPrice;

    @FXML
    private TextField txtDiscount;

    @FXML
    private TextField txtEndDate;

    @FXML
    private TextField txtEndTime;

    @FXML
    private TextField txtStartDate;

    @FXML
    private TextField txtStartTime;
    
    private String costumerID = ChatClient.user.getId();
    ArrayList<Product> products;

    /**
	* This method closes the current window ("Init Sale") and opens the window "Marketing Manager".
	* @param event
	* @throws IOException
	*/
    @FXML
    void getBackbtn(ActionEvent event) throws IOException {
    	((Node)event.getSource()).getScene().getWindow().hide();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/MarketingManager.fxml"));
    	Parent root = (Parent) loader.load();
    	MarketingManagerController control = loader.getController();
    	control.loadDetails(ChatClient.user);
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Marketing Manager");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);		
		primaryStage.show();
    }

    @FXML
    void getSelectProduct(ActionEvent event) {
    	String s = SelectProduct.getSelectionModel().getSelectedItem().toString();
		for (Product p : products) {
			if (p.getProductName().equals(s)) {
				txtCurPrice.setText(p.getProductPrice());
			}
		}
    }

	@FXML
	void getInitSalebtn(ActionEvent event) {
		// check empty fields
		if (SelectProduct.getValue() == null || SelectFacility.getValue() == null
				|| txtDiscount.getText().trim().isEmpty() || EndDatePicker.getValue().toString().trim().isEmpty()
				|| txtEndTime.getText().trim().isEmpty() || StartDatePicker.getValue().toString().trim().isEmpty()
				|| txtStartTime.getText().trim().isEmpty()) {
			errorMsg.setText("Error - You must fill all mandatory fields");
			return;
		}

		// check hours format
		String startHour = txtStartTime.getText();
		String endHour = txtEndTime.getText();
		if (startHour.length() != 5 || endHour.length() != 5 || startHour.charAt(2) != ':'
				|| endHour.charAt(2) != ':') {
			errorMsg.setText("Error - Incorrect hour format");
			return;
		}
		for (int i = 0; i < startHour.length(); i++) {
			if (i == 2)
				continue;
			if ((int) startHour.charAt(i) < 48 || (int) startHour.charAt(i) > 57 || (int) endHour.charAt(i) < 48
					|| (int) endHour.charAt(i) > 57) {
				errorMsg.setText("Error - Incorrect hour format");
				return;
			}
		}
		int h1 = Integer.parseInt("" + startHour.charAt(0) + startHour.charAt(1));
		int h2 = Integer.parseInt("" + endHour.charAt(0) + endHour.charAt(1));
		int m1 = Integer.parseInt("" + startHour.charAt(3) + startHour.charAt(4));
		int m2 = Integer.parseInt("" + endHour.charAt(3) + endHour.charAt(4));
		if (h1 > 23 || h1 < 0 || h2 > 23 || h2 < 0 || m1 > 59 || m1 < 0 || m2 > 59 || m2 < 0) {
			errorMsg.setText("Error - Incorrect hour format");
			return;
		}

		// check start date isn't after the end date
		if (StartDatePicker.getValue().isAfter(EndDatePicker.getValue())) {
			errorMsg.setText("Error - Start date must be before End date");
			return;
		}

		// if start date and end date are the same day, check hours
		if (StartDatePicker.getValue().isEqual(EndDatePicker.getValue())) {
			if (h1 > h2) {
				errorMsg.setText("Error - Start hour must be before end hour");
				return;
			}
			if (h1 == h2) {
				if (m1 > m2) {
					errorMsg.setText("Error - Start hour must be before end hour");
					return;
				}
			}
		}
		
		// if the sale price is higher than current price
		if (Double.parseDouble(txtDiscount.getText()) > Double.parseDouble(txtCurPrice.getText())) {
			errorMsg.setText("Error - Sale price cannot be higher than current price");
			return;
		}
		String s = SelectProduct.getSelectionModel().getSelectedItem().toString();
		Product p = null;
		for(Product product : products) {
			if(product.getProductName().equals(s)) {
				p = product;
			}
		}
		String startDate = StartDatePicker.getValue().toString() + "_" + txtStartTime.getText();
		String endDate = EndDatePicker.getValue().toString() + "_" + txtEndTime.getText();
		ClientUI.chat.accept("InitSale " + p.getProductID() + " " + p.getProductName() + " " + 
		SelectFacility.getSelectionModel().getSelectedItem().toString() + " " + startDate + " " +
				endDate + " " + p.getProductPrice() + " " + txtDiscount.getText());
		switch(ChatClient.answerMsg) {
		case "OK":
			errorMsg.setText("Sale added successfully");
			break;
		case "Error":
			errorMsg.setText("Update error");
			break;
		case "Exist":
			errorMsg.setText("Item is already on sale!");
			break;
		default:
			break;
		}
	}
    
	public void initialize() {
		ClientUI.chat.accept("showProductInLocation Show-All haifa x onSite");
		products = ChatClient.arrProduct;
		ObservableList<String> productNames = FXCollections.observableArrayList();
		for (Product p : products) {
			productNames.add(p.getProductName());
		}
		SelectProduct.setItems(productNames);
		ObservableList<String> facilities = FXCollections.observableArrayList("north", "south", "uae");
		SelectFacility.setItems(facilities);
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
