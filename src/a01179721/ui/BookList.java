/**
 * Project: a01179721_Assignment2
 * File: BookList.java
 * Date: Mar. 19, 2020
 * Time: 12:28:00 p.m.
 */
package a01179721.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

/**
 * @author Gabor Siffer, A01179721
 *
 */
@SuppressWarnings("serial")
public class BookList extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private static BookList theInstance = new BookList();
	private final DefaultListModel<String> listModel;
	private JList<String> list;

	/**
	 * Create the dialog.
	 */
	private BookList() {
		listModel = new DefaultListModel<String>();
		setTitle("Books");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(300, 245, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		{
			list = new JList<String>(listModel);
			contentPanel.add(list, "cell 0 0,grow");
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
	}

	/**
	 * 
	 * @return
	 */
	public static BookList getTheInstance() {
		return theInstance;
	}

}
