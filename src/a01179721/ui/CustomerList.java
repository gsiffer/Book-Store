/**
 * Project: a01179721_Assignment2
 * File: CustomerList.java
 * Date: Mar. 17, 2020
 * Time: 4:09:31 p.m.
 */
package a01179721.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import a01179721.data.CustomerDao;
import a01179721.db.Database;
import net.miginfocom.swing.MigLayout;

/**
 * @author Gabor Siffer, A01179721
 *
 */
@SuppressWarnings("serial")
public class CustomerList extends JDialog {

	private final DefaultListModel<String> listModel;
	private final JPanel contentPanel = new JPanel();
	private static final String HEADER_FORMAT = "%-8s %s";
	private JList<String> list;
	private static CustomerList theInstance = new CustomerList();
	private int selectedIndex = 0;

	/**
	 * Create the dialog.
	 * 
	 */
	private CustomerList() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				CustomerDialog.getTheInstance().close();
			}
		});
		listModel = new DefaultListModel<String>();

		setTitle("Customers");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(300, 245, 450, 300);
		// setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[][grow]"));
		{
			String h = String.format(HEADER_FORMAT, " ID", "Name");
			JLabel lblId = new JLabel(h);
			contentPanel.add(lblId, "cell 0 0 2 1,alignx left,aligny bottom");
		}
		createList();
	}

	public void createList() {

		{
			list = new JList<String>(listModel);
			list.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					selectedIndex = list.getSelectedIndex();
					selectCustomer();
				}
			});

			setSize(450, 300);
			getContentPane().add(contentPanel, BorderLayout.NORTH);
			getContentPane().add(new JScrollPane(list), BorderLayout.CENTER);
		}

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (!list.isSelectionEmpty()) {
							selectedIndex = list.getSelectedIndex();
							selectCustomer();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Close");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						CustomerDialog.getTheInstance().close();
						dispose();
					}
				});
				cancelButton.setActionCommand("Close");
				buttonPane.add(cancelButton);
			}
		}
	}

	/**
	 * Select a customer
	 */
	private void selectCustomer() {
		try {
			CustomerDialog dialog = CustomerDialog.getTheInstance();
			dialog.fillTextFields(list.getSelectedValue().substring(0, 4));
			dialog.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static CustomerList getTheInstance() {
		return theInstance;
	}

	/**
	 * Fill list with data
	 * 
	 * @param listElements
	 */
	public void fillList(String[] listElements) {
		listModel.clear();
		for (String element : listElements) {
			listModel.addElement(element);
		}
		list.setSelectedIndex(0);
	}

	/**
	 * Fill list if data changed
	 * 
	 * @param db
	 */
	public void changesHappened(Database db) {
		listModel.clear();
		try {
			String[] result = new CustomerDao(db).sortCustomer(MainFrame.sortCodeCustomer);
			for (String element : result) {
				listModel.addElement(element);
			}
			list.setSelectedIndex(selectedIndex);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
