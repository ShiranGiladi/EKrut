package server;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import controller.ServerPortFrameController;

public class ServerUI extends Application {
	final public static int DEFAULT_PORT = 5555;

	public static void main( String args[] ) throws Exception {   
		 launch(args);
	  } //end main
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		ServerPortFrameController aFrame = new ServerPortFrameController(); 
		aFrame.start(primaryStage);
		try {
			File myObj = new File("subscriberNum.txt");
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
				try {
					FileWriter myWriter = new FileWriter("subscriberNum.txt");
					myWriter.write("107");
					myWriter.close();
					System.out.println("Successfully wrote to the file.");
				} catch (IOException e) {
					System.out.println("An error occurred.");
					e.printStackTrace();
				}

			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	
	public static void runServer(String p, String dbPassword, ServerPortFrameController s) {
		 int port = 0; //Port to listen on 

	        try {
	        	port = Integer.parseInt(p); //Set port to 5555
	        }
	        catch(Throwable t) {
	        	System.out.println("ERROR - Could not connect!");
	        }
	        EchoServer sv = EchoServer.getInstance(port, s);
	        //EchoServer sv = new EchoServer(port, s);
	        sv.setDBPassword(dbPassword);
	        try {
	          sv.listen(); //Start listening for connections
	        } 
	        catch (Exception ex) {
	          System.out.println("ERROR - Could not listen for clients!");
	        }
	}
}
