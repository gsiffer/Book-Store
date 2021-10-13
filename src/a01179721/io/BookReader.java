/**
 * Project: a01179721_Assignment2
 * File: BookReader.java
 * Date: Mar. 18, 2020
 * Time: 8:30:41 p.m.
 */
package a01179721.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

import a01179721.ApplicationException;
import a01179721.data.Book;
import a01179721.data.BookDao;
import a01179721.util.Validator;

/**
 * @author Gabor Siffer, A01179721
 *
 */
public class BookReader {
	public static final String FIELD_DELIMITER = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

	/**
	 * private constructor to prevent instantiation
	 */
	private BookReader() {
	}

	/**
	 * Read the book input data.
	 * 
	 * @param data
	 *            The input data.
	 * @return A list of books.
	 * @throws ApplicationException
	 */
	public static void read(File bookDataFile, BookDao dao) throws ApplicationException {
		BufferedReader bookReader = null;
		try {
			bookReader = new BufferedReader(new FileReader(bookDataFile));

			String line = null;
			line = bookReader.readLine(); // skip the header line
			while ((line = bookReader.readLine()) != null) {
				Book book = readBookString(line);
				try {
					dao.add(book);
				} catch (SQLException e) {
					throw new ApplicationException(e);
				}
			}
		} catch (IOException e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			try {
				if (bookReader != null) {
					bookReader.close();
				}
			} catch (IOException e) {
				throw new ApplicationException(e.getMessage());
			}
		}
	}

	/**
	 * Parse a Book data string into a Book object;
	 * 
	 * @param row
	 * @throws ApplicationException
	 */
	private static Book readBookString(String data) throws ApplicationException {
		String[] elements = data.split(FIELD_DELIMITER);
		if (elements.length != Book.ATTRIBUTE_COUNT) {
			throw new ApplicationException(
					String.format("Expected %d but got %d: %s", Book.ATTRIBUTE_COUNT, elements.length, Arrays.toString(elements)));
		}

		int index = 0;
		long id = 0, ratingsCount = 0;
		int publicationYear = 0;
		double averageRating = 0.0;
		String authors = null, title = null, imageUrl = null, isbn = null;
		try {
			id = Integer.parseInt(elements[index++]);
			isbn = elements[index++];
			authors = elements[index++];
			publicationYear = Integer.parseInt(elements[index++]);
			if (!Validator.validateYear(publicationYear)) {
				throw new ApplicationException(String.format("Invalid publication year: %d", publicationYear));
			}
			title = elements[index++];
			averageRating = Double.parseDouble(elements[index++]);
			ratingsCount = Integer.parseInt(elements[index++]);
			imageUrl = elements[index++];
		} catch (NumberFormatException e) {
			switch (index) {
			case 1:
				errorMessage("id", elements[0]);
			case 4:
				errorMessage("publication year", elements[3]);
			case 6:
				errorMessage("average rating", elements[5]);
			case 7:
				errorMessage("ratings count", elements[6]);
			}
		}

		return new Book.Builder(id, isbn).setAuthors(authors).setPublicationYear(publicationYear).setTitle(title).setAverageRating(averageRating)
				.setRatingsCount(ratingsCount).setImageUrl(imageUrl).build();
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
