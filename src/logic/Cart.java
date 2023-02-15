package logic;

/**
 * This class represents the cart - will show the products that the client chose to buy, and the relevant info about them.
 * The info for each product is: the name of the product, the image of the product, the selected quantity,
 * 								 the price for one unit, and the price for total chosen quantity of a certain product.
 */
public class Cart {
	private String productName, productPrice, totalPerProduct, qauntity, imgSrc;

	/**
	 * This method is the constructor for Cart
	 * @param productName - the name of the chosen product
	 * @param productPrice - the price for one unit of the chosen product
	 * @param totalPerProduct - the total price for the quantity chosen of a certain product
	 * @param qauntity - the total quantity chosen
	 * @param imgSrc - the image of the product
	 */
	public Cart(String productName, String productPrice, String totalPerProduct, String qauntity, String imgSrc) {
		super();
		this.productName = productName;
		this.productPrice = productPrice;
		this.totalPerProduct = totalPerProduct;
		this.qauntity = qauntity;
		this.imgSrc = imgSrc;
	}
	/**
	 * 
	 * @return the name of the product
	 */
	public String getProductName() {
		return productName;
	}
	/** 
	 * 
	 * @param productName - set the name of the product to this value
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/**
	 * 
	 * @return the price of a product for a single unit
	 */
	public String getProductPrice() {
		return productPrice;
	}
	/**
	 * 
	 * @param productPrice - set the price of the product to this value
	 */
	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	/**
	 * 
	 * @return the total price for the quantity selected of a certain product 
	 */
	public String getTotalPerProduct() {
		return totalPerProduct;
	}
	/**
	 * 
	 * @param totalPerProduct - set the total price of the quantity selected of a certain product to this value
	 */
	public void setTotalPerProduct(String totalPerProduct) {
		this.totalPerProduct = totalPerProduct;
	}
	/**
	 * 
	 * @return the quantity selected of a certain product
	 */
	public String getQauntity() {
		return qauntity;
	}
	/**
	 * 
	 * @param qauntity - set the quantity selected of a certain product to this value
	 */
	public void setQauntity(String qauntity) {
		this.qauntity = qauntity;
	}
	/**
	 * 
	 * @return the image of the product
	 */
	public String getImgSrc() {
		return imgSrc;
	}
	/**
	 * 
	 * @param imgSrc - set the image of the product to this value 
	 */
	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}
	/**
	 * This method returns a string of all the information of a certain product added to the cart 
	 */
	@Override
	public String toString() {
		return "Cart [" + productName + ", " + productPrice + ", "	+ totalPerProduct + ", " + qauntity + ", " + imgSrc + "]";
	}
}
