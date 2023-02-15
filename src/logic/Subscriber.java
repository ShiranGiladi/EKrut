package logic;

/**
 * This class holds all the information about a subscribed client in the system.
 * The information about each client includes: their subscription number, their first name, their last name, their ID number, 
 * 											   their phone number, their email address, their credit card number. 
 *
 */

public class Subscriber {
	private String subscriberNumber,  firstName,  lastName,  id,  phoneNumber,  emailAddress, creditCardNumber;
	
	/**
	 * This method is the constructor for Subscriber
	 * @param subscriberNumber - the subscribed client's subscription number
	 * @param firstName - the subscribed client's first name 
	 * @param lastName - the subscribed client's last name
	 * @param id - the subscribed client's ID number
	 * @param phoneNumber - the subscribed client's phone number
	 * @param emailAddress - the subscribed client's email address
	 * @param creditCardNumber - the subscribed client's credit card number
	 */
	public Subscriber(String subscriberNumber, String firstName, String lastName, String id, String phoneNumber,
			String emailAddress, String creditCardNumber) {
		super();
		this.subscriberNumber = subscriberNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.creditCardNumber = creditCardNumber;
	}
	/**
	 * 
	 * @return the subscribed client's subscription number
	 */
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	/**
	 * 
	 * @param subscriberNumber - set the subscribed client's subscription number to this value
	 */
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	/**
	 * 
	 * @return the subscribed client's first name 
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * 
	 * @param firstName - set the subscribed client's first name to this value
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * 
	 * @return the subscribed client's last name
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * 
	 * @param lastName - set the subscribed client's last name to this value
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * 
	 * @return the subscribed client's ID number
	 */
	public String getId() {
		return id;
	}
	/**
	 * 
	 * @param id - set the subscribed client's ID number to this value
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 
	 * @return the subscribed client's phone number
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * 
	 * @param phoneNumber - set the subscribed client's credit card number to this value
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * 
	 * @return the subscribed client's email address
	 */
	public String getEmailAddress() {
		return emailAddress;
	}
	/**
	 * 
	 * @param emailAddress - set the subscribed client's email address to this value
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	/**
	 * 
	 * @return the subscribed client's credit card number
	 */
	public String getCreditCardNumber() {
		return creditCardNumber;
	}
	/**
	 * 
	 * @param creditCardNumber - set the subscribed client's credit card number to this value
	 */
	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}
	/**
	 * This method returns a string of all the information about a subscribed client's
	 */
	@Override
	public String toString() {
		return "subscriber [subscriberNumber=" + subscriberNumber + ", firstName=" + firstName + ", lastName="
				+ lastName + ", id=" + id + ", phoneNumber=" + phoneNumber + ", emailAddress=" + emailAddress
				+ ", creditCardNumber=" + creditCardNumber + "]";
	}
	
}
