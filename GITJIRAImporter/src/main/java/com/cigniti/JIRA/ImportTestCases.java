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
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
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
		setTitle("Jira Test Case Importer v0.1 - Login");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ImportTestCases.class.getResource("/images/JiraIcon.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 744, 472);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.inactiveCaption);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		panelLogin = new JPanel();
		contentPane.add(panelLogin, "name_1090758453858820");
		panelLogin.setLayout(null);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					getRootPane().setDefaultButton(null);			
					ITCWS.fn_LoginToJira();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnLogin.setBounds(303, 246, 89, 23);
		panelLogin.add(btnLogin);
		getRootPane().setDefaultButton(btnLogin);
		
		JLabel lblUserName = new JLabel("User Name");
		lblUserName.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblUserName.setBounds(173, 124, 67, 14);
		panelLogin.add(lblUserName);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPassword.setBounds(173, 185, 67, 14);
		panelLogin.add(lblPassword);
		
		JLabel lblWelcomeMessage = new JLabel("Welcome Message");
		lblWelcomeMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcomeMessage.setBounds(269, 37, 151, 14);
		panelLogin.add(lblWelcomeMessage);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(266, 182, 219, 20);
		panelLogin.add(txtPassword);
		
		txtUserName = new JTextField();
		txtUserName.setBounds(268, 121, 217, 20);
		panelLogin.add(txtUserName);
		txtUserName.setColumns(10);
		
		lblAuthmessage = new JLabel("");
		lblAuthmessage.setForeground(Color.RED);
		lblAuthmessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblAuthmessage.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblAuthmessage.setBounds(238, 310, 269, 14);
		panelLogin.add(lblAuthmessage);
		
		chckbxRememberMe = new JCheckBox("Remember Me");
		chckbxRememberMe.setBounds(276, 209, 116, 23);
		panelLogin.add(chckbxRememberMe);
		
		panelSecond = new JPanel();
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
		lstWorkSheets.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
			    if(lstWorkSheets.isSelectionEmpty())
			    	btnNext.setEnabled(false);
			    else
			    	btnNext.setEnabled(true);				
			}
		});
		scrollPane_1.setViewportView(lstWorkSheets);
		lstWorkSheets.setBorder(UIManager.getBorder("TextField.border"));
		
		
		JLabel lblWorksheets = new JLabel("Worksheets");
		lblWorksheets.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblWorksheets.setBounds(136, 235, 82, 16);
		panelSecond.add(lblWorksheets);
		
		
		panelMapping = new JPanel();
		contentPane.add(panelMapping, "name_1090770973820144");
		panelMapping.setLayout(null);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(75, 100, 119, 117);
		panelMapping.add(scrollPane_2);
		
		lstJiraFields = new JList();
		scrollPane_2.setViewportView(lstJiraFields);
		lstJiraFields.setBorder(new LineBorder(Color.GRAY));
		lstJiraFields.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(236, 100, 119, 117);
		panelMapping.add(scrollPane_3);
		
		lstExcelColumns = new JList();
		scrollPane_3.setViewportView(lstExcelColumns);
		lstExcelColumns.setBorder(new LineBorder(Color.GRAY));
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
		lblJiraFields.setBounds(90, 60, 86, 14);
		panelMapping.add(lblJiraFields);
		
		JLabel lblExcelColumn = new JLabel("Excel Column");
		lblExcelColumn.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblExcelColumn.setBounds(249, 60, 86, 14);
		panelMapping.add(lblExcelColumn);
		
		JLabel lblMappingList = new JLabel("Mapping List");
		lblMappingList.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMappingList.setBounds(524, 60, 86, 14);
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
		
		JButton btnMapNext = new JButton("Next");
		btnMapNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				ITCWS.fnLaunchConfirmationPanel();
			}
		});
		btnMapNext.setEnabled(false);
		btnMapNext.setBounds(384, 347, 89, 23);
		panelMapping.add(btnMapNext);
		
		btnValidate = new JButton("Validate");
		btnValidate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String message = ITCWS.fnValidateExcelFormat();
				if(message.equals("Success"))
				{
					lblCheckmark.setVisible(true);
					lblValidationmessage.setText("");
				}
				else
				{
					lblValidationmessage.setForeground(Color.RED);
					lblValidationmessage.setText(message);
					lblCheckmark.setVisible(false);
				}
				
				
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
		lblCheckmark.setIcon(new ImageIcon(ImportTestCases.class.getResource("/images/CheckMark.png")));
		lblCheckmark.setBounds(411, 253, 31, 34);
		lblCheckmark.setVisible(false);
		panelMapping.add(lblCheckmark);
		
		panelConfirm = new JPanel();
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
		lblProjectName_1.setBounds(183, 80, 89, 14);
		panelConfirm.add(lblProjectName_1);
		
		JLabel lblExcelPath_1 = new JLabel("Excel Path");
		lblExcelPath_1.setBounds(183, 107, 71, 14);
		panelConfirm.add(lblExcelPath_1);
		
		JLabel lblMappingList_1 = new JLabel("Mapping List");
		lblMappingList_1.setBounds(183, 198, 89, 14);
		panelConfirm.add(lblMappingList_1);
		
		lblProjectNameValue = new JLabel("Project Name Value");
		lblProjectNameValue.setBounds(306, 80, 224, 14);
		panelConfirm.add(lblProjectNameValue);
		
		lblExcelPathValue = new JLabel("Excel Path Value");
		lblExcelPathValue.setBounds(306, 107, 336, 14);
		panelConfirm.add(lblExcelPathValue);
		
		JLabel lblSheetName = new JLabel("Sheet Name");
		lblSheetName.setBounds(183, 134, 71, 16);
		panelConfirm.add(lblSheetName);
		
		lblSheetNameValue = new JLabel("Sheet Name Value");
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
		lblNoOfTest.setBounds(183, 163, 106, 16);
		panelConfirm.add(lblNoOfTest);
		
		lblTestCasesCount = new JLabel("No. of TestValue");
		lblTestCasesCount.setBounds(306, 163, 56, 16);
		panelConfirm.add(lblTestCasesCount);
		
		panelFinal = new JPanel();
		contentPane.add(panelFinal, "name_1090779593891243");
		panelFinal.setLayout(null);
		
		JTextPane txtpnTest = new JTextPane();
		txtpnTest.setBounds(269, 74, 411, 285);
		panelFinal.add(txtpnTest);
		
		JLabel lblConsoleOutput = new JLabel("Console Output");
		lblConsoleOutput.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblConsoleOutput.setBounds(269, 36, 100, 14);
		panelFinal.add(lblConsoleOutput);
		
		JLabel lblTotalNumberOf = new JLabel("Total Number of Test Cases to be Uploaded");
		lblTotalNumberOf.setBounds(27, 36, 209, 14);
		panelFinal.add(lblTotalNumberOf);
		
		JLabel lblTotalNumberOf_1 = new JLabel("Total Number of Test Cases Uploaded");
		lblTotalNumberOf_1.setBounds(27, 117, 209, 14);
		panelFinal.add(lblTotalNumberOf_1);
		
		JLabel lblTotaltestcasesvalue = new JLabel("TotalTestCasesValue");
		lblTotaltestcasesvalue.setBounds(27, 74, 107, 14);
		panelFinal.add(lblTotaltestcasesvalue);
		
		JLabel lblTotaltestcasesuploadedvalue = new JLabel("TotalTestCasesUploadedValue");
		lblTotaltestcasesuploadedvalue.setBounds(27, 163, 145, 14);
		panelFinal.add(lblTotaltestcasesuploadedvalue);
		
		JLabel lblSuccessMessage = new JLabel("Success Message");
		lblSuccessMessage.setBounds(27, 270, 133, 14);
		panelFinal.add(lblSuccessMessage);
		
		
	}
}
