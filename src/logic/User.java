package logic;

import javafx.beans.property.SimpleStringProperty;

/**
 * This class holds all the information about the user that logs in into the system, as well as the information about the computer they log in from.
 * The information about the computer includes: the IP address, the host name.
 * The information about the user includes: the user's ID, the user's first name, the user's last name, the the user's job's title,
 * 											the facility to which the user belongs to or in charge of, whether the user's is already logged in to the system,
 * 											the user's supply method (on site / for delivery / pick up later), the user's user name,
 * 											the user's password, whether the user's using the OL or the EK format of the system (simulation).
 *
 */
public class User {
	private SimpleStringProperty ip,   hostname, connected;
	private String id, firstName, lastName, job, status, note, isLogin, supplyMethod, username, password, systemFormat;
	
	/**
	 * This method is the constructor for User
	 * @param ip - the IP address of the client  
	 * @param hostname - the host name of the client
	 */
	public User(String ip, String hostname) {
		this.ip = new SimpleStringProperty(ip);
		this.hostname = new SimpleStringProperty(hostname);
		this.connected = new SimpleStringProperty("Connected");
	}
	/**
	 * 
	 * @return the IP address of the client 
	 */
	public String getIp() {
		return ip.get();
	}
	/**
	 * 
	 * @return the host name of the client
	 */
	public String getHostname() {
		return hostname.get();
	}
	/**
	 * 
	 * @return the connection status of the client 
	 */
	public String getConnected() {
		return connected.get();
	}
	/**
	 * 
	 * @param state - set the connection status of the client  to this value
	 */
	public void setConnected(String state) {
		connected = new SimpleStringProperty(state);
	}
	/**
	 * 
	 * @return user's first name
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * 
	 * @param firstName - set user's first name to this value
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * 
	 * @return user's last name
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * 
	 * @param lastName - set user's last name to this value
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * 
	 * @return user's job title
	 */
	public String getJob() {
		return job;
	}
	/**
	 * 
	 * @param job - set user's job title to this value
	 */
	public void setJob(String job) {
		this.job = job;
	}
	/**
	 * 
	 * @return user's status (regular client / subscriber / employee / etc.)
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * 
	 * @param status - set user's status (regular client / subscriber / employee / etc.) to this value
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * 
	 * @return if the user is a client - to which facility they belong, if the use is an employee - which facility they are in charge of
	 */
	public String getNote() {
		return note;
	}
	/**
	 * 
	 * @param note - set the facility the user's belong to / in charge of to this value
	 */
	public void setNote(String note) {
		this.note = note;
	}
	/**
	 * 
	 * @return if the user is already logged in to the system (binary value - 1 if already logged in, 0 if not logged in)
	 */
	public 	String getIsLogin() {
		return isLogin;
	}
	/**
	 * 
	 * @param isLogin - set the login status (0 or 1) to this value
	 */
	public void setIsLogin(String isLogin) {
		this.isLogin = isLogin;
	}
	/**
	 * 
	 * @return the user's ID
	 */
	public String getId() {
		return id;
	}
	/**
	 * 
	 * @param id - set the user's ID to this value
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 
	 * @return the user's supply method (on site / for delivery / pick up later)
	 */
	public String getSupplyMethod() {
		return supplyMethod;
	}
	/**
	 * 
	 * @param supplyMethod - set the user's supply method (on site / for delivery / pick up later) to this value
	 */
	public void setSupplyMethod(String supplyMethod) {
		this.supplyMethod = supplyMethod;
	}
	/**
	 * 
	 * @return the user's user name
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * 
	 * @param username - set the user's user name to this value
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * 
	 * @return the user's password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * 
	 * @param password - set the user's password to this value
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 
	 * @return if the system works as OL or EK (simulation)
	 */
	public String getSystemFormat() {
		return systemFormat;
	}
	/**
	 * 
	 * @param systemFormat - set the system simulation (OL or EK) to this value
	 */
	public void setSystemFormat(String systemFormat) {
		this.systemFormat = systemFormat;
	}
	/**
	 *  This method returns a string of all the information about the user
	 */
	@Override
	public String toString() {
		return "User [id=" + id + "firstName=" + firstName + ", lastName=" + lastName + ", job=" + job + ", status=" + status
				+ ", note=" + note + ",supplyMethod=" + supplyMethod + ",username=" + username + ",password=" + password + ",systemFormat=" + systemFormat + "]";
	}
}
