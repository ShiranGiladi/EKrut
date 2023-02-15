package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import logic.Product;

/**
 * This class has all the queries to the data base.
 *
 */
public class mysqlConnection {
	
	static Connection conn2;
	public static void ConnectDb(String[] args) {
		String dbPassword = args[0];
		try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            System.out.println("Driver definition succeed");
        } catch (Exception ex) {
        	/* handle the error*/
        	 System.out.println("Driver definition failed");
        	 }
        
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/ekrut?serverTimezone=IST","root", dbPassword);
            System.out.println("SQL connection succeed");
            conn2=conn;
     	} catch (SQLException ex) 
     	    {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            }
   	}
	/**
	 * This method gets a subscribed client's ID and a value to update to the data base. 
	 * The value can be the client's credit card number OR the client's subscriber number.
	 * The method returns a string that says if the update was successful or not.
	 * @param id - the subscribed client's ID
	 * @param str1 - the subscribed client's credit card number OR the subscribed client's subscriber number (depends on str2)
	 * @param str2 - "creditCard" / "subscriberNumber"
	 * @return "Update" (if the update was made successfully) OR "Error" (if failed to update)
	 */
	public static String updateToDB(String id, String str1, String str2) {
		Statement stmt;
		try {
			stmt = conn2.createStatement();
			if(str2.equals("creditCard")) {
				String query2 = "UPDATE subscriber SET credit_card_number=\"" + str1 + "\" WHERE id=\"" + id + "\"";
				int check = stmt.executeUpdate(query2);
				if(check == 0) {
					return "Error";
				}
			}
			else if (str2.equals("subscriberNumber")) {
				String query = "UPDATE subscriber SET subscriber_number=\"" + str1 + "\" WHERE id=\"" + id + "\"";
				int check = stmt.executeUpdate(query);
				if(check == 0) {
					return "Error";
				}
			}
			else {
				String query = "UPDATE subscriber SET subscriber_number=\"" + str1 + "\" WHERE id=\"" + id + "\"";
				String query2 = "UPDATE subscriber SET credit_card_number=\"" + str2 + "\" WHERE id=\"" + id + "\"";
				int check = stmt.executeUpdate(query);
				stmt.executeUpdate(query2);
				if(check == 0) {
					return "Error";
				}
			}
		} 
		catch (SQLException e) {e.printStackTrace();}
		return "Update";
	}
	/**
	 * This method gets a subscribed client's ID and return a string with the client's data.
	 * The data returned in the string is: the client's   first name, the client's  last name, the client's ID number, the client's  phone number,
	 * 									   the client's  email address, the client's  credit card number, and the client's  subscriber number.
	 * @param id - the subscribed client's ID
	 * @return "Error" (in case the given ID not found in the data base) OR a string with the client's details (in case of success).
	 * @throws SQLException
	 */
	public static String getData(String id) throws SQLException {
		String query = "SELECT first_name, last_name, id, phone_number, email_addres, credit_card_number, subscriber_number FROM subscriber WHERE id=" + id;
		Statement st = conn2.createStatement();
		String sql = (query);
		ResultSet rs = st.executeQuery(sql);
		String s = "";
		if(rs.next()) { 
			s = s + rs.getString("id");
			s = s + " " + rs.getString("first_name");
			s = s + " " + rs.getString("last_name");
			s = s + " " + rs.getString("phone_number");
			s = s + " " + rs.getString("email_addres");
			s = s + " " + rs.getString("credit_card_number");
			s = s + " " + rs.getString("subscriber_number");
		}
		else {
			return "Error";
		}
		return s;
	}
	/**
	 * This method gets a user's user name and password, checks if these are the correct values (according to the information in the data base),
	 * and checks if the user is not already logged in to the system. If so - changes the value in the data base that indicates that the user 
	 * is logged in and returns a string with the user's details from the data base.
	 * The data returned in the string is: the user's ID number, the user's first name, the user's last name, the user's login status,
	 * 									   the user's job, and the user's area (field name = note).
	 * @param username - the user's user name
	 * @param password - the user's password
	 * @return "checkLogin Error" (in case the user is already logged in or if the user entered incorrect details) OR a string with the 
	 * 																									user's details (in case of success).
	 * @throws SQLException
	 */
	public static String checkForLogin(String username, String password) throws SQLException {
		String query = "SELECT id, firstName, lastName, job, status, note, isLogin FROM users WHERE username= \"" + username + "\"" + " AND password= \"" + password + "\"";
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		String s = "checkLogin ";
		if(rs.next()) { 
			s = s + rs.getString("firstName");
			s = s + " " + rs.getString("lastName");
			s = s + " " + rs.getString("job");
			s = s + " " + rs.getString("status");
			s = s + " " + rs.getString("note");
			s = s + " " + rs.getString("id");
			s = s + " " + rs.getString("isLogin");
			if(rs.getString("isLogin").equals("0")) {
				String query2 = "UPDATE users SET isLogin=1 WHERE username= \"" + username + "\"" + " AND password= \"" + password + "\"";
				st.executeUpdate(query2);
			}
		}
		else {
			return "checkLogin Error";
		}
		return s;
	}
	/**
	 * This method logs out the user from the system. It gets the user's ID number and changes the value in the data base that 
	 * indicates that the user is logged. 
	 * @param costumerID - the user's ID number
	 * @throws SQLException
	 */
	public static void logout(String costumerID) throws SQLException {
		Statement stmt = conn2.createStatement();
		String query = "UPDATE users SET isLogin=0 WHERE id=\"" + costumerID + "\"";
		stmt.executeUpdate(query);
	}
	/**
	 * This method return an array with the information about the products in the chosen facility according to the chosen type of menu,
	 * 																					the client's status and the chosen supply method.
	 * The information about the product includes: the product's name, product's ID number, product's image, product's price.
	 * @param category - the chosen category in the menu (all products / food / drinks / desserts).
	 * @param facilityLocation - the facility from which the order is made.
	 * @param clientStatus - the client's status - regular client or subscribed client.
	 * @param supplyMethod - the type of order (on site / pick up late / for delivery).
	 * @return an array with the information about the products (in case of success) OR "Error" (in case no products found).
	 * @throws SQLException
	 */
	public static ArrayList<Product> productInLogation(String category, String facilityLocation, String clientStatus, String supplyMethod) throws SQLException{
		ArrayList<Product> arrProduct = new ArrayList<Product>();
		Product product = new Product();
		String query = null;
		int flag = 0; // 0 - for no products at facility location 
		String facility_Location = facilityLocation+"_products";
		if(supplyMethod.equals("delivery"))
			facility_Location = "delivery_center";
		else
			facility_Location = facilityLocation+"_products";
		String facility_Area;
		if(facilityLocation.equals("haifa") || facilityLocation.equals("karmiel"))
			facility_Area = "north";
		else if(facilityLocation.equals("beersheva") || facilityLocation.equals("eilat"))
			facility_Area = "south";
		else
			facility_Area = "uae";
		if(category.equals("Show-All")) {
			query = "SELECT productName, price, imgSrc, id FROM " + facility_Location;
		}
		else {
			query = "SELECT productName, price, imgSrc, id FROM " + facility_Location + " WHERE category= \"" + category + "\"";
		}
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		while(rs.next()) {
			flag = 1;
			product.setProductName(rs.getString("productName"));
			product.setProductPrice(rs.getString("price"));
			product.setImgSrc(rs.getString("imgSrc"));
			product.setProductID(rs.getString("id"));
			if(clientStatus.equals("Subscriber_Client")) {
				String returnVal = checkExistsSaleInProduct(product.getProductID(), facility_Area);
				String[] newPriceForSubscriberInSale = returnVal.split("\\s");
				if(!newPriceForSubscriberInSale[1].equals("NotInSale"))
					product.setNewPriceForSubscriberInSale(newPriceForSubscriberInSale[1]);
			}
			arrProduct.add(product);
			product = new Product();
		}
		if(flag == 0) {
			product = new Product();
			product.setProductName("Error");
			arrProduct.add(product);
		}
		return arrProduct;
	}
	/**
	 * This method checks if there is a sale on a certain product in a certain area. If there is a sale, the method will return 
	 * the new price of the product after the discount, otherwise the method will return "NotInSale".
	 * @param productID - the chosen product's ID number
	 * @param facility_Area - the chosen area 
	 * @return "NotInSale" (if there is no discount on the chosen product in the chosen facility) OR the new price of the product if it's on sale.
	 * @throws SQLException
	 */
	public static String checkExistsSaleInProduct(String productID, String facility_Area) throws SQLException {
		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String currentDate = myDateObj.format(myFormatObj);
		String newPrice = "checkExsitSaleInProduct ";
		String query = "SELECT newPrice FROM sales WHERE productID= \"" + productID + "\"" + " AND area= \"" + facility_Area + "\""
				+ " AND startDate<= \"" + currentDate + "\"" + " AND endDate>= \"" + currentDate + "\"" + " AND apply=\"" + "yes" + "\"";
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		if(rs.next()) {
			newPrice = newPrice + rs.getString("newPrice");
		}
		else {
			newPrice = newPrice + "NotInSale";
		}
		return newPrice;
	}
	/**
	 * This method check the quantity of a certain product in the chosen facility. If the product is not fount, the method will return "Error",
	 * otherwise the method will return the amount of the product in the chosen facility.
	 * @param facilityLocation - the chosen facility's name
	 * @param productName -  the chosen product's name
	 * @return "Error" (in case the product is not fount) OR the amount of the product in the chosen facility.
	 * @throws SQLException
	 */
	public static String checkProductQuantity(String facilityLocation, String productName) throws SQLException {
		Statement stmt = conn2.createStatement();
		facilityLocation = facilityLocation+"_products";
		String query = "SELECT amount FROM " + facilityLocation + " WHERE productName= \"" + productName + "\"";
		ResultSet rs = stmt.executeQuery(query);
		String s = "checkProductQuantity ";
		if(rs.next()) { 
			s = s + rs.getInt("amount") + "";
		}
		else {
			s = s + "Error";
		}
		return s;
	}
	/**
	 * This method updates the quantity of a certain product in the chosen facility. The update can increase or decrease the quantity of
	 * the product in the facility. The method returns a string that says if the update was successful or not.
	 * @param facilityLocation - the chosen facility's name
	 * @param productName -  the chosen product's name
	 * @param quantity - the value by which to increase or decrease the quantity of the product in the chosen facility
	 * @param updateType - the type of quantity update - increase or decrease
	 * @return "updateQuantity Update" (in case the update was successful) OR "updateQuantity Error" (in case the update failed).
	 * @throws SQLException
	 */
	public static String updateQuantity(String facilityLocation, String productName, String quantity, String updateType) throws SQLException {
		Statement stmt = conn2.createStatement();
		String curAmount = checkProductQuantity(facilityLocation, productName);
		String[] result = curAmount.split("\\s");
		int updateAmount;
		if(updateType.equals("Minus"))
			updateAmount = Integer.parseInt(result[1]) - Integer.parseInt(quantity);
		else
			updateAmount = Integer.parseInt(result[1]) + Integer.parseInt(quantity);
		facilityLocation = facilityLocation+"_products";
		String query = "UPDATE " + facilityLocation + " SET amount=\"" + updateAmount + "\" WHERE productName=\"" + productName + "\"";
		int check = stmt.executeUpdate(query);
		
		if(updateAmount == 0) {
			query = "SELECT countZeroInMonth FROM " + facilityLocation + " WHERE productName='" + productName + "'";
			stmt = conn2.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int count = 0;
			if(rs.next()) {
				count = rs.getInt("countZeroInMonth");
			}
			count++;
			
			query = "UPDATE " + facilityLocation + " SET countZeroInMonth='" + count + "' WHERE productName='" + productName + "'";
			stmt = conn2.createStatement();
			stmt.executeUpdate(query);
		}		
		
		if (check == 0) {
			return "updateQuantity Error";
		}
		return "updateQuantity Update";
	}
	/**
	 * This method return the number of order a subscribed client's has made.
	 * @param username - the subscribed client's user name
	 * @param password - the subscribed client's password 
	 * @return "purchasesNumber Error" (in case the user is not found) OR the number of orders the client has made.
	 * @throws SQLException
	 */
	public static String purchasesNumber(String username, String password) throws SQLException {
		String query = "SELECT numberOfpurchases FROM subscribers WHERE username= \"" + username + "\"" + " AND password= \"" + password + "\"";
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		String s = "purchasesNumber ";
		if(rs.next()) { 
			s = s + rs.getString("numberOfpurchases");
		}
		else {
			return "purchasesNumber Error";
		}
		return s;
	}
	/**
	 * This method updates the details of a certain order to the "orders" table in the data base
	 * @param price - the total price of the order
	 * @param facility - the facility in which the order was made
	 * @param costumerID - the costumer ID number
	 * @param payment - the method of payment (credit card / EKT app / deferred payment) 
	 * @param productsInOrder - the number of products in the order
	 * @param supplyMethod - the type of order (on site / pick up later / for delivery)
	 * @param orderContent - the content of the order - the name of the product and its amount
	 * @param address - the address (for distant orders for delivery)
	 * @param phone - the phone number of the client (for distant orders for delivery)
	 * @throws SQLException
	 */
	public static void addOrderDetailsToDB(String price, String facility, String costumerID, String payment, 
			String productsInOrder, String supplyMethod, String orderContent, String address, String phone) throws SQLException{
		Statement stmt = conn2.createStatement();
		int orderNumber = getAvailableOrderNumber();
		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String orderDate = myDateObj.format(myFormatObj);
		String query = "INSERT INTO orders (orderNumber, price, facility, orderDate, costumerID, payment,"
				+ " productsInOrder, confirmationDate, supplyMethod, orderContent) VALUES ("
				+ "\"" + orderNumber + "\"" + ", " + "\"" +  price + "\"" + ", " + "\"" + facility + "\"" + ", " + "\"" + orderDate + "\"" + ", " 
				+ "\"" + costumerID + "\"" + ", " + "\"" + payment + "\"" + ", " + "\"" + productsInOrder + "\"" + ", "
				+ "\"" + "null" + "\"" + ", " + "\"" + supplyMethod + "\"" + "," + "\"" + orderContent + "\"" + ")";
		stmt.executeUpdate(query);
		if(supplyMethod.equals("pick_up")) {
			String updateForPickUpQuery = "UPDATE orders SET orderStatus='notDone' " + " WHERE orderNumber=\"" + orderNumber + "\"";
			stmt.executeUpdate(updateForPickUpQuery);
		}
		if(supplyMethod.equals("delivery")) {
			String updateForDeliveryQuery = "UPDATE orders SET approveByDeliveryOperator='not_approved', orderStatus='notDone' " + " WHERE orderNumber=\"" + orderNumber + "\"";
			stmt.executeUpdate(updateForDeliveryQuery);
			
			String addDeliveryUserDetails = "INSERT INTO deliveries (orderNumber, address, costumerID, reciverPhoneNumber) " + "VALUES " 
					+ "(" + "\"" + orderNumber + "\"" + ", " + "\"" +  address + "\"" + ", " + "\"" + costumerID + "\"" + ", " + "\"" + phone + "\"" + ")";
			stmt.executeUpdate(addDeliveryUserDetails);
		}
		String result = addOneToNumberOfpurchasesForCostumer(costumerID, "clients");
		if(result.equals("notFound"))
			addOneToNumberOfpurchasesForCostumer(costumerID, "subscribers");
	}
	/**
	 * This method increases the number of orders of a certain client by one, once they make an order in the system.
	 * @param costumerID - the costumer ID number
	 * @param table - the name of the table in the data base
	 * @return "Update" (in case the update was successful) or "notFound" (in case the client is not found)
	 * @throws SQLException
	 */
	private static String addOneToNumberOfpurchasesForCostumer(String costumerID, String table) throws SQLException {
		String query = "SELECT numberOfpurchases FROM " + table + " WHERE id='" + costumerID + "'";
		Statement stmt = conn2.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		if(rs.next()) {
			int number = Integer.parseInt(rs.getString("numberOfpurchases"));
			number++;
			String query2 = "UPDATE " + table + " SET numberOfpurchases='" + String.valueOf(number) + "' WHERE id='" + costumerID + "'";
			Statement stmt2 = conn2.createStatement();
			stmt2.executeUpdate(query2);
			return "Update";
		}
		return "notFound";
	}
	/**
	 * This method get the order number for the next order it needs to enter into the orders table.
	 * The method checks what was the previous highest order number and returns its value + 1.
	 * @return the next available order number.
	 * @throws SQLException
	 */
	public static int getAvailableOrderNumber() throws SQLException {
		int max = 0, availableOrderNumber = 0;
		String query = "SELECT orderNumber FROM orders";
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		while(rs.next()) {
			int orderNum = rs.getInt("orderNumber");
			if(orderNum > max)
				max = orderNum;
		}
		availableOrderNumber = max + 1;
		return availableOrderNumber;
	}
	/**
	 * This method set a new value to one of the client's details, according to the field it gets to update.
	 * @param field - the field which needs to be updated 
	 * @param id - the client's ID number
	 * @param newValue - the new value to enter into the selected field.
	 * @return "updateClientDetails Error" (in case the user is not found) OR "updateClientDetails Update" (in case the update was made successfully).
	 * @throws SQLException
	 */
	public static String updateClientDetails(String field, String id, String newValue) throws SQLException {
		String query1 = "UPDATE clients SET " + field + "='" + newValue + "' WHERE id='" + id + "'";
		String query2 = "UPDATE users SET " + field + "='" + newValue + "' WHERE id='" + id + "'";
		Statement st = conn2.createStatement();
		int check = st.executeUpdate(query1);
		if (check == 0) {
			return "updateClientDetails Error";
		}
		st.executeUpdate(query2);
		return "updateClientDetails Update";
	}
	/**
	 * This method shows the current value of one of the client's details according to the field it gets, so that if the 
	 * service representative wants to update it, they would know what was the original value in the data base.
	 * @param field - the field which the service representative wants to be updated 
	 * @param id - the client's ID number
	 * @return "InfoToUpdate Error" (in case the user is not found) OR the original value in the data base that will be updated. 
	 * @throws SQLException
	 */
	public static String InfoToUpdate(String field, String id) throws SQLException {
		String query = "SELECT " + field + " FROM clients WHERE id='" + id + "'";
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		if (!rs.next()) {
			return "InfoToUpdate Error";
		}
		String s = rs.getString(field);
		return "InfoToUpdate " + s;
	}
	/**
	 * This method upgrades the client status from regular client to subscriber. 
	 * The method moves the clients data from the "clients" table in the data base to the "subscribers" table in the data base.
	 * @param id - the client's ID number
	 * @return "updateClientStatus Error" (in any case of an error) OR "updateClientStatus Update" (in case of success).
	 * @throws SQLException
	 */
	public static String updateClientStatus(String id) throws SQLException {
		String data = "";
		int newSubNum = 0;
		try {
			File myObj = new File("subscriberNum.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				data = myReader.nextLine();
			}
			newSubNum = (Integer.parseInt(data));
			newSubNum++;
			myReader.close();
			try {
				FileWriter myWriter = new FileWriter("subscriberNum.txt");
				myWriter.write(String.valueOf(newSubNum));
				myWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String query = "INSERT INTO subscribers (userName ,password,firstName,lastName, id,"
				+ " creditCard,phone,email) SELECT userName ,password,firstName,lastName, id, creditCard,phone,email FROM clients WHERE id='"
				+ id + "'";
		String query1 = "DELETE FROM clients WHERE id='" + id + "'";
		String query2 = "UPDATE subscribers SET numberOfpurchases='0', subscriberNumber='" + newSubNum + "' WHERE id='"
				+ id + "'";
		String query3 = "UPDATE users SET status='Subscriber_Client', subscriberNumber='" + newSubNum + "' WHERE id='"
				+ id + "'";
		Statement st = conn2.createStatement();
		int check = st.executeUpdate(query);
		if (check == 0) {
			return "updateClientStatus Error";
		}
		st.executeUpdate(query1);
		st.executeUpdate(query2);
		st.executeUpdate(query3);
		return "updateClientStatus Update";
	}
	/**
	 * This method gets all the information about users without an account in the system (clients).
	 * The information includes: the user's user name, the user's password, the user's  first name, the user's  last name,
	 * 							 the user's ID number, the user's credit card number, the user's phone number, and the user's email address
	 * @return "getUsersWithNoAccount Empty" (in case no users found) OR all the information about all the users without an account.
	 * @throws SQLException
	 */
	public static String getUsersWithNoAccount() throws SQLException {
		String query = "SELECT userName, password, firstName, lastName, id, creditCard, phone, email FROM clients WHERE haveAccount= 'no'";
		Statement st = conn2.createStatement();
		String sql = (query);
		ResultSet rs = st.executeQuery(sql);
		String s = "";
		while (rs.next()) {
			s += rs.getString("userName") + ",";
			s += rs.getString("password") + ",";
			s += rs.getString("firstName") + ",";
			s += rs.getString("lastName") + ",";
			s += rs.getString("id") + ",";
			s += rs.getString("creditCard") + ",";
			s += rs.getString("phone") + ",";
			s += rs.getString("email") + ",";
		}
		if(s.equals(""))
			return "getUsersWithNoAccount Empty";
		return "getUsersWithNoAccount " + s;
	}
	/**
	 * This method creates a client account for a user. (Updates the field "haveAccount" in the "clients" table in the data base from
	 * 																													"no" to "yes").
	 * @param id - the user's ID number
	 * @return "CreateAccount Error" (in case the user is not found) OR "CreateAccount Update" (in case an account was created successfully).
	 * @throws SQLException
	 */
	public static String  CreateAccount(String id) throws SQLException {
		String query = "UPDATE clients SET haveAccount='yes' WHERE id='" + id + "'";
		Statement st = conn2.createStatement();
		int check = st.executeUpdate(query);
		if (check == 0) {
			return "CreateAccount Error";
		}
		return "CreateAccount Update";
	}
	/**
	 * This method gets all the products that reached below their threshold level in their respective facilities, in a chosen area.
	 * @param area - the name of the are.
	 * @return "getProdutUnderTreshold Empty" (if there are no products below the threshold level in the area) OR a string of 
	 * 																			products that reached below their threshold level.
	 * @throws SQLException
	 */
	public static String getProdutUnderTreshold(String area) throws SQLException {
		String res = "";
		if(area.equals("north")){
			res = getProdutUnderTresholdPerLocation("haifa_product");
			String s = getProdutUnderTresholdPerLocation("karmiel_product");
			if(!s.equals(""))
				res = res + s;
		}
		else if(area.equals("south")) {
			res = getProdutUnderTresholdPerLocation("beersheva_products");
			String s = getProdutUnderTresholdPerLocation("eilat_products");
			if(!s.equals(""))
				res = res + s;
		}
		else if(area.equals("uae")) {
			res = getProdutUnderTresholdPerLocation("dubai_products");
			String s = getProdutUnderTresholdPerLocation("abudhabi_products");
			if(!s.equals(""))
				res = res + s;
		}
		res = res.replace(",,", ",");
		if(res.trim().isEmpty())
			return "getProdutUnderTreshold Empty";
		return "getProdutUnderTreshold " + res;
	}
	/**
	 * This method gets all the information about the products that reached below their threshold level in a certain chosen facility.
	 * The information includes: the product's ID number, the product's name, the current amount of the product in the facility,
	 * 							 the product's threshold level, the product's facility
	 * @param facility - the name of the facility
	 * @return a string with all the information about the products that reached below their threshold level.
	 * @throws SQLException
	 */
	private static String getProdutUnderTresholdPerLocation(String facility) throws SQLException {
		String[] facilityName = facility.split("\\_");
		String query = "SELECT id, productName, amount, minimumAmount FROM " + facility + " WHERE amount<minimumAmount AND refill='0'";
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		String s = "";
		while (rs.next()) {
			s += rs.getString("id") + ",";
			s += rs.getString("productName") + ",";
			s += facilityName[0] + ",";
			s += rs.getString("amount") + ",";
			s += rs.getString("minimumAmount") + ",";
		}
		return s;
	}
	/**
	 * This method get the names of all the operation workers from the data base.
	 * @return "getOprationWorkerNames Empty" (in case there are no operation workers in the data base) OR a string with the names of all 
	 *  																							the operation workers in the data base.
	 * @throws SQLException
	 */
	public static String getOprationWorkerNames() throws SQLException {
		String query = "SELECT firstName, lastName FROM users WHERE job='OperationWorker'";
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		String s = "";
		while (rs.next()) {
			s += rs.getString("firstName") + ",";
			s += rs.getString("lastName") + ",";
		}
		if(s.equals(""))
			return "getOprationWorkerNames Empty";
		return "getOprationWorkerNames " + s;
	}
	/**
	 * This method sets which employee (operation worker) is in charge of refilling a certain product in the chosen facility.
	 * @param productID - the product's ID number
	 * @param facility - the name of the facility
	 * @param employee - the name of the employee (operation worker) in charge
	 * @return "updateClientDetails Error" (in case failed to update the employee in charge of the update) OR "updateClientDetails Send" 
	 * 																												(in case of success).
	 * @throws SQLException
	 */
	public static String sendToOprationWorker(String productID, String facility, String employee) throws SQLException {
		String location = facility+"_products";
		String query = "UPDATE " + location + " SET inCharge='" + employee + "', refill='1', taskStatus='notDone' WHERE id='" + productID + "'";
		Statement st = conn2.createStatement();
		int check = st.executeUpdate(query);
		
		query = "SELECT countRifillInMonth FROM " + location + " WHERE id='" + productID + "'";
		st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		int count = 0;
		if(rs.next()) {
			count = rs.getInt("countRifillInMonth");
		}
		count++;
		query = "UPDATE " + location + " SET countRifillInMonth='" + count + "' WHERE id='" + productID + "'";
		st = conn2.createStatement();
		st.executeUpdate(query);
		
		if (check == 0) {
			return "updateClientDetails Error";
		}
		return "updateClientDetails Send";
	}
	/**
	 * This method gets the names of the products in the chosen facility.
	 * @param facility - the name of the facility
	 * @return "getProductNameForFacility Empty" (in case there are no products in the facility) OR a string with the the names of the 
	 * 																									products in the chosen facility.
	 * @throws SQLException
	 */
	public static String getProductNameForFacility(String facility) throws SQLException {
		String location = facility+"_products";
		String query = "SELECT productName FROM " + location;
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		String s = "";
		while (rs.next()) {
			s += rs.getString("productName") + ",";
		}
		if(s.equals(""))
			return "getProductNameForFacility Empty";
		return "getProductNameForFacility " + s;
	}
	/**
	 * This method gets the current threshold level of a certain product in its facility.
	 * @param productName - the name of the product
	 * @param facility - the name of the facility
	 * @return "getCurrentThreshold Error" (in case the product is not found) OR the current threshold level of the product.
	 * @throws SQLException
	 */
	public static String getCurrentThreshold(String productName, String facility) throws SQLException {
		String location = facility+"_products";
		String query = "SELECT minimumAmount FROM " + location + " WHERE productName='" + productName + "'";
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		String s = "";
		if (rs.next())
			s += rs.getInt("minimumAmount");
		if(s.equals(""))
			return "getCurrentThreshold Error";
		return "getCurrentThreshold " + s;
	}
	/**
	 * This method sets a new threshold level to a certain product in the chosen facility
	 * @param productName - the name of the product
	 * @param facility - the name of the facility
	 * @param updateValue - the new value for the threshold level (update the old value to this one)
	 * @return "updateThreshold Error" (in case of an error) OR "updateThreshold Update" (in case the update was successful).
	 * @throws SQLException
	 */
	public static String updateThreshold(String productName, String facility, String updateValue) throws SQLException {
		String location = facility+"_products";
		String query = "UPDATE " + location + " SET minimumAmount='" + updateValue + "' WHERE productName='" + productName + "'";
		Statement st = conn2.createStatement();
		int check = st.executeUpdate(query);
		if (check == 0) {
			return "updateThreshold Error";
		}
		return "updateThreshold Update";
	}
	/**
	 * This method gets the information about all the available discounts for a certain area.
	 * The information includes: the sale ID number, the product name, the start date and time of the sale, the end date and time of the sale,
	 * 							 the original price of the product, and the new price of the product (after the discount).
	 * @param area - the area name
	 * @return "getAvailableDiscounts Empty" (in case there are no available discounts in the area) OR a string of all the information about
	 * 																									all the discounts in the chosen area.
	 * @throws SQLException
	 */
	public static String getAvailableDiscounts(String area) throws SQLException {
		String temp;
		String query = "SELECT saleID, productName, area, startDate, endDate, originalPrice, newPrice FROM sales WHERE apply = 'no' AND area = '" + area + "'";
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		String s = "";
		while (rs.next()) {
			s += rs.getString("saleID") + ",";
			s += rs.getString("productName") + ",";
			s += rs.getString("area") + ",";
			temp = rs.getString("startDate");
			temp = temp.replace(" ", "_");
			s += temp + ",";
			temp = rs.getString("endDate");
			temp = temp.replace(" ", "_");
			s += temp + ",";
			s += rs.getString("originalPrice") + ",";
			s += rs.getString("newPrice") + ",";
		}
		if(s.equals(""))
			return "getAvailableDiscounts Empty";
		return "getAvailableDiscounts " + s;
	}
	/**
	 * This method applies a certain sale.
	 * @param saleID - the sale ID number
	 * @return "applyDiscount Error" (in case of an error) OR "applyDiscount Change" (in case the sale was applied successfully)
	 * @throws SQLException
	 */
	public static String applyDiscount(String saleID) throws SQLException {
		String query = "UPDATE sales SET apply='yes' WHERE saleID='" + saleID + "'";
		System.out.println(query);
		Statement st = conn2.createStatement();
		int check = st.executeUpdate(query);
		if (check == 0) {
			return "applyDiscount Error";
		}
		return "applyDiscount Change";
	}
	/**
	 * This method gets the necessary information for a distant order for delivery. 
	 * The necessary information is: the address for delivery, the client's phone number.
	 * @param clientStatus -  the client's status (regular client / subscriber)
	 * @param id - the client's ID number
	 * @return "getInfoForDeliveryFields Error" (in case the client is not found) OR a string with the necessary information
	 * @throws SQLException
	 */
	public static String getInfoForDeliveryFields(String clientStatus, String id) throws SQLException {
		String status = "";
		if(clientStatus.equals("Subscriber_Client"))
			status = "subscribers";
		else
			status = "clients";
		String query = "SELECT address, phone FROM " + status + " WHERE id='" + id + "'";
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		String s = "";
		if (rs.next()) {
			s += rs.getString("address") + ",";
			s += rs.getString("phone");
		}
		if(s.equals(""))
			return "getInfoForDeliveryFields Error";
		return "getInfoForDeliveryFields " + s;
	}
	/**
	 * This method gets the client's credit card details. 
	 * The details include: the name on the card, the number of the card, the card's expiration date, the security number at the back of the card. 
	 * @param clientStatus -  the client's status (regular client / subscriber)
	 * @param id - the client's ID number
	 * @return "getDetailsForPaymentFields Error" (in case the client is not found) OR a string with the cliet's credit card details.
	 * @throws SQLException
	 */
	public static String getDetailsForPaymentFields(String clientStatus, String id) throws SQLException {
		String status = "";
		if(clientStatus.equals("Subscriber_Client"))
			status = "subscribers";
		else
			status = "clients";
		String query = "SELECT nameOnCard, creditCard, expirationDate, cvv FROM " + status + " WHERE id='" + id + "'";
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		String s = "";
		if (rs.next()) {
			s += rs.getString("nameOnCard") + ",";
			s += rs.getString("creditCard") + ",";
			s += rs.getString("expirationDate") + ",";
			s += rs.getString("cvv");
		}
		if(s.equals(""))
			return "getDetailsForPaymentFields Error";
		return "getDetailsForPaymentFields " + s;
	}
	/**
	 * This method gets the information about the different reports.
	 * There are 4 typed of reports: orders, inventory, deliveries, and clients.
	 * The information about the reports inclines: the month of the report, the year of the report, the area of the report,
	 * 											   the facility of the report, and the data for the charts in the report.
	 * Some of the reports need all this information, others need just some of it.
	 * @param type - the type of the report
	 * @param month - the month of the report
	 * @param year - the year of the report
	 * @param facility - the facility of the report
	 * @param area - the are of the report
	 * @return "checktShowReports Error" (if the report is not available in the data base) OR a string with all the information about the report.
	 * @throws SQLException
	 */
	public static String checktShowReports(String type, String month, String year, String facility, String area) throws SQLException {
		String s = "";
		switch(type){
		case "Orders":
			String query = "SELECT month, year, barChartInfo, pieReg, pieSub, mostSales FROM monthlyordersyreports WHERE month= \"" + month + "\"" + " AND year= \"" + year + "\"";
			Statement st = conn2.createStatement();
			String sql = (query);
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				s += rs.getString("month") + ".";
				s += rs.getString("year") + ".";
				s += rs.getString("barChartInfo") + ".";
				s += rs.getString("pieReg") + ".";
				s += rs.getString("pieSub") + ".";
				s += area + ".";
				s += rs.getString("mostSales");
			}
			break;
		case "Inventory":
			String query2 = "SELECT month, year, facility, chart FROM monthlyinventoryreports WHERE month= \"" + month + "\"" + " AND year= \"" + year + "\"" + " AND facility= \"" + facility + "\"";
			Statement st2 = conn2.createStatement();
			String sql2 = (query2);
			ResultSet rs2 = st2.executeQuery(sql2);
			while (rs2.next()) {
				s += rs2.getString("month") + ".";
				s += rs2.getString("year") + ".";
				s += rs2.getString("facility") + ".";
				s += rs2.getString("chart");
			}
			break;
		case "Clients":
			String query3 = "SELECT month, year, facility, chart FROM monthlycostumersyreports WHERE month= \"" + month + "\"" + " AND year= \"" + year + "\"" + " AND facility= \"" + facility + "\"";
			Statement st3 = conn2.createStatement();
			String sql3 = (query3);
			ResultSet rs3 = st3.executeQuery(sql3);
			while (rs3.next()) {
				s += rs3.getString("month") + ".";
				s += rs3.getString("year") + ".";
				s += rs3.getString("facility") + ".";
				s += rs3.getString("chart");
			}
			break;
		case "Deliveries":
			String query4 = "SELECT month, year, chart FROM monthlydeliveriesreports WHERE month= \"" + month + "\"" + " AND year= \"" + year + "\"";
			Statement st4 = conn2.createStatement();
			String sql4 = (query4);
			ResultSet rs4 = st4.executeQuery(sql4);
			while (rs4.next()) {
				s += rs4.getString("month") + ".";
				s += rs4.getString("year") + ".";
				s += rs4.getString("chart");
			}
			break;
		}
		if(s.equals(""))
			return "checktShowReports Error";
		return "checktShowReports " + s;
	}
	/**
	 * This method gets all the details about distant orders for delivery that need to be approved by the deliveries operator.
	 * @param area - the name of the area
	 * @return "getDeliverirsToApprove Empty" (in case there are no deliveries that need to be approved) OR a string with all the details 
	 * 																										about the orders for delivery.
	 * @throws SQLException
	 */
	public static String getDeliverirsToApprove(String area) throws SQLException {
		String res = "";
		if(area.equals("north")){
			res = getDeliveryToApprovePerLocation("haifa");
			String s = getDeliveryToApprovePerLocation("karmiel");
			if(!s.equals(""))
				res = res + s;
		}
		else if(area.equals("south")) {
			res = getDeliveryToApprovePerLocation("beersheva");
			String s = getDeliveryToApprovePerLocation("eilat");
			if(!s.equals(""))
				res = res + s;
		}
		else if(area.equals("uae")) {
			res = getDeliveryToApprovePerLocation("dubai");
			String s = getDeliveryToApprovePerLocation("abudhabi");
			if(!s.equals(""))
				res = res + s;
		}
		res = res.replace(",,", ",");
		if(res.trim().isEmpty())
			return "getDeliverirsToApprove Empty";
		return "getDeliverirsToApprove " + res;
	}
	/**
	 * This method gets all the necessary details for distant orders for delivery that need to be approved by the deliveries operator.
	 * The details include: the costumer ID number, the delivery address, and the client's phone number
	 * @param facility - the name of the facility
	 * @return a string with all the details of the orders for delivery.
	 * @throws SQLException
	 */
	private static String getDeliveryToApprovePerLocation(String facility) throws SQLException {
		String query = "SELECT orderNumber FROM orders WHERE facility='" + facility + "' AND approveByDeliveryOperator='not_approved'";
		Statement st = conn2.createStatement();
		Statement st2 = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		String s = "";
		while (rs.next()) {
			s += facility + ",";
			String orderNumber = rs.getString("orderNumber");
			s += orderNumber + ",";
			String query2 = "SELECT address, costumerID, reciverPhoneNumber FROM deliveries WHERE orderNumber='" + orderNumber + "'";
			ResultSet rs2 = st2.executeQuery(query2);
			if(rs2.next()) {
				s+= rs2.getString("costumerID") + ",";
				s+= rs2.getString("address") + ",";
				s+= rs2.getString("reciverPhoneNumber") + ",";
			}
		}
		return s;
	}
	/**
	 * This method updates in the data base that a certain delivery is approved by the delivery operator.
	 * @param orderNumber - the number of the order
	 * @param estimateDeliveryDate - the estimated date in which the delivery is expected to arrive
	 * @return "approveDelivery Error" (in case of an error) OR "approveDelivery Approve" (in case of a successful update)
	 * @throws SQLException
	 */
	public static String approveDelivery(String orderNumber, String estimateDeliveryDate) throws SQLException {
		String query = "UPDATE orders SET approveByDeliveryOperator='approved', estimateDeliveryDate='" + estimateDeliveryDate + "' "
				+ "WHERE orderNumber='" + orderNumber + "'";
		System.out.println(query);
		Statement st = conn2.createStatement();
		int check = st.executeUpdate(query);
		if (check == 0) {
			return "approveDelivery Error";
		}
		return "approveDelivery Approve";
	}
	/**
	 * This method changes the delivery status to "done" in a certain area (by the delivery operator)
	 * @param area - the name of the area
	 * @return "getDeliverirsToClose Empty" (in case there are no deliveries that need a status change) OR a string with the details about the 
	 * 																				deliveries for which the status needs to be changed to "Done".
	 * @throws SQLException
	 */
	public static String getDeliverirsToClose(String area) throws SQLException {
		String res = "";
		if(area.equals("north")){
			res = getDeliveryToClosePerLocation("haifa");
			String s = getDeliveryToClosePerLocation("karmiel");
			if(!s.equals(""))
				res = res + s;
		}
		else if(area.equals("south")) {
			res = getDeliveryToClosePerLocation("beersheva");
			String s = getDeliveryToClosePerLocation("eilat");
			if(!s.equals(""))
				res = res + s;
		}
		else if(area.equals("uae")) {
			res = getDeliveryToClosePerLocation("dubai");
			String s = getDeliveryToClosePerLocation("abudhabi");
			if(!s.equals(""))
				res = res + s;
		}
		res = res.replace(",,", ",");
		if(res.trim().isEmpty())
			return "getDeliverirsToClose Empty";
		return "getDeliverirsToClose " + res;
	}
	/**
	 * This method get the details of the deliveries for which the status needs to be changed to "Done".
	 * The details include: the number of the order, the costumer ID number and the estimated delivery date.
	 * @param facility - the name of the facility
	 * @return a string with the details about the deliveries for which the status needs to be changed to "Done".
	 * @throws SQLException
	 */
	private static String getDeliveryToClosePerLocation(String facility) throws SQLException {
		String query = "SELECT orderNumber, costumerID, estimateDeliveryDate FROM orders WHERE facility='" + facility 
				+ "' AND confirmationDate != 'null' AND orderStatus='notDone'";
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		String s = "";
		while (rs.next()) {
			s += facility + ",";
			s += rs.getString("orderNumber") + ",";
			s += rs.getString("costumerID") + ",";
			s += rs.getString("estimateDeliveryDate") + ",";
		}
		return s;
	}
	/**
	 * This method updates the status of a certain delivery to "done" in the data base.
	 * @param orderNumber - the number of the order
	 * @return "changeStatusToDoneForDelivery Error" (in case of an error) OR "changeStatusToDoneForDelivery Change" (in case the update
	 * 																														was successful).
	 * @throws SQLException
	 */
	public static String changeStatusToDoneForDelivery(String orderNumber) throws SQLException {
		String query = "UPDATE orders SET orderStatus='Done' WHERE orderNumber='" + orderNumber + "'";
		Statement st = conn2.createStatement();
		int check = st.executeUpdate(query);
		if (check == 0) {
			return "changeStatusToDoneForDelivery Error";
		}
		return "changeStatusToDoneForDelivery Change";
	}
	/**
	 * This method get the details about all the products that a certain operation worker needs to refill.
	 * @param employeeName - the name of the employee (operation worker)
	 * @return "getProdutToFillForOprationWorker Empty" (in case there are no products that need to be re-stocked) OR a string with 
	 * 															details about the products that the operation worker needs to refill.
	 * @throws SQLException
	 */
	public static String getProdutToFillForOprationWorker(String employeeName) throws SQLException {
		String res = "";
		res += getProdutToFillForOprationWorkerPerLocation("haifa_products", employeeName);
		res += getProdutToFillForOprationWorkerPerLocation("karmiel_products", employeeName);
		res += getProdutToFillForOprationWorkerPerLocation("eilat_products", employeeName);
		res += getProdutToFillForOprationWorkerPerLocation("beersheva_products", employeeName);
		res += getProdutToFillForOprationWorkerPerLocation("dubai_products", employeeName);
		res += getProdutToFillForOprationWorkerPerLocation("abudhabi_products", employeeName);
		if(res.equals(""))
			return "getProdutToFillForOprationWorker Empty";
		return "getProdutToFillForOprationWorker " + res;
	}
	/**
	 * This method gets the details about the products that the operation worker needs to refill in a certain facility.
	 * @param facility - the facility name
	 * @param employeeName - the name of the employee (operation worker)
	 * @return a string with details about the products that the operation worker needs to refill
	 * @throws SQLException
	 */
	private static String getProdutToFillForOprationWorkerPerLocation(String facility, String employeeName) throws SQLException {
		String[] location = facility.split("_");
		String query = "SELECT id, productName FROM " + facility
				+ " WHERE refill='1' AND taskStatus='notDone' AND inCharge='" + employeeName + "'";
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		String s = "";
		while (rs.next()) {
			s += rs.getString("id") + ",";
			s += rs.getString("productName") + ",";
			s += location[0] + ",";
		}
		return s;
	}
	/**
	 * This method gets the threshold level and the current amount a certain product in the chosen facility.
	 * @param productID - the product ID number 
	 * @param facility - the facility name
	 * @return "getRelatadFieldsForProduct Empty" (in case the product is not found) OR a string with the details about the wanted product.
	 * @throws SQLException
	 */
	public static String getRelatadFieldsForProduct(String productID, String facility) throws SQLException {
		String facilityLocation = facility+"_products";
		String query = "SELECT minimumAmount, amount FROM " + facilityLocation + " WHERE id='" + productID + "'";
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		String s = "";
		if (rs.next()) {
			s += rs.getString("minimumAmount") + ",";
			s += rs.getString("amount") + ",";
		}
		if(s.equals(""))
			return "getRelatadFieldsForProduct Empty";
		return "getRelatadFieldsForProduct " + s;
	}
	/**
	 * This method updates the amount of a certain product in the chosen facility.
	 * @param facility - the name of the facility
	 * @param productName - the name of the product
	 * @param newAmount - the new value to enter as the quantity of the product in the chosen facility
	 * @return "updateInventory Error" (in case of an error) OR "updateInventory Update" (in case the update was successful)
	 * @throws SQLException
	 */
	public static String updateInventory(String facility,String productName,String newAmount) throws SQLException {
		String facilityLocation = facility+"_products";
		String query = "UPDATE " + facilityLocation + " SET amount=\"" + newAmount + "\","
				+ " refill='0', inCharge='null', taskStatus='Done' WHERE productName=\"" + productName + "\"";
		Statement stmt = conn2.createStatement();
		int check = stmt.executeUpdate(query);
		
		query = "SELECT countThresholdInMonth FROM " + facilityLocation + " WHERE productName='" + productName + "'";
		stmt = conn2.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		int count = 0;
		if(rs.next()) {
			count = rs.getInt("countThresholdInMonth");
		}
		count++;
		System.out.println("count=" + count);
		query = "UPDATE " + facilityLocation + " SET countThresholdInMonth='" + count + "' WHERE productName='" + productName + "'";
		stmt = conn2.createStatement();
		stmt.executeUpdate(query);		
		
		if (check == 0) {
			return "updateInventory Error";
		}
		return "updateInventory Update";
	}
	/**
	 * This method gets the details about all the users that are not approved in a certain area.
	 * @param area - the name of the area
	 * @return "getUsersAreNotApprove Empty" (in case there are no not approved users) OR a string with all the details of the not approved clients.
	 * @throws SQLException
	 */
	public static String getUsersAreNotApprove(String area) throws SQLException {
		String res = "";
		if(area.equals("north")){
			res = getUsersToApprovePerLocation("haifa");
			String s = getUsersToApprovePerLocation("karmiel");
			if(!s.equals(""))
				res = res + s;
		}
		else if(area.equals("south")) {
			res = getUsersToApprovePerLocation("beersheva");
			String s = getUsersToApprovePerLocation("eilat");
			if(!s.equals(""))
				res = res + s;
		}
		else if(area.equals("uae")) {
			res = getUsersToApprovePerLocation("dubai");
			String s = getUsersToApprovePerLocation("abudhabi");
			if(!s.equals(""))
				res = res + s;
		}
		res = res.replace(",,", ",");
		res = res.replace(" ", "");
		res = res.replaceFirst(",", "");
		System.out.println("res2=" + res);
		if(res.trim().isEmpty())
			return "getUsersAreNotApprove Empty";
		return "getUsersAreNotApprove " + res;
	}
	/**
	 * This method gets the details about all the users that are not approved in a certain facility.
	 * @param facility - the name of the facility
	 * @return a string with all the details of the not approved clients.
	 * @throws SQLException
	 */
	private static String getUsersToApprovePerLocation(String facility) throws SQLException {
		String query = "SELECT id FROM clients WHERE haveAccount='yes' AND approveByManager='not_approved'";
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		String s = "";
		while (rs.next()) {
			s += rs.getString("id") + ",";
		}
		String[] result = s.split(",");
		String res = "";
		for(int i=0; i<result.length; i++) {
			res += getUserWithNoAccountAndNeedManagerApproveDetails(result[0], facility) + ",";
		}
		System.out.println("res=" + res);
		return res.replace(",,", ",");
	}
	/**
	 * This method gets all the details about the users that have no account in the system and wait for the area manager approval
	 * The details include: the user's user name, the user's password, the user's first name, the user's last name,
	 * 						the user's ID number, the user's credit card number, the user's phone number, 
	 * 						and the user's email address.
	 * @param id - the user's ID number
	 * @param facility - the name of the facility
	 * @return a string with all the details about all the users that have no account in the system
	 * @throws SQLException
	 */
	private static String getUserWithNoAccountAndNeedManagerApproveDetails(String id, String facility) throws SQLException {
		String query = "SELECT username, password, firstName, lastName, id, creditCard, phoneNumber, email FROM users "
				+ "WHERE status='notApprovedClient' AND note='" + facility + "' AND id='" + id + "'";
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		String s = "";
		while (rs.next()) {
			s += rs.getString("username") + ",";
			s += rs.getString("password") + ",";
			s += rs.getString("firstName") + ",";
			s += rs.getString("lastName") + ",";
			s += rs.getString("id") + ",";
			s += rs.getString("creditCard") + ",";
			s += rs.getString("phoneNumber") + ",";
			s += rs.getString("email") + ",";
		}
		System.out.println("s=" + s);
		return s;
	}
	/**
	 * This method approves a user as a client (an action that the area mangers do in the system)
	 * @param id - the client's ID number
	 * @return "approveByManager Error" (in case of an error) OR "approveByManager Update" (in case the approval was successful).
	 * @throws SQLException
	 */
	public static String  approveByManager(String id) throws SQLException {
		String query = "UPDATE clients SET approveByManager='approved' WHERE id='" + id + "'";
		Statement st = conn2.createStatement();
		String query2 = "UPDATE users SET status='Regular_Client' WHERE id='" + id + "'";
		Statement st2 = conn2.createStatement();
		int check = st.executeUpdate(query);
		int check2 = st2.executeUpdate(query2);
		if (check == 0 || check2 == 0) {
			return "approveByManager Error";
		}
		return "approveByManager Update";
	}
	/**
	 * This method creates a new sale on a certain product in a certain facility. The new sale is initiated by the marketing manager.
	 * @param productId - the ID number of the product
	 * @param productName - the name of the product
	 * @param area - the area of the sale
	 * @param startDate - the start date and time of the sale
	 * @param endDate - the end date and time of the sale
	 * @param originalPrice - the original price of the product
	 * @param newPrice - the new price of the product (after the discount is applied)
	 * @return "test Error" (in case of an error) OR "test OK" (in case the sale initiation was successful).
	 * @throws SQLException
	 */
	public static String InitSale(String productId, String productName, String area, String startDate, String endDate, String originalPrice, String newPrice) throws SQLException {
		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String currentDate = myDateObj.format(myFormatObj);
		String query = "SELECT saleID FROM sales WHERE productID = '" + productId + "' AND area ='" + area + "'" 
				+ " AND startDate<= '" + currentDate + "'" + " AND endDate>= '" + currentDate + "'";
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		if (rs.next()) {
			return "test Exist";
		}
		query = "SELECT MAX(saleID) AS largestID FROM sales";
		rs = st.executeQuery(query);
		int newSaleId;
		String s = "";
		if (rs.next()) {
			s+= rs.getString("largestID");
			newSaleId = Integer.parseInt(s) + 1;
		}
		else newSaleId = 1;
		String[] startdate = startDate.split("\\_");
		String[] enddate = endDate.split("\\_");
		query = "INSERT INTO sales (saleID, productID, productName, area, startDate, endDate, originalPrice, newPrice, apply)"
				+ " VALUES ('" + newSaleId + "','" + productId + "','" + productName + "','" + area + "','" + startdate[0]+" "+startdate[1] + "','" 
				+ enddate[0]+" "+enddate[1] + "','" + originalPrice + "','" + newPrice + "','no')";
		int check = st.executeUpdate(query);
		if(check == 0 )
			return "test Error";
		return "test OK";	
	}
	/**
	 * This method gets the details of a client's order. The details include: the total price of the order and the content of the order.
	 * @param costumerID - the costumer ID number
	 * @param supplyMethod - the order type (on site / pick up later / for delivery)
	 * @param orderCode - the order code 
	 * @param facility - the name of the facility
	 * @return "getOrderDetailsForUser Empty" (in case the order is not found) OR a string with the order's details.
	 * @throws SQLException
	 */
	public static String getOrderDetailsForUser(String costumerID, String supplyMethod, String orderCode, String facility) throws SQLException {
		String query = "";
		if(supplyMethod.equals("pick_up")) {
			query = "SELECT price, orderContent FROM orders WHERE costumerID = '" + costumerID + "' AND supplyMethod='" + supplyMethod + "'" 
					+ " AND facility='" + facility + "'" + " AND orderNumber='" + orderCode + "'" + " AND orderStatus='notDone'";
		}
		else if(supplyMethod.equals("delivery")) {
			query = "SELECT price, orderContent FROM orders WHERE costumerID = '" + costumerID + "' AND supplyMethod='" + supplyMethod + "'" 
					+ " AND facility='" + facility + "'" + " AND orderNumber='" + orderCode + "'" + " AND orderStatus='notDone' AND approveByDeliveryOperator='approved'";
		}
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		String s = "";
		if(rs.next()) {
			s+= rs.getString("price") + "&";
			s+= rs.getString("orderContent");
		}
		if(s.equals(""))
			return "getOrderDetailsForUser Empty";
		return "getOrderDetailsForUser " + s;
	}
	/**
	 * This method updates the order status in the data base to "done" (when the client picks up the distant order they made).
	 * @param orderCode - the order code
	 * @return "getOrderDetailsForUser Empty" (in case of an error) OR "setPickUpOrderToDone Done" (in case the update was successful).
	 * @throws SQLException
	 */
	public static String setPickUpOrderToDone(String orderCode) throws SQLException {
		String query = "UPDATE orders SET orderStatus='Done' WHERE orderNumber='" + orderCode + "' AND orderStatus='notDone'";
		Statement st = conn2.createStatement();
		int check = st.executeUpdate(query);
		if (check == 0) {
			return "setPickUpOrderToDone Error";
		}
		return "setPickUpOrderToDone Done";
	}
	/**
	 * This method updates in the data base the date that the client got their order for delivery (as a confirmation).
	 * @param orderCode - the order code
	 * @return "setConfirmationDateForDeliveryOrder Error" (in case of an error) OR "setConfirmationDateForDeliveryOrder Approve" 
	 * 																						(in case the update was successful).
	 * @throws SQLException
	 */
	public static String setConfirmationDateForDeliveryOrder(String orderCode) throws SQLException {
		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String confirmationDate = myDateObj.format(myFormatObj);
		String query = "UPDATE orders SET confirmationDate='" + confirmationDate + "' WHERE orderNumber='" + orderCode 
				+ "' AND orderStatus='notDone' AND confirmationDate = 'null'";
		Statement st = conn2.createStatement();
		int check = st.executeUpdate(query);
		if (check == 0) {
			return "setConfirmationDateForDeliveryOrder Error";
		}
		return "setConfirmationDateForDeliveryOrder Approve";
	}
	/**
	 * This method is a simulation for touch login - in generates a random user from the subscribed client's and logs them into the system
	 * @return "setSubscriberForTouchSimulation Error" (in case of an error) OR a string with the client's details.
	 * @throws SQLException
	 */
	public static String setSubscriberForTouchSimulation() throws SQLException {
		int startSubNumber = 100;
		String query = "SELECT MAX(subscriberNumber) AS largestSubscriberNumber FROM subscribers";
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		rs = st.executeQuery(query);
		int endSubNumber = 0;
		String s = "";
		if (rs.next()) {
			s+= rs.getString("largestSubscriberNumber");
			endSubNumber = Integer.parseInt(s);
		}
		int randomSubNumber = (int)Math.floor(Math.random() * (endSubNumber - startSubNumber + 1) + startSubNumber);
		String subsciberNumber = String.valueOf(randomSubNumber);
		query = "SELECT id, username, password, firstName, lastName FROM subscribers WHERE subscriberNumber= '" + subsciberNumber + "'";
		rs = st.executeQuery(query);
		String id = "";
		s = "";
		if(rs.next()) {
			id = rs.getString("id");
			s+= id + ",";
			s+= rs.getString("username") + ",";
			s+= rs.getString("password") + ",";
			s+= rs.getString("firstName") + ",";
			s+= rs.getString("lastName") + ",";
		}
		query = "SELECT note, isLogin FROM users WHERE id= '" + id + "'";
		rs = st.executeQuery(query);
		if(rs.next()) {
			s+= rs.getString("note") + ",";
			s+= rs.getString("isLogin") + ",";
			if(rs.getString("isLogin").equals("0")) {
				String query2 = "UPDATE users SET isLogin=1 WHERE id= '" + id + "'";
				st.executeUpdate(query2);
			}
		}
		if(s.equals(""))
			return "setSubscriberForTouchSimulation Error";
		return "setSubscriberForTouchSimulation " + s;
	}
	/**
	 * This method is a "import external data" simulation - it takes all the external data and enters it into the data base (users table). 
	 * @param id - the user's ID number
	 * @param firstName - the user's  first name
	 * @param lastName - the user's last name 
	 * @param phoneNumber - the user's phone number
	 * @param email - the user's email address 
	 * @param job - the user's job title
	 * @param username - the user's user name
	 * @param password - the user's password
	 * @param subscriberNumber - the user's subscription number 
	 * @param creditCard - the user's credit card number
	 * @param status - the user's status (employee / regular client / subscriber)
	 * @param note - the user's area
	 * @param isLogin - the user's login status (binary value 0/1)
	 * @return "Unable to add user to table" (in case of an error) OR "User added successfully" (in case of success)
	 * @throws SQLException
	 */
	public static String  addUser(String id,String firstName,String lastName,String phoneNumber,String email,String job,String username,String password,String subscriberNumber,String creditCard,String status ,String note,String isLogin) throws SQLException {
		String query = "INSERT INTO users(id,firstName,lastName,phoneNumber,email,job,username,password,subscriberNumber,creditCard,status,note,isLogin)"
				+ " VALUES ('"+id+"','"+firstName+"','"+lastName+"','"+phoneNumber+"','"
				+email+"','"+job+"','"+username+"','"+password+"','"+subscriberNumber
				+"','"+creditCard+"','"+status+"','"+note+"','"+isLogin+"')";
		Statement st = conn2.createStatement();
		int check = st.executeUpdate(query);
		if (check == 0) {
			return "Unable to add user to table";
		}
		return "User added successfully";
	}
	/**
	 * This method is a "import external data" simulation - it takes the external data about the clients and enters it into the 
	 * 																									data base (clients table).
	 * @param nameOnCard - the user's name on their credit card 
	 * @param userName - the user's user name
	 * @param password - the user's password
	 * @param expirationDate - the user's credit card expiration date
	 * @param cvv - the user's security number on their credit card 
	 * @param firstName - the user's  first name
	 * @param lastName - the user's last name 
	 * @param id - the user's ID number
	 * @param creditCard - the user's credit card number
	 * @param phone - the user's phone number
	 * @param email - the user's email address 
	 * @param approvedByManager - if the user's account is approved by the area manager
	 * @param haveAccount - if the user has an account in the system
	 * @param address - the user's address
	 * @param numberOfpurchases - the number of purchases the user made in the system
	 * @return "Unable to add client to table" (in case of an error) OR "Client added successfully" (in case of success)
	 * @throws SQLException
	 */
	public static String  addClient(String nameOnCard,String userName,String password,String expirationDate,String cvv,String firstName,String lastName,String id,String creditCard,String phone,String email,String approvedByManager,String haveAccount,String address,String numberOfpurchases) throws SQLException {
		String query = "INSERT INTO clients(id,userName,password,firstName,lastName,nameOnCard,creditCard,expirationDate,cvv,phone,email,approveByManager,haveAccount,address,numberOfpurchases)"
				+ " VALUES ('"+id+"','"+userName+"','"+password+"','"+firstName+"','"+lastName+"','"
				+nameOnCard+"','"+creditCard+"','"+expirationDate+"','"+cvv+"','"+phone+"','"+email+"','"+approvedByManager
				+"','"+haveAccount+"','"+address+"','"+numberOfpurchases+"')";
		Statement st = conn2.createStatement();
		int check = st.executeUpdate(query);
		if (check == 0) {
			return "Unable to add client to table";
		}
		return "Client added successfully";
	}
	/**
	 * This method gathers the data for the monthly clients report.
	 * @throws SQLException
	 */
	public static void addCostumerReport() throws SQLException {
		String query = "SELECT id FROM users WHERE job='Client'";
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		String IDs = "";
		while(rs.next()) {
			IDs += rs.getString("id") + ",";
		}
		String[] clientsID = IDs.split("\\,");
		query = "SELECT MAX(id) AS maxID FROM users WHERE job='Client'";
		st = conn2.createStatement();
		rs = st.executeQuery(query);
		createCostumerReportForEachFacility("haifa", clientsID);
		createCostumerReportForEachFacility("karmiel", clientsID);
		createCostumerReportForEachFacility("beersheva", clientsID);
		createCostumerReportForEachFacility("eilat", clientsID);
		createCostumerReportForEachFacility("dubai", clientsID);
		createCostumerReportForEachFacility("abudhabi", clientsID);
	}
	/**
	 * This method generates the data for the monthly clients report each month.
	 * @param facility - the name of the facility
	 * @param clientsID
	 * @throws SQLException
	 */
	private static void createCostumerReportForEachFacility(String facility, String[] clientsID) throws SQLException {
		int[] arrayChart = {0,0,0,0,0,0};
		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String currentDate = myDateObj.format(myFormatObj);
		String[] currDate = currentDate.split("\\-");
		String startDate = currDate[0] + "-" + currDate[1] + "-01";
		String endDate = currDate[0] + "-" + currDate[1] + "-31";
		for(int i=0; i<clientsID.length; i++) {
			String query = "SELECT costumerID FROM orders WHERE costumerID='" + clientsID[i] + "' AND facility='" + facility
					+ "' AND orderDate>='" + startDate + "' AND orderDate<='" + endDate + "'";
			Statement st = conn2.createStatement();
			ResultSet rs = st.executeQuery(query);
			int countOrderForCostumer = 0;
			while(rs.next()) {
				countOrderForCostumer++;
			}
			if(countOrderForCostumer != 0)
				arrayChart[countOrderForCostumer/5]++;
		}
		String query2 = "SELECT MAX(reportID) AS largestReportID FROM monthlycostumersyreports";
		Statement st2 = conn2.createStatement();
		ResultSet rs2 = st2.executeQuery(query2);
		int newReportID;
		String s = "";
		if (rs2.next()) {
			s += rs2.getString("largestReportID");
			newReportID = Integer.parseInt(s) + 1;
		}
		else newReportID = 1;
		LocalDate currentdate = LocalDate.now();
		Month currentMonth = currentdate.getMonth();
		String Month = currentMonth.toString();
		Month = Month.toLowerCase();
		StringBuffer month = new StringBuffer(Month);   
		month.setCharAt(0, Character.toUpperCase(month.charAt(0)));
		int currentYear = currentdate.getYear();
		StringBuffer facility1 = new StringBuffer(facility);   
		facility1.setCharAt(0, Character.toUpperCase(facility1.charAt(0)));
		String chart = "";
		for(int i=0; i<arrayChart.length; i++) {
			if(i == arrayChart.length-1)
				chart += arrayChart[i];
			else
				chart += arrayChart[i] + ",";
		}
		String query3 = "INSERT INTO monthlycostumersyreports (reportID, month, year, facility, chart)"
				+ " VALUES ('" + newReportID + "','" + month + "','" + String.valueOf(currentYear) + "','" + facility1 + "','" + chart +"')";
		st2 = conn2.createStatement();
		st2.executeUpdate(query3);
	}
	/**
	 * This method generates the data for the monthly delivery report each month.
	 * @throws SQLException
	 */
	public static void createDeliveryOrder() throws SQLException {
		String chart = "North,";
		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String currentDate = myDateObj.format(myFormatObj);
		String[] currDate = currentDate.split("\\-");
		String startDate = currDate[0] + "-" + currDate[1] + "-01";
		String endDate = currDate[0] + "-" + currDate[1] + "-31";
		String query = "SELECT orderNumber FROM orders WHERE facility='haifa' AND supplyMethod='delivery'"
				+ " AND orderDate>='" + startDate + "' AND orderDate<='" + endDate + "'";
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		int countDelivery = 0;
		while(rs.next()) {
			countDelivery++;
		}
		chart += countDelivery + ",";
		query = "SELECT orderNumber FROM orders WHERE facility='eilat' AND supplyMethod='delivery'"
				+ " AND orderDate>='" + startDate + "' AND orderDate<='" + endDate + "'";
		st = conn2.createStatement();
		rs = st.executeQuery(query);
		countDelivery = 0;
		while(rs.next()) {
			countDelivery++;
		}
		chart += "South," + countDelivery + ",";
		query = "SELECT orderNumber FROM orders WHERE facility='dubai' AND supplyMethod='delivery'"
				+ " AND orderDate>='" + startDate + "' AND orderDate<='" + endDate + "'";
		st = conn2.createStatement();
		rs = st.executeQuery(query);
		countDelivery = 0;
		while(rs.next()) {
			countDelivery++;
		}
		chart += "UAE," + countDelivery;
		String query2 = "SELECT MAX(reportID) AS largestReportID FROM monthlydeliveriesreports";
		Statement st2 = conn2.createStatement();
		ResultSet rs2 = st2.executeQuery(query2);
		int newReportID;
		String s = "";
		if (rs2.next()) {
			s += rs2.getString("largestReportID");
			newReportID = Integer.parseInt(s) + 1;
		}
		else newReportID = 1;
		LocalDate currentdate = LocalDate.now();
		Month currentMonth = currentdate.getMonth();
		String Month = currentMonth.toString();
		Month = Month.toLowerCase();
		StringBuffer month = new StringBuffer(Month);   
		month.setCharAt(0, Character.toUpperCase(month.charAt(0)));
		int currentYear = currentdate.getYear();
		String query3 = "INSERT INTO monthlydeliveriesreports (reportID, year, month, chart)"
				+ " VALUES ('" + newReportID + "','" +  String.valueOf(currentYear) + "','" +month + "','" + chart +"')";
		st2 = conn2.createStatement();
		st2.executeUpdate(query3);
	}
	/**
	 * This method generates the data for the monthly orders report each month.
	 * @throws SQLException
	 */
	public static void createOrderReport() throws SQLException {
		String barChartInfo = "";
		barChartInfo += createBarChartOrder("haifa", "Haifa") + ",";
		barChartInfo += createBarChartOrder("karmiel", "Karmiel") + ",";
		barChartInfo += createBarChartOrder("eilat", "Eilat") + ",";
		barChartInfo += createBarChartOrder("beersheva", "BeerSheva") + ",";
		barChartInfo += createBarChartOrder("dubai", "Dubai") + ",";
		barChartInfo += createBarChartOrder("abudhabi", "AbuDhabi") + ",";
		String query = "SELECT id FROM users WHERE status='Regular_Client'";
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		String IDs = "";
		while(rs.next()) {
			IDs += rs.getString("id") + ",";
		}
		String[] clientsID = IDs.split("\\,");
		String pieReg = "";
		pieReg += "HaifaRegular," + createPieForOrderReport("haifa", clientsID) + ",";
		pieReg += "KarmielRegular," + createPieForOrderReport("karmiel", clientsID) + ",";
		pieReg += "EilatRegular," + createPieForOrderReport("eilat", clientsID) + ",";
		pieReg += "BeerShevaRegular," + createPieForOrderReport("beersheva", clientsID) + ",";
		pieReg += "DubaiRegular," + createPieForOrderReport("dubai", clientsID) + ",";
		pieReg += "AbuDhabiRegular," + createPieForOrderReport("abudhabi", clientsID);
		query = "SELECT id FROM users WHERE status='Subscriber_Client'";
		st = conn2.createStatement();
		rs = st.executeQuery(query);
		IDs = "";
		while(rs.next()) {
			IDs += rs.getString("id") + ",";
		}
		clientsID = IDs.split("\\,");
		String pieSub = "";
		pieSub += "HaifaSubscriber," + createPieForOrderReport("haifa", clientsID) + ",";
		pieSub += "KarmielSubscriber," + createPieForOrderReport("karmiel", clientsID) + ",";
		pieSub += "EilatSubscriber," + createPieForOrderReport("eilat", clientsID) + ",";
		pieSub += "BeerShevaSubscriber," + createPieForOrderReport("beersheva", clientsID) + ",";
		pieSub += "DubaiSubscriber," + createPieForOrderReport("dubai", clientsID) + ",";
		pieSub += "AbuDhabiSubscriber," + createPieForOrderReport("abudhabi", clientsID);
		String query2 = "SELECT MAX(reportID) AS largestReportID FROM monthlyordersyreports";
		Statement st2 = conn2.createStatement();
		ResultSet rs2 = st2.executeQuery(query2);
		int newReportID;
		String s = "";
		if (rs2.next()) {
			s += rs2.getString("largestReportID");
			newReportID = Integer.parseInt(s) + 1;
		}
		else newReportID = 1;
		LocalDate currentdate = LocalDate.now();
		Month currentMonth = currentdate.getMonth();
		String Month = currentMonth.toString();
		Month = Month.toLowerCase();
		StringBuffer month = new StringBuffer(Month);   
		month.setCharAt(0, Character.toUpperCase(month.charAt(0)));
		int currentYear = currentdate.getYear();
		String query3 = "INSERT INTO monthlyordersyreports (reportID, month, year, barChartInfo, pieReg, pieSub)"
				+ " VALUES ('" + newReportID + "','" +  month + "','" +String.valueOf(currentYear) + "','" + barChartInfo
				+ "','" + pieReg + "','" + pieSub + "')";
		st2 = conn2.createStatement();
		st2.executeUpdate(query3);
	}
	/**
	 * This method gathers the data for the pie chart in the monthly orders report.
	 * @param facility - the name of the facility
	 * @param clientsID
	 * @return the data for the pie chart in the monthly orders report.
	 * @throws SQLException
	 */
	private static int createPieForOrderReport(String facility, String[] clientsID) throws SQLException {
		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String currentDate = myDateObj.format(myFormatObj);
		String[] currDate = currentDate.split("\\-");
		String startDate = currDate[0] + "-" + currDate[1] + "-01";
		String endDate = currDate[0] + "-" + currDate[1] + "-31";
		int countOrderForCostumer = 0;
		for(int i=0; i<clientsID.length; i++) {
			String query = "SELECT costumerID FROM orders WHERE costumerID='" + clientsID[i] + "' AND facility='" + facility
					+ "' AND orderDate>='" + startDate + "' AND orderDate<='" + endDate + "' AND (supplyMethod='pick_up' OR supplyMethod='onSite')";
			Statement st = conn2.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				countOrderForCostumer++;
				break;
			}
		}
		return countOrderForCostumer;
	}
	/**
	 * This method gathers the data for the bar chart in the monthly orders report.
	 * @param facility - the name of the facility
	 * @param facilityName - the name of the facility + the type of order (on site / pick up latter) as it appears in the data base of
	 * 																												the orders report.
	 * @return the data for the bar chart in the monthly orders report.
	 * @throws SQLException
	 */
	private static String createBarChartOrder(String facility, String facilityName) throws SQLException {
		String res = "";
		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String currentDate = myDateObj.format(myFormatObj);
		String[] currDate = currentDate.split("\\-");
		String startDate = currDate[0] + "-" + currDate[1] + "-01";
		String endDate = currDate[0] + "-" + currDate[1] + "-31";
		String query = "SELECT orderNumber FROM orders WHERE facility='" + facility + "' AND supplyMethod='pick_up'"
				+ " AND orderDate>='" + startDate + "' AND orderDate<='" + endDate + "'";
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		int countPickUp = 0;
		while(rs.next()) {
			countPickUp++;
		}
		res += facilityName + "PUL," + countPickUp + ",";
		query = "SELECT orderNumber FROM orders WHERE facility='" + facility + "' AND supplyMethod='onSite'"
				+ " AND orderDate>='" + startDate + "' AND orderDate<='" + endDate + "'";
		st = conn2.createStatement();
		rs = st.executeQuery(query);
		int countOnSite = 0;
		while(rs.next()) {
			countOnSite++;
		}
		res += facilityName + "OS," + countOnSite;
		return res;
	}
	/**
	 * This method makes sure that no report is generated more than once.
	 * @param type - the type of the report (orders / inventory / delivery/ clients)
	 * @return true (if there is already a report for a certain month in a certain year) OR false (otherwise)
	 * @throws SQLException
	 */
	public static boolean checkIfThereIsReport(String type, String dateReport) throws SQLException {
		String[] datereport = dateReport.split("\\-");
		String monthReport = getMonth(datereport[1]);
		
		String query = "SELECT reportID FROM " + type + " WHERE month='" + monthReport + "' AND year='" + datereport[0] + "'";
		//System.out.println(query);
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		if(rs.next())
			return true;
		return false;
	}
	
	private static String getMonth(String month) {
		switch(month) {
		case "01":
			month = "January";
			break;
		case "02":
			month = "February";
			break;
		case "03":
			month = "March";
			break;
		case "04":
			month = "April";
			break;
		case "05":
			month = "May";
			break;
		case "06":
			month = "June";
			break;
		case "07":
			month = "July";
			break;
		case "08":
			month = "August";
			break;
		case "09":
			month = "September";
			break;
		case "10":
			month = "October";
			break;
		case "11":
			month = "November";
			break;
		case "12":
			month = "December";
			break;
		}
		return month;
	}
	
	public static void createInventoryReport() throws SQLException {
		createInventoryReportForFacility("haifa_products", "Haifa");
		createInventoryReportForFacility("karmiel_products", "Karmiel");
		createInventoryReportForFacility("eilat_products", "Eilat");
		createInventoryReportForFacility("beersheva_products", "BeerSheva");
		createInventoryReportForFacility("dubai_products", "Dubai");
		createInventoryReportForFacility("abudhabi_products", "AbuDhabi");
	}
	
	private static void createInventoryReportForFacility(String facility, String facilityName) throws SQLException {
		String query = "SELECT productName, countRifillInMonth, countThresholdInMonth, countZeroInMonth FROM " + facility;
		//System.out.println(query);
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);
		int refill = 0, threshold = 0, zero = 0;
		while(rs.next()) {
			refill += rs.getInt("countRifillInMonth");
			threshold += rs.getInt("countThresholdInMonth");
			zero += rs.getInt("countZeroInMonth");
			String productName = rs.getString("productName");
			String queryUpdate = "UPDATE " + facility + " SET countRifillInMonth='0',"
					+ " countThresholdInMonth='0', countZeroInMonth='0' WHERE productName='" + productName + "'";
			//System.out.println(queryUpdate);
			Statement stUpdate = conn2.createStatement();
			stUpdate.executeUpdate(queryUpdate);
		}
		String chart = "threshold," + threshold + ",zero," + zero + ",refill," + refill;
		
		String query2 = "SELECT MAX(reportID) AS largestReportID FROM monthlyinventoryreports";
		//System.out.println(query2);
		Statement st2 = conn2.createStatement();
		ResultSet rs2 = st2.executeQuery(query2);
		int newReportID;
		String s = "";
		if (rs2.next()) {
			s += rs2.getString("largestReportID");
			newReportID = Integer.parseInt(s) + 1;
		}
		else newReportID = 1;
		LocalDate currentdate = LocalDate.now();
		LocalDate earlier = currentdate.minusMonths(1);
		Month currentMonth = earlier.getMonth();
		String Month = currentMonth.toString();
		Month = Month.toLowerCase();
		StringBuffer month = new StringBuffer(Month);   
		month.setCharAt(0, Character.toUpperCase(month.charAt(0)));
		int currentYear = currentdate.getYear();
		if(month.toString().equals("December")) {
			currentYear = currentdate.minusYears(1).getYear();
		}
		String query3 = "INSERT INTO monthlyinventoryreports (reportID, month, year, facility, chart)"
				+ " VALUES ('" + newReportID + "','" +  month + "','" + String.valueOf(currentYear) + "','" + facilityName +"','" + chart + "')";
		st2 = conn2.createStatement();
		st2.executeUpdate(query3);
	}
}
