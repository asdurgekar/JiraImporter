package com.cigniti.JIRA;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.CardLayout;
import java.awt.SystemColor;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import java.awt.List;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.cigniti.util.Globalvars;

import javax.swing.JTree;
import javax.swing.JTextPane;
import javax.swing.JCheckBox;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.border.EtchedBorder;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import javax.swing.border.LineBorder;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

public class ImportTestCases extends JFrame {

	private JPanel contentPane;
	public static JTextField txtExcelPath;
	public static JTable tblMapping;
	public static JPasswordField txtPassword;
	public static JTextField txtUserName;
	public static JLabel lblAuthmessage;
	public static JPanel panelLogin;
	public static JPanel panelSecond;
	public static JPanel panelMapping;
	public static JPanel panelConfirm;
	public static JPanel panelFinal;
	public static JComboBox comBoxProjName;
	public static JCheckBox chckbxRememberMe;
	public static JLabel lblValidationMessage;
	public static JButton btnNext;
	public static JList lstJiraFields;
	public static JList lstWorkSheets;
	public static JList lstExcelColumns;
	public static JButton btnValidate;
	public static JLabel lblValidationmessage;
	public static JLabel lblCheckmark;
	public static JLabel lblProjectNameValue;
	public static JLabel lblExcelPathValue;
	public static JLabel lblSheetNameValue;
	public static JTable tblMapConfirm;
	public static JLabel lblTestCasesCount;
	public static JLabel lblTotaltestcasesvalue;
	public static JLabel lblTotaltestcasesuploadedvalue;
	public static JProgressBar prgbarImport;
	public static JButton btnCancelImport;
	public static JButton btnRunBack;
	public static JButton btnClose;
	public static JTextArea txtAreaConsole;
	public static JLabel lblSuccessMessage;
	public static JPasswordField txtAccessKey;
	public static JPasswordField txtSecretKey;
	public static JLabel lblLoginloading;
	public static JButton btnLogin;
	public static JLabel lblValidateloading;
	public static JButton btnMapNext;
	
