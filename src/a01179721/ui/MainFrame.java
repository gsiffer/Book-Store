/**
 * Project: a01179721Lab9
 * File: JFrame.java
 * Date: Mar. 13, 2020
 * Time: 5:39:22 p.m.
 */
package a01179721.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import a01179721.ApplicationException;
import a01179721.data.BookDao;
import a01179721.data.CreateConnection;
import a01179721.data.CustomerDao;
import a01179721.data.PurchaseDao;
import a01179721.db.Database;

/**
 * @author Gabor Siffer, A01179721
 *
 */
@SuppressWarnings("serial")
public class MainFrame extends javax.swing.JFrame {
	private JMenuItem mntmAbout;
	private final String MESSAGE = String.format("Assignment2%nBy Gabor Siffer A01179721");
	private final String COUNT_MESSAGE = "You have %d %s in the %s database";
	public static int sortCodeCustomer;
	private int[] sortCodeBook;
	private int[] sortCodePurchase;
	private JMenuItem mntmTotal;
	private JMenuItem mntmFilterByCustomer;
	private LinkedList<String> sqlQuery;
	private static MainFrame theInstance = new MainFrame();

	/**
	 * Create the frame.
	 */
	private MainFrame() {
		sortCodeBook = new int[] { 0, 0 };
		sortCodePurchase = new int[] { 0, 0, 0 };
		setBounds(300, 100, 450, 150);
		setFont(new Font("Arial", Font.BOLD, 12));
		setTitle("Book Store");
		setDefaultCloseOperation(MainFrame.EXIT_ON_CLOSE);
		buildMenu();
	}

	public void buildMenu() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic('F');
		mnFile.setFont(new Font("Arial", Font.PLAIN, 12));
		menuBar.add(mnFile);

