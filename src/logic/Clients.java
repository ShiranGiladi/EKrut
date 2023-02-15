package logic;

import javafx.beans.property.SimpleStringProperty;

/**
 * This class holds all the information about a client in the system.
 * The information about each client includes: their user name to the system, their password to the system, their first name, their last name,
 * 											   their ID number, their credit card number, their phone number, their email address. 
 *
 */
public class Clients {
	private SimpleStringProperty username, password, firstName, lastName, id, creditCard, phoneNumber, email;
	
	/**
	 * This method is the constructor for Clients
	 * @param username - the client's user name to the system
	 * @param password - the client's password to the system
	 * @param firstName - the client's first name 
	 * @param lastName - the client's last name
	 * @param id - the client's ID number
	 * @param creditCard - the clients' credit card number
	 * @param phoneNumber - the client's phone number
	 * @param email - the client's email address
	 */
	public Clients(String username, String password, String firstName, String lastName, String id, String creditCard,
					String phoneNumber, String email) {
		super();
		this.username = new SimpleStringProperty(username);
		this.password = new SimpleStringProperty(password);
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.id = new SimpleStringProperty(id);
		this.creditCard = new SimpleStringProperty(creditCard);
		this.phoneNumber = new SimpleStringProperty(phoneNumber);
		this.email = new SimpleStringProperty(email);
	}
	/**
	 * 
	 * @return the client's user name to the system
	 */
	public String getUsername() {
		return username.get();
	}
	/**
	 * 
	 * @return the client's password to the system
	 */
	public String getPassword() {
		return password.get();
	}
	/**
	 * 
	 * @return  the client's first name 
	 */
	public String getFirstName() {
		return firstName.get();
	}
	/**
	 * 
	 * @return  the client's last name 
	 */
	public String getLastName() {
		return lastName.get();
	}
	/**
	 * 
	 * @return the client's ID number
	 */
	public String getId() {
		return id.get();
	}
	/**
	 * 
	 * @return the client's credit card number
	 */
	public String getCreditCard() {
		return creditCard.get();
	}
	/**
	 * 
	 * @return the client's phone number
	 */
	public String getPhoneNumber() {
		return phoneNumber.get();
	}
	/**
	 * 
	 * @return the client's email address
	 */
	public String getEmail() {
		return email.get();
	}
	/**
	 * 
	 * @param username - set the client's user name to the system to this value
	 */
	public void setUsername(SimpleStringProperty username) {
		this.username = username;
	}
	/**
	 * 
	 * @param password - set the client's password to the system to this value
	 */
	public void setPassword(SimpleStringProperty password) {
		this.password = password;
	}
	/**
	 * 
	 * @param firstName - set the client's first name to this value
	 */
	public void setFirstName(SimpleStringProperty firstName) {
		this.firstName = firstName;
	}
	/**
	 * 
	 * @param lastName - set the client's last name to this value
	 */
	public void setLastName(SimpleStringProperty lastName) {
		this.lastName = lastName;
	}
	/**
	 * 
	 * @param id - set the client's ID number to this value
	 */
	public void setId(SimpleStringProperty id) {
		this.id = id;
	}
	/**
	 * 
	 * @param creditCard - set the client's credit card number to this value
	 */
	public void setCreditCard(SimpleStringProperty creditCard) {
		this.creditCard = creditCard;
	}
	/**
	 * 
	 * @param phoneNumber - set the client's phone number to this value
	 */
	public void setPhoneNumber(SimpleStringProperty phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * 
	 * @param email - set the client's email address to this value
	 */
	public void setEmail(SimpleStringProperty email) {
		this.email = email;
	}
	
}
