package com.cigniti;

import static com.cigniti.util.Globalvars.fSeperator;
import static com.cigniti.util.Globalvars.fSplitSeperator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.cigniti.JIRA.CreateTestWithTestSteps;

public class ALMGetTestCase {

	private RestConnector con;
	private Authentication login;
	Map<String, String> requestHeaders;

	public ALMGetTestCase(final String serverUrl, final String domain, final String project, String username,
			String password) throws Exception {

		con = RestConnector.getInstance().init(new HashMap<String, String>(), serverUrl, domain, project);

		requestHeaders = new HashMap<String, String>();
		requestHeaders.put("Content-Type", "application/xml");
		requestHeaders.put("Accept", "application/json");

		// Use the login example code to login for this test.
		// Go over this code to learn how to authenticate/login/logout
		login = new Authentication();

		boolean loginState = login.login(username, password);
//		System.out.println("Login State" + loginState + " : userName " + username);
		if (loginState)
			System.out.println("ALM authentication successfull");
		String qcsessionurl = con.buildUrl("rest/site-session");
		//System.out.println(qcsessionurl);
		try {
			Response resp = con.httpPost(qcsessionurl, null, requestHeaders);
			con.updateCookies(resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String confirmFolderId(String[] pathFolders, int curIndex, String parentId, String folderType)
			throws Exception {

		if (curIndex == -1) {
			return "success";
		} else {
			Response serverResponse;
			Map<String, Integer> requiredFields = new HashMap<String, Integer>();
			String testFoldersUrl = con.buildEntityCollectionUrl(folderType);
			StringBuilder testFolderQuery = new StringBuilder();
			testFolderQuery.append("query={id[" + parentId + "]}");
			testFolderQuery.append("&fields=id,parent-id,name");
			serverResponse = con.httpGet(testFoldersUrl, testFolderQuery.toString(), requestHeaders);
			String testFolderResponse = serverResponse.toString();
			requiredFields.put("id", 1);
			requiredFields.put("parent-id", 1);
			requiredFields.put("name", 1);
			List<Map<String, List<Map<String, String>>>> labFolderData = getDataFromResponse(testFolderResponse,
					requiredFields);

			if (labFolderData.isEmpty()) {
				System.out.println("Given ALM test lab folder path is invalid.");
				return "fail";
			}

			else {
				// String
				// parentFolderId=labFolderData.get(0).get("id").get(0).get("value");
				String parentFolderName = labFolderData.get(0).get("name").get(0).get("value");
				String parentFolderParent = labFolderData.get(0).get("parent-id").get(0).get("value");
				if (pathFolders[curIndex].equalsIgnoreCase(parentFolderName)) {
					return confirmFolderId(pathFolders, curIndex - 1, parentFolderParent, folderType);

				}

				else
					return "fail";
			}
		}

	}

	public String getFolderId(String testFolderPath, String folderType) throws Exception {

		Response serverResponse;

		String[] folderPath = testFolderPath.split(fSplitSeperator);

		String labFolderName = folderPath[folderPath.length - 1];
		labFolderName = labFolderName.replaceAll(" ", "%20");
		Map<String, Integer> requiredFields = new HashMap<String, Integer>();
		String testLabFoldersUrl = con.buildEntityCollectionUrl(folderType);

		StringBuilder testLabFolderQuery = new StringBuilder();
		testLabFolderQuery.append("query={name['" + labFolderName + "']}");
		testLabFolderQuery.append("&fields=id,parent-id,name");
		requestHeaders.put("Accept", "application/json");
		serverResponse = con.httpGet(testLabFoldersUrl, testLabFolderQuery.toString(), requestHeaders);
		String testLabFolderResponse = serverResponse.toString();

		requiredFields.put("id", 1);
		requiredFields.put("parent-id", 1);
		requiredFields.put("name", 1);
		List<Map<String, List<Map<String, String>>>> testLabFolderData = getDataFromResponse(testLabFolderResponse,
				requiredFields);

		if (testLabFolderData.isEmpty()) {

			String testSetID = getFolderId(testFolderPath, "test-set");
			if (testSetID == null) {
				System.out.println("Invalid folder path in ALM test lab");
				return null;
			} else {
				return testSetID;
			}

		}

		// if(testLabFolderData.size()>1){
		for (Map<String, List<Map<String, String>>> eachTestLabFolder : testLabFolderData) {

			// String
			// eachTestFolderName=eachTestFolder.get("name").get(0).get("value");
			String testFolderId = eachTestLabFolder.get("id").get(0).get("value");
			String testFolderParentId = eachTestLabFolder.get("parent-id").get(0).get("value");
			// String
			// testFolderName=eachTestLabFolder.get("name").get(0).get("value");
			if (folderType.equalsIgnoreCase("test-set")) {
				folderType = "test-set-folder";
			}

			String folderIdResult = confirmFolderId(folderPath, folderPath.length - 2, testFolderParentId, folderType);
			if (folderIdResult.equalsIgnoreCase("success"))
				return testFolderId;
		}

		// }

		// else
		// {
		// String
		// baseFolderId=testLabFolderData.get(0).get("id").get(0).get("value");
		// return baseFolderId;
		// }

		return null;
	}

	public void localTestCasesCleanUp(String oldTestCasesParentPath, String oldTestCasesFolderName) throws Exception {

		File testFolderToBackup = new File(oldTestCasesParentPath + fSeperator + oldTestCasesFolderName);
		File testCaseBackupFolder = new File(oldTestCasesParentPath + fSeperator + "TestCaseBackup");
		if (!testCaseBackupFolder.exists()) {
			testCaseBackupFolder.mkdirs();
		}
		String currentBackupDir = testCaseBackupFolder + fSeperator + oldTestCasesFolderName + "_Old_";
		File backupFile = new File(currentBackupDir);
		testFolderToBackup.renameTo(backupFile);
		// FileUtils.deleteDirectory(testFolderToDelete);
		return;
	}

	public void GetTestCasesInFolder(String folderId, String folderName) throws Exception {

		Response serverResponse;
		Map<String, Integer> requiredFields = new HashMap<String, Integer>();

		
		CreateTestWithTestSteps createTestWithTestSteps = new CreateTestWithTestSteps();
		String jiraCycleId = createTestWithTestSteps.createCycleInJIRA(folderName, "Migrated from ALM");
		
		String testCaseUrl = con.buildEntityCollectionUrl("test");
		StringBuilder getTestsInFolder = new StringBuilder();
		requestHeaders.put("Accept", "application/json");
		// Get tests based on the parent id
		getTestsInFolder.append("query={parent-id[" + folderId + "]}");
		getTestsInFolder.append("&fields=id,name,attachment,description"); // ,comment
		serverResponse = con.httpGet(testCaseUrl, getTestsInFolder.toString(), requestHeaders);
		String testNamesInFolderResponse = serverResponse.toString();
		requiredFields.put("id", 1);
		requiredFields.put("name", 1);
		requiredFields.put("attachment", 1);
		requiredFields.put("description", 1);
		// requiredFields.put("comment", 1);
		List<Map<String, List<Map<String, String>>>> testCasesInFolderData = getDataFromResponse(
				testNamesInFolderResponse, requiredFields);

		if (!testCasesInFolderData.isEmpty()) {

			for (Map<String, List<Map<String, String>>> eachTestCaseData : testCasesInFolderData) {

				String testCaseName = eachTestCaseData.get("name").get(0).get("value");
				String testCaseId = eachTestCaseData.get("id").get(0).get("value");
				String attachmentPresent = eachTestCaseData.get("attachment").get(0).get("value");
				if (attachmentPresent == null)
				{
					attachmentPresent = "";
				}
				// String comments=eachTestCaseData.get("comment").get(0).get("value");
				String description = eachTestCaseData.get("description").get(0).get("value");
				if (description == null)
				{
					description = " No Description ";
				}
				
				
				System.out.println("Test Case Name : " + testCaseName);
				System.out.println("Test Case Id : " + testCaseId);
				System.out.println("Description : " + description);
				System.out.println("Attachment Present at Test Case Level : " + attachmentPresent);
				

				String jiraTestCaseId = createTestWithTestSteps.createTestCaseinJira(testCaseName, description,"");
				createTestWithTestSteps.addTestToCycle(jiraTestCaseId, jiraCycleId);
				
				// System.out.println("comments : " + comments);
/*				if (attachmentPresent == null)
					attachmentPresent = "N";
*/				this.getTestDesignSteps(testCaseId, testCaseName, attachmentPresent, jiraTestCaseId );
			}
		}

		String testFoldersUrl = con.buildEntityCollectionUrl("test-folder");
		StringBuilder getChildFoldersQuery = new StringBuilder();
		getChildFoldersQuery.append("query={parent-id[" + folderId + "]}");
		getChildFoldersQuery.append("&fields=id,name");
		serverResponse = con.httpGet(testFoldersUrl, getChildFoldersQuery.toString(), requestHeaders);
		if (serverResponse.getStatusCode() == 406) {
			return;
		}
		String childFoldersResponse = serverResponse.toString();
		requiredFields.put("id", 1);
		requiredFields.put("name", 1);
		List<Map<String, List<Map<String, String>>>> childFoldersData = getDataFromResponse(childFoldersResponse,
				requiredFields);
		if (childFoldersData.isEmpty()) {
			return;
		} else {
			for (Map<String, List<Map<String, String>>> eachChildFolderData : childFoldersData) {

				String childFolderName = eachChildFolderData.get("name").get(0).get("value");
				String childFolderId = eachChildFolderData.get("id").get(0).get("value");
				this.GetTestCasesInFolder(childFolderId, childFolderName);
			}
		}
		return;
	}

	public Map<String, String> getTestDetails(String testId) throws Exception {

		Response serverResponse;
		Map<String, Integer> requiredFields = new HashMap<String, Integer>();

		Map<String, String> returnTestCaseData = new HashMap<String, String>();
		requestHeaders.put("Accept", "application/json");
		String testCaseUrl = con.buildEntityCollectionUrl("test") + "//" + testId;
		serverResponse = con.httpGet(testCaseUrl, null, requestHeaders);
		String testcaseResponse = serverResponse.toString();
		requiredFields.put("id", 1);
		requiredFields.put("name", 1);
		requiredFields.put("attachment", 1);
		requiredFields.put("description", 1);

		List<Map<String, List<Map<String, String>>>> testcaseData = getDataFromResponse(testcaseResponse,
				requiredFields);

		String testCaseName = testcaseData.get(0).get("name").get(0).get("value");
		String attachmentPresent = testcaseData.get(0).get("attachment").get(0).get("value");
		String testCaseDescription = testcaseData.get(0).get("description").get(0).get("value");
		if (attachmentPresent == null)
			attachmentPresent = "N";
		returnTestCaseData.put("testCaseName", testCaseName);
		returnTestCaseData.put("attachmentPresent", attachmentPresent);
		returnTestCaseData.put("testCaseDescription", testCaseDescription);

		return returnTestCaseData;
	}

	public void GetTestCasesInTestSet(String testSetId, String testSetName, String localFolderPath
			, String jiraCycleId) throws Exception {

		Response serverResponse;
		Map<String, Integer> requiredFields = new HashMap<String, Integer>();

		// KeywordsUtility.createLocalDirectory(testSetName, localFolderPath);
		CreateTestWithTestSteps createTestWithTestSteps = new CreateTestWithTestSteps();

		String testSetUrl = con.buildEntityCollectionUrl("test-instance");
		StringBuilder testCasesInTestSet = new StringBuilder();
		requestHeaders.put("Accept", "application/json");
		// Get tests based on the parent id
		testCasesInTestSet.append("query={cycle-id[" + testSetId + "]}");
		testCasesInTestSet.append("&fields=id,test-id");
		serverResponse = con.httpGet(testSetUrl, testCasesInTestSet.toString(), requestHeaders);
		String testNamesInFolderResponse = serverResponse.toString();
		requiredFields.put("id", 1);
		requiredFields.put("test-id", 1);

		List<Map<String, List<Map<String, String>>>> testcasesIntestSetData = getDataFromResponse(
				testNamesInFolderResponse, requiredFields);

		if (!testcasesIntestSetData.isEmpty()) {

			for (Map<String, List<Map<String, String>>> eachTestCaseData : testcasesIntestSetData) {

				String testId = eachTestCaseData.get("test-id").get(0).get("value");
				Map<String, String> testcaseDetails = getTestDetails(testId);

				String testCaseName = testcaseDetails.get("testCaseName");
				String attachmentPresent = testcaseDetails.get("attachmentPresent");
				String testCaseDescription = testcaseDetails.get("testCaseDescription");
				

				String jiraTestCaseId = createTestWithTestSteps.createTestCaseinJira(testCaseName, testCaseDescription,"");
				createTestWithTestSteps.addTestToCycle(jiraTestCaseId, jiraCycleId);

				getTestDesignSteps(testId, testCaseName, attachmentPresent, jiraTestCaseId);

			}
		}

		return;
	}

	public void GetTestSetsInLabFolder(String folderId, String folderName, String localFolderPath, boolean isParent)
			throws Exception {

		Response serverResponse;
		Map<String, Integer> requiredFields = new HashMap<String, Integer>();
		String testSetFolderPath = null;

		String testSetUrl = con.buildEntityCollectionUrl("test-set");
		StringBuilder testSetsInFolder = new StringBuilder();

		if (isParent) {
			// Get tests based on the parent id
			testSetsInFolder.append("query={parent-id[" + folderId + "]}");
		} else {
			// Get tests based on the parent id
			testSetsInFolder.append("query={id[" + folderId + "]}");
		}

		testSetsInFolder.append("&fields=id,name");
		serverResponse = con.httpGet(testSetUrl, testSetsInFolder.toString(), requestHeaders);
		String testNamesInFolderResponse = serverResponse.toString();
		requiredFields.put("id", 1);
		requiredFields.put("name", 1);

		List<Map<String, List<Map<String, String>>>> testSetsInLabFolderData = getDataFromResponse(
				testNamesInFolderResponse, requiredFields);

		if (!testSetsInLabFolderData.isEmpty()) {
			if (isParent) {
				// KeywordsUtility.createLocalDirectory(folderName,
				// localFolderPath);
			}
			for (Map<String, List<Map<String, String>>> eachTestSetData : testSetsInLabFolderData) {

				String testSetName = eachTestSetData.get("name").get(0).get("value");
				String testSetId = eachTestSetData.get("id").get(0).get("value");
				if (isParent) {
					testSetFolderPath = localFolderPath + fSeperator + folderName;
				} else {
					testSetFolderPath = localFolderPath;
				}
				CreateTestWithTestSteps createTestWithTestSteps = new CreateTestWithTestSteps();
				String jiraCycleId = createTestWithTestSteps.createCycleInJIRA(testSetName, "Migrated from ALM");
				GetTestCasesInTestSet(testSetId, testSetName, testSetFolderPath, jiraCycleId);

			}
		} else {
			GetTestSetsInLabFolder(folderId, folderName, localFolderPath, false);
		}

		return;
	}

	@SuppressWarnings("unchecked")
	public Map<String, List<Map<String, String>>> getFieldsData(JSONArray fieldsArray,
			Map<String, Integer> requiredFields) throws Exception {

		Map<String, List<Map<String, String>>> returnData = new HashMap<String, List<Map<String, String>>>();
		for (int i = 0; i < fieldsArray.size(); i++) {
			JSONObject eachFieldData = (JSONObject) fieldsArray.get(i);
			String fieldName = (String) eachFieldData.get("Name");

			if (!requiredFields.isEmpty()) {
				if (requiredFields.containsKey(fieldName)) {
					List<Map<String, String>> fieldValues = new ArrayList<Map<String, String>>();
					fieldValues = (List<Map<String, String>>) eachFieldData.get("values");
					returnData.put(fieldName, fieldValues);
				}
			} else {
				List<Map<String, String>> fieldValues = new ArrayList<Map<String, String>>();
				fieldValues = (List<Map<String, String>>) eachFieldData.get("values");
				returnData.put(fieldName, fieldValues);
			}
		}
		return returnData;

	}

	public List<Map<String, List<Map<String, String>>>> getDataFromResponse(String responseString,
			Map<String, Integer> requiredFields) throws Exception {

		List<Map<String, List<Map<String, String>>>> jsonRespFieldsData = new ArrayList<Map<String, List<Map<String, String>>>>();

		JSONObject testObject = new JSONObject();
		JSONParser responseParser = new JSONParser();

		testObject = (JSONObject) responseParser.parse(responseString);

		if (testObject.containsKey("entities")) {
			JSONArray allFields = (JSONArray) testObject.get("entities");
			for (int i = 0; i < allFields.size(); i++) {
				JSONObject eachFieldData = (JSONObject) allFields.get(i);
				jsonRespFieldsData.add(getFieldsData((JSONArray) eachFieldData.get("Fields"), requiredFields));
			}
		} else {
			jsonRespFieldsData.add(getFieldsData((JSONArray) testObject.get("Fields"), requiredFields));
		}
		return jsonRespFieldsData;
	}

	public void getTestDesignSteps(String testCaseID, String testCaseName, 
			String attachmentPresent, String jiraTestCaseId) throws Exception { 

		CreateTestWithTestSteps createTestWithTestSteps = new CreateTestWithTestSteps();

		Response serverResponse;
		Map<String, Integer> requiredFields = new HashMap<String, Integer>();

		List<Map<String, List<Map<String, String>>>> testDesignStepsData;

		String designStepsUrl = con.buildEntityCollectionUrl("design-step");
		StringBuilder designStepsQuery = new StringBuilder();
		designStepsQuery.append("query={parent-id[" + testCaseID + "]}");
		designStepsQuery.append("&fields=id,name,description,attachment,expected"); // ,user-01,user-02,user-03

		requestHeaders.put("Content-Type", "application/xml");
		requestHeaders.put("Accept", "application/json");
		System.out.println(con.serverUrl + designStepsUrl + designStepsQuery.toString());

		serverResponse = con.httpGet(designStepsUrl, designStepsQuery.toString(), requestHeaders);
		String testStepsResponse = serverResponse.toString();
		requiredFields.put("id", 1);
		requiredFields.put("name", 1);
		requiredFields.put("description", 1);
		requiredFields.put("attachment", 1);
		requiredFields.put("expected", 1);
		/*
		 * requiredFields.put("user-01",1); requiredFields.put("user-03",1);
		 * requiredFields.put("user-02",1);
		 */ 
		testDesignStepsData = this.getDataFromResponse(testStepsResponse, requiredFields);

		for (Map<String, List<Map<String, String>>> allTestSteps : testDesignStepsData) {
//			JSONArray idMap = (JSONArray)allTestSteps.get("id");
//			JSONArray nameMap = (JSONArray)allTestSteps.get("name");
			JSONArray desciptionMap =  (JSONArray) allTestSteps.get("description");
			JSONArray expectedMap =  (JSONArray) allTestSteps.get("expected");
//			JSONArray attachmentMap =  (JSONArray) allTestSteps.get("attachment");
			
//			String testStepid = getTestStepDetails(idMap);
//			String testStepName = getTestStepDetails(nameMap);
			String testStepDescription = getTestStepDetails(desciptionMap);
			String testStepExpected = getTestStepDetails(expectedMap);
//			String testStepAttachment = getTestStepDetails(attachmentMap);
			
/*			System.out.println("Test Step id " + testStepid);
			System.out.println("Test Step Name " + testStepName);
			System.out.println("Test Step Description " + testStepDescription);
			System.out.println("Test Step Expected Value" + testStepExpected);
			System.out.println("Test Step Attachment Present? " + testStepAttachment);
*/			createTestWithTestSteps.createTestStepinJira(testStepDescription, "", testStepExpected, jiraTestCaseId);
//			if ("Y".equalsIgnoreCase(testStepAttachment)) {
//			}
		}

		createTestWithTestSteps.uploadAttachmentsToJIRA(testCaseID, jiraTestCaseId);
		/*
		 * 
		 * 		UploadAttachments uploadAttachments = new UploadAttachments();
		uploadAttachments.getAttachmentsFromALM(testCaseID, jiraTestCaseId);

		 * 
		 * for(Map<String,List<Map<String,String>>>
		 * eachTestCaseData:testCasesInFolderData){
		 * 
		 * String
		 * testCaseName=eachTestCaseData.get("name").get(0).get("value");
		 * String testCaseId=eachTestCaseData.get("id").get(0).get("value");
		 * String
		 * attachmentPresent=eachTestCaseData.get("attachment").get(0).get(
		 * "value"); //String
		 * comments=eachTestCaseData.get("comment").get(0).get("value");
		 * 
		 */


		/*
		JSONArray fieldObject = (JSONArray) eachFieldData.get("Fields");
		 for (int j = 0; j < fieldObject.size(); j++) {
			JSONObject fieldDataData = (JSONObject) fieldObject.get(j);

			JSONArray testStepData = (JSONArray) fieldDataData.get("values");
			String testStepFieldName = fieldDataData.get("Name").toString();
			for (int z = 0; z < testStepData.size(); z++) {
				JSONObject testStepDataObject = (JSONObject) testStepData.get(z);
				String testStepValue = "";
				if (testStepDataObject.get("value") != null)
					testStepValue = testStepDataObject.get("value").toString();
				if ("attachment".equals(testStepFieldName) && "Y".equals(testStepValue)) {
					System.out.println("Found attachment for test Step");
					//getAttachmentForTestStep("618809");
				}
				System.out.println(" Value : " + testStepValue);
			} 
		} 
		

		if (attachmentPresent.equalsIgnoreCase("Y")) {

			String testAttachUrl = con.buildEntityCollectionUrl("test") + "/" + testCaseID + "/attachments";
			requestHeaders.put("Content-Type", "application/xml");
			requestHeaders.put("Accept", "application/json");
			serverResponse = con.httpGet(testAttachUrl, null, requestHeaders);
			requiredFields.clear();
			requiredFields.put("id", 1);
			requiredFields.put("name", 1);
			List<Map<String, List<Map<String, String>>>> attachmentDetails = getDataFromResponse(
					serverResponse.toString(), requiredFields);

			if (attachmentDetails.size() == 0) {
				System.out.println("Failed to download the attachment for " + testCaseName);
				return;
			}

			String attachmentName = attachmentDetails.get(0).get("name").get(0).get("value");
			String attachmentId = attachmentDetails.get(0).get("id").get(0).get("value");
			testAttachUrl += "/" + attachmentId + "?by-id=true";
			requestHeaders.put("Accept", "application/octet-stream");
			serverResponse = con.httpGet(testAttachUrl, null, requestHeaders);

			byte[] attachmentData = serverResponse.getResponseData();

			String fileName = downloadsDirPath + attachmentName;
			System.out.println("Attachment File Name " + fileName);
			FileUtils.writeByteArrayToFile(new File(fileName), attachmentData);
			createTestWithTestSteps.addAttachmentToIssue(jiraTestCaseId, fileName);
		}*/

	}

	private String getTestStepDetails(JSONArray testStepFieldMap) {
		String testStepFieldValue = "";
		if ((testStepFieldMap!=null)&&(((JSONObject)(testStepFieldMap.get(0))) != null)
				&& ((JSONObject)(testStepFieldMap.get(0))).get("value") != null)
		{
			testStepFieldValue = ((JSONObject)(testStepFieldMap.get(0))).get("value").toString();
		}
		
	/*	Html2Text parser = new Html2Text();
	     parser.parse(in);
	     in.close();
	     System.out.println(parser.getText());
		*/
		return testStepFieldValue;
	}

/*	public void getAttachmentForTestStep(String testStepID, 
			String testStepName, String testCaseID) throws Exception {

		Response serverResponse;
		Map<String, Integer> requiredFields = new HashMap<String, Integer>();

		String testAttachUrl = con.buildEntityCollectionUrl("design-step") + "/" + testStepID + "/attachments";

		StringBuilder designStepsQuery = new StringBuilder();
		designStepsQuery.append("query={parent-id[" + testCaseID + "]}");
	//	designStepsQuery.append("&fields=id,name,description,attachment,expected"); // ,user-01,user-02,user-03

		requestHeaders.put("Content-Type", "application/xml");
		requestHeaders.put("Accept", "application/json");
		serverResponse = con.httpGet(testAttachUrl, designStepsQuery.toString(), requestHeaders);
		requiredFields.clear();
		requiredFields.put("id", 1);
		requiredFields.put("name", 1);
		System.out.println(serverResponse.toString());
		System.out.println(con.serverUrl + testAttachUrl + designStepsQuery.toString());
		List<Map<String, List<Map<String, String>>>> attachmentDetails = getDataFromResponse(serverResponse.toString(),
				requiredFields);

		for (int index = 0;index<attachmentDetails.size();index++)
		{
			String attachmentName = attachmentDetails.get(index).get("name").get(0).get("value");
			String attachmentId = attachmentDetails.get(index).get("id").get(0).get("value");
			testAttachUrl += "/" + attachmentId + "?by-id=true";
			requestHeaders.put("Accept", "application/octet-stream");
			serverResponse = con.httpGet(testAttachUrl, null, requestHeaders);

			byte[] attachmentData = serverResponse.getResponseData();

			String fileName = downloadsDirPath + testStepName + attachmentName;
			System.out.println("Attachment File Name " + fileName);
			FileUtils.writeByteArrayToFile(new File(fileName), attachmentData);
		}

	}
*/
	public void logout() {
		try {
			login.logout();
			System.out.println("Successfully logged out from ALM");

		} catch (Exception e) {

			System.out.println("Error logging out from ALM");
		}
	}

}
