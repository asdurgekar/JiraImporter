package com.cigniti.JIRA;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;


import javax.swing.JLabel;
import javax.swing.JPasswordField;
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;

public class LoadToJira {

	public static JFrame frmJiraTestCase;
	public static JTextField txt_JIRAURL;
	public static JTextField txt_ProjectId;
	public static JPasswordField txt_SecretKey;
	public static JTextField txt_FilePath;
	public static JPasswordField txt_AccessKey;
	public static JLabel lblSuccessMessage;

	public SwingWorker<Void,String> worker;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {					
					LoadToJira window = new LoadToJira();
					window.frmJiraTestCase.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoadToJira() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmJiraTestCase = new JFrame();
		frmJiraTestCase.setResizable(false);
		frmJiraTestCase.getContentPane().setBackground(Color.LIGHT_GRAY);
		frmJiraTestCase.setContentPane(new JLabel(new ImageIcon(this.getClass().getResource("/images/Sky.jpg"))));
		frmJiraTestCase.setTitle("JIRA Test Case Importer - v0.1");
		frmJiraTestCase.setBounds(100, 100, 665, 556);
		frmJiraTestCase.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frmJiraTestCase.setIconImage(new ImageIcon(this.getClass().getResource("/images/JiraIcon.png")).getImage());
		
		JButton btnImport = new JButton("Load To Jira");		
		btnImport.addActionListener(new ActionListener() {			
			
			
			public void actionPerformed(ActionEvent arg0) {
				
					
					//Reset Main page					
					lblSuccessMessage.setText("");	
		 
					try {
						
						ImportTestCaseswithSteps ITCWS =	new ImportTestCaseswithSteps();
						ITCWS.fn_ImportTestCaseswithSteps();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				
					
					
				
			}

			
		});
		
		
		txt_JIRAURL = new JTextField();
		txt_JIRAURL.setColumns(10);
		
		txt_JIRAURL.setText("https://rentacenter.atlassian.net");
		
		txt_ProjectId = new JTextField();
		txt_ProjectId.setColumns(10);
		txt_ProjectId.setText("10357");
		
		JLabel lblJiraUrl = new JLabel("JIRA URL");
		lblJiraUrl.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblJiraUrl.setForeground(Color.WHITE);
		
		JLabel lblProjectId = new JLabel("Project Id");
		lblProjectId.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblProjectId.setForeground(Color.WHITE);
		
		JLabel lblAccessKey = new JLabel("Access Key");
		lblAccessKey.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblAccessKey.setForeground(Color.WHITE);
		
		JLabel lblSecretKey = new JLabel("Secret Key");
		lblSecretKey.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSecretKey.setForeground(Color.WHITE);
		
		txt_SecretKey = new JPasswordField();
		txt_SecretKey.setText(CreateTestWithTestSteps.secretKey);
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				final JFileChooser fc = new JFileChooser();
				fc.showOpenDialog(fc);
				File selFile = fc.getSelectedFile();
				txt_FilePath.setText(selFile.getAbsolutePath());
				
				
			}
		});
		
		txt_FilePath = new JTextField();
		txt_FilePath.setColumns(10);
		
		txt_AccessKey = new JPasswordField();
		txt_AccessKey.setText(CreateTestWithTestSteps.accessKey);
		JLabel lblExcelPath = new JLabel("Excel Path");
		lblExcelPath.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblExcelPath.setForeground(Color.WHITE);
		
		lblSuccessMessage = new JLabel("Test Cases are successfully uploaded!!");
		lblSuccessMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblSuccessMessage.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSuccessMessage.setForeground(new Color(255, 255, 255));
		lblSuccessMessage.setVisible(false);
		
		JLabel lblNewLabel = new JLabel("");
		GroupLayout groupLayout = new GroupLayout(frmJiraTestCase.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(133)
					.addComponent(lblJiraUrl, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
					.addGap(31)
					.addComponent(txt_JIRAURL, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(133)
					.addComponent(lblProjectId, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(txt_ProjectId, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(133)
					.addComponent(lblAccessKey, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(txt_AccessKey, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(135)
					.addComponent(lblSecretKey, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(txt_SecretKey, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(57)
					.addComponent(lblNewLabel))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(137)
					.addComponent(lblExcelPath, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(txt_FilePath, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addComponent(btnBrowse, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(258)
					.addComponent(btnImport, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(205)
					.addComponent(lblSuccessMessage, GroupLayout.PREFERRED_SIZE, 359, GroupLayout.PREFERRED_SIZE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(70)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(lblJiraUrl))
						.addComponent(txt_JIRAURL, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(37)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(lblProjectId))
						.addComponent(txt_ProjectId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(38)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(lblAccessKey))
						.addComponent(txt_AccessKey, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(36)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(lblSecretKey))
						.addComponent(txt_SecretKey, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addComponent(lblNewLabel)
					.addGap(32)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(4)
							.addComponent(lblExcelPath))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(1)
							.addComponent(txt_FilePath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnBrowse))
					.addGap(40)
					.addComponent(btnImport, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
					.addGap(27)
					.addComponent(lblSuccessMessage))
		);
		frmJiraTestCase.getContentPane().setLayout(groupLayout);
	}
	
	
	
}
