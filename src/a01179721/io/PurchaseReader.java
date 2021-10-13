/**
 * Project: a01179721_Assignment1
 * File: PurchaseReader.java
 * Date: Feb. 26, 2020
 * Time: 5:35:08 p.m.
 */
package a01179721.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

import a01179721.ApplicationException;
import a01179721.data.Purchase;
import a01179721.data.PurchaseDao;

/**
 * Class PurchaseReader to read input file to a List collection.
 * 
 * @author Gabor Siffer, A01179721
 *
 */
public class PurchaseReader {
	public static final String FIELD_DELIMITER = ",";

	/**
	 * private constructor to prevent instantiation
	 */
	private PurchaseReader() {
	}

	/**
	 * Read the purchase input data.
	 * 
	 * @param data
	 *            The input data.
	 * @return A list of purchase.
	 * @throws ApplicationException
	 */
	public static void read(File purchaseDataFile, PurchaseDao dao) throws ApplicationException {

		BufferedReader purchaseReader = null;
		try {
			purchaseReader = new BufferedReader(new FileReader(purchaseDataFile));

			String line = null;
			line = purchaseReader.readLine(); // skip the header line
			while ((line = purchaseReader.readLine()) != null) {
				Purchase purchase = readPurchaseString(line);
				try {
					dao.add(purchase);
				} catch (SQLException e) {
					throw new ApplicationException(e);
				}
			}
		} catch (IOException e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			try {
				if (purchaseReader != null) {
					purchaseReader.close();
				}
			} catch (IOException e) {
				throw new ApplicationException(e.getMessage());
			}
		}
	}

	/**
	 * Parse a Purchase data string into a Purchase object;
	 * 
	 * @param row
	 * @throws ApplicationException
	 */
	private static Purchase readPurchaseString(String row) throws ApplicationException {
		String[] elements = row.split(FIELD_DELIMITER);
		if (elements.length != Purchase.ATTRIBUTE_COUNT) {
			throw new ApplicationException(
					String.format("Expected %d but got %d: %s", Purchase.ATTRIBUTE_COUNT, elements.length, Arrays.toString(elements)));
		}

		int index = 0, id = 0;
		long customerId = 0, bookId = 0;
		double price = 0.0;
		try {
			id = Integer.parseInt(elements[index++]);
			customerId = Integer.parseInt(elements[index++]);
			bookId = Integer.parseInt(elements[index++]);
			price = Double.parseDouble(elements[index++]);
		} catch (NumberFormatException e) {
			switch (index) {
			case 1:
				errorMessage("id", elements[0]);
			case 2:
				errorMessage("customer id", elements[1]);
			case 3:
				errorMessage("book id", elements[2]);
			case 4:
				errorMessage("price", elements[3]);
			}
		}

		return new Purchase.Builder(customerId, bookId).setId(id).setPrice(price).build();
	}

	/**
	 * Throw an Application Exception
	 * 
	 * @param field
	 * @param element
	 * @throws ApplicationException
	 */
	private static void errorMessage(String field, String element) throws ApplicationException {
		throw new ApplicationException(String.format("Invalid parameter for %s: %s", field, element));
	}

}
