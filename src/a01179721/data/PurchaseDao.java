/**
 * Project: a01179721_Assignment2
 * File: PurchaseDao.java
 * Date: Mar. 19, 2020
 * Time: 1:49:30 p.m.
 */
package a01179721.data;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01179721.ApplicationException;
import a01179721.db.Dao;
import a01179721.db.Database;
import a01179721.db.DbConstants;
import a01179721.io.PurchaseReader;

/**
 * @author Gabor Siffer, A01179721
 *
 */
public class PurchaseDao extends Dao {
	public static final String TABLE_NAME = DbConstants.PURCHASES_TABLE_NAME;

	private static final String PURCHASES_DATA_FILENAME = "purchases.csv";
	private static Logger LOG = LogManager.getLogger();

	public PurchaseDao(Database database) throws ApplicationException {
		super(database, TABLE_NAME);

	}

	/**
	 * @param purchaseDataFile
	 * @throws ApplicationException
	 * @throws SQLException
	 */
	public void load() throws ApplicationException {
		File purchaseDataFile = new File(PURCHASES_DATA_FILENAME);
		try {
			if (!Database.tableExists(PurchaseDao.TABLE_NAME)) {
				create();
				LOG.debug("Inserting the books");

				if (!purchaseDataFile.exists()) {
					throw new ApplicationException(String.format("Required '%s' is missing.", PURCHASES_DATA_FILENAME));
				}

				PurchaseReader.read(purchaseDataFile, this);
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
				+ "%s INT, " // ID
				+ "%s BIGINT, " // CUSTOMER_ID
				+ "%s BIGINT, " // BOOK_ID
				+ "%s FLOAT, " // PRICE
				+ "PRIMARY KEY (%s))", // ID
				TABLE_NAME, //
				Column.ID.name, //
				Column.CUSTOMER_ID.name, //
				Column.BOOK_ID.name, //
				Column.PRICE.name, //
				Column.ID.name);

		super.create(sqlString);
	}

	/**
	 * Add a purchase to the table
	 * 
	 * @param purchase
	 * @throws SQLException
	 */
	public void add(Purchase purchase) throws SQLException {
		String sqlString = String.format("INSERT INTO %s values(?, ?, ?, ?)", TABLE_NAME);
		boolean result = execute(sqlString, //
				purchase.getId(), //
				purchase.getCustomerId(), //
				purchase.getBookId(), //
				purchase.getPrice()); //

		LOG.debug(String.format("Adding %s was %s", purchase, result ? "successful" : "successful"));
	}

	/**
	 * Update the purchase.
	 * 
	 * @param purchase
	 * @throws SQLException
	 */
	public void update(String[] purchase) throws SQLException {
		Connection connection;
		Statement statement = null;

		try {
			connection = Database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sql = String.format("UPDATE %s set %s='%s', %s='%s', %s='%s', %s='%s'", tableName, //
					Column.CUSTOMER_ID.name, purchase[1], //
					Column.BOOK_ID.name, purchase[2], //
					Column.PRICE.name, purchase[3], //
					Column.ID.name, purchase[0]);
			LOG.debug(sql);
			int rowcount = statement.executeUpdate(sql);
			LOG.debug(String.format("Updated %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	/**
	 * Delete the purchase from the database.
	 * 
	 * @param book
	 * @throws SQLException
	 */
	public void delete(Purchase purchase) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = Database.getConnection();
			statement = connection.createStatement();

			String sqlString = String.format("DELETE FROM %s WHERE %s='%s'", TABLE_NAME, Column.ID.name, purchase.getId());
			LOG.debug(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			LOG.debug(String.format("Deleted %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	/**
	 * Sort the purchase table
	 * 
	 * @param sortCodePurchase
	 * @return
	 * @throws Exception
	 */
	public LinkedList<String> sortPurchase(int[] sortCodePurchase) throws Exception {
		String sqlString = sqlSelection(sortCodePurchase);
		if (sqlString == null) {
			return null;
		}

		LinkedList<String> result = new LinkedList<String>();
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sqlString);
			LOG.debug(sqlString);
			while (resultSet.next()) {
				result.add(String.format(resultSet.getString(1) + " - " + resultSet.getString(2) + " " + resultSet.getString(3) + " - "
						+ resultSet.getString(4) + " - $" + resultSet.getString(5)));
			}
			return result;
		} finally {
			close(statement);
		}

	}

	/**
	 * Create different selections
	 * 
	 * @param sortCodePurchase
	 * @return
	 */
	private String sqlSelection(int[] sortCodePurchase) {
		String sqlString = null;
		if (sortCodePurchase[0] == 1 && sortCodePurchase[1] == 0 && sortCodePurchase[2] == 0) {
			sqlString = String.format("SELECT %s, %s, %s, %s, %s FROM %s JOIN %s ON %s.%s = %s.%s " //
					+ "JOIN %s ON %s.%s = %s.%s ORDER BY %s", "customerId", "firstName", "lastName", "title", "price", //
					TABLE_NAME, DbConstants.CUSTOMERS_TABLE_NAME, TABLE_NAME, "customerId", //
					DbConstants.CUSTOMERS_TABLE_NAME, "id", DbConstants.BOOKS_TABLE_NAME, //
					TABLE_NAME, "bookId", DbConstants.BOOKS_TABLE_NAME, "id", "lastName");
		} else if (sortCodePurchase[0] == 1 && sortCodePurchase[1] == 0 && sortCodePurchase[2] == 1) {
			sqlString = String.format("SELECT %s, %s, %s, %s, %s FROM %s JOIN %s ON %s.%s = %s.%s " //
					+ "JOIN %s ON %s.%s = %s.%s ORDER BY %s DESC", "customerId", "firstName", "lastName", "title", "price", //
					TABLE_NAME, DbConstants.CUSTOMERS_TABLE_NAME, TABLE_NAME, "customerId", //
					DbConstants.CUSTOMERS_TABLE_NAME, "id", DbConstants.BOOKS_TABLE_NAME, //
					TABLE_NAME, "bookId", DbConstants.BOOKS_TABLE_NAME, "id", "lastName");
		} else if (sortCodePurchase[0] == 0 && sortCodePurchase[1] == 1 && sortCodePurchase[2] == 0) {
			sqlString = String.format("SELECT %s, %s, %s, %s, %s FROM %s JOIN %s ON %s.%s = %s.%s " //
					+ "JOIN %s ON %s.%s = %s.%s ORDER BY %s", "customerId", "firstName", "lastName", "title", "price", //
					TABLE_NAME, DbConstants.CUSTOMERS_TABLE_NAME, TABLE_NAME, "customerId", //
					DbConstants.CUSTOMERS_TABLE_NAME, "id", DbConstants.BOOKS_TABLE_NAME, //
					TABLE_NAME, "bookId", DbConstants.BOOKS_TABLE_NAME, "id", "title");
		} else if (sortCodePurchase[0] == 0 && sortCodePurchase[1] == 1 && sortCodePurchase[2] == 1) {
			sqlString = String.format("SELECT %s, %s, %s, %s, %s FROM %s JOIN %s ON %s.%s = %s.%s " //
					+ "JOIN %s ON %s.%s = %s.%s ORDER BY %s DESC", "customerId", "firstName", "lastName", "title", "price", //
					TABLE_NAME, DbConstants.CUSTOMERS_TABLE_NAME, TABLE_NAME, "customerId", //
					DbConstants.CUSTOMERS_TABLE_NAME, "id", DbConstants.BOOKS_TABLE_NAME, //
					TABLE_NAME, "bookId", DbConstants.BOOKS_TABLE_NAME, "id", "title");
		} else if (sortCodePurchase[0] == 0 && sortCodePurchase[1] == 0 && sortCodePurchase[2] == 0) {
			sqlString = String.format("SELECT %s, %s, %s, %s, %s FROM %s JOIN %s ON %s.%s = %s.%s " //
					+ "JOIN %s ON %s.%s = %s.%s", "customerId", "firstName", "lastName", "title", "price", //
					TABLE_NAME, DbConstants.CUSTOMERS_TABLE_NAME, TABLE_NAME, "customerId", //
					DbConstants.CUSTOMERS_TABLE_NAME, "id", DbConstants.BOOKS_TABLE_NAME, //
					TABLE_NAME, "bookId", DbConstants.BOOKS_TABLE_NAME, "id");
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
		CUSTOMER_ID("customerId", 20), //
		BOOK_ID("bookId", 20), //
		PRICE("price", 10); //

		String name;
		int length;

		private Column(String name, int length) {
			this.name = name;
			this.length = length;
		}

	}
}
