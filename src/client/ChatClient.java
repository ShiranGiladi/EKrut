package client;

import ocsf.client.*;
import common.*;
import logic.Product;
import logic.Subscriber;
import logic.User;
import java.io.*;
import java.util.ArrayList;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of the display method in the client.
   */
  ChatIF clientUI;
  public static Subscriber subscriber = new Subscriber (null, null, null, null, null, null, null);
  public static User user = new User(null, null);
  public static ArrayList<Product> arrProduct = new ArrayList<Product>();
  public static String answerMsg;
  public static boolean awaitResponse = false;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
  }

  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   * @param msg The message from the server.
   */
  @SuppressWarnings("unchecked")
  public void handleMessageFromServer(Object msg) {
	//System.out.println("--> handleMessageFromServer"); 
	awaitResponse = false;
	if(msg instanceof String) {
		String serverMsg = msg.toString();
		String[] result = serverMsg.split("\\s");
		if(result[0].equals("checkLogin")) {
			user.setFirstName(result[1]);
			user.setLastName(result[2]);
			user.setJob(result[3]);
			user.setStatus(result[4]);
			user.setNote(result[5]);
			user.setId(result[6]);
			user.setIsLogin(result[7]);
		}
		else {
			answerMsg = result[1];
		}
	}
	else if(msg instanceof MyFile) {
		int fileSize =((MyFile)msg).getSize();
		
		String fileName = "src/Images/" + ((MyFile)msg).getFileName();
		//System.out.println("fileName=" + fileName);
		File newFile = new File(fileName);
		try {
			FileOutputStream fos = new FileOutputStream(newFile);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			try {
				bos.write(((MyFile)msg).mybytearray, 0 , fileSize);
			} catch (IOException e) {
				e.printStackTrace();
			}
			bos.flush();
			fos.flush();
			bos.close();
			fos.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	else if(msg instanceof ArrayList<?>) {
		arrProduct = (ArrayList<Product>) msg;
		//System.out.println(arrProduct.toString());
	}
  }

  /**
   * This method handles all data coming from the UI            
   * @param str The message from the UI.    
   */
  public void handleMessageFromClientUI(String message) {
    try {
    	openConnection();//in order to send more than one message
       	awaitResponse = true;
    	sendToServer(message);
		// wait for response
		while (awaitResponse) {
			try {
				Thread.sleep(100);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }
    catch(IOException e) {
      clientUI.display("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit() {
    try {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
