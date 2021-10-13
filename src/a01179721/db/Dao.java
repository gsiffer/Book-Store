/**
 * Project: a01179721_Assignment2
 * File: Book.java
 * Date: Feb. 22, 2020
 * Time: 1:56:28 p.m.
 */

package a01179721.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Sam Cirka, A00123456
 *
 */
public abstract class Dao {
	private static Logger LOG = LogManager.getLogger();
	protected final Database database;
	protected final String tableName;

	protected Dao(Database database, String tableName) {
		this.database = database;
		this.tableName = tableName;
	}

	public abstract void create() throws SQLException;

	/**
	 * Delete the database table
	 * 
	 * @throws SQLException
	 */
	public void drop() throws SQLException {
		Statement statement = null;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			if (Database.tableExists(tableName)) {
				LOG.debug("drop table " + tableName);
				statement.executeUpdate("drop table " + tableName);
			}
		} finally {
			close(statement);
		}
	}

	/**
	 * Tell the database we're shutting down.
	 */
	public void shutdown() {
		database.shutdown();
		LOG.debug("database shutdown");
	}

	/**
	 * Create table
	 * 
	 * @param createStatement
	 * @throws SQLException
	 */
	protected void create(String createStatement) throws SQLException {
		LOG.debug(createStatement);
		Statement statement = null;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			statement.executeUpdate(createStatement);
		} finally {
			close(statement);
		}
	}

	/**
	 * Count a table rows
	 * 
	 * @return
	 * @throws Exception
	 */
	public int countAllRows() throws Exception {
		Statement statement = null;
		int count = 0;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT COUNT(*) AS total FROM %s", tableName);
			ResultSet resultSet = statement.executeQuery(sqlString);
			if (resultSet.next()) {
				count = resultSet.getInt("total");
			}
		} finally {
			close(statement);
		}
		return count;
	}

	/**
	 * 
	 * @param preparedStatementString
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	protected boolean execute(String preparedStatementString, Object... args) throws SQLException {
		LOG.debug(preparedStatementString);
		boolean result = false;
		PreparedStatement statement = null;
		try {
			Connection connection = Database.getConnection();
			statement = connection.prepareStatement(preparedStatementString);
			int i = 1;
			for (Object object : args) {
				if (object instanceof String) {
					statement.setString(i, object.toString());
				} else if (object instanceof Boolean) {
					statement.setBoolean(i, (Boolean) object);
				} else if (object instanceof Integer) {
					statement.setInt(i, (Integer) object);
				} else if (object instanceof Long) {
					statement.setLong(i, (Long) object);
				} else if (object instanceof Float) {
					statement.setFloat(i, (Float) object);
				} else if (object instanceof Double) {
					statement.setDouble(i, (Double) object);
				} else if (object instanceof Byte) {
					statement.setByte(i, (Byte) object);
				} else if (object instanceof Timestamp) {
					statement.setTimestamp(i, (Timestamp) object);
				} else if (object instanceof LocalDateTime) {
					statement.setTimestamp(i, Timestamp.valueOf((LocalDateTime) object));
				} else {
					statement.setString(i, object.toString());
				}

				i++;
			}

			result = statement.execute();
		} finally {
			close(statement);
		}

		return result;
	}

	/**
	 * Close statement
	 * 
	 * @param statement
	 */
	protected void close(Statement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			LOG.error("Failed to close statment" + e);
		}
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static Date toDate(LocalDate date) {
		return Date.valueOf(date);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static Timestamp toTimestamp(LocalDate date) {
		return Timestamp.valueOf(LocalDateTime.of(date, LocalTime.now()));
	}

	/**
	 * 
	 * @param dateTime
	 * @return
	 */
	public static Timestamp toTimestamp(LocalDateTime dateTime) {
		return Timestamp.valueOf(dateTime);
	}
}
