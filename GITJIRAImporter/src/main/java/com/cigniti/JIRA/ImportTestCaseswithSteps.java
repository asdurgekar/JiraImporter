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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.cigniti.util.Globalvars;
import com.thed.zephyr.cloud.rest.ZFJCloudRestClient;
import com.thed.zephyr.cloud.rest.client.JwtGenerator;

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
	private SwingWorker<Void, String> bgWorker;
	public static String DisplayMessage = "Success:All test cases are uploaded successfully";
	public String appName = "Jira Test Case Importer";
	public String versionNumber = "1.4";
	public String pageName = "Login";
	public String strAuthenticationMessage;
	public Boolean blnLoginSuccess;
	public String returnMessage;
	public int AuthResponse;
	public String KeyResponse;
	public CreateTestWithTestSteps createTestWithTestSteps = new CreateTestWithTestSteps();
	
	public void fn_ImportTestCaseswithSteps() throws IOException {

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
					
					CreateTestWithTestSteps.userName = ImportTestCases.txtUserName.getText();
					CreateTestWithTestSteps.password = new String(ImportTestCases.txtPassword.getPassword());
					CreateTestWithTestSteps.accessKey = new String(ImportTestCases.txtAccessKey.getPassword());
					CreateTestWithTestSteps.secretKey = new String(ImportTestCases.txtSecretKey.getPassword());
					
					AuthResponse = fn_PerformAuthentication();
					KeyResponse = CreateTestWithTestSteps.fn_ValidateKeys(JSONProjectList);					
					
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
						strAuthenticationMessage = "Unable to connect on LAN. Please make sure you are connected on Wifi";
					}
					else if(AuthResponse != 200)
					{
						System.out.println("JSON Response: " + AuthResponse + "Login Failure");
						strAuthenticationMessage = "Unable to Login. Please enter valid credentials";				
					}
					else if(!KeyResponse.equals("Success"))
					{
						System.out.println(KeyResponse);
						strAuthenticationMessage = KeyResponse;				
					}
					else
					{
						System.out.println("JSON Response: " + AuthResponse + "Login Success");
						strAuthenticationMessage = "";
						fnCreateConfigFile();
						if(ImportTestCases.chckbxRememberMe.isSelected())
							fnStorePreferences("UserName",ImportTestCases.txtUserName.getText());
							fnStorePreferences("AccessKey",new String(ImportTestCases.txtAccessKey.getPassword()));
							fnStorePreferences("SecretKey",new String(ImportTestCases.txtSecretKey.getPassword()));
						fnUpdateClientToken();
						fnLaunchLoadSecondPanel();
						blnLoginSuccess = true;
						
					}
					
					ImportTestCases.btnLogin.setEnabled(true);
					ImportTestCases.lblAuthmessage.setText(strAuthenticationMessage);
					ImportTestCases.lblLoginloading.setVisible(false);
					
				}
				
			};			
			bgWorker.execute();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
