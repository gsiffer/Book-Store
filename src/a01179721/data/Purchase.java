/**
 * Project: a01179721_Assignment1
 * File: Purchase.java
 * Date: Feb. 22, 2020
 * Time: 9:41:05 p.m.
 */
package a01179721.data;

/**
 * Class Purchase to store purchase data.
 * 
 * @author Gabor Siffer, A01179721
 *
 */
public class Purchase {

	public static final int ATTRIBUTE_COUNT = 4;

	private int id;
	private long customerId;
	private long bookId;
	private double price;

	public static class Builder {
		// Required parameters
		private long customerId;
		private long bookId;

		// Optional parameters
		private int id;
		private double price;

		public Builder(long customerId, long bookId) {
			this.customerId = customerId;
			this.bookId = bookId;
		}

		/**
		 * @param id
		 *            the id to set
		 */
		public Builder setId(int id) {
			this.id = id;
			return this;
		}

		/**
		 * @param price
		 *            the price to set
		 */
		public Builder setPrice(double price) {
			this.price = price;
			return this;
		}

		public Purchase build() {
			return new Purchase(this);
		}
	}

	/**
	 * Default Constructor
	 */
	private Purchase(Builder builder) {

		id = builder.id;
		setCustomerId(builder.customerId);
		setBookId(builder.bookId);
		price = builder.price;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the customerId
	 */
	public long getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(long customerId) {
		if (customerId > 0) {
			this.customerId = customerId;
		} else {
			System.err.format("Invalid customer id number: %d. It has to be greater than zero", customerId);
			System.exit(-1);
		}
	}

	/**
	 * @return the bookId
	 */
	public long getBookId() {
		return bookId;
	}

	/**
	 * @param bookId
	 *            the bookId to set
	 */
	public void setBookId(long bookId) {
		if (bookId > 0) {
			this.bookId = bookId;
		} else {
			System.err.format("Invalid book id number: %d. It has to be greater than zero", bookId);
			System.exit(-1);
		}
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Purchase [id=" + id + ", customerId=" + customerId + ", bookId=" + bookId + ", price=" + price + "]";
	}

}
