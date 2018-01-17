package com.cigniti;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.cigniti.util.Globalvars;

import net.sourceforge.htmlunit.corejs.javascript.tools.shell.Global;

public class GetCredentials extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final List<byte[]> holder = new ArrayList<byte[]>();

	private static String OK = "ok";
	private static String Cancel = "Cancel";
	private static JFrame controllingFrame; // needed for dialogs
	private static JPasswordField passwordField;
	private static JTextField usernameField;
	private static JTextField almServerUrlField;
	private static JTextField almDomainField;
	private static JTextField almProjectField;
	private static JTextField almPathField;
	private static JTextField DownloadPathField;

	private static JTextField JIRA_userNameField;
	private static JPasswordField JIRA_passwordField;
	private static JTextField JIRA_projectIdField;
	private static JTextField JIRA_issueTypeIdField;
	
	
	private byte[] credBytes;

	public void actionPerformed(ActionEvent e) {

		synchronized (this.holder) {
			String cmd = e.getActionCommand();

			if (OK.equals(cmd)) { // Process the password.

				try {
					String username = usernameField.getText();
					String strPassword = new String(passwordField.getPassword());

					Globalvars.userName = usernameField.getText();
					Globalvars.password = passwordField.getPassword().toString();
					Globalvars.almServerUrl = almServerUrlField.getText();
					Globalvars.almDomain = almDomainField.getText();
					Globalvars.almProject = almProjectField.getText();
					System.out.println(Globalvars.almProject);
					Globalvars.almPath = almPathField.getText();
					Globalvars.DownloadPath = DownloadPathField.getText();
					
					Globalvars.JIRA_userName = JIRA_userNameField.getText();
					Globalvars.JIRA_password = JIRA_passwordField.getText();
					Globalvars.JIRA_projectId = JIRA_projectIdField.getText();
					Globalvars.JIRA_issueTypeId = JIRA_issueTypeIdField.getText();

					if (username == null || strPassword == null) {
						System.out.println("Invalid Alm Credentials.Unable to download testcases from ALM");
					}

					credBytes = (username + ":" + strPassword).getBytes();
					// Arrays.fill(password, '0');
					passwordField.selectAll();
					controllingFrame.dispose();
					this.holder.add(credBytes);
					this.holder.notify();
				} catch (Exception e2) {
					System.out.println("Error getting the ALM credentials");
				}
			}
		}
		controllingFrame.dispose();
	}

	public byte[] getAlmCredentials() {
		// Create and set up the window.
		controllingFrame = new JFrame("AlmCredentials");
		controllingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		passwordField = new JPasswordField(15);
		usernameField = new JTextField(15);
		
		
		almServerUrlField = new JTextField();
		almDomainField  = new JTextField();
		almProjectField = new JTextField();
		almPathField = new JTextField();
		DownloadPathField = new JTextField();

		JIRA_userNameField = new JTextField();
		JIRA_passwordField = new JPasswordField();
		JIRA_projectIdField = new JTextField();
		JIRA_issueTypeIdField = new JTextField();


		usernameField.setText(Globalvars.userName);
		passwordField.setText(Globalvars.password);

		almServerUrlField.setText(Globalvars.almServerUrl);
		almDomainField.setText(Globalvars.almDomain);
		almProjectField.setText(Globalvars.almProject);
		almPathField.setText(Globalvars.almPath);
		DownloadPathField.setText(Globalvars.DownloadPath);
		
		JIRA_userNameField.setText(Globalvars.JIRA_userName);
		JIRA_passwordField.setText(Globalvars.JIRA_password);
		JIRA_projectIdField.setText(Globalvars.JIRA_projectId);
		JIRA_issueTypeIdField.setText(Globalvars.JIRA_issueTypeId);

		JLabel usernamelabel = new JLabel("Enter your ALM Username: ");
		usernamelabel.setLabelFor(usernameField);

		JLabel label = new JLabel("Enter your ALM password: ");
		label.setLabelFor(passwordField);

		JLabel almServerLabel = new JLabel("Enter your ALM Server URL: ");
		almServerLabel.setLabelFor(almServerUrlField);

		JLabel almDomainLabel = new JLabel("Enter your ALM Domain: ");
		almDomainLabel.setLabelFor(almDomainField);

		JLabel almProjectLabel = new JLabel("Enter your ALM Project: ");
		almProjectLabel.setLabelFor(almProjectField);

		JLabel almPathLabel = new JLabel("Enter your ALM Test Case Path: ");
		almPathLabel.setLabelFor(almPathField);

		JLabel downloadPathLabel = new JLabel("Enter your Path for Attachments CSV file: ");
		downloadPathLabel.setLabelFor(DownloadPathField);

		JLabel JIRA_userNameLabel = new JLabel("Enter your Jira UserName: ");
		JIRA_userNameLabel.setLabelFor(JIRA_userNameField);

		JLabel JIRA_passwordLabel = new JLabel("Enter your Jira password: ");
		JIRA_passwordLabel.setLabelFor(JIRA_passwordField);

		JLabel JIRA_projectIdLabel = new JLabel("Enter Jira Project Id: ");
		JIRA_projectIdLabel.setLabelFor(JIRA_projectIdField);

		JLabel JIRA_issueTypeLabel = new JLabel("Enter Jira test Case Issue Type Id: ");
		JIRA_issueTypeLabel.setLabelFor(JIRA_issueTypeIdField);

		
		
		JPanel p = new JPanel(new GridLayout(0, 1));
		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("Cancel");

		okButton.setActionCommand(OK);
		cancelButton.setActionCommand(Cancel);

		p.add(okButton);
		p.add(cancelButton);

		okButton.addActionListener(this);
		cancelButton.addActionListener(this);

		JPanel contentpane = new JPanel();
		JComponent buttonPane = p;

		// Lay out everything.
		JPanel textPane = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		textPane.setLayout(new BoxLayout(textPane, BoxLayout.Y_AXIS));
		textPane.add(usernamelabel);
		textPane.add(usernameField);

		textPane.add(label);
		textPane.add(passwordField);

		textPane.add(almServerLabel);
		textPane.add(almServerUrlField);

		textPane.add(almDomainLabel);
		textPane.add(almDomainField);

		textPane.add(almProjectLabel);
		textPane.add(almProjectField);

		textPane.add(almPathLabel);
		textPane.add(almPathField);

		textPane.add(JIRA_userNameLabel);
		textPane.add(JIRA_userNameField);

		textPane.add(JIRA_passwordLabel);
		textPane.add(JIRA_passwordField);

		textPane.add(JIRA_projectIdLabel);
		textPane.add(JIRA_projectIdField);

		textPane.add(JIRA_issueTypeLabel);
		textPane.add(JIRA_issueTypeIdField);

		textPane.add(downloadPathLabel);
		textPane.add(DownloadPathField);

		contentpane.add(textPane);
		contentpane.add(buttonPane);

		contentpane.setOpaque(true); // content panes must be opaque
		controllingFrame.setContentPane(contentpane);

		controllingFrame.pack();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		controllingFrame.setLocation(dim.width / 2 - this.getSize().width / 2,
				dim.height / 2 - this.getSize().height / 2);
		controllingFrame.setVisible(true);
		while (this.holder.isEmpty()) {
			try {
				this.holder.wait();
			} catch (Exception e) {
			}
		}

		credBytes = this.holder.remove(0);
		return this.credBytes;
	}

}