//				System.out.println("JSON Response: " + AuthResponse + "Login Failure");
//				strAuthenticationMessage = "Unable to Login. Please enter valid credentials";				
//			}
//			else if(!KeyResponse.equals("Success"))
//			{
//				System.out.println(KeyResponse);
//				strAuthenticationMessage = KeyResponse;				
//			}
//			else
//			{
//				System.out.println("JSON Response: " + AuthResponse + "Login Success");
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
			e.printStackTrace();
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
			e.printStackTrace();
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
				
	            System.out.println("Preferences are stored in the system");
			
			}
						
		}
		catch(Exception e)
		{
			e.printStackTrace();
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
				System.out.println("Project values are loaded onto the application");
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
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
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
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
			
			if(!Globalvars.ExcelSheetPath.equals(OldExcelPath) || !Globalvars.ExcelWorkSheetName.equals(OldSheetName))
			{
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
				
			}
				
		}
		catch(Exception e)
		{
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void fnCreateJiraFields() {
		
		try {
			//Load Jira columns in HashMap
			JiraExcelMap.put("Summary", "");
			JiraExcelMap.put("Labels", "");
			JiraExcelMap.put("Description", "");
			JiraExcelMap.put("Step", "");
			JiraExcelMap.put("Data", "");
			JiraExcelMap.put("Result", "");
			
			JiralistModel = new DefaultListModel();
			
			//enable Validate button if no elements exist in list
			if(JiralistModel.size() == 0)
				ImportTestCases.btnValidate.setEnabled(true);
			
			ImportTestCases.lstJiraFields.setListData(JiralistModel.toArray());
									
			JiralistModel.addListDataListener(new MyListDataListener());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			e.printStackTrace();
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
			
			System.out.println("Jira field values are loaded");
			
			
			
			//DefaultListModel jDataModel = (DefaultListModel) ImportTestCases.lstJiraFields.getModel();
			//jDataModel.addListDataListener(new MyListDataListener()); 
			//JiralistModel.addListDataListener(new MyListDataListener());
		}
		catch(Exception e)
		{
			e.printStackTrace();
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
			System.out.println("Excel Header values are loaded");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
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
			System.out.println("Excel Header values are loaded");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
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
					System.out.println(JSONProjectList.toString());
					returnCode = response.getStatusLine().getStatusCode();
				}
			}
			else if(response.getStatusLine().getStatusCode() == 401)
			{				
				System.out.println(response.getStatusLine());
				returnCode = response.getStatusLine().getStatusCode();
			}
        }
        catch(Exception e)
        {
        	e.printStackTrace();        	
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
						case "UserName":
							ImportTestCases.txtUserName.setText(strLine.split("::")[1]);
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
			e.printStackTrace();
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
					
					String strTestStep = "";
					String strTestData = "";
					String strExpectedResult = "";
					Boolean blnValidationFlag = true;
					for(int intRowCounter = 1;intRowCounter <= rowCount; intRowCounter++)
					{
						strTestStep = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath,Globalvars.ExcelWorkSheetName, intRowCounter, JiraExcelMap.get("Step"));
						strTestData = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath,Globalvars.ExcelWorkSheetName, intRowCounter, JiraExcelMap.get("Data"));
						strExpectedResult = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath,Globalvars.ExcelWorkSheetName, intRowCounter, JiraExcelMap.get("Result"));
						
						//verify blank values for Test Step
						if(strTestStep == null)
						{
							returnMessage = "Blank value in Column 'Test Step' at row " + intRowCounter;
							blnValidationFlag = false;
							break;
						}
						else if(strTestStep.trim().isEmpty())
						{
							returnMessage = "Blank value in Column 'Test Step' at row " + intRowCounter;
							blnValidationFlag = false;
							break;
						}
					
						//verify blank values for Test Data
						if(strTestData == null)
						{
							returnMessage = "Blank value in Column 'Test Data' at row " + intRowCounter;
							blnValidationFlag = false;
							break;
						}
						else if(strTestData.trim().isEmpty())
						{
							returnMessage = "Blank value in Column 'Test Data' at row " + intRowCounter;
							blnValidationFlag = false;
							break;
						}
					
						//verify blank values for Expected Result
						if(strExpectedResult == null)
						{
							returnMessage = "Blank value in Column 'Expected Result' at row " + intRowCounter;
							blnValidationFlag = false;
							break;
						}
						else if(strExpectedResult.trim().isEmpty())
						{
							returnMessage = "Blank value in Column 'Expected Result' at row " + intRowCounter;
							blnValidationFlag = false;
							break;
						}
					
					}
					
					if(blnValidationFlag)
					{
						//get total count of test cases
						for(int intRowCounter = 1;intRowCounter <=rowCount; intRowCounter++)
						{
							strRowString = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath,Globalvars.ExcelWorkSheetName, intRowCounter, JiraExcelMap.get("Labels"));
							if(strRowString != null)
							{
								TotalTestCaseCount++;
							}
						
						}
						Globalvars.TotalTestCaseCount = TotalTestCaseCount;
						returnMessage = "Success";						
						
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
			e.printStackTrace();
		}
		
		return returnMessage;
		
		//------------------------------------without loading icon---------------------------------------		
		
