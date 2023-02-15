package logic;

import javafx.beans.property.SimpleStringProperty;

/**
 * This class holds all the information about orders for delivery.
 * The information includes: the order number, the costumer ID of the costumer that made the order, 
 *							 the address to which the delivery is to be sent, the costumer's phone number,
 *							 and the estimated time for the delivery's arrival to the costumer.
 */
public class Deliveries {
	private SimpleStringProperty orderNumber, costumerID, address, receiverPhoneNumber, facility, estimateDeliveryDate;
	
	/**
	 * This method is one of the constructors for Deliveries
	 * @param facility
	 * @param orderNumber - the order number
	 * @param costumerID - the costumer ID of the costumer that made the order for delivery
	 * @param address - the address to which the delivery is to be sent
	 * @param receiverPhoneNumber - the costumer's phone number
	 */
	public Deliveries(String facility, String orderNumber, String costumerID, String address, String receiverPhoneNumber) {
		super();
		this.orderNumber = new SimpleStringProperty(orderNumber);
		this.costumerID = new SimpleStringProperty(costumerID);
		this.address = new SimpleStringProperty(address);
		this.receiverPhoneNumber = new SimpleStringProperty(receiverPhoneNumber);
		this.facility = new SimpleStringProperty(facility);
	}
	/**
	 * This method is one of the constructors for Deliveries
	 * @param facility
	 * @param orderNumber - the order number
	 * @param costumerID - the costumer ID of the costumer that made the order for delivery
	 * @param estimateDeliveryDate - the estimated time for the delivery's arrival to the costumer
	 */
	public Deliveries(String facility, String orderNumber, String costumerID, String estimateDeliveryDate) {
		super();
		this.orderNumber = new SimpleStringProperty(orderNumber);
		this.costumerID = new SimpleStringProperty(costumerID);
		this.estimateDeliveryDate = new SimpleStringProperty(estimateDeliveryDate);
		this.facility = new SimpleStringProperty(facility);
	}
	/**
	 * 
	 * @return the costumer ID of the costumer that made the order for delivery
	 */
	public String getCostumerID() {
		return costumerID.get();
	}
	/**
	 * 
	 * @return the order number
	 */
	public String getOrderNumber() {
		return orderNumber.get();
	}
	/**
	 * 
	 * @return the address to which the delivery is to be sent
	 */
	public String getAddress() {
		return address.get();
	}
	/**
	 * 
	 * @return the costumer's phone number
	 */
	public String getReceiverPhoneNumber() {
		return receiverPhoneNumber.get();
	}
	/**
	 * 
	 * @param costumerID - set the costumer ID of the costumer that made the order for delivery to this value
	 */
	public void setCostumerID(SimpleStringProperty costumerID) {
		this.costumerID = costumerID;
	}
	/**
	 * 
	 * @param orderNumber - set the order number to this value
	 */
	public void setOrderNumber(SimpleStringProperty orderNumber) {
		this.orderNumber = orderNumber;
	}
	/**
	 * 
	 * @param address - set the address to which the delivery is to be sent to this value
	 */
	public void setAddress(SimpleStringProperty address) {
		this.address = address;
	}
	/**
	 * 
	 * @param receiverPhoneNumber - set the costumer's phone number to this value
	 */
	public void setReceiverPhoneNumber(SimpleStringProperty receiverPhoneNumber) {
		this.receiverPhoneNumber = receiverPhoneNumber;
	}
	/**
	 * 
	 * @return 
	 */
	public String getFacility() {
		return facility.get();
	}
	/**
	 * 
	 * @param facility
	 */
	public void setFacility(SimpleStringProperty facility) {
		this.facility = facility;
	}
	/**
	 * 
	 * @return the estimated time for the delivery's arrival to the costumer
	 */
	public String getEstimateDeliveryDate() {
		return estimateDeliveryDate.get();
	}
	/**
	 * 
	 * @param estimateDeliveryDate - set the estimated time for the delivery's arrival to the costumer to this value
	 */
	public void setEstimateDeliveryDate(SimpleStringProperty estimateDeliveryDate) {
		this.estimateDeliveryDate = estimateDeliveryDate;
	}
}
