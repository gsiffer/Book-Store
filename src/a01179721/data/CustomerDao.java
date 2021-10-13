/**
 * Project: A00123456Lab8
 * File: CustomerDao.java
 */

package a01179721.data;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01179721.ApplicationException;
import a01179721.db.Dao;
import a01179721.db.Database;
import a01179721.db.DbConstants;
import a01179721.io.CustomerReader;

/**
 * @author Sam Cirka, A00123456
 *
 */
public class CustomerDao extends Dao {

	public static final String TABLE_NAME = DbConstants.CUSTOMERS_TABLE_NAME;

	private static final String CUSTOMERS_DATA_FILENAME = "customers.dat";
	private static Logger LOG = LogManager.getLogger();

	public CustomerDao(Database database) throws ApplicationException {
		super(database, TABLE_NAME);

	}

	/**
	 * @param customerDataFile
	 * @throws ApplicationException
	 * @throws SQLException
	 */
	public void load() throws ApplicationException {
		File customerDataFile = new File(CUSTOMERS_DATA_FILENAME);
		try {
			if (!Database.tableExists(CustomerDao.TABLE_NAME)) {
				create();
				LOG.debug("Inserting the customers");

				if (!customerDataFile.exists()) {
					throw new ApplicationException(String.format("Required '%s' is missing.", CUSTOMERS_DATA_FILENAME));
				}

				CustomerReader.read(customerDataFile, this);
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

		// With MS SQL Server, JOINED_DATE needs to be a DATETIME type.
		String sqlString = String.format("CREATE TABLE %s(" //
				+ "%s BIGINT, " // ID
				+ "%s VARCHAR(%d), " // FIRST_NAME
				+ "%s VARCHAR(%d), " // LAST_NAME
				+ "%s VARCHAR(%d), " // STREET
				+ "%s VARCHAR(%d), " // CITY
				+ "%s VARCHAR(%d), " // POSTAL_CODE
				+ "%s VARCHAR(%d), " // PHONE
				+ "%s VARCHAR(%d), " // EMAIL_ADDRESS
				+ "%s DATETIME, " // JOINED_DATE
				+ "PRIMARY KEY (%s))", // ID
				TABLE_NAME, //
				Column.ID.name, //
				Column.FIRST_NAME.name, Column.FIRST_NAME.length, //
				Column.LAST_NAME.name, Column.LAST_NAME.length, //
				Column.STREET.name, Column.STREET.length, //
				Column.CITY.name, Column.CITY.length, //
				Column.POSTAL_CODE.name, Column.POSTAL_CODE.length, //
				Column.PHONE.name, Column.PHONE.length, //
				Column.EMAIL_ADDRESS.name, Column.EMAIL_ADDRESS.length, //
				Column.JOINED_DATE.name, //
				Column.ID.name);

		super.create(sqlString);
	}

	/**
	 * Add a customer to the table
	 * 
	 * @param customer
	 * @throws SQLException
	 */
	public void add(Customer customer) throws SQLException {
		String sqlString = String.format("INSERT INTO %s values(?, ?, ?, ?, ?, ?, ?, ?, ?)", TABLE_NAME);
		boolean result = execute(sqlString, //
				customer.getId(), //
				customer.getFirstName(), //
				customer.getLastName(), //
				customer.getStreet(), //
				customer.getCity(), //
				customer.getPostalCode(), //
				customer.getPhone(), //
				customer.getEmailAddress(), //
				toTimestamp(customer.getJoinedDate()));
		LOG.debug(String.format("Adding %s was %s", customer, result ? "successful" : "successful"));
	}

	/**
	 * Update the customer.
	 * 
	 * @param customer
	 * @throws SQLException
	 */
	public void update(String[] customer) throws SQLException {
		Connection connection;
		Statement statement = null;

		try {
			connection = Database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sql = String.format("UPDATE %s set %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s' WHERE %s='%s'",
					tableName, //
					Column.FIRST_NAME.name, customer[1], //
					Column.LAST_NAME.name, customer[2], //
					Column.STREET.name, customer[3], //
					Column.CITY.name, customer[4], //
					Column.POSTAL_CODE.name, customer[5], //
					Column.PHONE.name, customer[6], //
					Column.EMAIL_ADDRESS.name, customer[7], //
					Column.JOINED_DATE.name, customer[8], //
					Column.ID.name, customer[0]);
			LOG.debug(sql);
			int rowcount = statement.executeUpdate(sql);
			LOG.debug(String.format("Updated %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	/**
	 * Delete the customer from the database.
	 * 
	 * @param customer
	 * @throws SQLException
	 */
	public void delete(Customer customer) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = Database.getConnection();
			statement = connection.createStatement();

			String sqlString = String.format("DELETE FROM %s WHERE %s='%s'", TABLE_NAME, Column.ID.name, customer.getId());
			LOG.debug(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			LOG.debug(String.format("Deleted %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	/**
	 * Return a selected customer from the database
	 * 
	 * @return
	 * @throws Exception
	 */
	public String[] SelectedCustomer(String customerId) throws Exception {
		// String sqlString = String.format("SELECT * FROM %s WHERE %s='%s'", TABLE_NAME, Column.ID.name, customerId);
		String sqlString = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_NAME, Column.ID.name);

		String[] result = new String[9];

		// Statement statement = null;
		PreparedStatement prepStmt = null;

		ResultSet resultSet = null;

		try {
			Connection connection = Database.getConnection();
			// statement = connection.createStatement();
			// resultSet = statement.executeQuery(sqlString);
			prepStmt = connection.prepareStatement(sqlString);
			prepStmt.setString(1, customerId);
			resultSet = prepStmt.executeQuery();

			LOG.debug(sqlString);
			while (resultSet.next()) {
				for (int i = 0; i < result.length; i++) {
					result[i] = resultSet.getString(i + 1);
				}
			}
			return result;

		} finally {
			// close(statement);
			prepStmt.close();
		}
	}

	/**
	 * Sort Customer table
	 * 
	 * @param sortCodeCustomer
	 * @return
	 * @throws Exception
	 */
	public String[] sortCustomer(int sortCodeCustomer) throws Exception {
		String sqlString = null;
		if (sortCodeCustomer == 0) {
			sqlString = String.format("SELECT * FROM %s ORDER BY %s", TABLE_NAME, Column.FIRST_NAME.name);
		} else {
			sqlString = String.format("SELECT * FROM %s ORDER BY %s", TABLE_NAME, Column.JOINED_DATE.name);
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
				result[i] = String.format("%-7s %s %s", resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
				i++;
			}
			return result;

		} finally {
			close(statement);
		}
	}

	/**
	 * 
	 * @author Gabor Siffer, A01179721
	 *
	 */
	public enum Column {
		ID("id", 16), //
		FIRST_NAME("firstName", 20), //
		LAST_NAME("lastName", 20), //
		STREET("street", 40), //
		CITY("city", 40), //
		POSTAL_CODE("postalCode", 10), //
		PHONE("phone", 20), //
		EMAIL_ADDRESS("emailAddress", 40), //
		JOINED_DATE("joinedDate", 8); //

		String name;
		int length;

		private Column(String name, int length) {
			this.name = name;
			this.length = length;
		}

	}

}