		JMenuItem mntmDrop = new JMenuItem("Drop");
		mntmDrop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(MainFrame.this, "Are you sure you want to drop all tables and exit App?", "WARNING",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					try {
						dropTables();
						dispose();
					} catch (ApplicationException e1) {
						e1.printStackTrace();
					}
				} else {
					// no option
				}

			}
		});
		KeyStroke keyStroketoDrop = KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.ALT_DOWN_MASK);
		mntmDrop.setAccelerator(keyStroketoDrop);
		mnFile.add(mntmDrop);
		mnFile.addSeparator();

		JMenuItem mntmExitAltx = new JMenuItem("Quit");
		mntmExitAltx.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		KeyStroke keyStroketoExit = KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.ALT_DOWN_MASK);
		mntmExitAltx.setAccelerator(keyStroketoExit);
		mntmExitAltx.setHorizontalAlignment(SwingConstants.LEFT);
		mntmExitAltx.setFont(new Font("Arial", Font.PLAIN, 12));
		mnFile.add(mntmExitAltx);

		JMenu mnBook = new JMenu("Books");
		mnBook.setFont(new Font("Arial", Font.PLAIN, 12));
		mnBook.setMnemonic('B');
		menuBar.add(mnBook);

		JMenuItem mntmNewMenuItem = new JMenuItem("Count");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String message;
				try {
					message = String.format(COUNT_MESSAGE, numberOfBook(), "books", "Book");
					JOptionPane.showMessageDialog(MainFrame.this, message, "Number of Books", JOptionPane.INFORMATION_MESSAGE);
				} catch (ApplicationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		mnBook.add(mntmNewMenuItem);
		mnBook.addSeparator();

		JCheckBoxMenuItem chckbxmntmByAuthor = new JCheckBoxMenuItem("By Author");
		chckbxmntmByAuthor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (chckbxmntmByAuthor.isSelected()) {
					sortCodeBook[0] = 1;
				} else {
					sortCodeBook[0] = 0;
				}
			}
		});
		mnBook.add(chckbxmntmByAuthor);

		JCheckBoxMenuItem chckbxmntmDescending = new JCheckBoxMenuItem("Descending");
		chckbxmntmDescending.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (chckbxmntmDescending.isSelected()) {
					sortCodeBook[1] = 1;
				} else {
					sortCodeBook[1] = 0;
				}
			}
		});
		mnBook.add(chckbxmntmDescending);
		mnBook.addSeparator();

		JMenuItem mntmNewMenuItem_3 = new JMenuItem("List");
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String[] sqlQuery = sortBook();
					if (sqlQuery != null) {
						BookList dialog = BookList.getTheInstance();
						dialog.fillList(sqlQuery);
						dialog.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(MainFrame.this, "Invalid filter combination.", "Warning", JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		mnBook.add(mntmNewMenuItem_3);

		JMenu mnCustomers = new JMenu("Customers");
		mnCustomers.setFont(new Font("Arial", Font.PLAIN, 12));
		mnCustomers.setMnemonic('C');
		menuBar.add(mnCustomers);

		JMenuItem mntmCount = new JMenuItem("Count");
		mntmCount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String message;
				try {
					message = String.format(COUNT_MESSAGE, numberOfCustomer(), "customers", "Customer");
					JOptionPane.showMessageDialog(MainFrame.this, message, "Number of Customers", JOptionPane.INFORMATION_MESSAGE);
				} catch (ApplicationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		mnCustomers.add(mntmCount);
		mnCustomers.addSeparator();

		JCheckBoxMenuItem chckbxmntmByJoinDate = new JCheckBoxMenuItem("By Join Date");
		chckbxmntmByJoinDate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (chckbxmntmByJoinDate.isSelected()) {
					sortCodeCustomer = 1;
				} else {
					sortCodeCustomer = 0;
				}
			}
		});
		mnCustomers.add(chckbxmntmByJoinDate);
		mnCustomers.addSeparator();

		JMenuItem mntmList = new JMenuItem("List");
		mntmList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					CustomerList dialog = CustomerList.getTheInstance();
					dialog.fillList(sortCustomer());
					dialog.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		mnCustomers.add(mntmList);

		JMenu mnPurchases = new JMenu("Purchases");
		mnPurchases.setFont(new Font("Arial", Font.PLAIN, 12));
		mnPurchases.setMnemonic('P');
		menuBar.add(mnPurchases);

		mntmTotal = new JMenuItem("Total");
		mntmTotal.setEnabled(false);
		mntmTotal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double sum = 0.0;
				for (String element : sqlQuery) {
					sum += Double.parseDouble(element.substring(element.indexOf('$') + 1));
				}
				try {
					String message = String.format("Total purchases: $%,.2f", sum);
					JOptionPane.showMessageDialog(MainFrame.this, message, "Total", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		mnPurchases.add(mntmTotal);
		mnPurchases.addSeparator();

		JCheckBoxMenuItem chckbxmntmByLastName = new JCheckBoxMenuItem("By Last Name");
		chckbxmntmByLastName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (chckbxmntmByLastName.isSelected()) {
					sortCodePurchase[0] = 1;
				} else {
					sortCodePurchase[0] = 0;
				}
			}
		});
		mnPurchases.add(chckbxmntmByLastName);

		JCheckBoxMenuItem chckbxmntmByTitle = new JCheckBoxMenuItem("By Title");
		chckbxmntmByTitle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (chckbxmntmByTitle.isSelected()) {
					sortCodePurchase[1] = 1;
				} else {
					sortCodePurchase[1] = 0;
				}
			}
		});
		mnPurchases.add(chckbxmntmByTitle);

		JCheckBoxMenuItem chckbxmntmDescending_1 = new JCheckBoxMenuItem("Descending");
		chckbxmntmDescending_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (chckbxmntmDescending_1.isSelected()) {
					sortCodePurchase[2] = 1;
				} else {
					sortCodePurchase[2] = 0;
				}
			}
		});
		mnPurchases.add(chckbxmntmDescending_1);
		mnPurchases.addSeparator();

		mntmFilterByCustomer = new JMenuItem("Filter by Customer ID");
		mntmFilterByCustomer.setEnabled(false);
		mntmFilterByCustomer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					CustomerIdList dialog = CustomerIdList.getTheInstance();
					dialog.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		mnPurchases.add(mntmFilterByCustomer);
		mnPurchases.addSeparator();

		JMenuItem mntmList_1 = new JMenuItem("List");
		mntmList_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createPurchaseList();
			}
		});
		mnPurchases.add(mntmList_1);

		JMenu mnHelp = new JMenu("Help");
		mnHelp.setFont(new Font("Arial", Font.PLAIN, 12));
		mnHelp.setMnemonic('H');
		menuBar.add(mnHelp);

		mntmAbout = new JMenuItem("About");
		KeyStroke keyStrokeAbout = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
		mntmAbout.setAccelerator(keyStrokeAbout);
		mntmAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MainFrame.this, MESSAGE, "About Assignment2", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mntmAbout.setFont(new Font("Arial", Font.PLAIN, 12));
		mntmAbout.setHorizontalAlignment(SwingConstants.LEFT);
		mnHelp.add(mntmAbout);
	}

	/**
	 * Count all customer
	 * 
	 * @return
	 * @throws ApplicationException
	 */
	private int numberOfCustomer() throws ApplicationException {
		try {
			Database db = CreateConnection.connect();
			int result = new CustomerDao(db).countAllRows();
			db.shutdown();
			return result;
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		}

	}

	/**
	 * Count all books
	 * 
	 * @return
	 * @throws ApplicationException
	 */
	private int numberOfBook() throws ApplicationException {
		try {
			Database db = CreateConnection.connect();
			int result = new BookDao(db).countAllRows();
			db.shutdown();
			return result;
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		}

	}

	/**
	 * Sort customer
	 * 
	 * @return
	 * @throws ApplicationException
	 */
	private String[] sortCustomer() throws ApplicationException {
		try {
			Database db = CreateConnection.connect();
			String[] result = new CustomerDao(db).sortCustomer(sortCodeCustomer);
			db.shutdown();
			return result;
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		}
	}

	/**
	 * Sort books
	 * 
	 * @return
	 * @throws ApplicationException
	 */
	private String[] sortBook() throws ApplicationException {
		try {
			Database db = CreateConnection.connect();
			String[] result = new BookDao(db).sortBook(sortCodeBook);
			db.shutdown();
			return result;
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		}
	}

	/**
	 * Sort purchase
	 * 
	 * @return
	 * @throws ApplicationException
	 */
	private LinkedList<String> sortPurchase() throws ApplicationException {
		try {
			Database db = CreateConnection.connect();
			LinkedList<String> result = new PurchaseDao(db).sortPurchase(sortCodePurchase);
			db.shutdown();
			return result;
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		}
	}

	/**
	 * Create purchase list
	 */
	private void createPurchaseList() {
		try {
			LinkedList<String> sqlQueryTemp = sortPurchase();
			if (sqlQueryTemp != null) {
				sqlQuery = sqlQueryTemp;
				PurchaseList dialog = PurchaseList.getTheInstance();
				dialog.fillList(sqlQuery);
				mntmTotal.setEnabled(true);
				mntmFilterByCustomer.setEnabled(true);
				dialog.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(MainFrame.this, "Invalid filter combination.", "Warning", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * List by customer id
	 * 
	 * @param id
	 * @throws ApplicationException
	 */
	public void listByCustomerId(String id) throws ApplicationException {
		LinkedList<String> sqlQueryTemp = sortPurchase();
		try {
			if (sqlQueryTemp != null) {
				Iterator<String> elements = sqlQueryTemp.iterator();
				while (elements.hasNext()) {
					String element = elements.next();
					if (!element.substring(0, 4).equals(id)) {
						elements.remove();
					}
				}
				if (sqlQueryTemp.size() != 0) {
					sqlQuery = sqlQueryTemp;
					PurchaseList dialog = PurchaseList.getTheInstance();
					dialog.fillList(sqlQuery);
				} else {
					String message = String.format("Customer Id: '%s' doesn't exist in the purchase database.", id);
					JOptionPane.showMessageDialog(MainFrame.this, message, "Warning", JOptionPane.INFORMATION_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(MainFrame.this, "Invalid filter combination.", "Warning", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Drop tables
	 * 
	 * @throws ApplicationException
	 */
	private void dropTables() throws ApplicationException {
		try {
			Database db = CreateConnection.connect();
			new CustomerDao(db).drop();
			new BookDao(db).drop();
			new PurchaseDao(db).drop();
			db.shutdown();
			System.exit(0);
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param switcher
	 */
	public void disableMenuItems(boolean switcher) {
		mntmTotal.setEnabled(switcher);
		mntmFilterByCustomer.setEnabled(switcher);
	}

	/**
	 * 
	 * @return
	 */
	public static MainFrame getTheInstance() {
		return theInstance;
	}

}
