package com.cigniti.JIRA;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.cigniti.util.Globalvars;
import com.cigniti.util.TextOutputFile;
import com.cigniti.util.EmailOperations;
import com.cigniti.util.FileOperations;
import com.thed.zephyr.cloud.rest.ZFJCloudRestClient;
import com.cigniti.util.AESEncryption;

public class ImportTestCaseswithSteps{
	
	
	public static JSONArray JSONProjectList;
	public static HashMap<String, String> ProjectMap;
	public static HashMap<String, String> JiraExcelMap = new HashMap<String, String>();
	@SuppressWarnings("rawtypes")
	public static DefaultListModel JiralistModel;
	@SuppressWarnings("rawtypes")
	public static DefaultListModel ExcellistModel;
	@SuppressWarnings("rawtypes")
	public static DefaultListModel jDataModel;
	public String OldProjectName;
	public String OldExcelPath;
	public String OldSheetName;
	public Boolean startCount;
	public static String testId;
	public static String testKey;
	public static String linkId;
	public static Boolean blnStepExecute = false;
	public static Boolean blnUpdateTestCaseId = false;
	public static Integer intTestStepCounter = 0;
	public static String strCreatedTestCases;
	public static Boolean blnValidationCompleteFlag;
	public static Boolean blnSuccessFlag;
	public static List<String> strFailedTestCases = new ArrayList<String>();
	private SwingWorker<Void, String> bgWorker;
	public static String DisplayMessage = "Success~All Test Cases are imported successfully";
	public String appName = "Jira Test Case Importer";
	public String versionNumber = "3.0";
	public String pageName = "Login";
	public String TestCaseIdColumn = "TestCaseId";
	public String strAuthenticationMessage;
	public Boolean blnLoginSuccess;
	public String returnMessage;
	public int AuthResponse;
	public String KeyResponse;
	public static HashMap<String, String> ExcelRowData = new HashMap<String, String>();
	public CreateTestWithTestSteps createTestWithTestSteps = new CreateTestWithTestSteps();
	
	
	//Initial prototype
	
