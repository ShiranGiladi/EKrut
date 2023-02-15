package server;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;

import common.MyFile;
import controller.ServerPortFrameController;
import logic.Product;
import logic.User;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 */

public class EchoServer extends AbstractServer {
  //Class variables *************************************************
	private static EchoServer instance = null;
	private ServerPortFrameController portController;
	private String[] dbPassword = new String[1];
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   * @param port The port number to connect on.
   */
 
  public EchoServer(int port, ServerPortFrameController portController) {
    super(port);
    this.portController = portController;
  }

  //Instance methods ************************************************
  public static EchoServer getInstance(int port, ServerPortFrameController portController){
	  if (instance==null)
		  instance = new EchoServer(port, portController);
	  return instance;
  }
  
  /**
   * this method runs automatically when client connects to server.
   * @param client The connection from which the message originated.
   */
  @Override
  protected void clientConnected(ConnectionToClient client) {
	  boolean userExists = false;
	  for(User user : ServerPortFrameController.users) {
			if(user.getIp().equals(client.getHostAddress())) {
				userExists = true;
				user.setConnected("Connected");
			}
	  }
	  if(!userExists) {
		  ServerPortFrameController.users.add(new User(client.getHostAddress(), client.getHostName()));
	  }
	  portController.refreshTable();
  }

  
  /**
   * This method handles any messages received from the client.
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   * @param 
 * @throws SQLException 
   */
  
