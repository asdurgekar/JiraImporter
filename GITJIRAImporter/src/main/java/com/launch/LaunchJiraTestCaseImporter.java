package com.launch;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.JPopupMenu;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.SwingConstants;

public class LaunchJiraTestCaseImporter extends JFrame {

	private JPanel contentPane;
	public static JLabel lblCopyLoading;
	public static Boolean blnSameVersion;
	public static String strLatestReleaseInfo;
	public static String strReleaseHistory;
	public static String strCloudVersion;
	public static JTextPane txtpnncolor;
	public static JLabel lblToggleReleaseInfo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LaunchJiraTestCaseImporter frame = new LaunchJiraTestCaseImporter();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LaunchJiraTestCaseImporter() {
		
		//set look & feel for the application
		try {

			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				//Nimbus;System;Metal;Motif
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());	
		            
		            UIDefaults defaults = UIManager.getLookAndFeelDefaults();
		            NimbusLookAndFeel localNimbusLookAndFeel = (NimbusLookAndFeel)UIManager.getLookAndFeel();
		            Color derivedColor = localNimbusLookAndFeel.getDerivedColor("nimbusGreen", 0, 0.8835404F, 0, 0, true);		            
		            defaults.put("nimbusOrange",derivedColor);
		            
		            break;
		        }
		    }
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		SupportingMethods suppMethods = new SupportingMethods();
		
		//set path variables
		suppMethods.fnSetPathVariables();
		//create the log file for this component
		suppMethods.createLaunchLogFile();
		//get the tool version from common drive in Cloud
		//strCloudVersion = suppMethods.getCommonDriveCloudVersion();
		
		//get the tool version from Git Hub
		strCloudVersion = suppMethods.getGitHubVersion();
				
		
		blnSameVersion = suppMethods.verifyToolVersion(strCloudVersion);
		if(blnSameVersion)
		{
			suppMethods.LaunchTool();
		}
		/*
		//get the Tool's latest version release information
		strLatestReleaseInfo = suppMethods.getLatestReleaseInfo();
		strReleaseHistory = suppMethods.getReleaseHistory();
		*/
		
		
		//get the Tool's latest version release information from Git Hub
		strLatestReleaseInfo = suppMethods.getGitHubReleaseInfo();
		strReleaseHistory = suppMethods.getGitHubReleaseHistory();
		
		setResizable(false);
		setTitle("Jira Test Case Importer - " + strCloudVersion + " : New Version available!");
		setIconImage(Toolkit.getDefaultToolkit().getImage(LaunchJiraTestCaseImporter.class.getResource("/images/JiraIcon.png")));
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 526, 396);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblANewVersion = new JLabel("Version " + strCloudVersion + " of Jira Test Case Importer is available for Update.");
		lblANewVersion.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblANewVersion.setBounds(69, 13, 400, 34);
		contentPane.add(lblANewVersion);
		
		JButton btnDownload = new JButton("Update & Launch");
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Copy files from Cloud and launch tool
				suppMethods.copyFiles_Launch();
				
			}
		});
		btnDownload.setBounds(88, 281, 142, 23);
		contentPane.add(btnDownload);
		
		JButton btnIgnore = new JButton("Run Existing Version");
		btnIgnore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//Launch Tool
				suppMethods.LaunchTool();
				
			}
		});
		btnIgnore.setBounds(270, 281, 142, 23);
		contentPane.add(btnIgnore);
		
		lblCopyLoading = new JLabel("");
		lblCopyLoading.setIcon(new ImageIcon(LaunchJiraTestCaseImporter.class.getResource("/images/Spinner-1s-78px.gif")));
		lblCopyLoading.setBounds(207, 295, 82, 67);
		contentPane.add(lblCopyLoading);
		
		JLabel lblBelowAreThe = new JLabel("Below are the updates for this version");
		lblBelowAreThe.setBounds(78, 47, 228, 16);
		contentPane.add(lblBelowAreThe);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(78, 67, 348, 179);
		contentPane.add(scrollPane);
		
		txtpnncolor = new JTextPane();
		txtpnncolor.setEditable(false);
		txtpnncolor.setContentType("text/html");
		scrollPane.setViewportView(txtpnncolor);
		txtpnncolor.setText(strLatestReleaseInfo);
		txtpnncolor.setCaretPosition(0);
		
		lblToggleReleaseInfo = new JLabel("<html><a href=''>View Release History</a></html>");
		lblToggleReleaseInfo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblToggleReleaseInfo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblToggleReleaseInfo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				JLabel ToggleLabel = (JLabel) arg0.getSource();				
				suppMethods.displayReleaseInfo(ToggleLabel.getText());
			}
		});
		lblToggleReleaseInfo.setBounds(284, 244, 142, 23);
		contentPane.add(lblToggleReleaseInfo);
		
		JLabel lblBackground = new JLabel("");
		lblBackground.setIcon(new ImageIcon(LaunchJiraTestCaseImporter.class.getResource("/images/FileTransfer_WithCignitiLogo.png")));
		lblBackground.setBounds(0, 0, 520, 368);
		contentPane.add(lblBackground);
		lblCopyLoading.setVisible(false);
		
		
	}
}
