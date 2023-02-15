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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.Sales;

/**
 * This class allows the marketing worker to apply a new sale in their area, according to the marketing manage initiation.
 * Also from the window this class represents, the marketing worker can go back to their home page, or exit the client side of the system altogether.
 */
public class ApplySaleController {

    @FXML
    private Button Exitbtn;

    @FXML
    private Button ApplyDiscountbtn;
    
    @FXML
    private Button Backbtn;

    @FXML
    private TableView<Sales> DiscountsTable;
    
    @FXML
    private TableColumn<Sales, String> Product;

    @FXML
    private TableColumn<Sales, String> Area;
    
    @FXML
    private TableColumn<Sales, String> StartDate;

    @FXML
    private TableColumn<Sales, String> EndDate;
    
    @FXML
    private TableColumn<Sales, String> OriginalPrice;
    
    @FXML
    private TableColumn<Sales, String> NewPrice;
    
    @FXML
    private TableColumn<Sales, String> saleID;
    
    @FXML
    private Text errorMsg;
    
    private String costumerID = ChatClient.user.getId();
    private String area = ChatClient.user.getNote();
    
    /**
     * This method initializes the table that shows on the screen.
     */
    public void initialize() {
    	saleID.setCellValueFactory(new PropertyValueFactory<Sales, String>("saleID"));
    	Product.setCellValueFactory(new PropertyValueFactory<Sales, String>("product"));
    	Area.setCellValueFactory(new PropertyValueFactory<Sales, String>("area"));
    	StartDate.setCellValueFactory(new PropertyValueFactory<Sales, String>("startDate"));
    	EndDate.setCellValueFactory(new PropertyValueFactory<Sales, String>("endDate"));
    	OriginalPrice.setCellValueFactory(new PropertyValueFactory<Sales, String>("originalPrice"));
    	NewPrice.setCellValueFactory(new PropertyValueFactory<Sales, String>("newPrice"));
		ObservableList<Sales> returnList = getSales();
		if (!returnList.isEmpty()) {
			DiscountsTable.setItems(returnList);
			DiscountsTable.refresh();
		}
    }
    /**
     * This method fills the table with the data from the data base about available sales for the marketing worker to choose from.
     * @return
     */
    public ObservableList<Sales> getSales() {
    	ObservableList<Sales> sales = FXCollections.observableArrayList();
		ClientUI.chat.accept("getAvailableDiscounts " + area);
		if (!ChatClient.answerMsg.equals("Empty")) {
			String[] result = ChatClient.answerMsg.split("\\,");
			for (int i = 0; i < result.length; i += 7)
				sales.add(new Sales(result[i], result[i + 1], result[i + 2], result[i + 3].replace("_", " "), result[i + 4].replace("_", " "), 
						result[i + 5], result[i + 6]));
		}
		return sales;
    }
    /**
     * This method applies a new sale in the area that the marketing worker is in charge of.
     * In case of any error, an appropriate error message will appear on the screen,
     * In case of success, a pop up window will appear on the screen to let the marketing worker know that the sale applied successfully.
     * @param event
     */
    @FXML
    void getApplyDiscountbtn(ActionEvent event) {
    	Sales s = DiscountsTable.getSelectionModel().getSelectedItem();
		if (s == null) {
			errorMsg.setText("Please choose a sale.");
			return;
		}
		ClientUI.chat.accept("applyDiscount " + s.getSaleID());
		switch (ChatClient.answerMsg) {
		case "Error":
			errorMsg.setText("Failed to apply discount.");
			break;
		case "Change":
			ObservableList<Sales> returnList = getSales();
			if (returnList.isEmpty()) {
				DiscountsTable.getItems().clear();
			} else {
				DiscountsTable.setItems(returnList);
			}
			DiscountsTable.refresh();
			popUpWindow();
			break;
		}
    }
    /**
	* This method opens a pop up window, that lets the marketing worker know that they applied the sale successfully.
	*/
    private void popUpWindow() {
		Scene popUp = new Scene(initPane(), 370, 155);
		Stage popUpStage = new Stage();
		popUpStage.setTitle("Sale Apply");
		popUpStage.initStyle(StageStyle.UNDECORATED);
		popUpStage.setScene(popUp);
		popUpStage.show();
	}
    /**
	* This method sets the pop up window that lets the marketing worker know that they applied the sale successfully.
	* @return the pop up window
	*/
	private Pane initPane() {
	  	Pane pane = new Pane();
	  	pane.setStyle("-fx-background-color: #94b5fe; -fx-border-color: #000000;");
		
		Label label = new Label("Sale apply successfully.");
		label.setStyle("-fx-font-family: Rockwell; -fx-font-size: 18;");
		label.setLayoutX(62);
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
	* This method closes the current window ("Apply Sale") and opens the window "Marketing Worker".
	* @param event
	* @throws IOException
	*/ 
	@FXML
	void getBackbtn(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/MarketingWorker.fxml"));
    	Parent root = (Parent) loader.load();
    	MarketingWorkerController control = loader.getController();
    	control.loadDetails(ChatClient.user);
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Marketing Worker");
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
