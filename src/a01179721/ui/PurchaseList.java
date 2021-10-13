/**
 * Project: a01179721_Assignment2
 * File: PurchaseList.java
 * Date: Mar. 19, 2020
 * Time: 3:26:10 p.m.
 */
package a01179721.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;

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
public class PurchaseList extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private final DefaultListModel<String> listModel;
	private JList<String> list;
	private static PurchaseList theInstance = new PurchaseList();

	/**
	 * Create the dialog.
	 */
	private PurchaseList() {
		listModel = new DefaultListModel<String>();
		setTitle("Purchase");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(300, 245, 450, 300);
		// setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				MainFrame.getTheInstance().disableMenuItems(false);
				CustomerIdList.getTheInstance().close();
			}
		});
		createList();
	}

	public void createList() {
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
	public void fillList(LinkedList<String> listElements) {
		if (listElements != null) {
			listModel.clear();
			for (String element : listElements) {
				listModel.addElement(element);
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public static PurchaseList getTheInstance() {
		return theInstance;
	}

}
