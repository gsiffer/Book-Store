/**
 * Project: a01179721_Assignment2
 * File: BookDao.java
 * Date: Mar. 18, 2020
 * Time: 8:49:15 p.m.
 */
package a01179721.data;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01179721.ApplicationException;
import a01179721.db.Dao;
import a01179721.db.Database;
import a01179721.db.DbConstants;
import a01179721.io.BookReader;

/**
 * @author Gabor Siffer, A01179721
 *
 */
public class BookDao extends Dao {
	public static final String TABLE_NAME = DbConstants.BOOKS_TABLE_NAME;

	private static final String BOOKS_DATA_FILENAME = "books500.csv";
	private static Logger LOG = LogManager.getLogger();

	public BookDao(Database database) throws ApplicationException {
		super(database, TABLE_NAME);

	}

	/**
	 * @param bookDataFile
	 * @throws ApplicationException
	 * @throws SQLException
	 */
	public void load() throws ApplicationException {
		File bookDataFile = new File(BOOKS_DATA_FILENAME);
		try {
			if (!Database.tableExists(BookDao.TABLE_NAME)) {
				create();
				LOG.debug("Inserting the books");

				if (!bookDataFile.exists()) {
					throw new ApplicationException(String.format("Required '%s' is missing.", BOOKS_DATA_FILENAME));
				}

				BookReader.read(bookDataFile, this);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
	}

	/**
	 * 
	 */
	@Override
	public void create() throws SQLException {
		LOG.debug("Creating database table " + TABLE_NAME);

		String sqlString = String.format("CREATE TABLE %s(" //
				+ "%s BIGINT, " // ID
				+ "%s VARCHAR(%d), " // ISBN
				+ "%s VARCHAR(%d), " // AUTHORS
				+ "%s INT, " // PUBLICATION_YEAR
				+ "%s VARCHAR(%d), " // TITLE
				+ "%s FLOAT, " // AVERAGE_RATING
				+ "%s BIGINT, " // RATINGS_COUNT
				+ "%s VARCHAR(%d), " // IMAGE_URL
				+ "PRIMARY KEY (%s))", // ID
				TABLE_NAME, //
				Column.ID.name, //
				Column.ISBN.name, Column.ISBN.length, //
				Column.AUTHORS.name, Column.AUTHORS.length, //
				Column.PUBLICATION_YEAR.name, //
				Column.TITLE.name, Column.TITLE.length, //
				Column.AVERAGE_RATING.name, //
				Column.RATINGS_COUNT.name, //
				Column.IMAGE_URL.name, Column.IMAGE_URL.length, //
				Column.ID.name);

		super.create(sqlString);
	}

	/**
	 * Add a book to the table
	 * 
	 * @param book
	 * @throws SQLException
	 */
	public void add(Book book) throws SQLException {
		String sqlString = String.format("INSERT INTO %s values(?, ?, ?, ?, ?, ?, ?, ?)", TABLE_NAME);
		boolean result = execute(sqlString, //
				book.getId(), //
				book.getIsbn(), //
				book.getAuthors(), //
				book.getPublicationYear(), //
				book.getTitle(), //
				book.getAverageRating(), //
				book.getRatingsCount(), //
				book.getImageUrl()); //

		LOG.debug(String.format("Adding %s was %s", book, result ? "successful" : "successful"));
	}

	/**
	 * Update the book.
	 * 
	 * @param book
	 * @throws SQLException
	 */
	public void update(String[] book) throws SQLException {
		Connection connection;
		Statement statement = null;

		try {
			connection = Database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sql = String.format("UPDATE %s set %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s' WHERE %s='%s'", tableName, //
					Column.ISBN.name, book[1], //
					Column.AUTHORS.name, book[2], //
					Column.PUBLICATION_YEAR.name, book[3], //
					Column.TITLE.name, book[4], //
					Column.AVERAGE_RATING.name, book[5], //
					Column.RATINGS_COUNT.name, book[6], //
					Column.IMAGE_URL.name, book[7], //
					Column.ID.name, book[0]);
			LOG.debug(sql);
			int rowcount = statement.executeUpdate(sql);
			System.out.println(String.format("Updated %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	/**
	 * Delete the book from the database.
	 * 
	 * @param book
	 * @throws SQLException
	 */
	public void delete(Book book) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = Database.getConnection();
			statement = connection.createStatement();

			String sqlString = String.format("DELETE FROM %s WHERE %s='%s'", TABLE_NAME, Column.ID.name, book.getId());
			LOG.debug(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			LOG.debug(String.format("Deleted %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	/**
	 * Sort book table
	 * 
	 * @param sortCodeBook
	 * @return
	 * @throws Exception
	 */
	public String[] sortBook(int[] sortCodeBook) throws Exception {

		String sqlString = sqlSelection(sortCodeBook);
		if (sqlString == null) {
			return null;
		}

		int i = 0;
		String[] result = new String[countAllRows()];
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sqlString);
			LOG.debug(sqlString);
			while (resultSet.next()) {
				result[i] = String.format(resultSet.getString(3) + " - " + resultSet.getString(5));
				i++;
			}
			return result;
		} finally {
			close(statement);
		}

	}

	/**
	 * SQL selections
	 * 
	 * @param sortCodeBook
	 * @return
	 */
	private String sqlSelection(int[] sortCodeBook) {
		String sqlString = null;
		if (sortCodeBook[0] == 1 && sortCodeBook[1] == 0) {
			sqlString = String.format("SELECT * FROM %s ORDER BY %s", TABLE_NAME, Column.AUTHORS.name);
		} else if (sortCodeBook[0] == 1 && sortCodeBook[1] == 1) {
			sqlString = String.format("SELECT * FROM %s ORDER BY %s DESC", TABLE_NAME, Column.AUTHORS.name);
		} else if (sortCodeBook[0] == 0 && sortCodeBook[1] == 0) {
			sqlString = String.format("SELECT * FROM %s", TABLE_NAME);
		}

		return sqlString;
	}

	/**
	 * 
	 * @author Gabor Siffer, A01179721
	 *
	 */
	public enum Column {
		ID("id", 16), //
		ISBN("isbn", 20), //
		AUTHORS("authors", 100), //
		PUBLICATION_YEAR("publicationYear", 10), //
		TITLE("title", 100), //
		AVERAGE_RATING("averageRating", 10), //
		RATINGS_COUNT("ratingsCount", 20), //
		IMAGE_URL("imageUrl", 200); //

		String name;
		int length;

		private Column(String name, int length) {
			this.name = name;
			this.length = length;
		}

	}

}
