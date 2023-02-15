package logic;

import javafx.beans.property.SimpleStringProperty;

/**
 * This class has all the information of a sale.
 * The information of sale includes: the sale ID, the name of the product participating in the sale, the area in which the sale will take place,
 * 									 the start date and time of the sale, the end date and time of the sale, the original price of the product,
 * 									 the reduced price of the product (after a discount is applied).
 *
 */
public class Sales {
	private SimpleStringProperty saleID, product, area, startDate, EndDate, originalPrice, newPrice;
	
	/**
	 * This method is the constructor for Sales
	 * @param saleID - the ID of the sale (the primary key in the DB table of sales)
	 * @param product - the name of the product participating in the sale
	 * @param area - the name of area in which the sale will take place
	 * @param startDate - the start date and time of the sale
	 * @param endDate - the end date and time of the sale
	 * @param originalPrice - the original price of the product
	 * @param newPrice - the reduced price of the product (after a discount is applied)
	 */
	public Sales(String saleID, String product, String area, String startDate, String endDate, String originalPrice, String newPrice) {
		super();
		this.saleID =  new SimpleStringProperty(saleID);
		this.product =  new SimpleStringProperty(product);
		this.area =  new SimpleStringProperty(area);
		this.startDate =  new SimpleStringProperty(startDate);
		this.EndDate =  new SimpleStringProperty(endDate);
		this.originalPrice =  new SimpleStringProperty(originalPrice);
		this.newPrice =  new SimpleStringProperty(newPrice);
	}
	/**
	 * 
	 * @return the name of the product participating in the sale
	 */
	public String getProduct() {
		return product.get();
	}
	/**
	 * 
	 * @return the name of area in which the sale will take place
	 */
	public String getArea() {
		return area.get();
	}
	/**
	 * 
	 * @return the start date and time of the sale
	 */
	public String getStartDate() {
		return startDate.get();
	}
	/**
	 * 
	 * @return the end date and time of the sale
	 */
	public String getEndDate() {
		return EndDate.get();
	}
	/**
	 * 
	 * @return the original price of the product
	 */
	public String getOriginalPrice() {
		return originalPrice.get();
	}
	/**
	 * 
	 * @return the reduced price of the product (after a discount is applied)
	 */
	public String getNewPrice() {
		return newPrice.get();
	}
	/**
	 * 
	 * @return the ID of the sale
	 */
	public String getSaleID() {
		return saleID.get();
	}
	/**
	 * 
	 * @param product - set the name of the product participating in the sale to this value
	 */
	public void setProduct(SimpleStringProperty product) {
		this.product = product;
	}
	/**
	 * 
	 * @param area - set the name of area in which the sale will take place to this value
	 */
	public void setArea(SimpleStringProperty area) {
		this.area = area;
	}
	/**
	 * 
	 * @param startDate - set the start date and time of the sale to this value
	 */
	public void setStartDate(SimpleStringProperty startDate) {
		this.startDate = startDate;
	}
	/**
	 * 
	 * @param endDate - set the end date and time of the sale to this value
	 */
	public void setEndDate(SimpleStringProperty endDate) {
		EndDate = endDate;
	}
	/**
	 * 
	 * @param originalPrice - set the original price of the product to this value
	 */
	public void setOriginalPrice(SimpleStringProperty originalPrice) {
		this.originalPrice = originalPrice;
	}
	/**
	 * 
	 * @param newPrice - set the reduced price of the product (after a discount is applied) to this value
	 */
	public void setNewPrice(SimpleStringProperty newPrice) {
		this.newPrice = newPrice;
	}
	/**
	 * 
	 * @param saleID - set the ID of the sale to this value
	 */
	public void setSaleID(SimpleStringProperty saleID) {
		this.saleID = saleID;
	}
}