	ImportTestCaseswithSteps ITCWS =	new ImportTestCaseswithSteps();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImportTestCases frame = new ImportTestCases();
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
	public ImportTestCases() {
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				ITCWS.fnInitialization();
				ITCWS.fnLoadPreferences();
			}
			
		});
		
		addWindowListener( new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
		        JFrame pframe = (JFrame)e.getSource();
		    	JFrame frame = new JFrame("...");
		        int result = JOptionPane.showConfirmDialog(frame,"Are you sure you want to exit the application?",
		            "Exit Application",JOptionPane.YES_NO_OPTION);
		        if (result == JOptionPane.YES_OPTION)
		            dispose();
		        else
		        	pframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		    }
		});
		setBackground(Color.WHITE);
		setTitle(ITCWS.appName + " v" + ITCWS.versionNumber + " - " + ITCWS.pageName);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ImportTestCases.class.getResource("/images/JiraIcon.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 744, 472);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.inactiveCaption);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		panelLogin = new JPanel();
		panelLogin.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				ITCWS.pageName = "Login";
				setTitle(ITCWS.appName + " v" + ITCWS.versionNumber + " - " + ITCWS.pageName);
			}
		});
		contentPane.add(panelLogin, "name_1090758453858820");
		
		btnLogin = new JButton("Login");
		btnLogin.setBounds(289, 313, 157, 23);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					if(ITCWS.fn_LoginToJira())
					{
						getRootPane().setDefaultButton(null);
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		panelLogin.setLayout(null);
		panelLogin.add(btnLogin);
		getRootPane().setDefaultButton(btnLogin);
		
		JLabel lblUserName = new JLabel("User Name");
		lblUserName.setForeground(new Color(0, 0, 0));
		lblUserName.setBounds(173, 100, 67, 14);
		lblUserName.setFont(new Font("Tahoma", Font.BOLD, 11));
		panelLogin.add(lblUserName);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(173, 149, 67, 14);
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 11));
		panelLogin.add(lblPassword);
		
		JLabel lblWelcomeMessage = new JLabel("");
		lblWelcomeMessage.setBounds(269, 64, 151, 14);
		lblWelcomeMessage.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblWelcomeMessage.setHorizontalAlignment(SwingConstants.CENTER);
		panelLogin.add(lblWelcomeMessage);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(269, 145, 219, 20);
		panelLogin.add(txtPassword);
		
		txtUserName = new JTextField();
		txtUserName.setBounds(271, 96, 217, 20);
		panelLogin.add(txtUserName);
		txtUserName.setColumns(10);
		
		lblAuthmessage = new JLabel("");
		lblAuthmessage.setBounds(189, 350, 380, 14);
		lblAuthmessage.setForeground(Color.RED);
		lblAuthmessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblAuthmessage.setFont(new Font("Tahoma", Font.PLAIN, 11));
		panelLogin.add(lblAuthmessage);
		
		chckbxRememberMe = new JCheckBox("");
		chckbxRememberMe.setBackground(Color.GRAY);
		chckbxRememberMe.setBounds(310, 271, 21, 14);
		panelLogin.add(chckbxRememberMe);
		
		txtAccessKey = new JPasswordField();
		txtAccessKey.setBounds(269, 193, 219, 20);
		panelLogin.add(txtAccessKey);
		
		txtSecretKey = new JPasswordField();
		txtSecretKey.setBounds(269, 244, 219, 20);
		panelLogin.add(txtSecretKey);
		
		JLabel lblAccessKey = new JLabel("Access Key");
		lblAccessKey.setBounds(173, 195, 67, 14);
		lblAccessKey.setFont(new Font("Tahoma", Font.BOLD, 11));
		panelLogin.add(lblAccessKey);
		
		JLabel lblSecretKey = new JLabel("Secret Key");
		lblSecretKey.setBounds(173, 248, 67, 14);
		lblSecretKey.setFont(new Font("Tahoma", Font.BOLD, 11));
		panelLogin.add(lblSecretKey);
		
		JLabel lblRememberMe = new JLabel("Remember Me");
		lblRememberMe.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblRememberMe.setBounds(337, 273, 109, 14);
		panelLogin.add(lblRememberMe);
		
		lblLoginloading = new JLabel("");
		lblLoginloading.setIcon(new ImageIcon(ImportTestCases.class.getResource("/images/Spinner-1s-78px_White.gif")));
		lblLoginloading.setBounds(327, 342, 78, 61);
		lblLoginloading.setVisible(false);
		panelLogin.add(lblLoginloading);
		
		JLabel lblBackground = new JLabel("");
		lblBackground.setBackground(UIManager.getColor("Button.shadow"));
		lblBackground.setIcon(new ImageIcon(ImportTestCases.class.getResource("/images/NewBackground1.png")));
		lblBackground.setBounds(0, 0, 728, 434);
		panelLogin.add(lblBackground);
		
		
		panelSecond = new JPanel();
		panelSecond.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				ITCWS.pageName = "Project & Sheet";
				setTitle(ITCWS.appName + " v" + ITCWS.versionNumber + " - " + ITCWS.pageName);
			}
		});
		contentPane.add(panelSecond, "name_1090766844706967");
		panelSecond.setLayout(null);
		
		txtExcelPath = new JTextField();
		txtExcelPath.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
				  
				  if(ITCWS.fnValidateFileFormat())
					  ITCWS.fnLoadExcelSheets();  
			  }
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				if(ITCWS.fnValidateFileFormat())
					  ITCWS.fnLoadExcelSheets(); 
			}
			@Override
			public void removeUpdate(DocumentEvent arg0) {

				if(ITCWS.fnValidateFileFormat())
				{
					ITCWS.fnLoadExcelSheets(); 
					
				}
			}
		
		  	
		});
		txtExcelPath.setBounds(230, 180, 293, 20);
		panelSecond.add(txtExcelPath);
		txtExcelPath.setColumns(10);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				final JFileChooser fc = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
				        "Excel Files (.xls, .xlsx, .xlsm)", "xls", "xlsx", "xlsm");
				fc.setFileFilter(filter);
			    int returnVal = fc.showOpenDialog(fc);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			       System.out.println("You chose to open this file: " +
			    		   fc.getSelectedFile().getName());
			       File selFile = fc.getSelectedFile();
					txtExcelPath.setText(selFile.getAbsolutePath());
					
			    }
			}
		});
		btnBrowse.setBounds(533, 179, 89, 23);
		panelSecond.add(btnBrowse);
		
		comBoxProjName = new JComboBox();
		comBoxProjName.setBounds(230, 111, 212, 20);
		panelSecond.add(comBoxProjName);
		
		JLabel lblProjectName = new JLabel("Project Name");
		lblProjectName.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblProjectName.setBounds(136, 114, 76, 14);
		panelSecond.add(lblProjectName);
		
		JLabel lblExcelPath = new JLabel("Excel Path");
		lblExcelPath.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblExcelPath.setBounds(136, 183, 64, 14);
		panelSecond.add(lblExcelPath);
		
		btnNext = new JButton("Next");
		btnNext.setEnabled(false);
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ITCWS.fnValidateFileFormat())
					ITCWS.fnLaunchMappingPanel();
			}
		});
		btnNext.setBounds(413, 326, 89, 23);
		panelSecond.add(btnNext);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				getRootPane().setDefaultButton(btnLogin);			
				ITCWS.fnLaunchLoginPanel();
				
			}
		});
		btnBack.setBounds(314, 326, 89, 23);
		panelSecond.add(btnBack);
		
		JButton btnCancel = new JButton("Cancel");		
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
						JFrame frame = new JFrame("...");
				        int result = JOptionPane.showConfirmDialog(frame,"Are you sure you want to exit the application?",
				            "Exit Application",JOptionPane.YES_NO_OPTION);
				        if (result == JOptionPane.YES_OPTION)
				            dispose();
				        else
				        	setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					
				}
		});
		btnCancel.setBounds(215, 326, 89, 23);
		panelSecond.add(btnCancel);
		
		lblValidationMessage = new JLabel("");
		lblValidationMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblValidationMessage.setBounds(160, 297, 415, 16);
		panelSecond.add(lblValidationMessage);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(230, 233, 160, 51);
		panelSecond.add(scrollPane_1);
		
		lstWorkSheets = new JList();
		lstWorkSheets.setBackground(new Color(245, 245, 245));
		lstWorkSheets.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
			    if(lstWorkSheets.isSelectionEmpty())
			    	btnNext.setEnabled(false);
			    else
			    	btnNext.setEnabled(true);				
			}
		});
		scrollPane_1.setViewportView(lstWorkSheets);
		lstWorkSheets.setBorder(UIManager.getBorder("Button.border"));
		
		
		JLabel lblWorksheets = new JLabel("Worksheets");
		lblWorksheets.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblWorksheets.setBounds(136, 235, 82, 16);
		panelSecond.add(lblWorksheets);
		
		JLabel lblBackgroundSec = new JLabel("");
		lblBackgroundSec.setIcon(new ImageIcon(ImportTestCases.class.getResource("/images/NewBackground1.png")));
		lblBackgroundSec.setBounds(0, 0, 728, 434);
		panelSecond.add(lblBackgroundSec);
		
		
		panelMapping = new JPanel();
		panelMapping.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				ITCWS.pageName = "Mapping";
				setTitle(ITCWS.appName + " v" + ITCWS.versionNumber + " - " + ITCWS.pageName);
			}
		});
		contentPane.add(panelMapping, "name_1090770973820144");
		panelMapping.setLayout(null);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(75, 100, 119, 117);
		panelMapping.add(scrollPane_2);
		
		lstJiraFields = new JList();
		scrollPane_2.setViewportView(lstJiraFields);
		lstJiraFields.setBorder(null);
		lstJiraFields.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(236, 100, 119, 117);
		panelMapping.add(scrollPane_3);
		
		lstExcelColumns = new JList();
		scrollPane_3.setViewportView(lstExcelColumns);
		lstExcelColumns.setBorder(null);
		lstExcelColumns.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(482, 95, 205, 128);
		panelMapping.add(scrollPane);
		
		tblMapping = new JTable();
		tblMapping.setBorder(new MatteBorder(1, 0, 0, 0, (Color) new Color(0, 0, 0)));
		scrollPane.setViewportView(tblMapping);
		tblMapping.setFillsViewportHeight(true);
		tblMapping.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Jira Field", "Excel Column"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		
		JLabel lblJiraFields = new JLabel("Jira Fields");
		lblJiraFields.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblJiraFields.setBounds(96, 73, 86, 14);
		panelMapping.add(lblJiraFields);
		
		JLabel lblExcelColumn = new JLabel("Excel Column");
		lblExcelColumn.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblExcelColumn.setBounds(252, 73, 86, 14);
		panelMapping.add(lblExcelColumn);
		
		JLabel lblMappingList = new JLabel("Mapping List");
		lblMappingList.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMappingList.setBounds(529, 68, 86, 14);
		panelMapping.add(lblMappingList);
		
		JButton btnAdd = new JButton(">>");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				ITCWS.fnAddToMapping();
			}
		});
		btnAdd.setBounds(381, 148, 73, 23);
		panelMapping.add(btnAdd);
		
		JButton btnRemove = new JButton("<<");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ITCWS.fnRemovefromMapping();
			}
		});
		btnRemove.setBounds(380, 194, 74, 23);
		panelMapping.add(btnRemove);
		
		JButton btnMapCancel = new JButton("Cancel");
		btnMapCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFrame frame = new JFrame("...");
		        int result = JOptionPane.showConfirmDialog(frame,"Are you sure you want to exit the application?",
		            "Exit Application",JOptionPane.YES_NO_OPTION);
		        if (result == JOptionPane.YES_OPTION)
		            dispose();
		        else
		        	setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}
		});
		btnMapCancel.setBounds(173, 347, 89, 23);
		panelMapping.add(btnMapCancel);
		
		JButton btnMapBack = new JButton("Back");
		btnMapBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ITCWS.fnLaunchLoadSecondPanel();
			}
		});
		btnMapBack.setBounds(282, 347, 89, 23);
		panelMapping.add(btnMapBack);
		
		btnMapNext = new JButton("Next");
		btnMapNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ITCWS.fnStoreExcelandMapping();
				ITCWS.fnLaunchConfirmationPanel();
			}
		});
		btnMapNext.setEnabled(false);
		btnMapNext.setBounds(384, 347, 89, 23);
		panelMapping.add(btnMapNext);
		
		btnValidate = new JButton("Validate");
		btnValidate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				ITCWS.fnValidateExcelFormat();
				
			}
		});
		btnValidate.setEnabled(false);
		btnValidate.setBounds(285, 253, 97, 25);
		panelMapping.add(btnValidate);
		
		lblValidationmessage = new JLabel("");
		lblValidationmessage.setForeground(new Color(0, 0, 0));
		lblValidationmessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblValidationmessage.setBounds(173, 303, 359, 16);
		panelMapping.add(lblValidationmessage);
		
		lblCheckmark = new JLabel("");
		lblCheckmark.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent arg0) {
				btnMapNext.setEnabled(true);
			}
		});
		lblCheckmark.setIcon(new ImageIcon(ImportTestCases.class.getResource("/images/CheckMark1.png")));
		lblCheckmark.setBounds(411, 253, 31, 34);
		lblCheckmark.setVisible(false);
		panelMapping.add(lblCheckmark);
		
		lblValidateloading = new JLabel("");
		lblValidateloading.setIcon(new ImageIcon(ImportTestCases.class.getResource("/images/Spinner-1s-78px_White.gif")));
		lblValidateloading.setBounds(295, 278, 86, 63);
		lblValidateloading.setVisible(false);
		panelMapping.add(lblValidateloading);
		
		panelConfirm = new JPanel();
		panelConfirm.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				ITCWS.pageName = "Confirmation";
				setTitle(ITCWS.appName + " v" + ITCWS.versionNumber + " - " + ITCWS.pageName);
			}
		});
		contentPane.add(panelConfirm, "name_1090774873869945");
		panelConfirm.setLayout(null);
		
		JButton btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ITCWS.fnLaunchRunPanel();
			}
		});
		btnRun.setBounds(400, 347, 89, 23);
		panelConfirm.add(btnRun);
		
		JLabel lblConfirmation = new JLabel("Confirmation");
		lblConfirmation.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblConfirmation.setBounds(293, 24, 89, 14);
		panelConfirm.add(lblConfirmation);
		
		JLabel lblPleaseConfirmThe = new JLabel("Please confirm the below options and click on Run button");
		lblPleaseConfirmThe.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPleaseConfirmThe.setBounds(146, 51, 415, 14);
		panelConfirm.add(lblPleaseConfirmThe);
		
		JButton btnBack_2 = new JButton("Back");
		btnBack_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ITCWS.fnLaunchMappingPanel();
			}
		});
		btnBack_2.setBounds(293, 347, 89, 23);
		panelConfirm.add(btnBack_2);
		
		JButton btnCancel_2 = new JButton("Cancel");
		btnCancel_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFrame frame = new JFrame("...");
		        int result = JOptionPane.showConfirmDialog(frame,"Are you sure you want to exit the application?",
		            "Exit Application",JOptionPane.YES_NO_OPTION);
		        if (result == JOptionPane.YES_OPTION)
		            dispose();
		        else
		        	setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}
		});
		btnCancel_2.setBounds(183, 347, 89, 23);
		panelConfirm.add(btnCancel_2);
		
		JLabel lblProjectName_1 = new JLabel("Project Name");
		lblProjectName_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblProjectName_1.setBounds(183, 80, 89, 14);
		panelConfirm.add(lblProjectName_1);
		
		JLabel lblExcelPath_1 = new JLabel("Excel Path");
		lblExcelPath_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblExcelPath_1.setBounds(183, 107, 71, 14);
		panelConfirm.add(lblExcelPath_1);
		
		JLabel lblMappingList_1 = new JLabel("Mapping List");
		lblMappingList_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMappingList_1.setBounds(183, 198, 89, 14);
		panelConfirm.add(lblMappingList_1);
		
		lblProjectNameValue = new JLabel("Project Name Value");
		lblProjectNameValue.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblProjectNameValue.setBounds(306, 80, 224, 14);
		panelConfirm.add(lblProjectNameValue);
		
		lblExcelPathValue = new JLabel("Excel Path Value");
		lblExcelPathValue.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblExcelPathValue.setBounds(306, 107, 336, 14);
		panelConfirm.add(lblExcelPathValue);
		
		JLabel lblSheetName = new JLabel("Sheet Name");
		lblSheetName.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSheetName.setBounds(183, 134, 71, 16);
		panelConfirm.add(lblSheetName);
		
		lblSheetNameValue = new JLabel("Sheet Name Value");
		lblSheetNameValue.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSheetNameValue.setBounds(306, 134, 106, 16);
		panelConfirm.add(lblSheetNameValue);
		
		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(306, 198, 206, 128);
		panelConfirm.add(scrollPane_4);
		
		tblMapConfirm = new JTable();
		tblMapConfirm.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Jira Fields", "Excel Columns"
			}
		));
		scrollPane_4.setViewportView(tblMapConfirm);
		tblMapConfirm.setFillsViewportHeight(true);
		tblMapConfirm.setBorder(new MatteBorder(1, 0, 0, 0, (Color) new Color(0, 0, 0)));
		
		JLabel lblNoOfTest = new JLabel("No. of Test Cases");
		lblNoOfTest.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNoOfTest.setBounds(183, 163, 106, 16);
		panelConfirm.add(lblNoOfTest);
		
		lblTestCasesCount = new JLabel("No. of TestValue");
		lblTestCasesCount.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTestCasesCount.setBounds(306, 163, 56, 16);
		panelConfirm.add(lblTestCasesCount);
		
		JLabel lblBackgroundCon = new JLabel("");
		lblBackgroundCon.setIcon(new ImageIcon(ImportTestCases.class.getResource("/images/NewBackground1.png")));
		lblBackgroundCon.setBounds(0, 0, 728, 434);
		panelConfirm.add(lblBackgroundCon);
		
		panelFinal = new JPanel();
		panelFinal.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				ITCWS.pageName = "Run";
				setTitle(ITCWS.appName + " v" + ITCWS.versionNumber + " - " + ITCWS.pageName);
			}
		});
		contentPane.add(panelFinal, "name_1090779593891243");
		panelFinal.setLayout(null);
		
		JLabel lblTotalNumberOf = new JLabel("Total Number of Test Cases to be Uploaded");
		lblTotalNumberOf.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTotalNumberOf.setBounds(136, 50, 295, 14);
		panelFinal.add(lblTotalNumberOf);
		
		JLabel lblTotalNumberOf_1 = new JLabel("Number of Test Cases Uploaded");
		lblTotalNumberOf_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTotalNumberOf_1.setBounds(136, 80, 253, 14);
		panelFinal.add(lblTotalNumberOf_1);
		
		lblTotaltestcasesvalue = new JLabel("0");
		lblTotaltestcasesvalue.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTotaltestcasesvalue.setBounds(502, 50, 61, 14);
		panelFinal.add(lblTotaltestcasesvalue);
		
		lblTotaltestcasesuploadedvalue = new JLabel("0");
		lblTotaltestcasesuploadedvalue.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTotaltestcasesuploadedvalue.setBounds(502, 80, 97, 14);
		panelFinal.add(lblTotaltestcasesuploadedvalue);
		
		lblSuccessMessage = new JLabel("");
		lblSuccessMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblSuccessMessage.setVerticalAlignment(SwingConstants.TOP);
		lblSuccessMessage.setForeground(Color.BLACK);
		lblSuccessMessage.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblSuccessMessage.setBounds(146, 352, 389, 16);
		panelFinal.add(lblSuccessMessage);
		
		prgbarImport = new JProgressBar();
		prgbarImport.setForeground(new Color(0, 128, 0));
		prgbarImport.setBounds(136, 314, 405, 31);
		panelFinal.add(prgbarImport);
		
		btnCancelImport = new JButton("Cancel Import");
		btnCancelImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFrame frame = new JFrame("...");
		        int result = JOptionPane.showConfirmDialog(frame,"If you cancel, import process might be incomplete and might lead to erroneous data in Jira.\n Are you sure you want to continue?",
		            "Cancelling Test Case Import",JOptionPane.YES_NO_OPTION);
		        if (result == JOptionPane.YES_OPTION)
		        {
		            ITCWS.fnCancelImport();
		        	setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		        }
		        else
		        	setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}
		});
		btnCancelImport.setBounds(269, 379, 145, 25);
		panelFinal.add(btnCancelImport);
		
		btnRunBack = new JButton("Back");
		btnRunBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				ITCWS.fnLaunchConfirmationPanel();
			}
		});
		btnRunBack.setBounds(125, 379, 97, 25);
		panelFinal.add(btnRunBack);
		
		btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				dispose();
			}
		});
		btnClose.setBounds(459, 379, 97, 25);
		panelFinal.add(btnClose);
		
		JScrollPane scrollPane_5 = new JScrollPane();
		scrollPane_5.setBounds(136, 124, 407, 173);
		panelFinal.add(scrollPane_5);
		
		txtAreaConsole = new JTextArea();
		scrollPane_5.setViewportView(txtAreaConsole);
		txtAreaConsole.setEditable(false);
		txtAreaConsole.setBackground(SystemColor.control);
		
		JLabel lblBackgroundRun = new JLabel("");
		lblBackgroundRun.setIcon(new ImageIcon(ImportTestCases.class.getResource("/images/NewBackground1.png")));
		lblBackgroundRun.setBounds(0, 0, 728, 434);
		panelFinal.add(lblBackgroundRun);
		
		
	}
}
