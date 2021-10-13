/**
 * Project: a01179721Lab9
 * File: CustomerDialog.java
 * Date: Mar. 13, 2020
 * Time: 8:00:38 p.m.
 */
package a01179721.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import a01179721.ApplicationException;
import a01179721.data.CreateConnection;
import a01179721.data.CustomerDao;
import a01179721.db.Database;
import a01179721.util.Validator;
import net.miginfocom.swing.MigLayout;

/**
 * @author Gabor Siffer, A01179721
 *
 */
@SuppressWarnings("serial")
public class CustomerDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField;
	private static CustomerDialog theInstance = new CustomerDialog();

	/**
	 * Create the dialog.
	 * 
	 * @throws Exception
	 * @throws ApplicationException
	 * @throws SQLException
	 * @throws IOException
	 */
	private CustomerDialog() {
		setTitle("Selected Customer");
		setBounds(740, 100, 450, 400);
		// setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[3.00][78.00][grow]", "[42.00][15.00][][][][][][][][]"));
		{
			JLabel lblRandomCustomer = new JLabel("Selected Customer");
			lblRandomCustomer.setFont(new Font("Arial", Font.PLAIN, 14));
			contentPanel.add(lblRandomCustomer, "cell 1 0 2 1,alignx center,aligny top");
		}
		{
			JLabel lblId = new JLabel("ID");
			lblId.setFont(new Font("Tahoma", Font.BOLD, 11));
			contentPanel.add(lblId, "cell 1 1,alignx trailing");
		}
		{
			textField = new JTextField();
			textField.setFont(new Font("Tahoma", Font.BOLD, 11));
			textField.setFocusable(false);
			textField.setBackground(SystemColor.info);
			textField.setEditable(false);
			contentPanel.add(textField, "cell 2 1,growx");
			textField.setColumns(10);
		}
		{
			JLabel lblFirsName = new JLabel("First Name");
			lblFirsName.setFont(UIManager.getFont("Button.font"));
			contentPanel.add(lblFirsName, "cell 1 2,alignx trailing");
		}
		{
			textField_1 = new JTextField();
			textField_1.setBackground(Color.WHITE);
			contentPanel.add(textField_1, "cell 2 2,growx");
			textField_1.setColumns(10);
		}
		{
			JLabel lblLastName = new JLabel("Last Name");
			lblLastName.setFont(UIManager.getFont("Button.font"));
			contentPanel.add(lblLastName, "cell 1 3,alignx trailing");
		}
		{
			textField_2 = new JTextField();
			textField_2.setBackground(Color.WHITE);
			contentPanel.add(textField_2, "cell 2 3,growx");
			textField_2.setColumns(10);
		}
		{
			JLabel lblStreet = new JLabel("Street");
			lblStreet.setFont(UIManager.getFont("Button.font"));
			contentPanel.add(lblStreet, "cell 1 4,alignx trailing");
		}
		{
			textField_3 = new JTextField();
			textField_3.setBackground(Color.WHITE);
			contentPanel.add(textField_3, "cell 2 4,growx");
			textField_3.setColumns(10);
		}
		{
			JLabel lblCity = new JLabel("City");
			lblCity.setFont(UIManager.getFont("Button.font"));
			contentPanel.add(lblCity, "cell 1 5,alignx trailing");
		}
		{
			textField_4 = new JTextField();
			textField_4.setBackground(Color.WHITE);
			contentPanel.add(textField_4, "cell 2 5,growx");
			textField_4.setColumns(10);
		}
		{
			JLabel lblPostalCode = new JLabel("Postal Code");
			lblPostalCode.setFont(UIManager.getFont("Button.font"));
			contentPanel.add(lblPostalCode, "cell 1 6,alignx right");
		}
		{
			textField_5 = new JTextField();
			textField_5.setBackground(Color.WHITE);
			contentPanel.add(textField_5, "cell 2 6,growx");
			textField_5.setColumns(10);
		}
		{
			JLabel lblPhone = new JLabel("Phone");
			lblPhone.setFont(UIManager.getFont("Button.font"));
			contentPanel.add(lblPhone, "cell 1 7,alignx trailing");
		}
		{
			textField_6 = new JTextField();
			textField_6.setBackground(Color.WHITE);
			contentPanel.add(textField_6, "cell 2 7,growx");
			textField_6.setColumns(10);
		}
		{
			JLabel lblEmail = new JLabel("Email");
			lblEmail.setFont(UIManager.getFont("Button.font"));
			contentPanel.add(lblEmail, "cell 1 8,alignx trailing");
		}
		{
			textField_7 = new JTextField();
			textField_7.setBackground(Color.WHITE);
			contentPanel.add(textField_7, "cell 2 8,growx");
			textField_7.setColumns(10);
		}
		{
			JLabel lblJoinedDate = new JLabel("Joined Date");
			lblJoinedDate.setFont(UIManager.getFont("Button.font"));
			contentPanel.add(lblJoinedDate, "cell 1 9,alignx trailing");
		}
		{
			textField_8 = new JTextField();
			textField_8.setBackground(Color.WHITE);
			contentPanel.add(textField_8, "cell 2 9,growx");
			textField_8.setColumns(10);
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
						if (JOptionPane.showConfirmDialog(CustomerDialog.this, "Are you sure you want to update this customer?", "WARNING",
								JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
							try {
								String[] updateCells = { textField.getText(), //
										textField_1.getText(), //
										textField_2.getText(), //
										textField_3.getText(), //
										textField_4.getText(), //
										textField_5.getText(), //
										textField_6.getText(), //
										textField_7.getText(), //
										textField_8.getText(),//
								};

								if (!validateTextFields()) {
									return;
								}
								updateCustomer(updateCells);
								dispose();
							} catch (ApplicationException e1) {
								e1.printStackTrace();
							}
						} else {
							// no option
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
						dispose();
					}
				});
				cancelButton.setActionCommand("Close");
				buttonPane.add(cancelButton);
			}
		}
	}

	/**
	 * Get a customer String[]
	 * 
	 * @return
	 * @throws ApplicationException
	 */
	private String[] getCustomer(String customerId) throws ApplicationException {
		String[] result;
		try {
			Database db = CreateConnection.connect();
			result = new CustomerDao(db).SelectedCustomer(customerId);
			db.shutdown();
			return result;
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		}
	}

	/**
	 * Fill text fields with data
	 * 
	 * @param customerId
	 * @throws ApplicationException
	 */
	public void fillTextFields(String customerId) throws ApplicationException {
		String[] textFields = getCustomer(customerId);
		textField.setText(textFields[0]);
		textField_1.setText(textFields[1]);
		textField_2.setText(textFields[2]);
		textField_3.setText(textFields[3]);
		textField_4.setText(textFields[4]);
		textField_5.setText(textFields[5]);
		textField_6.setText(textFields[6]);
		textField_7.setText(textFields[7]);
		textField_8.setText(textFields[8].substring(0, 10));
	}

	/**
	 * Update customer
	 * 
	 * @param customer
	 * @throws ApplicationException
	 */
	private void updateCustomer(String[] customer) throws ApplicationException {
		try {
			Database db = CreateConnection.connect();
			new CustomerDao(db).update(customer);
			CustomerList.getTheInstance().changesHappened(db);
			db.shutdown();
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		}
	}

	private boolean validateTextFields() {
		boolean validate = true;
		String yyyymmdd = textField_8.getText().replace("-", "");
		if (!Validator.validateJoinedDate(yyyymmdd)) {
			JOptionPane.showMessageDialog(CustomerDialog.this, String.format("Invalid joined date: %s", textField_8.getText()), "Warning",
					JOptionPane.INFORMATION_MESSAGE);

			validate = false;
		} else if (!Validator.validateEmail(textField_7.getText())) {
			JOptionPane.showMessageDialog(CustomerDialog.this, String.format("Invalid email address: %s", textField_7.getText()), "Warning",
					JOptionPane.INFORMATION_MESSAGE);

			validate = false;
		}

		return validate;
	}

	/**
	 * 
	 */
	public void close() {
		dispose();
	}

	/**
	 * 
	 * @return
	 */
	public static CustomerDialog getTheInstance() {
		return theInstance;
	}
}
