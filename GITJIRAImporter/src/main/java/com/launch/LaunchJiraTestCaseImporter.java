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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import java.awt.Font;

public class LaunchJiraTestCaseImporter extends JFrame {

	private JPanel contentPane;
	public static JLabel lblCopyLoading;
	public static Boolean blnSameVersion;
	public static String strReleaseInfo;
	public static String strCloudVersion;

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
		
		setResizable(false);
		setTitle("Jira Test Case Importer - New Version available!");
		setIconImage(Toolkit.getDefaultToolkit().getImage(LaunchJiraTestCaseImporter.class.getResource("/images/JiraIcon.png")));
		
		
		SupportingMethods suppMethods = new SupportingMethods();
		
		blnSameVersion = suppMethods.verifyToolVersion();
		if(blnSameVersion)
		{
			suppMethods.LaunchTool();
		}
		//get the tool version in Cloud
		strCloudVersion = suppMethods.getCloudVersion();
		
		//get the Tool's latest version release information
		strReleaseInfo = suppMethods.getReleaseInfo();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 526, 396);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblANewVersion = new JLabel("Version 1.6 of Jira Test Case Importer is available for download.");
		lblANewVersion.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblANewVersion.setBounds(68, 16, 400, 34);
		contentPane.add(lblANewVersion);
		
		JButton btnDownload = new JButton("Download");
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Copy files from Cloud and launch tool
				suppMethods.copyFiles_Launch();
				
			}
		});
		btnDownload.setBounds(120, 272, 89, 23);
		contentPane.add(btnDownload);
		
		JButton btnIgnore = new JButton("Ignore");
		btnIgnore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//Launch Tool
				suppMethods.LaunchTool();
				
			}
		});
		btnIgnore.setBounds(286, 272, 89, 23);
		contentPane.add(btnIgnore);
		
		lblCopyLoading = new JLabel("");
		lblCopyLoading.setIcon(new ImageIcon(LaunchJiraTestCaseImporter.class.getResource("/images/Spinner-1s-78px.gif")));
		lblCopyLoading.setBounds(207, 295, 82, 67);
		contentPane.add(lblCopyLoading);
		
		JLabel lblBelowAreThe = new JLabel("Below are the updates for this version");
		lblBelowAreThe.setBounds(78, 62, 228, 16);
		contentPane.add(lblBelowAreThe);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(75, 81, 348, 179);
		contentPane.add(scrollPane);
		
		JTextPane txtpnncolor = new JTextPane();
		txtpnncolor.setEditable(false);
		txtpnncolor.setContentType("text/html");
		scrollPane.setViewportView(txtpnncolor);
		txtpnncolor.setText(strReleaseInfo);
		txtpnncolor.setCaretPosition(0);
		
		JLabel lblBackground = new JLabel("");
		lblBackground.setIcon(new ImageIcon(LaunchJiraTestCaseImporter.class.getResource("/images/FileTransfer_Resized1.png")));
		lblBackground.setBounds(0, 0, 520, 368);
		contentPane.add(lblBackground);
		lblCopyLoading.setVisible(false);
	}
}