	public void fn_ImportTestCaseswithSteps() throws IOException {

		/*
		try
		{
			
			//Load values from UI
			CreateTestWithTestSteps.jiraBaseURL = LoadToJira.txt_JIRAURL.getText();
			Globalvars.JIRA_projectId = LoadToJira.txt_ProjectId.getText();
			CreateTestWithTestSteps.accessKey = new String(LoadToJira.txt_AccessKey.getPassword());
			CreateTestWithTestSteps.secretKey = new String(LoadToJira.txt_SecretKey.getPassword());
			Globalvars.ExcelSheetPath = LoadToJira.txt_FilePath.getText();
			
			//validation messages
			if(CreateTestWithTestSteps.jiraBaseURL.trim().isEmpty())
			{
				LoadToJira.lblSuccessMessage.setText("JIRA URL field is blank. Please enter a valid value");
			}
			else if(Globalvars.JIRA_projectId.trim().isEmpty())
			{
				LoadToJira.lblSuccessMessage.setText("Project Id field is blank. Please enter a valid value");
			}
			else if(CreateTestWithTestSteps.accessKey.trim().isEmpty())
			{
				LoadToJira.lblSuccessMessage.setText("Access Key field is blank. Please enter a valid value");
			}
			else if(CreateTestWithTestSteps.secretKey.trim().isEmpty())
			{
				LoadToJira.lblSuccessMessage.setText("Secret Key field is blank. Please enter a valid value");
			}
			else if(Globalvars.ExcelSheetPath.trim().isEmpty())												
			{
				LoadToJira.lblSuccessMessage.setText("Excel Sheet Path field is blank. Please enter a valid value");
			}
			else
			{
				//get values from excel
				CreateTestWithTestSteps createTestWithTestSteps = new CreateTestWithTestSteps();
				String sheetPath = Globalvars.ExcelSheetPath;
				String sheetName = "TestCases";
				String testId = "";
				String testSummary, testDescription, testStepDescription, testStepData, testStepExpectedResult, ApplicationLabel;
				
				int rowCount = ExcelFunctions.fn_GetRowCount(sheetPath,sheetName);
				for (int count = 1; count <= rowCount; count++) 
				{
					
					ApplicationLabel = ExcelFunctions.fn_GetCellData(sheetPath, sheetName, count, "Application Label");	
					testSummary = ExcelFunctions.fn_GetCellData(sheetPath, sheetName, count, "Test Name");		
					testDescription = ExcelFunctions.fn_GetCellData(sheetPath, sheetName, count, "Test Description");
					
					testStepDescription = ExcelFunctions.fn_GetCellData(sheetPath, sheetName, count, "Test Step");
					testStepData = ExcelFunctions.fn_GetCellData(sheetPath, sheetName, count, "Test Data");
					testStepExpectedResult = ExcelFunctions.fn_GetCellData(sheetPath, sheetName, count, "Expected Result");
			
					if(testSummary != null)
					{
						testId = createTestWithTestSteps.createTestCaseinJira(testSummary, testDescription, ApplicationLabel);
					}
			
					createTestWithTestSteps.createTestStepinJira(testStepDescription, testStepData, testStepExpectedResult, testId);
					
					testSummary = testDescription = testStepDescription = testStepData = testStepExpectedResult = null;
					
				}
				
				LoadToJira.lblSuccessMessage.setText("Test Cases are successfully uploaded!!");
				
			}
			
			LoadToJira.lblSuccessMessage.setVisible(true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	
		*/
		
	}
	

	
	public boolean fn_LoginToJira() {

		blnLoginSuccess = false;
		strAuthenticationMessage = "";
		
		try {
			bgWorker = new SwingWorker<Void, String>(){
				

				@Override
				protected Void doInBackground() throws Exception {
					// TODO Auto-generated method stub
					publish("Start Login");
					
					//Update all URLs for connection
					fnUpdateURIs(ImportTestCases.txtJiraURL.getText());
					
					//get values from UI
					CreateTestWithTestSteps.jiraURL = ImportTestCases.txtJiraURL.getText();
					CreateTestWithTestSteps.userName = ImportTestCases.txtUserName.getText();
					CreateTestWithTestSteps.password = new String(ImportTestCases.txtPassword.getPassword());
					CreateTestWithTestSteps.accessKey = new String(ImportTestCases.txtAccessKey.getPassword());
					CreateTestWithTestSteps.secretKey = new String(ImportTestCases.txtSecretKey.getPassword());
					
					AuthResponse = fn_PerformAuthentication();
					
					if(ImportTestCases.chckbxCloud.isSelected())
					{
						KeyResponse = CreateTestWithTestSteps.fn_ValidateKeys(JSONProjectList);
					}
					else
					{
						KeyResponse = "Success";
					}
					
					return null;
				}
				
				@Override
				protected void process(List<String> chunks) {
					
					//initialize variables
					strAuthenticationMessage = "";
					
					//change UI components
					ImportTestCases.btnLogin.setEnabled(false);
					ImportTestCases.lblAuthmessage.setText(strAuthenticationMessage);
					ImportTestCases.lblLoginloading.setVisible(true);
				}
				
				@Override
				protected void done() {
					// TODO Auto-generated method stub
					//super.done();
					//after task complete					
					
					if(AuthResponse == 0)
					{
						strAuthenticationMessage = "Please enter a valid Jira URL";
					}
					else if(AuthResponse != 200)
					{
						TextOutputFile.writeToLog("JSON Response: " + AuthResponse + "Login Failure");
						strAuthenticationMessage = "Unable to Login. Please enter valid credentials";				
					}
					else if(!KeyResponse.equals("Success"))
					{
						TextOutputFile.writeToLog(KeyResponse);
						strAuthenticationMessage = KeyResponse;				
					}
					else
					{
						TextOutputFile.writeToLog("JSON Response: " + AuthResponse + "Login Success");
						strAuthenticationMessage = "";
						fnCreateConfigFile();
						if(ImportTestCases.chckbxRememberMe.isSelected())
							fnStorePreferences("JiraURL",ImportTestCases.txtJiraURL.getText());
							fnStorePreferences("Cloud",String.valueOf(ImportTestCases.chckbxCloud.isSelected()));
							fnStorePreferences("UserName",ImportTestCases.txtUserName.getText());
							//Encrypt Password before storing
							String strEncryptedPassword = AESEncryption.encrypt(new String(ImportTestCases.txtPassword.getPassword()),Globalvars.strAESSecretKey);
							fnStorePreferences("Password",strEncryptedPassword);
							fnStorePreferences("AccessKey",new String(ImportTestCases.txtAccessKey.getPassword()));
							fnStorePreferences("SecretKey",new String(ImportTestCases.txtSecretKey.getPassword()));
						fnUpdateClientToken();
						fnUpdateOrgFields();
						
						fnLaunchLoadSecondPanel();
						blnLoginSuccess = true;
						
					}
					
					ImportTestCases.btnLogin.setEnabled(true);
					ImportTestCases.lblAuthmessage.setText(strAuthenticationMessage);
					ImportTestCases.lblLoginloading.setVisible(false);
					
				}

				private void fnUpdateURIs(String jiraURL) {
					
					
					try {
						CreateTestWithTestSteps.jiraBaseURL = jiraURL;
						CreateTestWithTestSteps.createTestUri = CreateTestWithTestSteps.API_CREATE_TEST.replace("{SERVER}", jiraURL);
						if(ImportTestCases.chckbxCloud.isSelected())
						{
							CreateTestWithTestSteps.createTestStepUri = CreateTestWithTestSteps.API_CREATE_TEST_STEP.replace("{SERVER}", CreateTestWithTestSteps.zephyrBaseUrl);							
						}
						else
						{
							CreateTestWithTestSteps.createTestStepUri = CreateTestWithTestSteps.API_CREATE_TEST_STEP_JIRASERVER.replace("{SERVER}", jiraURL);
							
						}
						CreateTestWithTestSteps.validateKeyUri = CreateTestWithTestSteps.API_VALIDKEY.replace("{SERVER}", CreateTestWithTestSteps.zephyrBaseUrl);
						CreateTestWithTestSteps.getIssueUri = CreateTestWithTestSteps.API_GET_ISSUE.replace("{SERVER}", jiraURL);
						CreateTestWithTestSteps.issueLinkUri = CreateTestWithTestSteps.API_LINK_ISSUE.replace("{SERVER}", jiraURL);
						CreateTestWithTestSteps.updateTestUri = CreateTestWithTestSteps.API_UPDATE_TEST.replace("{SERVER}", jiraURL);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
					}
					
				}
				
			};			
			bgWorker.execute();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		
		return blnLoginSuccess;
		
		
		
		
		//--------------------------------Without background worker-------------------------------------------------
		
//		
//		boolean blnLoginSuccess = false;
//		try
//		{
//			String strAuthenticationMessage = "";
//			CreateTestWithTestSteps.userName = ImportTestCases.txtUserName.getText();
//			CreateTestWithTestSteps.password = new String(ImportTestCases.txtPassword.getPassword());
//			CreateTestWithTestSteps.accessKey = new String(ImportTestCases.txtAccessKey.getPassword());
//			CreateTestWithTestSteps.secretKey = new String(ImportTestCases.txtSecretKey.getPassword());
//			
//			int AuthResponse = fn_PerformAuthentication();
//			String KeyResponse = CreateTestWithTestSteps.fn_ValidateKeys(JSONProjectList);
//			
//			if(AuthResponse == 0)
//			{
//				strAuthenticationMessage = "Unable to connect on LAN. Please make sure you are connected on Wifi";
//			}
//			else if(AuthResponse != 200)
//			{
//				TextOutputFile.writeToLog("JSON Response: " + AuthResponse + "Login Failure");
//				strAuthenticationMessage = "Unable to Login. Please enter valid credentials";				
//			}
//			else if(!KeyResponse.equals("Success"))
//			{
//				TextOutputFile.writeToLog(KeyResponse);
//				strAuthenticationMessage = KeyResponse;				
//			}
//			else
//			{
//				TextOutputFile.writeToLog("JSON Response: " + AuthResponse + "Login Success");
//				strAuthenticationMessage = "";
//				fnCreateConfigFile();
//				if(ImportTestCases.chckbxRememberMe.isSelected())
//					fnStorePreferences("UserName",ImportTestCases.txtUserName.getText());
//					fnStorePreferences("AccessKey",new String(ImportTestCases.txtAccessKey.getPassword()));
//					fnStorePreferences("SecretKey",new String(ImportTestCases.txtSecretKey.getPassword()));
//				fnUpdateClientToken();
//				fnLaunchLoadSecondPanel();
//				blnLoginSuccess = true;
//				
//			}
//			ImportTestCases.lblAuthmessage.setText(strAuthenticationMessage);
//		
//			
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		return blnLoginSuccess;
		
	}


	

	private void fnUpdateOrgFields() {
		
		//Update issue type & sprint field id for the organization
		if(CreateTestWithTestSteps.jiraURL.contains("rentacenter"))
		{
			//CreateTestWithTestSteps.issueTypeId = Globalvars.JIRA_RC_issueTypeId;
			CreateTestWithTestSteps.sprintCustFieldId = Globalvars.JIRA_RC_SprintCustomField;
			
		}
		else if(CreateTestWithTestSteps.jiraURL.contains("gamestop"))
		{
			//CreateTestWithTestSteps.issueTypeId = Globalvars.JIRA_GS_issueTypeId;
			CreateTestWithTestSteps.sprintCustFieldId = Globalvars.JIRA_GS_SprintCustomField;
		}
		
		//Update Sprint custom field for the organization
		
		
	}



	private void fnCreateConfigFile() {

		try
		{
			
			//Check if Preferences location and file exist
			File jfile = new File(Globalvars.strPreferencesPath);
			if(!jfile.exists()) 
			{ 
				//If not, create the folder and file
				jfile.getParentFile().mkdirs();
				jfile.createNewFile();
				
			}
		}
		catch(Exception e)
		{
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
	}


	private void fnUpdateClientToken() {

		try
		{
			CreateTestWithTestSteps.client = ZFJCloudRestClient.restBuilder(CreateTestWithTestSteps.zephyrBaseUrl, 
					CreateTestWithTestSteps.accessKey, CreateTestWithTestSteps.secretKey, CreateTestWithTestSteps.userName).build();
			CreateTestWithTestSteps.header = CreateTestWithTestSteps.createAuthorizationHeader();
		}
		catch(Exception e)
		{
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
	}


	private void fnStorePreferences(String propName, String propValue) {
		
		try
		{
			List<String> lines = new ArrayList<String>();
			//Check if Preferences location and file exist
			File jfile = new File(Globalvars.strPreferencesPath);
			if(jfile.exists()) 
			{ 
				//Check if user name property already exists, update new user name
				// Open the file
				FileInputStream fstream = new FileInputStream(Globalvars.strPreferencesPath);
				BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
				String strLine;
				Boolean propFlag = false;
				while ((strLine = br.readLine()) != null)   
				{
				  if(strLine.contains("::")) 
				  {
					  if(strLine.split("::")[0].contains(propName))
					  {
						  strLine = propName + "::" + propValue;
						  propFlag = true;
					  }
				  }
				  lines.add(strLine);	
				}
				if(!propFlag)
					lines.add(propName + "::" + propValue);
				fstream.close();
	            br.close();

	            FileWriter fwrite = new FileWriter(Globalvars.strPreferencesPath);
	            BufferedWriter out = new BufferedWriter(fwrite);
	            if(!lines.isEmpty())
	            {
	            	for(String textline : lines)
	            	{
	            		out.write(textline);
	            		out.newLine();
	            	}
	            }
	                 
	            out.flush();
	            out.close();
				
	            TextOutputFile.writeToLog("Preferences are stored in the system");
			
			}
						
		}
		catch(Exception e)
		{
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void fnLaunchLoadSecondPanel() {
		
		try
		{
			//Changes in UI
			//Panel visibility
			ImportTestCases.panelLogin.setVisible(false);
			ImportTestCases.panelSecond.setVisible(true);
			ImportTestCases.panelMapping.setVisible(false);
			ImportTestCases.panelConfirm.setVisible(false);
			ImportTestCases.panelFinal.setVisible(false);
			
			if(OldProjectName == null)
			{
				ProjectMap = new HashMap<String, String>();
				List<String> ProjectList = new ArrayList<String>();
				for (int arrCounter = 0; arrCounter < JSONProjectList.length(); arrCounter++) 
				{
				    JSONObject Project = JSONProjectList.getJSONObject(arrCounter);
				    ProjectList.add(Project.get("name").toString());
				    ProjectMap.put(Project.get("name").toString(),Project.get("id").toString());
				}
				ImportTestCases.comBoxProjName.setModel(new DefaultComboBoxModel(ProjectList.toArray()));
				ImportTestCases.comBoxProjName.setSelectedIndex(-1);				
				
				//reset all the fields to initial state
				ImportTestCases.lstWorkSheets.setListData(new Object[0]);
				ImportTestCases.txtExcelPath.setText("");
				ImportTestCases.txtExcelPath.setEnabled(false);
				ImportTestCases.btnBrowse.setEnabled(false);
				ImportTestCases.lblValidationMessage.setText("");
				TextOutputFile.writeToLog("Project values are loaded onto the application");
			}
			
		}
		catch(Exception e)
		{
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		
			
	}
	
	public void fnLaunchLoginPanel() {
		
		try
		{
			//Changes in UI
			//Panel visibility
			ImportTestCases.panelLogin.setVisible(true);
			ImportTestCases.panelSecond.setVisible(false);
			ImportTestCases.panelMapping.setVisible(false);
			ImportTestCases.panelConfirm.setVisible(false);
			ImportTestCases.panelFinal.setVisible(false);
			
			//Validation messages are cleared from Project screen
			ImportTestCases.lblValidationMessage.setText("");
			
			
		}
		catch(Exception e)
		{
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		
			
	}
	
	public void fnLaunchMappingPanel() {
		
		try
		{
			//Changes in UI
			//Panel visibility
			ImportTestCases.panelLogin.setVisible(false);
			ImportTestCases.panelSecond.setVisible(false);
			ImportTestCases.panelMapping.setVisible(true);
			ImportTestCases.panelConfirm.setVisible(false);
			ImportTestCases.panelFinal.setVisible(false);
			
			//Update Sheet Name
			Globalvars.ExcelWorkSheetName = ImportTestCases.lstWorkSheets.getSelectedValue().toString();
			
			//commented code to check if same excel has been selected again
			
//			if(!Globalvars.ExcelSheetPath.equals(OldExcelPath) || !Globalvars.ExcelWorkSheetName.equals(OldSheetName))
//			{
				fnCreateJiraFields();
				//check if Mapping is in preferences
				if(fnLoadPreferencesMapping())
				{
					//fnInitializeJiraFields();
					fnLoadFilteredExcelColumns();
				}
				//if not preferences does not contain mapping
				else
				{	
					fnLoadJiraFields();
					fnLoadExcelColumns();
					
					//clear mapping table
					//remove from JTable
					DefaultTableModel model = (DefaultTableModel)ImportTestCases.tblMapping.getModel();
					int intTableRows = model.getRowCount();
					
					if(intTableRows > 0)
					{
						for(int intRowCounter = 0; intRowCounter < intTableRows; intRowCounter++)
						{
							model.removeRow(0);
						}
					}
				}
				
				//clear validation message and checkmark
				ImportTestCases.lblValidationmessage.setText("");
				ImportTestCases.lblCheckmark.setVisible(false);
				
				//update old excel and sheet names
				OldExcelPath = Globalvars.ExcelSheetPath;
				OldSheetName = Globalvars.ExcelWorkSheetName;
				OldProjectName = ImportTestCases.comBoxProjName.getSelectedItem().toString();
				
				//get the project id
				CreateTestWithTestSteps.projectId = ProjectMap.get(ImportTestCases.comBoxProjName.getSelectedItem().toString());
				
//			}
				
		}
		catch(Exception e)
		{
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		
			
	}



	private void fnInitializeJiraFields() {
		
		try
		{
			JiralistModel = new DefaultListModel();
			
			//enable Validate button if no elements exist in list
			if(JiralistModel.size() == 0)
				ImportTestCases.btnValidate.setEnabled(true);
						
			JiralistModel.addListDataListener(new MyListDataListener());
		}
		catch(Exception e)
		{
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void fnCreateJiraFields() {
		
		try {
			//Load Jira columns in HashMap
			JiraExcelMap.put("Summary", "");
			JiraExcelMap.put("Labels", "");
			JiraExcelMap.put("Description", "");
			JiraExcelMap.put("Test Step", "");
			JiraExcelMap.put("Test Data", "");
			JiraExcelMap.put("Expected Result", "");
			
			//new fields for Issue linking
			JiraExcelMap.put("Link Issue", "");
			JiraExcelMap.put("Link Type", "");
			
			//Sprint field
			JiraExcelMap.put("Sprint", "");
			
			JiralistModel = new DefaultListModel();
			
			//enable Validate button if no elements exist in list
			if(JiralistModel.size() == 0)
				ImportTestCases.btnValidate.setEnabled(true);
			
			ImportTestCases.lstJiraFields.setListData(JiralistModel.toArray());
									
			JiralistModel.addListDataListener(new MyListDataListener());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}

		
	}


	private boolean fnLoadPreferencesMapping() {

		Boolean blnPrefMapLoad = false;
		
		try
		{
			HashMap<String, String> Preferences = new HashMap<String, String>();
			//Get preferences onto Hash Map
			File jfile = new File(Globalvars.strPreferencesPath);
			if(jfile.exists()) 
			{ 
				//Check if user name property already exists, update new user name
				// Open the file
				FileInputStream fstream = new FileInputStream(Globalvars.strPreferencesPath);
				BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
				String strLine;
				while ((strLine = br.readLine()) != null)   
				{ 
					if(strLine.contains("::"))
					{
						Preferences.put(strLine.split("::")[0],strLine.split("::")[1]);
					}
				}
				fstream.close();
	            br.close();
			}
			
			if(Preferences.containsKey("ExcelPath") && Preferences.containsKey("ExcelSheet"))
			{
				if(Preferences.get("ExcelPath").equals(Globalvars.ExcelSheetPath) && Preferences.get("ExcelSheet").contains(Globalvars.ExcelWorkSheetName))
				{
					//verify if Jira fields and Jira field values exists in preferences file 
					Boolean blnAllJiraFields = true;
					Boolean blnAllJiraFieldValues = true;
					for(String JiraField : JiraExcelMap.keySet())
					{
						if(Preferences.containsKey(JiraField))
						{
							if(Preferences.get(JiraField) != null)
							{
								if(Preferences.get(JiraField).trim() == "")
								{
									blnAllJiraFieldValues = false;
								}
							}
							else
							{
								blnAllJiraFieldValues = false;
							}
							
						}
						else
						{
							blnAllJiraFields = false;
						}
					}
					if(blnAllJiraFields && blnAllJiraFieldValues)
					{
						//Load mapping values to Mapping table
						DefaultTableModel model = (DefaultTableModel)ImportTestCases.tblMapping.getModel();
						//clear values before load
						model.setRowCount(0);
						for(String JiraField : JiraExcelMap.keySet())
						{
							//update mapping table
							model.addRow(new Object[]{JiraField, Preferences.get(JiraField).toString()});
							//Load Jira columns in HashMap
							JiraExcelMap.put(JiraField, Preferences.get(JiraField).toString());
							
						}
						blnPrefMapLoad = true;
						
					}
				}
			}
		}
		catch(Exception e)
		{
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		
		return blnPrefMapLoad;
	}


	@SuppressWarnings({ "unused", "unchecked","rawtypes" })
	private void fnLoadJiraFields() {

		try
		{
			//JiralistModel = new DefaultListModel();
			for(String listItem : JiraExcelMap.keySet())
				JiralistModel.addElement(listItem);
			
			//Sort list model
			JiralistModel = fnSortListModel(JiralistModel);
			
			//ImportTestCases.lstJiraFields.setListData(JiraExcelMap.keySet().toArray());
			ImportTestCases.lstJiraFields.setListData(JiralistModel.toArray());
			
			TextOutputFile.writeToLog("Jira field values are loaded");
			
			
			
			//DefaultListModel jDataModel = (DefaultListModel) ImportTestCases.lstJiraFields.getModel();
			//jDataModel.addListDataListener(new MyListDataListener()); 
			//JiralistModel.addListDataListener(new MyListDataListener());
		}
		catch(Exception e)
		{
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void fnLoadExcelColumns() {

		try
		{
			
			
			ExcellistModel = new DefaultListModel();
			String cellData = "";
			int intColumnCount = ExcelFunctions.fn_GetColumnCount(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, 0);
			for(int intColCounter = 0;intColCounter < intColumnCount;intColCounter++)
			{
				cellData = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, 0, intColCounter);
				if(cellData != null)
					if(cellData.trim() != "")
						ExcellistModel.addElement(cellData);
			}
			
			//Sort list model
			ExcellistModel = fnSortListModel(ExcellistModel);
			
			ImportTestCases.lstExcelColumns.setListData(ExcellistModel.toArray());
			TextOutputFile.writeToLog("Excel Header values are loaded");
			
		}
		catch(Exception e)
		{
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void fnLoadFilteredExcelColumns() {

		try
		{
			//initialize Jira & Excel list columns
			JiralistModel = new DefaultListModel();
			ExcellistModel = new DefaultListModel();
			
			//listener to enable Jira Fields list
			JiralistModel.addListDataListener(new MyListDataListener());
						
			Globalvars.ExcelWorkSheetName = ImportTestCases.lstWorkSheets.getSelectedValue().toString();
			
			//get Mapped Excel columns in list
			List<String> MappedColumns = new ArrayList<String>();
			DefaultTableModel model = (DefaultTableModel)ImportTestCases.tblMapping.getModel();
			int intTableRows = model.getRowCount();
			
			if(intTableRows > 0)
			{
				for(int intRowCounter = 0; intRowCounter < intTableRows; intRowCounter++)
				{
					MappedColumns.add(model.getValueAt(intRowCounter, 1).toString());
				}
			}
			//load filtered excel columns
			//ExcellistModel = new DefaultListModel();
			String cellData = "";
			int intColumnCount = ExcelFunctions.fn_GetColumnCount(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, 0);
			for(int intColCounter = 0;intColCounter < intColumnCount;intColCounter++)
			{
				cellData = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, 0, intColCounter);
				
				if(MappedColumns.contains(cellData))
				{
					continue;
				}
				if(cellData != null)
					if(cellData.trim() != "")
						ExcellistModel.addElement(cellData);
			}
			
			//Sort list model
			ExcellistModel = fnSortListModel(ExcellistModel);
			
			ImportTestCases.lstExcelColumns.setListData(ExcellistModel.toArray());
			TextOutputFile.writeToLog("Excel Header values are loaded");
			
		}
		catch(Exception e)
		{
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
	}


	private int fn_PerformAuthentication() throws IOException, KeyManagementException, NoSuchAlgorithmException {

		
		//handle certificate
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { 
			    new X509TrustManager() {     
			        public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
			            return new X509Certificate[0];
			        } 
			        public void checkClientTrusted( 
			            java.security.cert.X509Certificate[] certs, String authType) {
			            } 
			        public void checkServerTrusted( 
			            java.security.cert.X509Certificate[] certs, String authType) {
			        }
			    } 
			}; 

		// Install the all-trusting trust manager
		SSLContext sslcontext = SSLContexts.custom().useSSL().build();
        sslcontext.init(null, trustAllCerts, new SecureRandom());
        SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(factory).build();
		
		//login
		int returnCode = 0;
		//CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(CreateTestWithTestSteps.jiraBaseURL+"/rest/api/2/project");
		httpget.setHeader("Content-Type", "application/json");
		httpget.setHeader(CreateTestWithTestSteps.createAuthorizationHeader());
		
        CloseableHttpResponse response = null;
		
        try {
			response = httpclient.execute(httpget);
			
			if(response.getStatusLine().getStatusCode() == 200)
			{
				String result = EntityUtils.toString(response.getEntity());
				JSONProjectList = new JSONArray(result);
				if(JSONProjectList.length() == 0)
				{
					returnCode = 0;
				}
				else
				{
					TextOutputFile.writeToLog(JSONProjectList.toString());
					returnCode = response.getStatusLine().getStatusCode();
				}
			}
			else if(response.getStatusLine().getStatusCode() == 401)
			{				
				TextOutputFile.writeToLog(response.getStatusLine().toString());
				returnCode = response.getStatusLine().getStatusCode();
			}
        }
        catch(Exception e)
        {
        	TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));      	
		} finally {
			httpclient.close();
		} 
		
		return returnCode;
		
	}
	
	public void fnLoadPreferences() {
		
		try
		{
			//Check if Preferences location and file exist
			File jfile = new File(Globalvars.strPreferencesPath);
			if(jfile.exists()) 
			{ 
				//Check if user name property already exists, update new user name
				// Open the file
				FileInputStream fstream = new FileInputStream(Globalvars.strPreferencesPath);
				BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
				String strLine;
				while ((strLine = br.readLine()) != null)   
				{ 
					if(strLine.contains("::"))
					{
						switch(strLine.split("::")[0])
						{
						case "JiraURL":
							ImportTestCases.txtJiraURL.setText(strLine.split("::")[1]);
							break;
						case "Cloud":
							ImportTestCases.chckbxCloud.setSelected(Boolean.valueOf(strLine.split("::")[1]));
							break;
						case "UserName":
							ImportTestCases.txtUserName.setText(strLine.split("::")[1]);
							break;
						case "Password":
							//Decrypt password before displaying on UI							
							ImportTestCases.txtPassword.setText(AESEncryption.decrypt(strLine.split("::")[1],Globalvars.strAESSecretKey));
							break;
						case "AccessKey":
							ImportTestCases.txtAccessKey.setText(strLine.split("::")[1]);
							break;
						case "SecretKey":
							ImportTestCases.txtSecretKey.setText(strLine.split("::")[1]);
							break;
						default:
							break;
						}
						ImportTestCases.txtPassword.requestFocusInWindow();
						
					}
				}
				fstream.close();
	            br.close();
			}
				
		}
		catch(Exception e)
		{
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
	}


	public String fnValidateExcelFormat() {
		
		returnMessage = "";
		
		try {
			bgWorker = new SwingWorker<Void, String>(){

				@Override
				protected Void doInBackground() throws Exception {
					// TODO Auto-generated method stub
					
					publish("Start Validation");
					String strRowString = "";
					int TotalTestCaseCount = 0;
					//get total row count
					int rowCount = ExcelFunctions.fn_GetRowCount(Globalvars.ExcelSheetPath,Globalvars.ExcelWorkSheetName);
					
					String strSummary = "";
					String strLinkIssue = "";
					String strLinkType = "";
					String strTestStep = "";
					String strTestData = "";
					String strExpectedResult = "";
					String strTestCaseId = "";
					Boolean blnCheckTestCaseId = false;
					
					Boolean blnValidationFlag = true;
					
					
					ExcelRowData.put(JiraExcelMap.get("Summary"),"");
					ExcelRowData.put(JiraExcelMap.get("Test Step"),"");
					ExcelRowData.put(JiraExcelMap.get("Test Data"),"");
					ExcelRowData.put(JiraExcelMap.get("Expected Result"),"");
					ExcelRowData.put(JiraExcelMap.get("Link Issue"),"");
					ExcelRowData.put(JiraExcelMap.get("Link Type"),"");
					
					//check if Test Case Id column should be considered while counting number of test cases
					if(ExcelFunctions.fn_VerifyColumnExist(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, TestCaseIdColumn))
						blnCheckTestCaseId = true;
					
					for(int intRowCounter = 1;intRowCounter <= rowCount; intRowCounter++)
					{					
						
						
						try {
							
							ExcelRowData = ExcelFunctions.fn_GetRowData(Globalvars.ExcelSheetPath,Globalvars.ExcelWorkSheetName, intRowCounter, ExcelRowData);
							
							strSummary = ExcelRowData.get(JiraExcelMap.get("Summary"));
							
							strTestStep = ExcelRowData.get(JiraExcelMap.get("Test Step"));
							strTestData = ExcelRowData.get(JiraExcelMap.get("Test Data"));
							strExpectedResult = ExcelRowData.get(JiraExcelMap.get("Expected Result"));
							
							strLinkIssue = ExcelRowData.get(JiraExcelMap.get("Link Issue"));
							strLinkType = ExcelRowData.get(JiraExcelMap.get("Link Type"));
							
							/*
							strSummary = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath,Globalvars.ExcelWorkSheetName, intRowCounter, JiraExcelMap.get("Summary"));
							
							strTestStep = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath,Globalvars.ExcelWorkSheetName, intRowCounter, JiraExcelMap.get("Test Step"));
							strTestData = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath,Globalvars.ExcelWorkSheetName, intRowCounter, JiraExcelMap.get("Test Data"));
							strExpectedResult = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath,Globalvars.ExcelWorkSheetName, intRowCounter, JiraExcelMap.get("Expected Result"));
							
							strLinkIssue = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath,Globalvars.ExcelWorkSheetName, intRowCounter, JiraExcelMap.get("Link Issue"));
							strLinkType = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath,Globalvars.ExcelWorkSheetName, intRowCounter, JiraExcelMap.get("Link Type"));
							*/
							
						} catch (Exception e) {
							
							returnMessage = "Excel Operation Failure: " + e.getMessage();							
							blnValidationFlag = false;
							break;
							
						}
						
						//verify blank values for Link Type if Link Issue value exists
						if(strSummary != null && !strSummary.trim().isEmpty())
						{
							
							if(strLinkIssue != null)
							{
								if(!strLinkIssue.trim().isEmpty())
								{	
									if(strLinkType == null)
									{
										returnMessage = "'Link Type' value is required at row " + (intRowCounter + 1);
										blnValidationFlag = false;
										break;
									}
									else if(strLinkType.trim().isEmpty())
									{
										returnMessage = "'Link Type' value is required at row " + (intRowCounter + 1);
										blnValidationFlag = false;
										break;
									}
								}
							}
							
							
						}
						
						//verify blank values for Test Step
						if(strTestStep == null)
						{
							returnMessage = "Blank value in Column 'Test Step' at row " + (intRowCounter + 1);
							blnValidationFlag = false;
							break;
						}
						else if(strTestStep.trim().isEmpty())
						{
							returnMessage = "Blank value in Column 'Test Step' at row " + (intRowCounter + 1);
							blnValidationFlag = false;
							break;
						}
					
						//verify blank values for Test Data
						if(strTestData == null)
						{
							returnMessage = "Blank value in Column 'Test Data' at row " + (intRowCounter + 1);
							blnValidationFlag = false;
							break;
						}
						else if(strTestData.trim().isEmpty())
						{
							returnMessage = "Blank value in Column 'Test Data' at row " + (intRowCounter + 1);
							blnValidationFlag = false;
							break;
						}
					
						//verify blank values for Expected Result
						if(strExpectedResult == null)
						{
							returnMessage = "Blank value in Column 'Expected Result' at row " + (intRowCounter + 1);
							blnValidationFlag = false;
							break;
						}
						else if(strExpectedResult.trim().isEmpty())
						{
							returnMessage = "Blank value in Column 'Expected Result' at row " + (intRowCounter + 1);
							blnValidationFlag = false;
							break;
						}
						
						
						//get total count of test cases -- increment count
						strRowString = strSummary;
						if(blnCheckTestCaseId)
						{
							strTestCaseId = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath,Globalvars.ExcelWorkSheetName, intRowCounter, TestCaseIdColumn);
							if(strRowString != null && (strTestCaseId == null || strTestCaseId.trim().isEmpty()))
							{
								TotalTestCaseCount++;
							}
						}
						
						else
						{	
							if(strRowString != null)
							{
								TotalTestCaseCount++;
							}
						}
						//Console update
						TextOutputFile.writeToLog("Validating excel row :" + (intRowCounter + 1));
					
					}
					
					
					if(blnValidationFlag)
					{
						
						//check if number of test cases to uploaded if greater than 0
						if(TotalTestCaseCount <= 0)
						{
							returnMessage = "<html>Number of test cases to be uploaded is " + TotalTestCaseCount + ". " + "<br>" + 
									" Update the '" + TestCaseIdColumn + "' column accordingly for selection</html>";													
						}
						else
						{
							Globalvars.TotalTestCaseCount = TotalTestCaseCount;
							returnMessage = "Success";
							//updated validation complete check flag for execution
							blnValidationCompleteFlag = blnValidationFlag;
						}
						
					}
					return null;
					
				}
				
				@Override
				protected void process(List<String> chunks) {
					// TODO Auto-generated method stub
					//super.process(chunks);
					
					//initialize variable
					returnMessage = "";
					
					//change UI Components
					ImportTestCases.lblCheckmark.setVisible(false);
					ImportTestCases.lblValidateloading.setVisible(true);
					ImportTestCases.btnValidate.setEnabled(false);	
					ImportTestCases.lblValidationmessage.setText(returnMessage);
					ImportTestCases.btnMapNext.setEnabled(false);
					
						
				}
				
				@Override
				protected void done() {
					// TODO Auto-generated method stub
					//super.done();
					//after task complete
					ImportTestCases.lblValidateloading.setVisible(false);
					ImportTestCases.btnValidate.setEnabled(true);	
					
					if(returnMessage.equals("Success"))
					{
						ImportTestCases.lblCheckmark.setVisible(true);
						ImportTestCases.lblValidationmessage.setText("");
					}
					else
					{
						ImportTestCases.lblValidationmessage.setVisible(true);
						ImportTestCases.lblValidationmessage.setForeground(Color.RED);
						ImportTestCases.lblValidationmessage.setText(returnMessage);
						ImportTestCases.lblCheckmark.setVisible(false);
					}
					
				}
				
			};			
			bgWorker.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		
		return returnMessage;
		
	}


	public boolean fnValidateFileFormat() {

		try
		{
			String ExcelPath = ImportTestCases.txtExcelPath.getText();
			File jfile = new File(ExcelPath);
			//return if file does not exists
			if(!jfile.exists())
			{
				ImportTestCases.lblValidationMessage.setForeground(Color.RED);
				ImportTestCases.lblValidationMessage.setText("File path do not exist. Please enter a valid file path");
				return false;
			}
			//return if file extension is not correct
			String fileExtension = FilenameUtils.getExtension(ExcelPath);
			if(!(fileExtension.equalsIgnoreCase("xls") || fileExtension.equalsIgnoreCase("xlsx") || fileExtension.equalsIgnoreCase("xlsm")))
			{
				ImportTestCases.lblValidationMessage.setForeground(Color.RED);
				ImportTestCases.lblValidationMessage.setText("Invalid file type. Please select an excel file with valid format");
				return false;
			}
			Globalvars.ExcelSheetPath = ExcelPath;
			ImportTestCases.lblValidationMessage.setText("");
			return true;
			
		}
		catch(Exception e)
		{
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			return false;
		}
	}
	
	public boolean fnValidateTestIssueType() {

		Boolean blnTestIssueTypeFound = false;
		CloseableHttpClient httpclient = null;
		try
		{
			
			//get the project id for selected Project name
			String strProjectId = ProjectMap.get(ImportTestCases.comBoxProjName.getSelectedItem().toString());
			//retrieve issue meta information
			httpclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet(CreateTestWithTestSteps.API_CREATE_META.replace("{SERVER}", CreateTestWithTestSteps.jiraBaseURL) + strProjectId);
			httpget.setHeader("Content-Type", "application/json");
			httpget.setHeader(CreateTestWithTestSteps.header);
			
	        CloseableHttpResponse response = null;

			response = httpclient.execute(httpget);
			String result = "";
			if(response.getStatusLine().getStatusCode() == 200)
			{
				result = EntityUtils.toString(response.getEntity());
				JSONObject issuesListObject = new JSONObject(result);
				JSONArray issuesListArray = issuesListObject.getJSONArray("projects").getJSONObject(0).getJSONArray("issuetypes");
				
				for(int intIssueCounter = 0;intIssueCounter < issuesListArray.length();intIssueCounter++)
				{
					if(issuesListArray.getJSONObject(intIssueCounter).get("name").toString().equals("Test"))
					{
						CreateTestWithTestSteps.issueTypeId = issuesListArray.getJSONObject(intIssueCounter).get("id").toString();
						blnTestIssueTypeFound = true;
						ImportTestCases.lblValidationMessage.setText("");
						break;
					}
				}
				if(blnTestIssueTypeFound)
				{
					TextOutputFile.writeToLog(result);
				}
				else 
				{
					ImportTestCases.lblValidationMessage.setForeground(Color.RED);
					ImportTestCases.lblValidationMessage.setText("Project '" + ImportTestCases.comBoxProjName.getSelectedItem().toString() + 
							"' does not contain Test as one of the Issue Type");
					TextOutputFile.writeToLog(result);
					
				}
			}
			else
			{				
				TextOutputFile.writeToLog(response.getStatusLine().toString());
				TextOutputFile.writeToLog(result);	
				ImportTestCases.lblValidationMessage.setForeground(Color.RED);
				ImportTestCases.lblValidationMessage.setText("Unable to retrieve the test issue type id for the project '"
						+ ImportTestCases.comBoxProjName.getSelectedItem().toString() + "'");
			}
			httpclient.close();
			return blnTestIssueTypeFound;
		}
	        
        catch(Exception e)
        {
        	TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
        	ImportTestCases.lblValidationMessage.setForeground(Color.RED);
        	ImportTestCases.lblValidationMessage.setText("Unable to retrieve the test issue type id for the project '"
					+ ImportTestCases.comBoxProjName.getSelectedItem().toString() + "'");
        	try {
				httpclient.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	return blnTestIssueTypeFound;
		} 		
		
	}


	@SuppressWarnings("unchecked")
	public void fnLoadExcelSheets() {

		try
		{
			List<String> workSheets = ExcelFunctions.fnGetSheets(Globalvars.ExcelSheetPath);
			ImportTestCases.lstWorkSheets.setListData(workSheets.toArray());
			// if only 1 sheet is present in the excel, then select the sheet by default
			if(workSheets.size() == 1)
			{
				ImportTestCases.lstWorkSheets.setSelectedIndex(0);
			}
		}
		catch(Exception e)
		{
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
	}


	@SuppressWarnings("unchecked")
	public void fnAddToMapping() {
		
		try
		{
			
			if(!ImportTestCases.lstJiraFields.isSelectionEmpty() && !ImportTestCases.lstExcelColumns.isSelectionEmpty())
			{				
				//add values to Mapping list
				DefaultTableModel model = (DefaultTableModel)ImportTestCases.tblMapping.getModel();
				model.addRow(new Object[]{ImportTestCases.lstJiraFields.getSelectedValue(), ImportTestCases.lstExcelColumns.getSelectedValue()});
				//remove values from JLists
				JiralistModel.remove(ImportTestCases.lstJiraFields.getSelectedIndex());
				ExcellistModel.remove(ImportTestCases.lstExcelColumns.getSelectedIndex());				
				
				//update Global Hashmap
				JiraExcelMap.put(ImportTestCases.lstJiraFields.getSelectedValue().toString(), ImportTestCases.lstExcelColumns.getSelectedValue().toString());
				
				//Sort list model
				JiralistModel = fnSortListModel(JiralistModel);
				ExcellistModel = fnSortListModel(ExcellistModel);
				
				//update values to JLists
				ImportTestCases.lstJiraFields.setListData(JiralistModel.toArray());
				ImportTestCases.lstExcelColumns.setListData(ExcellistModel.toArray());
				
				
				
				
			}
				
		}
		catch(Exception e)
		{
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
	}


	@SuppressWarnings("unchecked")
	public void fnRemovefromMapping() {

		try
		{
			if(!ImportTestCases.tblMapping.getSelectionModel().isSelectionEmpty())
			{	
				//add values to JLists
				JiralistModel.addElement(ImportTestCases.tblMapping.getValueAt(ImportTestCases.tblMapping.getSelectedRow(), 0).toString());
				ExcellistModel.addElement(ImportTestCases.tblMapping.getValueAt(ImportTestCases.tblMapping.getSelectedRow(), 1));
				
				//Sort list model
				JiralistModel = fnSortListModel(JiralistModel);
				ExcellistModel = fnSortListModel(ExcellistModel);
				
				//clear values from JTable
				ImportTestCases.lstJiraFields.setListData(JiralistModel.toArray());
				ImportTestCases.lstExcelColumns.setListData(ExcellistModel.toArray());
				//remove from JTable
				DefaultTableModel model = (DefaultTableModel)ImportTestCases.tblMapping.getModel();
				model.removeRow(ImportTestCases.tblMapping.getSelectedRow());
				
				
			}
		}
		catch(Exception e)
		{
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DefaultListModel fnSortListModel(DefaultListModel model) {

		List<String> list = new ArrayList<>();
	    for (int i = 0; i < model.size(); i++) {
	        list.add((String)model.get(i));
	    }
	    Collections.sort(list);
	    model.removeAllElements();
	    for (String s : list) {
	        model.addElement(s);
	    }
	    return model;
	}

	public Boolean fn_VerifyFileClosed()
	{
		
		Boolean blnExcelOpen = false;
		try {
			blnExcelOpen = ExcelFunctions.fn_VerifyFileClosed(Globalvars.ExcelSheetPath);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			return blnExcelOpen;
		}
		
		return blnExcelOpen;
	}
	
	
	
	
	public void fnLaunchConfirmationPanel() {
		try
		{
			//Changes in UI
			//Panel visibility
			ImportTestCases.panelLogin.setVisible(false);
			ImportTestCases.panelSecond.setVisible(false);
			ImportTestCases.panelMapping.setVisible(false);
			ImportTestCases.panelConfirm.setVisible(true);
			ImportTestCases.panelFinal.setVisible(false);
			
			//Load values
			ImportTestCases.lblProjectNameValue.setText(ImportTestCases.comBoxProjName.getSelectedItem().toString());
			
			//Load values
			ImportTestCases.lblExcelPathValue.setText(Globalvars.ExcelSheetPath);
			ImportTestCases.lblSheetNameValue.setText(Globalvars.ExcelWorkSheetName);
			ImportTestCases.lblTestCasesCount.setText(String.valueOf((int)Globalvars.TotalTestCaseCount));
			
			//clear table if any existing rows
			DefaultTableModel model = (DefaultTableModel)ImportTestCases.tblMapConfirm.getModel();
			int intTableRows = model.getRowCount();
			
			if(intTableRows > 0)
			{
				for(int intRowCounter = 0; intRowCounter < intTableRows; intRowCounter++)
				{
					model.removeRow(0);
				}
			}
			//load values in confirmation mapping table
			for(String MapKey : JiraExcelMap.keySet())
			{
				model.addRow(new Object[]{MapKey, JiraExcelMap.get(MapKey)});
			}
		}
		catch(Exception e)
		{
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
	
	}


	public void fnLaunchRunPanel() {
		
		try
		{
			//Changes in UI
			//Panel visibility
			ImportTestCases.panelLogin.setVisible(false);
			ImportTestCases.panelSecond.setVisible(false);
			ImportTestCases.panelMapping.setVisible(false);
			ImportTestCases.panelConfirm.setVisible(false);
			ImportTestCases.panelFinal.setVisible(true);
			
			//reset values
			startCount = false;
			ImportTestCases.txtAreaConsole.setText("");
			ImportTestCases.lblSuccessMessage.setText("");
			ImportTestCases.lblDetLog.setVisible(false);
			blnValidationCompleteFlag = false;
			strCreatedTestCases = "";
			strFailedTestCases.clear();
			createTestWithTestSteps.RespMessage = createTestWithTestSteps.SuccessMessage;
			Globalvars.TotalTestCaseUploaded = 0;
			Globalvars.TotalTestCaseFailed = 0;
			
			
			//Load values
			ImportTestCases.lblTotaltestcasesvalue.setText(String.valueOf((int)Globalvars.TotalTestCaseCount));
			ImportTestCases.lblTotaltestcasesuploadedvalue.setText(String.valueOf(Globalvars.TotalTestCaseUploaded));
			ImportTestCases.lblTotalTestCaseFailureValue.setText(String.valueOf(Globalvars.TotalTestCaseFailed));
			
			ImportTestCases.prgbarImport.setValue(0);
			ImportTestCases.lblSuccessMessage.setForeground(Color.BLACK);
			ImportTestCases.btnCancelImport.setEnabled(true);
			ImportTestCases.btnRunBack.setEnabled(false);
			ImportTestCases.btnClose.setEnabled(false);
			
			
			//Start run operation
			fnPerformImportOperation();
			
			

		}
		catch(Exception e)
		{
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		
	}


	private void fnUpdateImportSummary() {
		
		TextOutputFile.writeToLog("Import Summary");
		TextOutputFile.writeToLog("--------------");
		TextOutputFile.writeToLog("Test Cases which were imported successfully");
		TextOutputFile.writeToLog("-------------------------------------------");
		if(!strCreatedTestCases.isEmpty())
		{
			TextOutputFile.writeToLog(strCreatedTestCases.substring(1, strCreatedTestCases.length()));
		}
		TextOutputFile.writeToLog("Test Cases which failed during import");
		TextOutputFile.writeToLog("-------------------------------------");
		for(String failedTestDetail : strFailedTestCases)
		{
			TextOutputFile.writeToLog(failedTestDetail);
		}
		
	}



	private void fnPerformImportOperation() {
		
		
		try {
			bgWorker = new SwingWorker<Void, String>(){

				@Override
				protected Void doInBackground() throws Exception {
					// TODO Auto-generated method stub
					int rowCount = ExcelFunctions.fn_GetRowCount(Globalvars.ExcelSheetPath,Globalvars.ExcelWorkSheetName);
					
					//Check if TestCaseId/IssueKey Column exist
					if(!ExcelFunctions.fn_VerifyColumnExist(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, TestCaseIdColumn))
					{
						//if not, create a new TestCaseId Column
						if(ExcelFunctions.fn_AddColumn(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, TestCaseIdColumn))
						{
							blnUpdateTestCaseId = true;
						}		
					}
					else
					{
						blnUpdateTestCaseId = true;
					}
					
					for (int counter = 1; counter <= rowCount; counter++) 
					{	
						if(isCancelled())
						{
							break;
						}
						
						
						ExcelRowData.put(JiraExcelMap.get("Labels"),"");
						ExcelRowData.put(JiraExcelMap.get("Summary"),"");
						ExcelRowData.put(JiraExcelMap.get("Description"),"");
						ExcelRowData.put(JiraExcelMap.get("Test Step"),"");
						ExcelRowData.put(JiraExcelMap.get("Test Data"),"");
						ExcelRowData.put(JiraExcelMap.get("Expected Result"),"");
						ExcelRowData.put(JiraExcelMap.get("Link Issue"),"");
						ExcelRowData.put(JiraExcelMap.get("Link Type"),"");
						ExcelRowData.put(JiraExcelMap.get("Sprint"),"");
						
						
						String Message = fnImportExcelRowData(rowCount, counter);
						
						publish(Message);
						//exit for if failure
//						if(createTestWithTestSteps.RespMessage.split("~")[0].toLowerCase().contains("failure"))
//						{
//							break;
//						}
						if((Globalvars.TotalTestCaseUploaded + Globalvars.TotalTestCaseFailed) == Globalvars.TotalTestCaseCount)
						{
							setProgress(100);						
						}
						else
						{
							setProgress((int)((Globalvars.TotalTestCaseUploaded + Globalvars.TotalTestCaseFailed) * (100/Globalvars.TotalTestCaseCount)));							
						}
						
					}
					//cancel on failure
//					if(createTestWithTestSteps.RespMessage.split("~")[0].toLowerCase().contains("failure"))
//						bgWorker.cancel(true);
					
					
					return null;
				}
				
				@Override
				protected void process(List<String> chunks) {
					// TODO Auto-generated method stub
					//super.process(chunks);
					for(String line:chunks)
					{
						String ConsoleText = ImportTestCases.txtAreaConsole.getText();
						if(ConsoleText.isEmpty())
						{
							if(line != null && !line.isEmpty())
							ImportTestCases.txtAreaConsole.setText(line + "\n");
						}
						else
						{
							if(line != null && !line.isEmpty())
								ImportTestCases.txtAreaConsole.setText(ConsoleText + line + "\n");
						}
						
					}
					ImportTestCases.lblTotaltestcasesuploadedvalue.setText(String.valueOf(Globalvars.TotalTestCaseUploaded));
					ImportTestCases.lblTotalTestCaseFailureValue.setText(String.valueOf(Globalvars.TotalTestCaseFailed));					
					ImportTestCases.txtAreaConsole.setCaretPosition(ImportTestCases.txtAreaConsole.getDocument().getLength());
					
					
						
				}
				
				@Override
				protected void done() {
					// TODO Auto-generated method stub
					//super.done();
					//after task complete
					ImportTestCases.btnCancelImport.setEnabled(false);
					ImportTestCases.btnRunBack.setEnabled(true);
					ImportTestCases.btnClose.setEnabled(true);
					
					
					if(isCancelled())
					{
						ImportTestCases.lblSuccessMessage.setText("Test Case import has been interuppted");
						ImportTestCases.lblSuccessMessage.setForeground(Color.RED);
						ImportTestCases.lblDetLog.setVisible(true);
					}
					else if(Globalvars.TotalTestCaseFailed > 0)
					{
						ImportTestCases.lblSuccessMessage.setForeground(Color.RED);
						ImportTestCases.lblSuccessMessage.setText("All the Test Cases are not imported sucessfully");
						ImportTestCases.lblDetLog.setVisible(true);
					}
					else if(Globalvars.TotalTestCaseFailed == 0)
					{
						//ImportTestCases.lblSuccessMessage.setForeground(new Color(0, 128, 0));
						ImportTestCases.lblSuccessMessage.setText(createTestWithTestSteps.SuccessMessage.split("~")[1]);
					}
					fnUpdateImportSummary();					
					
				}
				
			};
			bgWorker.addPropertyChangeListener(new PropertyChangeListener() {
				
				@Override
				public void propertyChange(PropertyChangeEvent evt) {

					switch(evt.getPropertyName())
					{
						case "progress":
							ImportTestCases.prgbarImport.setValue((Integer) evt.getNewValue());
					}
					
				}
			});
			bgWorker.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		
	}


	protected String fnImportExcelRowData(Integer TotalRowCount, Integer counter) throws Exception {
		
		String retMessage = "";
		try {
			String ApplicationLabel, testSummary, testDescription, testStepDescription, testStepData,
					testStepExpectedResult, linkIssue, linkType, sprint, str_TestDetails;
			String strTestCaseId = "";
			
			
			
			ExcelRowData = ExcelFunctions.fn_GetRowData(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, counter, ExcelRowData);
			
			ApplicationLabel = ExcelRowData.get(JiraExcelMap.get("Labels"));
			testSummary = ExcelRowData.get(JiraExcelMap.get("Summary"));
			testDescription = ExcelRowData.get(JiraExcelMap.get("Description"));
			testStepDescription = ExcelRowData.get(JiraExcelMap.get("Test Step"));
			testStepData = ExcelRowData.get(JiraExcelMap.get("Test Data"));
			testStepExpectedResult = ExcelRowData.get(JiraExcelMap.get("Expected Result"));
			linkIssue = ExcelRowData.get(JiraExcelMap.get("Link Issue"));
			linkType = ExcelRowData.get(JiraExcelMap.get("Link Type"));
			sprint = ExcelRowData.get(JiraExcelMap.get("Sprint"));
			
			/*
			ApplicationLabel = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, counter, JiraExcelMap.get("Labels"));
			
			
			testSummary = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, counter, JiraExcelMap.get("Summary"));		
			testDescription = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, counter, JiraExcelMap.get("Description"));
			
			testStepDescription = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, counter, JiraExcelMap.get("Test Step"));
			testStepData = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, counter, JiraExcelMap.get("Test Data"));
			testStepExpectedResult = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, counter, JiraExcelMap.get("Expected Result"));
			
			//new fields for issue linking
			linkIssue = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, counter, JiraExcelMap.get("Link Issue"));
			linkType = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, counter, JiraExcelMap.get("Link Type"));
			//Sprint
			sprint = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, counter, JiraExcelMap.get("Sprint"));
			*/
			//TestCaseId - Only if column exist
			if(blnUpdateTestCaseId)
			{
				strTestCaseId = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, counter, TestCaseIdColumn);
			}
			
			if(strTestCaseId != null && !strTestCaseId.trim().isEmpty() && testSummary != null)
			{
				blnStepExecute = false;
			}	
			else if((strTestCaseId == null || strTestCaseId.trim().isEmpty()) && testSummary != null)
			{

				blnStepExecute = true;
				blnSuccessFlag = true;
				//receive testid if optional boolean parameter is not passed
				str_TestDetails = createTestWithTestSteps.createTestCaseinJira(testSummary, testDescription, ApplicationLabel, sprint, true);
				
				if(str_TestDetails != null && str_TestDetails.contains(":"))
				{
					testId = str_TestDetails.split(":")[0];
					testKey = str_TestDetails.split(":")[1]; 
				}
				//Jira API to get details about an issue. To be used for debugging
				//testId = createTestWithTestSteps.getIssueDetails("CAPPS-10986");
				
				//check if there is failure
				if(createTestWithTestSteps.RespMessage.split("~")[0].toLowerCase().contains("failure"))
				{
					String currRetMessage = "Error uploading Test Case with Summary : " + testSummary + "----\nRow Number : " + counter + 
							"----\nError Description : " + createTestWithTestSteps.RespMessage.split("~")[1];
					retMessage+= currRetMessage;
					blnSuccessFlag = false;
					strFailedTestCases.add(currRetMessage);					
				}
				else
				{
					retMessage = "Created Test Case '" + testKey + "': '" + testSummary + "'\n";
				}
				
				if(linkIssue != null && linkType != null && blnSuccessFlag)
				{
					//if there are multiple stories to link for the test case
					if(linkIssue.contains(","))
					{
						String[] arrlinkIssue = linkIssue.split(",");
						for(String indlinkIssue : arrlinkIssue)
						{	
							if(indlinkIssue !=null && !indlinkIssue.trim().isEmpty())
							{
								linkId = createTestWithTestSteps.linkIssue(String.valueOf(testId), indlinkIssue.trim(), linkType);
								
								if(createTestWithTestSteps.RespMessage.split("~")[0].toLowerCase().contains("failure"))
								{
									retMessage+= createTestWithTestSteps.RespMessage.split("~")[1];
									blnSuccessFlag = false;
									strFailedTestCases.add("Error while linking Test Case : " + testKey + " to Issue " + indlinkIssue.trim() + "----\nRow Number : " + counter + 
											"----\nError Description : " + createTestWithTestSteps.RespMessage.split("~")[1]);
								}
								else
								{
									retMessage+= "This test case is linked to : '" + indlinkIssue.trim() + " successfully '\n";
								}
							}
						}
					}
					else
					{
						if(linkIssue !=null && !linkIssue.trim().isEmpty())
						{
							linkId = createTestWithTestSteps.linkIssue(String.valueOf(testId), linkIssue.trim(), linkType);
							
							if(createTestWithTestSteps.RespMessage.split("~")[0].toLowerCase().contains("failure"))
							{
								retMessage+= createTestWithTestSteps.RespMessage.split("~")[1];
								blnSuccessFlag = false;
								strFailedTestCases.add("Error while linking Test Case : " + testKey + " to Issue " + linkIssue.trim() + "----\nRow Number : " + counter +  
										"----\nError Description : " + createTestWithTestSteps.RespMessage.split("~")[1]);
							}
							else
							{
								retMessage+= "This test case is linked to : '" + linkIssue.trim() + " successfully '\n";
							}
						}
					}
				}
				
				//Update Test Case Id for the created test case
				if(blnUpdateTestCaseId && blnSuccessFlag)
				{
					ExcelFunctions.fn_SetCellData(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, TestCaseIdColumn, counter, testKey);
				}
				//Set test case step counter to 0
				intTestStepCounter = 0;
			}

			
			if(blnStepExecute && blnSuccessFlag)
			{
				//increment test case step counter of display
				intTestStepCounter++;
				//Create test step accordingly depending on Cloud or Jira Server
				if(ImportTestCases.chckbxCloud.isSelected())
				{
					createTestWithTestSteps.createTestStepinJira(testStepDescription, testStepData, testStepExpectedResult, testId);
				}
				else
				{
					createTestWithTestSteps.createTestStepinJiraServer(testStepDescription, testStepData, testStepExpectedResult, testId);
				}
				//check if there is failure
				if(createTestWithTestSteps.RespMessage.split("~")[0].toLowerCase().contains("failure"))
				{
					retMessage+= createTestWithTestSteps.RespMessage.split("~")[1];
					blnSuccessFlag = false;
					strFailedTestCases.add("Error while adding Step " + intTestStepCounter + " for Test Case : " + testKey + "----\nRow Number : " + counter +  
							"----\nError Description : " + createTestWithTestSteps.RespMessage.split("~")[1]);
					Globalvars.TotalTestCaseFailed++;
				}
				else
				{
					retMessage += "Adding Step " + intTestStepCounter + " for the test case";
					//get test case completion count
					if(counter.equals(TotalRowCount))
					{
						Globalvars.TotalTestCaseUploaded++;
						strCreatedTestCases = strCreatedTestCases + "," + testKey;
					}
					else
					{
						ApplicationLabel = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, counter + 1, JiraExcelMap.get("Labels"));
						if(ApplicationLabel != null)
						{
							Globalvars.TotalTestCaseUploaded++;
							strCreatedTestCases = strCreatedTestCases + "," + testKey;
						}
					}
				}
			
				
			}
			//capture import failed test cases
			else if(blnStepExecute && !blnSuccessFlag)
			{
				//get test case completion count
				if(counter.equals(TotalRowCount))
				{
					Globalvars.TotalTestCaseFailed++;
				}
				else
				{
					ApplicationLabel = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, counter + 1, JiraExcelMap.get("Labels"));
					if(ApplicationLabel != null)
					{
						Globalvars.TotalTestCaseFailed++;
					}
				}
			}
			
			
			
			testSummary = testDescription = testStepDescription = testStepData = testStepExpectedResult = null;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		return retMessage;
		
	}


	public void fnCancelImport() {

		try {
			
			bgWorker.cancel(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
	}


	public void fnInitialization() {

		
		try {
			//set Cert path
			//System.setProperty("javax.net.ssl.trustStore", Globalvars.strCertPath);
			
			
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { 
				    new X509TrustManager() {     
				        public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
				            return new X509Certificate[0];
				        } 
				        public void checkClientTrusted( 
				            java.security.cert.X509Certificate[] certs, String authType) {
				            } 
				        public void checkServerTrusted( 
				            java.security.cert.X509Certificate[] certs, String authType) {
				        }
				    } 
				}; 

			// Install the all-trusting trust manager
			try {
			    SSLContext sc = SSLContext.getInstance("SSL"); 
			    sc.init(null, trustAllCerts, new java.security.SecureRandom()); 
			    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			} catch (GeneralSecurityException e) {
			} 

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		
	}


	public void fnStoreExcelandMapping() {
		
		try {
			
			//Store Excel path & sheet name in preferences
			fnStorePreferences("ExcelPath",Globalvars.ExcelSheetPath);
			fnStorePreferences("ExcelSheet",Globalvars.ExcelWorkSheetName);
			
			DefaultTableModel model = (DefaultTableModel)ImportTestCases.tblMapping.getModel();
			int intTableRows = model.getRowCount();
			
			if(intTableRows > 0)
			{
				for(int intRowCounter = 0; intRowCounter < intTableRows; intRowCounter++)
				{
					fnStorePreferences(model.getValueAt(intRowCounter, 0).toString(),model.getValueAt(intRowCounter, 1).toString());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		
		
	}
	
	
public void fnLoadMappingPreferences() {
		
		try
		{
			//Check if Preferences location and file exist
			File jfile = new File(Globalvars.strPreferencesPath);
			if(jfile.exists()) 
			{ 
				//Check if user name property already exists, update new user name
				// Open the file
				FileInputStream fstream = new FileInputStream(Globalvars.strPreferencesPath);
				BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
				String strLine;
				while ((strLine = br.readLine()) != null)   
				{ 
					if(strLine.contains("::"))
					{
						//check if it is the same excel
						if(strLine.split("::")[0].equals("ExcelSheetPath"))
						{
							if(strLine.split("::")[1].trim().equals(Globalvars.ExcelSheetPath))
							{
								
							}
						}
						
						
					}
				}
				fstream.close();
	            br.close();
			}
				
		}
		catch(Exception e)
		{
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
	}



	public void fnMatchandMap() {
	
		try
		{
			List<String> MatchedFields = new ArrayList<String>();
			Integer jiraFieldCount = ImportTestCases.lstJiraFields.getModel().getSize();
			Integer excelColCount = ImportTestCases.lstExcelColumns.getModel().getSize();
			
			for(int intJFCounter = 0; intJFCounter < jiraFieldCount; intJFCounter++)
			{
				for(int intExCounter = 0; intExCounter < excelColCount; intExCounter++)
				{
					if(ImportTestCases.lstJiraFields.getModel().getElementAt(intJFCounter).toString().trim()
							.equals(ImportTestCases.lstExcelColumns.getModel().getElementAt(intExCounter).toString().trim()))
					{	
						//add values to mapping
						DefaultTableModel model = (DefaultTableModel)ImportTestCases.tblMapping.getModel();
						model.addRow(new Object[]{ImportTestCases.lstJiraFields.getModel().getElementAt(intJFCounter).toString().trim()
								, ImportTestCases.lstExcelColumns.getModel().getElementAt(intExCounter).toString().trim()});
						
						//store matched names
						MatchedFields.add(ImportTestCases.lstExcelColumns.getModel().getElementAt(intExCounter).toString().trim());
						
						//update Global Hashmap
						JiraExcelMap.put(ImportTestCases.lstJiraFields.getModel().getElementAt(intJFCounter).toString().trim(),
								ImportTestCases.lstExcelColumns.getModel().getElementAt(intExCounter).toString().trim());
						
					}
					
					
				}
				
				
			}
			//remove items from jira list model
			for(int intListCounter = JiralistModel.getSize() - 1; intListCounter >= 0; intListCounter--)
			{
				if(MatchedFields.contains(JiralistModel.getElementAt(intListCounter).toString()))
				{
					JiralistModel.remove(intListCounter);
				}
			}
			
			
			//remove items from excel list model
			for(int intListCounter = ExcellistModel.getSize() - 1; intListCounter >= 0; intListCounter--)
			{
				if(MatchedFields.contains(ExcellistModel.getElementAt(intListCounter).toString()))
				{
					ExcellistModel.remove(intListCounter);
				}
			}
			
			
			//Sort list model
			JiralistModel = fnSortListModel(JiralistModel);
			ExcellistModel = fnSortListModel(ExcellistModel);
			
			//update values to JLists
			ImportTestCases.lstJiraFields.setListData(JiralistModel.toArray());
			ImportTestCases.lstExcelColumns.setListData(ExcellistModel.toArray());				
			
				
		}
		catch(Exception e)
		{
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		
		
	}

	public void fnToggleRememberCheckbox() {
		// TODO Auto-generated method stub
		try {
			if(ImportTestCases.chckbxRememberMe.isSelected())
			{
				ImportTestCases.chckbxRememberMe.setSelected(false);
			}
			else
			{
				ImportTestCases.chckbxRememberMe.setSelected(true);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		
	}



	public void fnCreateCoreLogFile() {

		try {
			TextOutputFile.createFile(Globalvars.strCoreLogPath);
			Globalvars.strLogPath = Globalvars.strCoreLogPath;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		
	}



	public void launchCurrentLog() {
		
		FileOperations.openFileinNotepad(Globalvars.strCoreLogPath);
		
	}

	public void fnToggleCloudCheckbox() {
		// TODO Auto-generated method stub
		try {
			if(ImportTestCases.chckbxCloud.isSelected())
			{				
				ImportTestCases.txtAccessKey.setVisible(true);
				ImportTestCases.txtSecretKey.setVisible(true);
				ImportTestCases.lblAccessKey.setVisible(true);
				ImportTestCases.lblSecretKey.setVisible(true);
			}
			else
			{				
				ImportTestCases.txtAccessKey.setVisible(false);
				ImportTestCases.txtSecretKey.setVisible(false);
				ImportTestCases.lblAccessKey.setVisible(false);
				ImportTestCases.lblSecretKey.setVisible(false);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		
	}

	public void fnSetPathVariables() {
		// TODO Auto-generated method stub
		String strLocalLocation = "";
		String strLogLocation = "";
		if(System.getProperty("os.name").contains("Windows"))
		{
			strLocalLocation = Globalvars.strLocalLocation;
			strLogLocation = Globalvars.strLogLocation;
		}
		else if(System.getProperty("os.name").contains("Mac"))
		{
			strLocalLocation = Globalvars.strMacLocalLocation;
			strLogLocation = Globalvars.strMacLogLocation;
		}
		Globalvars.strLaunchLogPath = strLogLocation + Globalvars.strLaunchLogFileName;
		Globalvars.strLocalVersionPath = strLocalLocation + Globalvars.strVersionFileName;
		Globalvars.strLocalExePath = strLocalLocation + Globalvars.strExeFileName;
		Globalvars.strCoreLogPath = strLogLocation + Globalvars.strCoreLogFileName;
		
		
	}


}
