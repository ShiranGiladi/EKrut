package client;

import javafx.application.Application;
import javafx.stage.Stage;
import controller.ClientConnectFormController;

public class ClientUI extends Application {
	public static ClientController chat; //only one instance

	public static void main( String args[] ) throws Exception {  
		launch(args);  
	} //end main
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		chat= new ClientController("localhost", 5555);
		ClientConnectFormController cFrame = new ClientConnectFormController(); //create Client Connect Frame
		cFrame.start(primaryStage);
	}
}
