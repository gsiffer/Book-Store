/**
 * Project: a01179721Lab9
 * File: CreateConnection.java
 * Date: Mar. 13, 2020
 * Time: 9:54:19 p.m.
 */
package a01179721.data;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import a01179721.ApplicationException;
import a01179721.db.Database;

/**
 * @author Gabor Siffer, A01179721
 *
 */
public class CreateConnection {
	private static final String DB_PROPERTIES_FILENAME = "db.properties";

	public static Database connect() throws IOException, SQLException, ApplicationException {
		Properties dbProperties = new Properties();
		dbProperties.load(new FileInputStream(DB_PROPERTIES_FILENAME));
		return new Database(dbProperties);
	}
}
