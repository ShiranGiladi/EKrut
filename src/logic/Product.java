package logic;

import java.io.Serializable;

/**
 * This class has all the information about a certain product.
 * The information includes: the product's name, the product's  price, the product's  image, the product's ID number.
 * 							 the product's price after a sale is applied, the product's amount in its facility's inventory,
 * 							 the product's threshold level in its facility, the product's facility, 
 * 							 and the product's amount in its facility's inventory after an update (inventory re-stock).
 */
@SuppressWarnings("serial")
public class Product implements Serializable{
	private String productName, productPrice, imgSrc, productID, newPriceForSubscriberInSale = null, amount, thresholdLevel, facility, newAmount;
	
	/**
	 * This method is one of the constructors for Product
	 */
	public Product() {}
	/**
	 * This method is one of the constructors for Product
	 * @param productID - the product's ID number 
	 * @param productName - the product's name
	 * @param facility - the product's facility
	 * @param amount - the product's amount in its facility's inventory
	 * @param thresholdLevel - the product's threshold level in its facility
	 */
	public Product(String productID, String productName, String facility, String amount, String thresholdLevel) {
		this.productID = productID;
		this.productName = productName;
		this.facility = facility;
		this.amount = amount;
		this.thresholdLevel = thresholdLevel;
	}
	/**
	 * This method is one of the constructors for Product
	 * @param productID - the product's ID number
	 * @param productName - the product's name
	 * @param facility - the product's facility
	 */
	public Product(String productID, String productName, String facility) {
		this.productID = productID;
		this.productName = productName;
		this.facility = facility;
	}
	/**
	 * 
	 * @return the product name
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * 
	 * @param productName - set the product name to this value
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/**
	 * 
	 * @return the product price
	 */
	public String getProductPrice() {
		return productPrice;
	}
	/**
	 * 
	 * @param productPrice - set the product price to this value
	 */
	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	/**
	 * 
	 * @return the product image
	 */
	public String getImgSrc() {
		return imgSrc;
	}
	/**
	 * 
	 * @param imgSrc - set the product image to this value
	 */
	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}
	/**
	 * 
	 * @return the product ID number
	 */
	public String getProductID() {
		return productID;
	}
	/**
	 * 
	 * @param productID - set the product ID number to this value
	 */
	public void setProductID(String productID) {
		this.productID = productID;
	}
	/**
	 * 
	 * @return the product price after a sale is applied on the product 
	 */
	public String getNewPriceForSubscriberInSale() {
		return newPriceForSubscriberInSale;
	}
	/**
	 * 
	 * @param newPriceForSubscriberInSale - set the product price after a sale is applied on it to this value
	 */
	public void setNewPriceForSubscriberInSale(String newPriceForSubscriberInSale) {
		this.newPriceForSubscriberInSale = newPriceForSubscriberInSale;
	}
	/**
	 * This method returns a string of all the information of a certain product
	 */
	@Override
	public String toString() {
		return "Product [productName=" + productName +  ", productID=" + productID + ", productPrice=" + productPrice + ", newPriceForSubscriberInSale=" + newPriceForSubscriberInSale + ", imgSrc=" + imgSrc + "]";
	}
	/**
	 * 
	 * @return the amount of the product in the product's facility's inventory
	 */
	public String getAmount() {
		return amount;
	}
	/**
	 * 
	 * @param amount - set the amount of the product in the product's facility's inventory to this value
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}
	/**
	 * 
	 * @return the threshold level of a product (in a certain facility)
	 */
	public String getThresholdLevel() {
		return thresholdLevel;
	}
	/**
	 * 
	 * @param thresholdLevel - set the threshold level of a product (in a certain facility) to this value
	 */
	public void setThresholdLevel(String thresholdLevel) {
		this.thresholdLevel = thresholdLevel;
	}
	/**
	 * 
	 * @return the product's facility
	 */
	public String getFacility() {
		return facility;
	}
	/**
	 * 
	 * @param facility - set the product's facility to this value
	 */
	public void setFacility(String facility) {
		this.facility = facility;
	}
	/**
	 * 
	 * @return the amount of the product in the product's facility's inventory after an update
	 */
	public String getNewAmount() {
		return newAmount;
	}
	/**
	 * 
	 * @param newAmount - set the amount of the product in the product's facility's inventory after an update to this value
	 */
	public void setNewAmount(String newAmount) {
		this.newAmount = newAmount;
	}
}