//		try
//		{
//			String returnMessage = "Success";
//			String strRowString = "";
//			int TotalTestCaseCount = 0;
//			//get total row count
//			int rowCount = ExcelFunctions.fn_GetRowCount(Globalvars.ExcelSheetPath,Globalvars.ExcelWorkSheetName);
//			
//			String strTestStep = "";
//			String strTestData = "";
//			String strExpectedResult = "";
//			for(int intRowCounter = 1;intRowCounter <= rowCount; intRowCounter++)
//			{
//				strTestStep = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath,Globalvars.ExcelWorkSheetName, intRowCounter, JiraExcelMap.get("Step"));
//				strTestData = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath,Globalvars.ExcelWorkSheetName, intRowCounter, JiraExcelMap.get("Data"));
//				strExpectedResult = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath,Globalvars.ExcelWorkSheetName, intRowCounter, JiraExcelMap.get("Result"));
//				
//				//verify blank values for Test Step
//				if(strTestStep == null)
//				{
//					returnMessage = "Blank value in Column 'Test Step' at row " + intRowCounter;
//					return returnMessage;
//				}
//				else if(strTestStep.trim().isEmpty())
//				{
//					returnMessage = "Blank value in Column 'Test Step' at row " + intRowCounter;
//					return returnMessage;
//				}
//			
//				//verify blank values for Test Data
//				if(strTestData == null)
//				{
//					returnMessage = "Blank value in Column 'Test Data' at row " + intRowCounter;
//					return returnMessage;
//				}
//				else if(strTestData.trim().isEmpty())
//				{
//					returnMessage = "Blank value in Column 'Test Data' at row " + intRowCounter;
//					return returnMessage;
//				}
//			
//				//verify blank values for Expected Result
//				if(strExpectedResult == null)
//				{
//					returnMessage = "Blank value in Column 'Expected Result' at row " + intRowCounter;
//					return returnMessage;
//				}
//				else if(strExpectedResult.trim().isEmpty())
//				{
//					returnMessage = "Blank value in Column 'Expected Result' at row " + intRowCounter;
//					return returnMessage;
//				}
//			
//			}
//			//get total count of test cases
//			for(int intRowCounter = 1;intRowCounter <=rowCount; intRowCounter++)
//			{
//				strRowString = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath,Globalvars.ExcelWorkSheetName, intRowCounter, JiraExcelMap.get("Labels"));
//				if(strRowString != null)
//				{
//					TotalTestCaseCount++;
//				}
//			
//			}
//			Globalvars.TotalTestCaseCount = TotalTestCaseCount;
//			
//			
//			return returnMessage;
//			
//			
//				
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		return null;
		
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
			e.printStackTrace();
			return false;
		}
	}


	@SuppressWarnings("unchecked")
	public void fnLoadExcelSheets() {

		try
		{
			List<String> workSheets = ExcelFunctions.fnGetSheets(Globalvars.ExcelSheetPath);
			ImportTestCases.lstWorkSheets.setListData(workSheets.toArray());
		}
		catch(Exception e)
		{
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			ImportTestCases.lblTestCasesCount.setText(String.valueOf(Globalvars.TotalTestCaseCount));
			
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
			e.printStackTrace();
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
			Globalvars.TotalTestCaseUploaded = 0;
			ImportTestCases.txtAreaConsole.setText("");
			ImportTestCases.lblSuccessMessage.setText("");
			
			//Load values
			ImportTestCases.lblTotaltestcasesvalue.setText(String.valueOf(Globalvars.TotalTestCaseCount));
			ImportTestCases.lblTotaltestcasesuploadedvalue.setText(String.valueOf(Globalvars.TotalTestCaseUploaded));
			ImportTestCases.prgbarImport.setValue(0);
			
			ImportTestCases.btnCancelImport.setEnabled(true);
			ImportTestCases.btnRunBack.setEnabled(false);
			ImportTestCases.btnClose.setEnabled(false);
			
			
			//Start run operation
			fnPerformImportOperation();
			

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}


	private void fnPerformImportOperation() {
		
		
		try {
			bgWorker = new SwingWorker<Void, String>(){

				@Override
				protected Void doInBackground() throws Exception {
					// TODO Auto-generated method stub
					int rowCount = ExcelFunctions.fn_GetRowCount(Globalvars.ExcelSheetPath,Globalvars.ExcelWorkSheetName);
					startCount = false;
					for (int counter = 1; counter <= rowCount; counter++) 
					{	
						if(isCancelled())
						{
							break;
						}
						String Message = fnImportExcelRowData(rowCount, counter);
						
						publish(Message);
						//exit for if failure
						if(createTestWithTestSteps.RespMessage.split("~")[0].toLowerCase().contains("failure"))
						{
							break;
						}
						if(Globalvars.TotalTestCaseUploaded == Globalvars.TotalTestCaseCount)
						{
							setProgress(100);						
						}
						else
						{
							setProgress(Globalvars.TotalTestCaseUploaded * (100/Globalvars.TotalTestCaseCount));							
						}
						
					}
					//cancel on failure
					if(createTestWithTestSteps.RespMessage.split("~")[0].toLowerCase().contains("failure"))
						bgWorker.cancel(true);
					
					
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
							ImportTestCases.txtAreaConsole.setText(line);
						}
						else
						{
							ImportTestCases.txtAreaConsole.setText(ConsoleText + "\n" + line);
						}
						
					}
					ImportTestCases.lblTotaltestcasesuploadedvalue.setText(String.valueOf(Globalvars.TotalTestCaseUploaded));
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
					
					if(isCancelled() && createTestWithTestSteps.RespMessage.split("~")[0].toLowerCase().contains("failure"))
					{
						ImportTestCases.lblSuccessMessage.setText("Failed to import all the test cases");
						ImportTestCases.lblSuccessMessage.setForeground(Color.RED);
					}
					else if(isCancelled())
					{
						ImportTestCases.lblSuccessMessage.setText("Test Case import has been interuppted");
						ImportTestCases.lblSuccessMessage.setForeground(Color.RED);
					}
					else
					{
						//ImportTestCases.lblSuccessMessage.setForeground(new Color(0, 128, 0));
						ImportTestCases.lblSuccessMessage.setText(createTestWithTestSteps.RespMessage.split("~")[1]);
					}
					
				}
				
			};
			bgWorker.addPropertyChangeListener(new PropertyChangeListener() {
				
				@Override
				public void propertyChange(PropertyChangeEvent evt) {

					//System.out.println(evt);
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
			e.printStackTrace();
		}
		
	}


	protected String fnImportExcelRowData(Integer TotalRowCount, Integer counter) throws Exception {
		
		String retMessage = "";
		try {
			String ApplicationLabel, testSummary, testDescription, testStepDescription, testStepData, testStepExpectedResult;
			//String testId = "";			
			
			ApplicationLabel = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, counter, JiraExcelMap.get("Labels"));	
			testSummary = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, counter, JiraExcelMap.get("Summary"));		
			testDescription = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, counter, JiraExcelMap.get("Description"));
			
			testStepDescription = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, counter, JiraExcelMap.get("Step"));
			testStepData = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, counter, JiraExcelMap.get("Data"));
			testStepExpectedResult = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, counter, JiraExcelMap.get("Result"));
			
			if(testSummary != null)
			{
				//get test case completion count
//				if(startCount)
//					Globalvars.TotalTestCaseUploaded++;
				
				testId = createTestWithTestSteps.createTestCaseinJira(testSummary, testDescription, ApplicationLabel);
				//check if there is failure
				if(createTestWithTestSteps.RespMessage.split("~")[0].toLowerCase().contains("failure"))
				{
					retMessage+= createTestWithTestSteps.RespMessage.split("~")[1];
					return retMessage;
				}
				retMessage = "Created Test Case : '" + testSummary + "'\n";

//				if(!startCount)
//					startCount = true;
			}
