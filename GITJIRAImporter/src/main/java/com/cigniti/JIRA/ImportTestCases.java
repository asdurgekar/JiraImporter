package com.cigniti.JIRA;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;

import java.awt.CardLayout;
import java.awt.SystemColor;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.cigniti.util.TextOutputFile;
import com.launch.SupportingMethods;

import javax.swing.JCheckBox;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class ImportTestCases extends JFrame {

	private JPanel contentPane;
	public static JTextField txtExcelPath;
	public static JTable tblMapping;	
	public static JTextField txtJiraURL;
	public static JTextField txtUserName;
	public static JPasswordField txtPassword;
	public static JLabel lblAuthmessage;
	public static JPanel panelLogin;
	public static JPanel panelSecond;
	public static JPanel panelMapping;
	public static JPanel panelConfirm;
	public static JPanel panelFinal;
	public static JComboBox comBoxProjName;
	public static JCheckBox chckbxRememberMe;
	public static JCheckBox chckbxCloud;
	
	public static JButton btnNext;
	public static JList lstJiraFields;
	public static JList lstExcelColumns;
	public static JList lstWorkSheets;
	public static JButton btnValidate;
	
	public static JLabel lblAccessKey;
	public static JLabel lblSecretKey;
	public static JLabel lblValidationMessage;
	public static JLabel lblValidationmessage;
	public static JLabel lblCheckmark;
	public static JLabel lblProjectNameValue;
	public static JLabel lblExcelPathValue;
	public static JLabel lblSheetNameValue;
	public static JTable tblMapConfirm;
	public static JLabel lblTestCasesCount;
	public static JLabel lblTotaltestcasesvalue;
	public static JLabel lblTotaltestcasesuploadedvalue;
	public static JLabel lblTotalTestCaseFailure;
	public static JLabel lblTotalTestCaseFailureValue;
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
	public static JButton btnBrowse;
	public static JLabel lblDetLog;
	
	ImportTestCaseswithSteps ITCWS =	new ImportTestCaseswithSteps();
	SupportingMethods suppMethods = new SupportingMethods();
	private JLabel lblBackgroundConf;
	
	//local variables
	private String str_VersionNumber;
	private JLabel lblBackgroundMap;	
	private JScrollPane scrollPane_5;
	private JLabel lblBackgroundRun;
	private JLabel lblBackgroundLogin;
	
	private JScrollPane scrollPane_1;
	private JLabel lblBackgroundProject;
	
	
	
	
	
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
		
		
		//set look & feel for the application
		try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				//Nimbus;System;Metal;Motif
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());	
		               
		            UIDefaults defaults = UIManager.getLookAndFeelDefaults();
		            NimbusLookAndFeel localNimbusLookAndFeel = (NimbusLookAndFeel)UIManager.getLookAndFeel();
		            Color derivedColor = localNimbusLookAndFeel.getDerivedColor("nimbusSelection", 0,0,0,0, true);		            
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
		
		str_VersionNumber = suppMethods.fnGetLocalVersion();
		if(str_VersionNumber != null)
		{
			ITCWS.versionNumber = str_VersionNumber;
		}
		
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				ITCWS.fnCreateCoreLogFile();
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
		setBounds(100, 100, 776, 537);
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
		btnLogin.setBounds(289, 347, 157, 37);
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
		lblUserName.setBounds(173, 134, 67, 14);
		lblUserName.setFont(new Font("Tahoma", Font.BOLD, 11));
		panelLogin.add(lblUserName);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(173, 183, 67, 14);
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 11));
		panelLogin.add(lblPassword);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(269, 179, 219, 28);
		panelLogin.add(txtPassword);
		
		txtUserName = new JTextField();
		txtUserName.setBounds(271, 130, 217, 28);
		panelLogin.add(txtUserName);
		txtUserName.setColumns(10);
		
		lblAuthmessage = new JLabel("");
		lblAuthmessage.setBounds(189, 395, 380, 14);
		lblAuthmessage.setForeground(Color.RED);
		lblAuthmessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblAuthmessage.setFont(new Font("Tahoma", Font.PLAIN, 11));
		panelLogin.add(lblAuthmessage);
		
		chckbxRememberMe = new JCheckBox("");
		chckbxRememberMe.setBackground(Color.WHITE);
		chckbxRememberMe.setBounds(269, 319, 21, 14);
		panelLogin.add(chckbxRememberMe);
		
		txtAccessKey = new JPasswordField();
		txtAccessKey.setBounds(269, 227, 219, 28);
		panelLogin.add(txtAccessKey);
		
		txtSecretKey = new JPasswordField();
		txtSecretKey.setBounds(269, 278, 219, 28);
		panelLogin.add(txtSecretKey);
		
		lblAccessKey = new JLabel("Access Key");
		lblAccessKey.setBounds(173, 229, 67, 14);
		lblAccessKey.setFont(new Font("Tahoma", Font.BOLD, 11));
		panelLogin.add(lblAccessKey);
		
		lblSecretKey = new JLabel("Secret Key");
		lblSecretKey.setBounds(173, 282, 67, 14);
		lblSecretKey.setFont(new Font("Tahoma", Font.BOLD, 11));
		panelLogin.add(lblSecretKey);
		
		JLabel lblRememberMe = new JLabel("Remember Credentials");
		lblRememberMe.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				ITCWS.fnToggleRememberCheckbox();
			}
		});
		lblRememberMe.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblRememberMe.setBounds(296, 319, 150, 17);
		panelLogin.add(lblRememberMe);
		
		lblLoginloading = new JLabel("");
		lblLoginloading.setIcon(new ImageIcon(ImportTestCases.class.getResource("/images/Spinner-1s-78px.gif")));
		lblLoginloading.setBounds(327, 389, 78, 61);
		lblLoginloading.setVisible(false);
		panelLogin.add(lblLoginloading);
		
		JLabel lbl_jiraURL = new JLabel("Jira URL");
		lbl_jiraURL.setForeground(Color.BLACK);
		lbl_jiraURL.setFont(new Font("Tahoma", Font.BOLD, 11));
		lbl_jiraURL.setBounds(173, 84, 67, 14);
		panelLogin.add(lbl_jiraURL);
		
		txtJiraURL = new JTextField();
		txtJiraURL.setColumns(10);
		txtJiraURL.setBounds(271, 77, 217, 28);
		panelLogin.add(txtJiraURL);
		
		chckbxCloud = new JCheckBox("");
		chckbxCloud.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ITCWS.fnToggleCloudCheckbox();
			}
		});
		chckbxCloud.setSelected(true);
		chckbxCloud.setBackground(Color.WHITE);
		chckbxCloud.setBounds(514, 84, 21, 14);
		panelLogin.add(chckbxCloud);
		
		JLabel lblCloud = new JLabel("Jira Cloud");
		lblCloud.setForeground(Color.BLACK);
		lblCloud.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCloud.setBounds(536, 84, 67, 14);
		panelLogin.add(lblCloud);
		
		lblBackgroundLogin = new JLabel("");
		lblBackgroundLogin.setIcon(new ImageIcon(ImportTestCases.class.getResource("/images/FileTransfer_WithCignitiLogo.png")));
		lblBackgroundLogin.setBounds(0, 0, 762, 499);
		panelLogin.add(lblBackgroundLogin);
		
		
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
		txtExcelPath.setEnabled(false);
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
		txtExcelPath.setBounds(245, 188, 293, 28);
		panelSecond.add(txtExcelPath);
		txtExcelPath.setColumns(10);
		
		btnBrowse = new JButton("Browse");
		btnBrowse.setEnabled(false);
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				final JFileChooser fc = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
				        "Excel Files (.xls, .xlsx, .xlsm)", "xls", "xlsx", "xlsm");
				fc.setFileFilter(filter);
			    int returnVal = fc.showOpenDialog(fc);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			       TextOutputFile.writeToLog("You chose to open this file: " +
			    		   fc.getSelectedFile().getName());
			       File selFile = fc.getSelectedFile();
					txtExcelPath.setText(selFile.getAbsolutePath());
					
			    }
			}
		});
		btnBrowse.setBounds(548, 187, 89, 28);
		panelSecond.add(btnBrowse);
		
		comBoxProjName = new JComboBox();
		comBoxProjName.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				
				JComboBox comboBoxIn = (JComboBox) arg0.getSource();
				if(comboBoxIn.getSelectedIndex() >= 0)
				{
					txtExcelPath.setEnabled(true);
					btnBrowse.setEnabled(true);
				}
			}
		});
		comBoxProjName.setBounds(245, 119, 212, 25);
		panelSecond.add(comBoxProjName);
		
		JLabel lblProjectName = new JLabel("Project Name");
		lblProjectName.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblProjectName.setBounds(151, 122, 76, 14);
		panelSecond.add(lblProjectName);
		
		JLabel lblExcelPath = new JLabel("Excel Path");
		lblExcelPath.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblExcelPath.setBounds(151, 191, 64, 14);
		panelSecond.add(lblExcelPath);
		
		btnNext = new JButton("Next");
		btnNext.setEnabled(false);
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ITCWS.fnValidateFileFormat() && ITCWS.fnValidateTestIssueType())
					ITCWS.fnLaunchMappingPanel();
			}
		});
		btnNext.setBounds(432, 359, 89, 23);
		panelSecond.add(btnNext);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				getRootPane().setDefaultButton(btnLogin);			
				ITCWS.fnLaunchLoginPanel();
				
			}
		});
		btnBack.setBounds(333, 359, 89, 23);
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
		btnCancel.setBounds(234, 359, 89, 23);
		panelSecond.add(btnCancel);
		
		lblValidationMessage = new JLabel("");
		lblValidationMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblValidationMessage.setBounds(179, 330, 415, 16);
		panelSecond.add(lblValidationMessage);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(245, 243, 158, 77);
		panelSecond.add(scrollPane_1);
		
		lstWorkSheets = new JList();
		
		lstWorkSheets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstWorkSheets.setSelectedIndex(0);
		lstWorkSheets.setBorder(UIManager.getBorder("Button.border"));
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
		
		JLabel lblWorksheets = new JLabel("Worksheets");
		lblWorksheets.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblWorksheets.setBounds(151, 243, 82, 16);
		panelSecond.add(lblWorksheets);
		
		lblBackgroundProject = new JLabel("");
		lblBackgroundProject.setIcon(new ImageIcon(ImportTestCases.class.getResource("/images/FileTransfer_WithCignitiLogo.png")));
		lblBackgroundProject.setBounds(0, 0, 762, 499);
		panelSecond.add(lblBackgroundProject);
		
		
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
		
		JLabel lblJiraFields = new JLabel("Jira Fields");
		lblJiraFields.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblJiraFields.setBounds(102, 61, 86, 14);
		panelMapping.add(lblJiraFields);
		
		JLabel lblExcelColumn = new JLabel("Excel Column");
		lblExcelColumn.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblExcelColumn.setBounds(256, 61, 86, 14);
		panelMapping.add(lblExcelColumn);
		
		JLabel lblMappingList = new JLabel("Mapping List");
		lblMappingList.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMappingList.setBounds(548, 61, 86, 14);
		panelMapping.add(lblMappingList);
		
		JButton btnAdd = new JButton(">>");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				ITCWS.fnAddToMapping();
			}
		});
		btnAdd.setBounds(387, 172, 73, 23);
		panelMapping.add(btnAdd);
		
		JButton btnRemove = new JButton("<<");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ITCWS.fnRemovefromMapping();
			}
		});
		btnRemove.setBounds(387, 207, 74, 23);
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
		btnMapCancel.setBounds(179, 367, 89, 23);
		panelMapping.add(btnMapCancel);
		
		JButton btnMapBack = new JButton("Back");
		btnMapBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ITCWS.fnLaunchLoadSecondPanel();
			}
		});
		btnMapBack.setBounds(288, 367, 89, 23);
		panelMapping.add(btnMapBack);
		
		btnMapNext = new JButton("Next");
		btnMapNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ITCWS.fnStoreExcelandMapping();
				ITCWS.fnLaunchConfirmationPanel();
			}
		});
		btnMapNext.setEnabled(false);
		btnMapNext.setBounds(390, 367, 89, 23);
		panelMapping.add(btnMapNext);
		
		btnValidate = new JButton("Validate");
		btnValidate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				ITCWS.fnValidateExcelFormat();
				
			}
		});
		btnValidate.setEnabled(false);
		btnValidate.setBounds(291, 273, 97, 25);
		panelMapping.add(btnValidate);
		
		lblValidationmessage = new JLabel("");
		lblValidationmessage.setForeground(new Color(0, 0, 0));
		lblValidationmessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblValidationmessage.setBounds(91, 309, 520, 33);
		panelMapping.add(lblValidationmessage);
		
		lblCheckmark = new JLabel("");
		lblCheckmark.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent arg0) {
				btnMapNext.setEnabled(true);
			}
		});
		lblCheckmark.setIcon(new ImageIcon(ImportTestCases.class.getResource("/images/CheckMark1.png")));
		lblCheckmark.setBounds(417, 273, 31, 34);
		lblCheckmark.setVisible(false);
		panelMapping.add(lblCheckmark);
		
		lblValidateloading = new JLabel("");
		lblValidateloading.setIcon(new ImageIcon(ImportTestCases.class.getResource("/images/Spinner-1s-78px.gif")));
		lblValidateloading.setBounds(301, 298, 86, 63);
		lblValidateloading.setVisible(false);
		panelMapping.add(lblValidateloading);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(66, 87, 128, 173);
		panelMapping.add(scrollPane_3);
		
		lstJiraFields = new JList();
		scrollPane_3.setViewportView(lstJiraFields);
		lstJiraFields.setBorder(null);
		lstJiraFields.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(230, 87, 128, 174);
		panelMapping.add(scrollPane_2);
		
		lstExcelColumns = new JList();
		scrollPane_2.setViewportView(lstExcelColumns);
		lstExcelColumns.setBorder(null);
		lstExcelColumns.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(486, 87, 195, 173);
		panelMapping.add(scrollPane);
		
		tblMapping = new JTable();
		scrollPane.setViewportView(tblMapping);
		tblMapping.setBorder(new MatteBorder(1, 0, 0, 0, (Color) new Color(0, 0, 0)));
		tblMapping.setFillsViewportHeight(true);
		tblMapping.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Jira Fields", "Excel Column"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		
		JButton btnMatch = new JButton("Map Fields");
		btnMatch.setToolTipText("Please map the Jira fields with Excel columns.  You can use Match button to map columns matching by name. ");
		btnMatch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ITCWS.fnMatchandMap();
			}
		});
		btnMatch.setBounds(375, 112, 97, 33);
		panelMapping.add(btnMatch);
		
		lblBackgroundMap = new JLabel("");
		lblBackgroundMap.setIcon(new ImageIcon(ImportTestCases.class.getResource("/images/FileTransfer_WithCignitiLogo.png")));
		lblBackgroundMap.setBounds(0, 0, 760, 499);
		panelMapping.add(lblBackgroundMap);
		
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
				
				if(!ITCWS.fn_VerifyFileClosed())
				{
					JFrame frame = new JFrame("...");
			        JOptionPane.showMessageDialog(frame, "Please close the test case excel file before running \n the import process","Excel is Open",JOptionPane.WARNING_MESSAGE);
			    }
				else if(!ITCWS.blnValidationCompleteFlag)
				{
					JFrame frame = new JFrame("...");
			        JOptionPane.showMessageDialog(frame, "Please perform excel validation on the previous screen\n in order to run the import process again.","Perform Excel Validation",JOptionPane.WARNING_MESSAGE);
				}
				else
				{
					ITCWS.fnLaunchRunPanel();
				}
			}
		});
		btnRun.setBounds(403, 395, 89, 23);
		panelConfirm.add(btnRun);
		
		JLabel lblConfirmation = new JLabel("Confirmation");
		lblConfirmation.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblConfirmation.setBounds(294, 54, 89, 14);
		panelConfirm.add(lblConfirmation);
		
		JLabel lblPleaseConfirmThe = new JLabel("Please confirm the below options and close the excel sheet before clicking on Run button");
		lblPleaseConfirmThe.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPleaseConfirmThe.setBounds(104, 79, 516, 14);
		panelConfirm.add(lblPleaseConfirmThe);
		
		JButton btnBack_2 = new JButton("Back");
		btnBack_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ITCWS.fnLaunchMappingPanel();
			}
		});
		btnBack_2.setBounds(294, 395, 89, 23);
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
		btnCancel_2.setBounds(184, 395, 89, 23);
		panelConfirm.add(btnCancel_2);
		
		JLabel lblProjectName_1 = new JLabel("Project Name");
		lblProjectName_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblProjectName_1.setBounds(184, 110, 89, 14);
		panelConfirm.add(lblProjectName_1);
		
		JLabel lblExcelPath_1 = new JLabel("Excel Path");
		lblExcelPath_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblExcelPath_1.setBounds(184, 137, 71, 14);
		panelConfirm.add(lblExcelPath_1);
		
		JLabel lblMappingList_1 = new JLabel("Mapping List");
		lblMappingList_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMappingList_1.setBounds(184, 228, 89, 14);
		panelConfirm.add(lblMappingList_1);
		
		lblProjectNameValue = new JLabel("Project Name Value");
		lblProjectNameValue.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblProjectNameValue.setBounds(307, 110, 224, 14);
		panelConfirm.add(lblProjectNameValue);
		
		lblExcelPathValue = new JLabel("Excel Path Value");
		lblExcelPathValue.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblExcelPathValue.setBounds(307, 137, 336, 14);
		panelConfirm.add(lblExcelPathValue);
		
		JLabel lblSheetName = new JLabel("Sheet Name");
		lblSheetName.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSheetName.setBounds(184, 164, 71, 16);
		panelConfirm.add(lblSheetName);
		
		lblSheetNameValue = new JLabel("Sheet Name Value");
		lblSheetNameValue.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblSheetNameValue.setBounds(307, 164, 106, 16);
		panelConfirm.add(lblSheetNameValue);
		
		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(303, 221, 228, 162);
		panelConfirm.add(scrollPane_4);
		
		tblMapConfirm = new JTable();
		scrollPane_4.setViewportView(tblMapConfirm);
		tblMapConfirm.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Jira Fields", "Excel Columns"
			}
		));
		tblMapConfirm.setFillsViewportHeight(true);
		tblMapConfirm.setBorder(new MatteBorder(1, 0, 0, 0, (Color) new Color(0, 0, 0)));
		
		JLabel lblNoOfTest = new JLabel("No. of Test Cases");
		lblNoOfTest.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNoOfTest.setBounds(184, 193, 106, 16);
		panelConfirm.add(lblNoOfTest);
		
		lblTestCasesCount = new JLabel("No. of TestValue");
		lblTestCasesCount.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblTestCasesCount.setBounds(307, 193, 56, 16);
		panelConfirm.add(lblTestCasesCount);
		
		lblBackgroundConf = new JLabel("");
		lblBackgroundConf.setIcon(new ImageIcon(ImportTestCases.class.getResource("/images/FileTransfer_WithCignitiLogo.png")));
		lblBackgroundConf.setBounds(0, 0, 760, 499);
		panelConfirm.add(lblBackgroundConf);
		
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
		lblTotalNumberOf.setBounds(137, 25, 295, 14);
		panelFinal.add(lblTotalNumberOf);
		
		JLabel lblTotalNumberOf_1 = new JLabel("Number of Test Cases Uploaded");
		lblTotalNumberOf_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTotalNumberOf_1.setBounds(137, 50, 253, 14);
		panelFinal.add(lblTotalNumberOf_1);
		
		lblTotaltestcasesvalue = new JLabel("0");
		lblTotaltestcasesvalue.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTotaltestcasesvalue.setBounds(505, 25, 61, 14);
		panelFinal.add(lblTotaltestcasesvalue);
		
		lblTotaltestcasesuploadedvalue = new JLabel("0");
		lblTotaltestcasesuploadedvalue.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTotaltestcasesuploadedvalue.setBounds(505, 50, 97, 14);
		panelFinal.add(lblTotaltestcasesuploadedvalue);
		
		lblSuccessMessage = new JLabel("");
		lblSuccessMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblSuccessMessage.setVerticalAlignment(SwingConstants.TOP);
		lblSuccessMessage.setForeground(Color.BLACK);
		lblSuccessMessage.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblSuccessMessage.setBounds(158, 381, 389, 16);
		panelFinal.add(lblSuccessMessage);
		

		prgbarImport = new JProgressBar();
		prgbarImport.setStringPainted(true);
		prgbarImport.setForeground(new Color(0, 0, 0));
		prgbarImport.setBounds(123, 342, 479, 31);
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
		btnCancelImport.setBounds(292, 407, 145, 25);
		panelFinal.add(btnCancelImport);
		
		btnRunBack = new JButton("Back");
		btnRunBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				ITCWS.fnLaunchConfirmationPanel();
			}
		});
		btnRunBack.setBounds(123, 407, 97, 25);
		panelFinal.add(btnRunBack);
		
		btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				dispose();
			}
		});
		btnClose.setBounds(505, 407, 97, 25);
		panelFinal.add(btnClose);
		
		lblTotalTestCaseFailure = new JLabel("Number of Test Cases with Import Failure");
		lblTotalTestCaseFailure.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTotalTestCaseFailure.setBounds(137, 76, 295, 14);
		panelFinal.add(lblTotalTestCaseFailure);
		
		lblTotalTestCaseFailureValue = new JLabel("0");
		lblTotalTestCaseFailureValue.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTotalTestCaseFailureValue.setBounds(505, 76, 97, 14);
		panelFinal.add(lblTotalTestCaseFailureValue);
		
		lblDetLog = new JLabel("<html><a href=''>View Detailed Log</a></html>");
		lblDetLog.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				ITCWS.launchCurrentLog();
			}
		});
		lblDetLog.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDetLog.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblDetLog.setBounds(502, 317, 100, 14);
		panelFinal.add(lblDetLog);
		
		scrollPane_5 = new JScrollPane();
		scrollPane_5.setBounds(125, 115, 477, 202);
		panelFinal.add(scrollPane_5);
		
		txtAreaConsole = new JTextArea();
		scrollPane_5.setViewportView(txtAreaConsole);
		txtAreaConsole.setEditable(false);
		txtAreaConsole.setBackground(SystemColor.control);
		
		lblBackgroundRun = new JLabel("");
		lblBackgroundRun.setIcon(new ImageIcon(ImportTestCases.class.getResource("/images/FileTransfer_WithCignitiLogo.png")));
		lblBackgroundRun.setBounds(0, 0, 760, 499);
		panelFinal.add(lblBackgroundRun);
		
		
	}
}
