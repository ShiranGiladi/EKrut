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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.User;

/**
 * This class allows all types of users to log in to the system.
 * If the user entered all the correct data (and chose a format,  OL/EK) that is available for them, a "home page" according to 
 * 							  		the type and status of the user will open (and the window this class represents will close).
 * Also from the window this class represents, it is possible to exit the client side of system altogether.
 */
public class WelcomeToEKrutLoginController {

    @FXML
    private Button Exitbtn;
    
    @FXML
    private Button Backbtn;

    @FXML
    private Button Loginbtn;
    
    @FXML
    private Button touchSimulationBtn;
    
    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;
    
    @FXML
    private Text errorMsg;
    
    private String format = "", facility, errorMsgForTesting = "";
    private User user;
    
    public IClientServerCommunication clientServerCommunication = new ClientServerCommunication();

	class ClientServerCommunication implements IClientServerCommunication {
		public void sendToServer(Object msg) {
			ClientUI.chat.accept((String) msg);
		}

		public Object getServerMsg() {
			user = ChatClient.user;
			return (User)user;
		}
	}
	
	/**check if the user name field or the password field is empty**/
	public boolean checkEmpty(String UserNameField, String PasswordField) {
		if (UserNameField.trim().isEmpty() || PasswordField.trim().isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**check if the user name field or the password field is incorrect**/
	public boolean checkUserdetails(String UserFirstNameField) {
		if (UserFirstNameField.equals("Error"))
			return true;
		return false;
	}
	
	public void setClientServerCommunication(IClientServerCommunication clientServerCommunication){
		this.clientServerCommunication = clientServerCommunication;
	}
	
    /**
	* This method closes entirely the client side of the system.
	* @param event
	*/
    @FXML
    void getExitbtn(ActionEvent event) {
		((Node)event.getSource()).getScene().getWindow().hide();
    }
    /**
	* This method closes the current window ("Welcome To E-Krut - Login") and opens the window "OL EK".
	* @param event
	* @throws IOException
	*/ 
    @FXML
    void getBackbtn(ActionEvent event) throws IOException{
    	((Node)event.getSource()).getScene().getWindow().hide();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/OL_EK.fxml"));
    	Parent root = (Parent) loader.load();
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("OL EK");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);		
		primaryStage.show();
    }
    
    @FXML
    void getLoginbtn(ActionEvent event) throws IOException {
		String username = txtUsername.getText(), password = txtPassword.getText();
		if(checkUsernameAndPassword(username, password)) {
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			ChatClient.user.setSystemFormat(format);
			ChatClient.user.setUsername(txtUsername.getText());
			ChatClient.user.setPassword(txtPassword.getText());
			openUserWindow();
		}
    }
    /**
     * This method logs in the user.
     * If the user didn't filled one of the fields - user name or password - an appropriate error message will appear on the screen.
     * If the user filled incorrect data (not corresponding the data in the data base) - an appropriate error message will appear on the screen.
     * If the user tries to connect with a format (OL/EK) that is not available for them - an appropriate error message will appear on the screen.
     * Otherwise the current window ("Welcome To E-Krut") will close and a "home page" will open, according to the type and status of the user.
     * 													(if the user is a client - the home page depends on the chosen format - EK / OL,
     * 													 if the user is an employee - the home page depends on the job title of the employee).
     * @param event
     * @throws IOException
     */
    public boolean checkUsernameAndPassword(String username, String password) throws IOException {
    	if(username.trim().isEmpty() || password.trim().isEmpty()) {
    		try {
    			errorMsg.setText("You must enter username and password");
    		} catch(Exception e) {}
			//errorMsg.setText("You must enter username and password");
    		errorMsgForTesting = "You must enter username and password";
			return false;
		}
		else{
			//ClientUI.chat.accept("login " + username + " " + password);
			clientServerCommunication.sendToServer("login " + username + " " + password);
			user = (User) clientServerCommunication.getServerMsg();
			if(user.getFirstName().equals("Error")) {
				try {
	    			errorMsg.setText("ERROR - username or password incorrect");
	    		} catch(Exception e) {}
				//errorMsg.setText("ERROR - username or password incorrect");
				errorMsgForTesting = "ERROR - username or password incorrect";
				return false;
			}
			else if(user.getIsLogin().equals("1")) {
				try {
	    			errorMsg.setText("ERROR - username already login");
	    		} catch(Exception e) {}
				//errorMsg.setText("ERROR - username already login");
				errorMsgForTesting = "ERROR - username already login";
				return false;
			}
			else if(user.getStatus().equals("notApprovedClient")){
				//ClientUI.chat.accept("logout " + ChatClient.user.getId());
				clientServerCommunication.sendToServer("logout " + ChatClient.user.getId());
				try {
	    			errorMsg.setText("If you want to enjoy EKrut service, register as a customer!");
	    		} catch(Exception e) {}
				//errorMsg.setText("If you want to enjoy EKrut service, register as a customer!");
				errorMsgForTesting = "If you want to enjoy EKrut service, register as a customer!";
				return false;
				
			}
			else if(!user.getJob().equals("Client") && format.equals("EK")) {
				//ClientUI.chat.accept("logout " + ChatClient.user.getId());
				clientServerCommunication.sendToServer("logout " + ChatClient.user.getId());
				try {
	    			errorMsg.setText("ERROR - only client account can be used in EK format");
	    		} catch(Exception e) {}
				//errorMsg.setText("ERROR - only client account can be used in EK format");
				errorMsgForTesting = "ERROR - only client account can be used in EK format";
				return false;
			}
			else { 
				return true;
			}
		}
    }

    
    /**
     * This method opens a "home page" according to the type and status of the user:
     * 						- if the user is a client - the home page depends on the chosen format - EK/OL.
     * 						- if the user is an employee - the home page depends on the job title of the employee.
     * @throws IOException
     */
    public void openUserWindow() throws IOException {
    	Stage primaryStage = new Stage();
		switch(ChatClient.user.getJob()) {
		case "AreaManager":
			FXMLLoader loaderAM = new FXMLLoader(getClass().getResource("/GUI/AreaManager.fxml"));
	    	Parent rootAM = (Parent) loaderAM.load();
	    	AreaManagerController controlAM = loaderAM.getController();
	    	controlAM.loadDetails(ChatClient.user);
			Scene sceneAM = new Scene(rootAM);
			primaryStage.setScene(sceneAM);	
			primaryStage.setTitle("Area Manager");
			break;
		case "CEO":
			FXMLLoader loaderCEO = new FXMLLoader(getClass().getResource("/GUI/CEO.fxml"));
	    	Parent rootCEO = (Parent) loaderCEO.load();
	    	CEOController controlCEO = loaderCEO.getController();
	    	controlCEO.loadDetails(ChatClient.user);
			Scene sceneCEO = new Scene(rootCEO);
			primaryStage.setScene(sceneCEO);
			primaryStage.setTitle("CEO");
			break;
		case "ServiceRepresentative":
			FXMLLoader loaderSR = new FXMLLoader(getClass().getResource("/GUI/ServiceRepresentative.fxml"));
	    	Parent rootSR = (Parent) loaderSR.load();
	    	ServiceRepresentativeController controlSR = loaderSR.getController();
	    	controlSR.loadDetails(ChatClient.user);
			Scene sceneSR = new Scene(rootSR);
			primaryStage.setScene(sceneSR);
			primaryStage.setTitle("Service Representative");
			break;
		case "Client":
			if(format.equals("EK")) {
				FXMLLoader loaderOSC = new FXMLLoader(getClass().getResource("/GUI/OnSiteClient.fxml"));
		    	Parent rootOSC = (Parent) loaderOSC.load();
		    	OnSiteClientController controlOSC = loaderOSC.getController();
				controlOSC.loadDetails(ChatClient.user);
				ChatClient.user.setSupplyMethod("onSite");
				ChatClient.user.setNote(facility);
				if(userSubscriberAndFirstPurchase(ChatClient.user.getUsername(), ChatClient.user.getPassword())) {
					controlOSC.initDiscountMsg();
				}
				Scene sceneOSC = new Scene(rootOSC);
				primaryStage.setScene(sceneOSC);
				primaryStage.setTitle("On Site Client");
			}
			else {
				FXMLLoader loaderDC = new FXMLLoader(getClass().getResource("/GUI/DistantClient.fxml"));
		    	Parent rootDC = (Parent) loaderDC.load();
		    	DistantClientController controlDC = loaderDC.getController();
		    	controlDC.loadDetails(ChatClient.user);
		    	if(userSubscriberAndFirstPurchase(ChatClient.user.getUsername(), ChatClient.user.getPassword())) {
		    		controlDC.initDiscountMsg();
				}
				Scene sceneDC = new Scene(rootDC);
				primaryStage.setScene(sceneDC);
				primaryStage.setTitle("Distant Client");
			}
			break;
		case "DeliveriesOperator":
			FXMLLoader loaderDM = new FXMLLoader(getClass().getResource("/GUI/DeliveriesOperator.fxml"));
	    	Parent rootDM = (Parent) loaderDM.load();
	    	DeliveriesOperatorController controlDM = loaderDM.getController();
	    	controlDM.loadDetails(ChatClient.user);
			Scene sceneDM = new Scene(rootDM);
			primaryStage.setScene(sceneDM);
			primaryStage.setTitle("Deliveries Operator");
			break;
		case "MarketingManager":
			FXMLLoader loaderMM = new FXMLLoader(getClass().getResource("/GUI/MarketingManager.fxml"));
	    	Parent rootMM = (Parent) loaderMM.load();
	    	MarketingManagerController controlMM = loaderMM.getController();
	    	controlMM.loadDetails(ChatClient.user);
			Scene sceneMM = new Scene(rootMM);
			primaryStage.setScene(sceneMM);
			primaryStage.setTitle("Marketing Manager");
			break;
		case "MarketingWorker":
			FXMLLoader loaderMW = new FXMLLoader(getClass().getResource("/GUI/MarketingWorker.fxml"));
	    	Parent rootMW = (Parent) loaderMW.load();
	    	MarketingWorkerController controlMW = loaderMW.getController();
	    	controlMW.loadDetails(ChatClient.user);
			Scene sceneMW = new Scene(rootMW);
			primaryStage.setScene(sceneMW);
			primaryStage.setTitle("Marketing Worker");
			break;
		case "OperationWorker":
			FXMLLoader loaderOW = new FXMLLoader(getClass().getResource("/GUI/OperationWorker.fxml"));
	    	Parent rootOW = (Parent) loaderOW.load();
	    	OperationWorkerController controlOW = loaderOW.getController();
	    	controlOW.loadDetails(ChatClient.user);
			Scene sceneOW = new Scene(rootOW);
			primaryStage.setScene(sceneOW);
			primaryStage.setTitle("Operation Worker");
			break;
		default:
			break;
		}
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.show();
    }
    
    private boolean userSubscriberAndFirstPurchase(String username, String password) {
    	if(ChatClient.user.getStatus().equals("Subscriber_Client")) {
    		ClientUI.chat.accept("purchasesNumber " + username + " " + password);
        	if(ChatClient.answerMsg.equals("0"))
        		return true;
    	}
		return false;
	}
    
    @FXML
    void getTouchSimulationBtn(ActionEvent event) throws IOException {
    	ClientUI.chat.accept("setSubscriberForTouchSimulation");
    	if(!ChatClient.answerMsg.equals("Error")) {
    		String[] result = ChatClient.answerMsg.split("\\,");
    		ChatClient.user.setId(result[0]);
    		ChatClient.user.setUsername(result[1]);
    		ChatClient.user.setPassword(result[2]);
    		ChatClient.user.setFirstName(result[3]);
    		ChatClient.user.setLastName(result[4]);
    		ChatClient.user.setNote(result[5]);
    		ChatClient.user.setIsLogin(result[6]);
    		ChatClient.user.setJob("Client");
    		ChatClient.user.setStatus("Subscriber_Client");
    		ChatClient.user.setSystemFormat(format);
    		if(ChatClient.user.getIsLogin().equals("1")) {
				errorMsg.setText("ERROR - username already login");
				return;
			}
    		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    		Stage primaryStage = new Stage();
    		if(format.equals("EK")) {
    			FXMLLoader loaderOSC = new FXMLLoader(getClass().getResource("/GUI/OnSiteClient.fxml"));
    	    	Parent rootOSC = (Parent) loaderOSC.load();
    	    	OnSiteClientController controlOSC = loaderOSC.getController();
    			controlOSC.loadDetails(ChatClient.user);
    			ChatClient.user.setSupplyMethod("onSite");
    			ChatClient.user.setNote(facility);
    			if(userSubscriberAndFirstPurchase(ChatClient.user.getUsername(), ChatClient.user.getPassword())) {
    				controlOSC.initDiscountMsg();
    			}
    			Scene sceneOSC = new Scene(rootOSC);
    			primaryStage.setScene(sceneOSC);
    		}
    		else {
    			FXMLLoader loaderDC = new FXMLLoader(getClass().getResource("/GUI/DistantClient.fxml"));
		    	Parent rootDC = (Parent) loaderDC.load();
		    	DistantClientController controlDC = loaderDC.getController();
		    	controlDC.loadDetails(ChatClient.user);
		    	if(userSubscriberAndFirstPurchase(ChatClient.user.getUsername(), ChatClient.user.getPassword())) {
		    		controlDC.initDiscountMsg();
				}
				Scene sceneDC = new Scene(rootDC);
				primaryStage.setScene(sceneDC);
				primaryStage.setTitle("Distant Client");
    		}
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.show();
    	}
    }
    /**
	 * 
	 * @return the chosen format (OL/EK) - simulation
	 */
	public String getFormat() {
		return format;
	}
	/** 
	 * 
	 * @param format - set the chosen format (OL/EK) to this value (simulation)
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	/**
	 * 
	 * @return the facility the user chose to make their order from (simulation)
	 */
	public String getFacility() {
		return facility;
	}
	/**
	 * 
	 * @param facility - set the facility the user chose to make their order from to this value (simulation)
	 */
	public void setFacility(String facility) {
		this.facility = facility;
	}
}
