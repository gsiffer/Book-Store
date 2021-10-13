/**
 * Project: a01179721_Assignment2
 * File: Book.java
 * Date: Feb. 22, 2020
 * Time: 1:56:28 p.m.
 */

package a01179721;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import a01179721.data.BookDao;
import a01179721.data.CreateConnection;
import a01179721.data.CustomerDao;
import a01179721.data.PurchaseDao;
import a01179721.db.Database;
import a01179721.ui.MainFrame;

/**
 * To demonstrate knowledge of JDBC
 * 
 * @author Sam Cirka, A00123456
 *
 */
public class BookStore2 {

	private static final Instant startTime = Instant.now();
	private static final String LOG4J_CONFIG_FILENAME = "log4j2.xml";
	private static final String PLAYERS_DATA_FILENAME = "customers.dat";

	static {
		configureLogging();
	}

	private static final Logger LOG = LogManager.getLogger();

	private static void configureLogging() {
		ConfigurationSource source;
		try {
			source = new ConfigurationSource(new FileInputStream(LOG4J_CONFIG_FILENAME));
			Configurator.initialize(null, source);
		} catch (IOException e) {
			System.out.println(String.format("Can't find the log4j logging configuration file %s.", LOG4J_CONFIG_FILENAME));
		}
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		LOG.info("Start: " + startTime);

		File file = new File(PLAYERS_DATA_FILENAME);
		if (!file.exists()) {
			System.out.format("Required '%s' is missing.", PLAYERS_DATA_FILENAME);
			System.exit(-1);
		}

		new BookStore2().run();

		Instant endTime = Instant.now();
		LOG.info("End: " + endTime);
		LOG.info(String.format("Duration: %d ms", Duration.between(startTime, endTime).toMillis()));
	}

	/**
	 * Populate the customers and print them out.
	 */
	private void run() {
		try {
			Database db = CreateConnection.connect();
			new CustomerDao(db).load();
			new BookDao(db).load();
			new PurchaseDao(db).load();
			setNimbus();
			createUI();
			db.shutdown();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * Open JFrame
	 */
	private void createUI() {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainFrame frame = MainFrame.getTheInstance();
					frame.setVisible(true);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}
		});
	}

	private void setNimbus() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, use the default.
		}
	}
}