  public void handleMessageFromClient(Object msg, ConnectionToClient client) throws SQLException{
	  String msgBack = "";
	  String[] result = ((String) msg).split("\\s");
	  switch(result[0]) {
	  case "connect":
		  try {
			client.sendToClient("loged in");
		  } catch (IOException e2) {
			e2.printStackTrace();
		  }
		  break;
	  case "disconnect":
		  for(int i=0 ; i<ServerPortFrameController.users.size() ; i++) {
				if(ServerPortFrameController.users.get(i).getIp().equals(client.getHostAddress()))
					ServerPortFrameController.users.get(i).setConnected("Disconnected");
		  }
		  portController.refreshTable();
		  try {
			  client.sendToClient("disconnect");
		  } catch (IOException e) {
			  e.printStackTrace();
		  }
		  break;
	  case "login":
		  //String msgBack = "";
		  msgBack = mysqlConnection.checkForLogin(result[1], result[2]);
			try {
				client.sendToClient(msgBack);
			} catch (IOException e) {
				e.printStackTrace();
			}
		  break;
	  case "logout":
		  mysqlConnection.logout(result[1]);
		  try {
			client.sendToClient("loged out");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		  break;
	  case "showProductInLocation":
		  ArrayList<Product> arrProduct;
		  arrProduct = mysqlConnection.productInLogation(result[1], result[2], result[3], result[4]);
		  for(int i=0; i<arrProduct.size(); i++)
				restoreImageToClient(arrProduct.get(i).getImgSrc(), client);
			try {
				client.sendToClient((Object)arrProduct);
			} catch (IOException e) {
				e.printStackTrace();
			}
		  break;
	  case "CheckQuantity":
		  String quantity;
		  quantity = mysqlConnection.checkProductQuantity(result[1], result[2]);
		  try {
			client.sendToClient(quantity);
		} catch (IOException e) {
			e.printStackTrace();
		}
		  break;
	  case "UpdateQuantity":
		  msgBack = mysqlConnection.updateQuantity(result[1], result[2], result[3], result[4]);
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
	  case "purchasesNumber":
		  String purchasesNum;
		  purchasesNum = mysqlConnection.purchasesNumber(result[1], result[2]);
		  try {
			client.sendToClient(purchasesNum);
		} catch (IOException e) {
			e.printStackTrace();
		}
		  break;
	  case "addOrderDetailsToDB":
		  //System.out.println(result[1] + result[2] + result[3] + result[4] + result[5] + result[6] + result[7] + result[8] + result[9] + result[10]);
		  mysqlConnection.addOrderDetailsToDB(result[1], result[2], result[3], result[4], result[5], result[6], result[7], result[8], result[9]);
		  try {
			client.sendToClient(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		  break;
	  case "updateClientDetails":
		  msgBack = mysqlConnection.updateClientDetails(result[1], result[2], result[3]);
		  try {
			client.sendToClient(msgBack);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
	  case "InfoToUpdate":
		  msgBack = mysqlConnection.InfoToUpdate(result[1], result[2]);
		  try {
			client.sendToClient(msgBack);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
	  case "updateClientStatus":
		  msgBack = mysqlConnection.updateClientStatus(result[1]);
		  try {
			client.sendToClient(msgBack);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
	  case "getUsersWithNoAccount":
		  msgBack = mysqlConnection.getUsersWithNoAccount();
		  try {
			client.sendToClient(msgBack);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
	  case "CreateAccount":
		  msgBack = mysqlConnection.CreateAccount(result[1]);
		  try {
			client.sendToClient(msgBack);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
	  case "getProdutUnderTreshold":
		  msgBack = mysqlConnection.getProdutUnderTreshold(result[1]);
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		  break;
	  case "getOprationWorkerNames":
		  msgBack = mysqlConnection.getOprationWorkerNames();
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		  break;
	  case "sendToOprationWorker":
		  msgBack = mysqlConnection.sendToOprationWorker(result[1], result[2], result[3]);
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		  break;
	  case "getProductNameForFacility":
		  msgBack = mysqlConnection.getProductNameForFacility(result[1]);
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		  break;
	  case "getCurrentThreshold":
		  msgBack = mysqlConnection.getCurrentThreshold(result[1], result[2]);
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		  break;
	  case "updateThreshold":
		  msgBack = mysqlConnection.updateThreshold(result[1], result[2], result[3]);
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		  break;
	  case "getAvailableDiscounts":
		  msgBack = mysqlConnection.getAvailableDiscounts(result[1]);
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		break;
	  case "applyDiscount":
		  msgBack = mysqlConnection.applyDiscount(result[1]);
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		break;
	  case "getInfoForDeliveryFields":
		  msgBack = mysqlConnection.getInfoForDeliveryFields(result[1], result[2]);
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		break;
	  case "getDetailsForPaymentFields":
		  msgBack = mysqlConnection.getDetailsForPaymentFields(result[1], result[2]);
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		break;
	  case "checktShowReports":
		  msgBack = mysqlConnection.checktShowReports(result[1], result[2], result[3], result[4], result[5]);
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		break;
	  case "getDeliverirsToApprove":
		  msgBack = mysqlConnection.getDeliverirsToApprove(result[1]);
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		break;
	  case "approveDelivery":
		  msgBack = mysqlConnection.approveDelivery(result[1], result[2]);
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		break;
	  case "getDeliverirsToClose":
		  msgBack = mysqlConnection.getDeliverirsToClose(result[1]);
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		break;
	  case "changeStatusToDoneForDelivery":
		  msgBack = mysqlConnection.changeStatusToDoneForDelivery(result[1]);
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		break;
	  case "getProdutToFillForOprationWorker":
		  msgBack = mysqlConnection.getProdutToFillForOprationWorker(result[1]);
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		break;
	  case "getRelatadFieldsForProduct":
		  msgBack = mysqlConnection.getRelatadFieldsForProduct(result[1], result[2]);
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		break;
	  case "updateInventory":
		  msgBack = mysqlConnection.updateInventory(result[1], result[2], result[3]);
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		break;
	  case "getUsersAreNotApprove":
		  msgBack = mysqlConnection.getUsersAreNotApprove(result[1]);
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		break;
	  case "approveByManager":
		  msgBack = mysqlConnection.approveByManager(result[1]);
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		break;
	  case "InitSale":
		  msgBack = mysqlConnection.InitSale(result[1], result[2], result[3], result[4], result[5], result[6], result[7]);
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		  break;
	  case "getOrderDetailsForUser":
		  msgBack = mysqlConnection.getOrderDetailsForUser(result[1], result[2], result[3], result[4]);
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		  break;
	  case "setPickUpOrderToDone":
		  msgBack = mysqlConnection.setPickUpOrderToDone(result[1]);
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		  break;
	  case "setConfirmationDateForDeliveryOrder":
		  msgBack = mysqlConnection.setConfirmationDateForDeliveryOrder(result[1]);
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		  break;
	  case "setSubscriberForTouchSimulation":
		  msgBack = mysqlConnection.setSubscriberForTouchSimulation();
		  try {
			client.sendToClient(msgBack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		  break;
	  default:
		  break;
	  }
	}
  
  private void restoreImageToClient(String imagePath, ConnectionToClient client){
	  try{
	      File newFile = new File (imagePath);
	      MyFile msg= new MyFile(newFile.getName());
	      //System.out.println(imagePath);
	      byte [] mybytearray  = new byte [(int)newFile.length()];
	      FileInputStream fis = new FileInputStream(newFile);
	      BufferedInputStream bis = new BufferedInputStream(fis);			  
	      msg.initArray(mybytearray.length);
	      msg.setSize(mybytearray.length);
	      
	      bis.read(msg.getMybytearray(),0,mybytearray.length);
	      client.sendToClient(msg);
	  }
	  catch (Exception e) {
		System.out.println("Error send (Files)msg) to Client\n" + e);
	  }
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted() {
	mysqlConnection.ConnectDb(dbPassword);
    System.out.println ("Server listening for connections on port " + getPort());
  }
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()  {
    System.out.println ("Server has stopped listening for connections.");
  }
  
  public void setDBPassword(String password) {
	  this.dbPassword[0] = password;
  }
}
//End of EchoServer class
