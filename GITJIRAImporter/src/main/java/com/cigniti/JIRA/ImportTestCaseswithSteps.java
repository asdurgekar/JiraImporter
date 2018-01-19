package com.cigniti.JIRA;

import java.awt.Color;
import java.awt.event.ContainerEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.cigniti.util.Globalvars;

public class ImportTestCaseswithSteps {
	
	
	public static JSONArray JSONProjectList;
	public static HashMap<String, String> ProjectMap;
	public static HashMap<String, String> JiraExcelMap = new HashMap<String, String>();
	@SuppressWarnings("rawtypes")
	public static DefaultListModel JiralistModel;
	@SuppressWarnings("rawtypes")
	public static DefaultListModel ExcellistModel;
	@SuppressWarnings("rawtypes")
	public static DefaultListModel jDataModel;
	
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
	
	
	public void fn_LoginToJira() throws IOException {

		try
		{
			String strAuthenticationMessage = "";
			CreateTestWithTestSteps.userName = ImportTestCases.txtUserName.getText();
			CreateTestWithTestSteps.password = new String(ImportTestCases.txtPassword.getPassword());
			
			int AuthResponse = fn_PerformAuthentication();
			
			if(AuthResponse != 200)
			{
				System.out.println("JSON Response: " + AuthResponse + "Login Failure");
				strAuthenticationMessage = "Unable to Login. Please enter valid credentials";				
			}
			else
			{
				System.out.println("JSON Response: " + AuthResponse + "Login Success");
				strAuthenticationMessage = "";
				if(ImportTestCases.chckbxRememberMe.isSelected())
					fnStorePreferences("UserName",ImportTestCases.txtUserName.getText());
				
				fnLaunchLoadSecondPanel();
				
			}
			ImportTestCases.lblAuthmessage.setText(strAuthenticationMessage);
			
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
			if(!jfile.exists()) 
			{ 
				//If not, create the folder and file
				jfile.getParentFile().mkdirs();
				jfile.createNewFile();
				
			}
			
			//Check if user name property already exists, update new user name
			// Open the file
			FileInputStream fstream = new FileInputStream(Globalvars.strPreferencesPath);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			Boolean propFlag = false;
			while ((strLine = br.readLine()) != null)   
			{
			  if(strLine.contains(propName)) 
			  {
				  strLine = propName + "::" + propValue;
				  propFlag = true;
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
            		out.write(textline);
            }
                 
            out.flush();
            out.close();
			
            System.out.println("Preferences are stored in the system");
			
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
			
			fnLoadJiraFields();
			fnLoadExcelColumns();
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
			
	}



	@SuppressWarnings({ "unused", "unchecked","rawtypes" })
	private void fnLoadJiraFields() {

		try
		{
			
			
			//Load Jira columns in HashMap
			JiraExcelMap.put("Summary", "");
			JiraExcelMap.put("Labels", "");
			JiraExcelMap.put("Description", "");
			JiraExcelMap.put("Step", "");
			JiraExcelMap.put("Data", "");
			JiraExcelMap.put("Result", "");


			JiralistModel = new DefaultListModel();
			for(String listItem : JiraExcelMap.keySet())
				JiralistModel.addElement(listItem);
			//ImportTestCases.lstJiraFields.setListData(JiraExcelMap.keySet().toArray());
			ImportTestCases.lstJiraFields.setListData(JiralistModel.toArray());
			System.out.println("Jira field values are loaded");
			
			
			//DefaultListModel jDataModel = (DefaultListModel) ImportTestCases.lstJiraFields.getModel();
			//jDataModel.addListDataListener(new MyListDataListener()); 
			JiralistModel.addListDataListener(new MyListDataListener());
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
			Globalvars.ExcelWorkSheetName = ImportTestCases.lstWorkSheets.getSelectedValue().toString();
			
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
			
			ImportTestCases.lstExcelColumns.setListData(ExcellistModel.toArray());
			System.out.println("Excel Header values are loaded");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	private int fn_PerformAuthentication() throws IOException {

		
		CloseableHttpClient httpclient = HttpClients.createDefault();
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
				System.out.println(JSONProjectList.toString());
			}
        }
        catch(Exception e)
        {
        	e.printStackTrace();
		} finally {
			httpclient.close();
		} 
		
		return response.getStatusLine().getStatusCode();
		
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
							ImportTestCases.txtPassword.requestFocusInWindow();
							break;
						default:
							break;
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


	public void fnValidateExcelFormat() {
		
		try
		{
			//get total count of test cases
			
			
			//verify blank values for 
			
			
			
			
			
				
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
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
				//clear values from JLists
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


	@SuppressWarnings("unchecked")
	public void fnSortListModel(ContainerEvent arg0) {

//		Object[] ArrList = JiralistModel.toArray();
//		Arrays.sort(ArrList);
//		JiralistModel.clear();
//		for(Object item: ArrList)
//		{
//			JiralistModel.addElement(item);		
//		}
//		ImportTestCases.lstJiraFields.setListData(JiralistModel.toArray());
	}

}