//			//get test case completion count
//			else
//			{
//				if(counter == TotalRowCount)
//				{
//					Globalvars.TotalTestCaseUploaded++;
//				}
//				else
//				{
//					ApplicationLabel = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, counter + 1, JiraExcelMap.get("Labels"));
//					if(ApplicationLabel != null)
//						Globalvars.TotalTestCaseUploaded++;
//				}
//				
//			}

			
			createTestWithTestSteps.createTestStepinJira(testStepDescription, testStepData, testStepExpectedResult, testId);
			//check if there is failure
			if(createTestWithTestSteps.RespMessage.split("~")[0].toLowerCase().contains("failure"))
			{
				retMessage+= createTestWithTestSteps.RespMessage.split("~")[1];
				return retMessage;
			}
			retMessage += "Adding Steps for the test case";
			//get test case completion count
			if(counter.equals(TotalRowCount))
			{
				Globalvars.TotalTestCaseUploaded++;
			}
			else
			{
				ApplicationLabel = ExcelFunctions.fn_GetCellData(Globalvars.ExcelSheetPath, Globalvars.ExcelWorkSheetName, counter + 1, JiraExcelMap.get("Labels"));
				if(ApplicationLabel != null)
					Globalvars.TotalTestCaseUploaded++;
			}
			testSummary = testDescription = testStepDescription = testStepData = testStepExpectedResult = null;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retMessage;
		
	}


	public void fnCancelImport() {

		try {
			
			bgWorker.cancel(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}

}
