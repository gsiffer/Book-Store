/**
 * Project: a01179721_Assignment2
 * File: CustomerIdList.java
 * Date: Mar. 20, 2020
 * Time: 4:32:13 p.m.
 */
package a01179721.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import a01179721.ApplicationException;
import net.miginfocom.swing.MigLayout;

/**
 * @author Gabor Siffer, A01179721
 *
 */
@SuppressWarnings("serial")
public class CustomerIdList extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private static CustomerIdList theInstance = new CustomerIdList();
	private JTextField textField;

	/**
	 * Create the dialog.
	 */
	private CustomerIdList() {
		setTitle("Customer ID");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(300, 100, 450, 150);
		// setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][][grow]", "[][]"));
		{
			JLabel lblCustomerId = new JLabel("Customer ID:");
			contentPanel.add(lblCustomerId, "cell 1 1,alignx trailing");
		}
		{
			textField = new JTextField();
			contentPanel.add(textField, "cell 2 1,growx");
			textField.setColumns(10);
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
						try {
							MainFrame.getTheInstance().listByCustomerId(textField.getText().trim());
						} catch (ApplicationException e1) {
							e1.printStackTrace();
						}
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	public void close() {
		dispose();
	}

	/**
	 * 
	 * @return
	 */
	public static CustomerIdList getTheInstance() {
		return theInstance;
	}

}
