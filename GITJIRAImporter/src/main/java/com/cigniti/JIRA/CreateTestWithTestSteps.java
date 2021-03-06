/* D SOFTWARE INCORPORATED
 * Copyright 2007-2011 D Software Incorporated
 * All Rights Reserved.
 *
 * NOTICE: D Software permits you to use, modify, and distribute this 
file
 * in accordance with the terms of the license agreement accompanying 
it.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an “AS IS? BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
implied.
 */
/*
 * This is a sample of what can be done by using API's with Zephyr through 
the JAVA coding language.
 * By creating the .java files, you can import them 
into your workspace and then call them in your custom program. 
 * 
 * Eclipse Java EE IDE for Web Developers.
Version: Neon Release (4.6.0)
Build id: 20160613-1800
 * Java- Java JDK 1.8.0_101
 * 
 * Author: Swapna Kumar Vemula, Product Support Engineer, D Software Inc.
 */
package com.cigniti.JIRA;

import java.io.File;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cigniti.util.Globalvars;
import com.cigniti.util.Html2Text;
import com.cigniti.util.ReadAttachmentsMapping;
import com.cigniti.util.TextOutputFile;
import com.thed.zephyr.cloud.rest.ZFJCloudRestClient;
import com.thed.zephyr.cloud.rest.client.JwtGenerator;





@SuppressWarnings("deprecation")
public class CreateTestWithTestSteps {

	public static String API_CREATE_TEST = "{SERVER}/rest/api/2/issue";
	public static String API_CREATE_TEST_STEP = "{SERVER}/public/rest/api/1.0/teststep/";
	public static String API_CREATE_TEST_STEP_JIRASERVER = "{SERVER}/rest/zapi/latest/teststep/";
	public static Long versionId = -1l;
	public static String API_VALIDKEY = "{SERVER}/public/rest/api/1.0/teststep/-1?projectId=";
	public static String API_GET_ISSUE = "{SERVER}/rest/api/2/issue/";
	public static String API_LINK_ISSUE = "{SERVER}/rest/api/2/issueLink";
	public static String API_UPDATE_TEST = "{SERVER}/rest/api/2/issue/bulk";
	public static String API_CREATE_META = "{SERVER}/rest/api/2/issue/createmeta?projectIds=";
	
	/** Declare JIRA,Zephyr URL,access and secret Keys */
	// Jira Cloud URL for the instance
	public static String jiraBaseURL = "";
	// Replace zephyr baseurl <ZAPI_Cloud_URL> shared with the user for ZAPI
	// Cloud Installation
	public static String zephyrBaseUrl = "https://prod-api.zephyr4jiracloud.com/connect";
	// zephyr accessKey , we can get from Addons >> zapi section
	//public static String accessKey = "N2RjOTAxZWUtYTc2Mi0zMzkzLWEwYmYtZWIxNjM2ZmM3MjAxIGFrYXJzaC5kdXJnZWthciBVU0VSX0RFRkFVTFRfTkFNRQ";
	public static String accessKey = "";
	// zephyr secretKey , we can get from Addons >> zapi section
	//public static String secretKey = "xEWGV8W7J1eiu2glXJS_ChSUntc-hIS_f_39i5w_B3M";
	public static String secretKey = "";
	
	/** Declare parameter values here */
	
	public static String jiraURL = Globalvars.JIRA_URL;
	public static String userName = Globalvars.JIRA_userName;
	public static String password = Globalvars.JIRA_password;
	public static String projectId = Globalvars.JIRA_projectId;
	public static String issueTypeId = "";
	public static String sprintCustFieldId = "";
	
	public static String createTestUri = "";
	public static String createTestStepUri = "";
	public static String validateKeyUri = "";
	public static String getIssueUri = "";
	public static String issueLinkUri = "";
	public static String updateTestUri = "";
	
	static ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(zephyrBaseUrl, accessKey, secretKey, userName)
			.build();
	static Header header = createAuthorizationHeader();
	// JwtGenerator jwtGenerator = client.getJwtGenerator();
	public static String RespMessage = "";
	public static String SuccessMessage = "Success~All Test Cases are imported successfully";
	

	public static void main(String[] args) throws JSONException, URISyntaxException, ParseException, IOException {

		/**
		 * Create Test Parameters, declare Create Test Issue fields Declare more
		 * field objects if required
		 */
		
		//Not in use
		/*
		CreateTestWithTestSteps createTestWithTestSteps = new CreateTestWithTestSteps();
		
		
		String cycleName = "Test Cycle -- API DEMO";
		String cycleDescription = "Created by ZAPI CLOUD API";
		String ApplicationLabel = "Test Label"; // Test
		
		String testSummary = "Sample Test case With Steps created through ZAPI Cloud_10_06_2017_3"; // Test
		String testDescription = "Sample Test case Description With Steps created through ZAPI Cloud"; // Test

		String testStepDescription = "Sample Test Step1006_3";
		String testStepData = "Sample Test Data";
		String testStepExpectedResult = "Sample Expected Result";

		String testStepDescription1 = "Sample Test Step1006_3_1";
		String testStepData1 = "Sample Test Data1";
		String testStepExpectedResult1 = "Sample Expected Result1";

		
		String testId = createTestWithTestSteps.createTestCaseinJira(testSummary, testDescription, ApplicationLabel);

//		String cycleID = createTestWithTestSteps.createCycleInJIRA(cycleName, cycleDescription);
//
//		createTestWithTestSteps.addTestToCycle(testId, cycleID);

		createTestWithTestSteps.createTestStepinJira(testStepDescription, testStepData, testStepExpectedResult, testId);
		createTestWithTestSteps.createTestStepinJira(testStepDescription1, testStepData1, testStepExpectedResult1, testId);
		//addAttachment("10627", projectId);
	//	addAttachmentToIssue(testId, "C:\\Users\\dinran\\Downloads\\Zephyr1.png");
		//createTestWithTestSteps.addAttachmentToIssue(testId, "C:\\Users\\dinran\\Downloads\\pom.xml");
		 
		 */
	}

	@SuppressWarnings("resource")
	public void createTestStepinJira(String testStepDescription,
			String testStepData, String testStepExpectedResult, String testId) throws URISyntaxException, ParseException, IOException {
		
		testStepDescription = getTextFromHTML(testStepDescription);
		testStepData = getTextFromHTML(testStepData);
		testStepExpectedResult = getTextFromHTML(testStepExpectedResult);

		TextOutputFile.writeToLog("Test Step Description : " + testStepDescription);
		TextOutputFile.writeToLog("Test Step Data : " + testStepData);
		TextOutputFile.writeToLog("Test Step Expected Value : " + testStepExpectedResult);
		
		HttpEntity entity = null;
		/** Create test Steps ***/

		/**
		 * Create Steps Replace the step,data,result values as required
		 */

		JSONObject testStepJsonObj = createTestStepJSON(testStepDescription, testStepData, testStepExpectedResult);

		/** DONOT EDIT FROM HERE ***/

		StringEntity createTestStepJSON = null;
		try {
			createTestStepJSON = new StringEntity(testStepJsonObj.toString());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String finalURL = createTestStepUri + testId + "?projectId=" + projectId;
		URI uri = new URI(finalURL);
		int expirationInSec = 360;
		
		JwtGenerator jwtGenerator = client.getJwtGenerator();
		String jwt = jwtGenerator.generateJWT("POST", uri, expirationInSec);

		HttpResponse responseTestStep = null;
		HttpPost addTestStepReq = new HttpPost(uri);
		addTestStepReq.addHeader("Content-Type", "application/json");
		addTestStepReq.addHeader(HttpHeaders.AUTHORIZATION, jwt);
		addTestStepReq.addHeader("zapiAccessKey", accessKey);
		addTestStepReq.setEntity(createTestStepJSON);

		try {
			HttpClient restClient = new DefaultHttpClient();
			responseTestStep = restClient.execute(addTestStepReq);
		} catch (ClientProtocolException e) {
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		int statusCode = getHTTPResponseCode(responseTestStep);

		if (statusCode >= 200 && statusCode < 300) {
			entity = responseTestStep.getEntity();
			String string = null;
			try {
				string = EntityUtils.toString(entity);
				JSONObject testStepObj = new JSONObject(string);
				testStepObj.getString("id");
				RespMessage = SuccessMessage;
			} catch (ParseException e) {
				TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			} catch (IOException e) {
				TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			}

		} else {
			try {
				TextOutputFile.writeToLog("__________________________________________________________________________________________");
				TextOutputFile.writeToLog("Test Step Values : " + testStepDescription + ":" + testStepData +":" + testStepExpectedResult);
				TextOutputFile.writeToLog("__________________________________________________________________________________________");
				RespMessage = "Failure~" + EntityUtils.toString(responseTestStep.getEntity());
				throw new ClientProtocolException("Unexpected response status: " + statusCode);
			} catch (ClientProtocolException e) {
				TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			}
		}
		TextOutputFile.writeToLog("Created Test Step in JIRA " + testStepDescription );

	}
	
	@SuppressWarnings("resource")
	public void createTestStepinJiraServer(String testStepDescription,
			String testStepData, String testStepExpectedResult, String testId) throws URISyntaxException, ParseException, IOException {
		
		testStepDescription = getTextFromHTML(testStepDescription);
		testStepData = getTextFromHTML(testStepData);
		testStepExpectedResult = getTextFromHTML(testStepExpectedResult);

		TextOutputFile.writeToLog("Test Step Description : " + testStepDescription);
		TextOutputFile.writeToLog("Test Step Data : " + testStepData);
		TextOutputFile.writeToLog("Test Step Expected Value : " + testStepExpectedResult);
		
		HttpEntity entity = null;
		/** Create test Steps ***/

		/**
		 * Create Steps Replace the step,data,result values as required
		 */

		JSONObject testStepJsonObj = createTestStepJSON(testStepDescription, testStepData, testStepExpectedResult);

		/** DONOT EDIT FROM HERE ***/

		StringEntity createTestStepJSON = null;
		try {
			createTestStepJSON = new StringEntity(testStepJsonObj.toString());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String finalURL = createTestStepUri + testId;
		URI uri = new URI(finalURL);
		int expirationInSec = 360;
		
		HttpResponse responseTestStep = null;
		HttpPost addTestStepReq = new HttpPost(uri);
		addTestStepReq.addHeader("Content-Type", "application/json");
		addTestStepReq.addHeader(header);
		addTestStepReq.setEntity(createTestStepJSON);

		try {
			HttpClient restClient = new DefaultHttpClient();
			responseTestStep = restClient.execute(addTestStepReq);
		} catch (ClientProtocolException e) {
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		int statusCode = getHTTPResponseCode(responseTestStep);

		if (statusCode >= 200 && statusCode < 300) {
			entity = responseTestStep.getEntity();
			String string = null;
			try {
				string = EntityUtils.toString(entity);
				TextOutputFile.writeToLog(string);								
				RespMessage = SuccessMessage;
			} catch (ParseException e) {
				TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			} catch (IOException e) {
				TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			}

		} else {
			try {
				TextOutputFile.writeToLog("__________________________________________________________________________________________");
				TextOutputFile.writeToLog("Test Step Values : " + testStepDescription + ":" + testStepData +":" + testStepExpectedResult);
				TextOutputFile.writeToLog("__________________________________________________________________________________________");
				RespMessage = "Failure~" + EntityUtils.toString(responseTestStep.getEntity());
				throw new ClientProtocolException("Unexpected response status: " + statusCode);
			} catch (ClientProtocolException e) {
				TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			}
		}
		TextOutputFile.writeToLog("Created Test Step in JIRA " + testStepDescription );

	}

	public String createTestCaseinJira(String testSummary, String testDescription,
			String ApplicationLabel, String sprint, Boolean... getIssueKey) throws IOException {
		
		String str_TestCaseReturn = null;
		Boolean bln_SendIDAndKey = false;
		if (getIssueKey.length > 0) {
			// int_SearchResultsRow = int_SearchResults.length > 0 ?
			// int_SearchResults[0] : 0;
			if(getIssueKey[0] == true)
			{
				bln_SendIDAndKey = true;
			}
			
		}
		testDescription = getTextFromHTML(testDescription);
		testSummary = getTextFromHTML(testSummary);

		
		StringEntity createTestJSON = createTestCaseEntity(testSummary, testDescription, ApplicationLabel, sprint);
		HttpResponse response = executeCreateTestCase(createTestUri, header, createTestJSON);
		int statusCode = getHTTPResponseCode(response);
		String testId = null;
		String testKey = null;
		HttpEntity entity = response.getEntity();
		if (statusCode >= 200 && statusCode < 300) {
			
			
			//if you are retrieving only Id after creation
			if(!bln_SendIDAndKey)
			{
				str_TestCaseReturn = getTestCaseId(entity);
			}
			//if you are retrieving Id & Key after creation
			else
			{
				str_TestCaseReturn = getTestCaseIDKey(entity);
			}
			
			RespMessage = SuccessMessage;
			TextOutputFile.writeToLog("Created Test Case in JIRA : " + str_TestCaseReturn + " : " + testSummary );

		} else {
			try {
				String string = null;
				
				string = EntityUtils.toString(entity);
				RespMessage = "Failure~" + string;
				new JSONObject(string);
				throw new ClientProtocolException("Unexpected response status: " + statusCode);

			} catch (ClientProtocolException e) {
				TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			}
		}
		return str_TestCaseReturn;
	}

	public String createCycleInJIRA(String cycleName, String cycleDescription) throws JSONException, URISyntaxException {
		String createCycleUri = zephyrBaseUrl + "/public/rest/api/1.0/cycle?expand=&clonedCycleId=";
		/** Cycle Object created - DO NOT EDIT **/
		JSONObject createCycleObj = new JSONObject();
		createCycleObj.put("name", cycleName);        			
		createCycleObj.put("description", cycleDescription);			
		createCycleObj.put("startDate", System.currentTimeMillis());
		createCycleObj.put("projectId", projectId);
		createCycleObj.put("versionId", versionId);

		StringEntity cycleJSON = null;
		try {
			cycleJSON = new StringEntity(createCycleObj.toString());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String cycleID = createCycle(createCycleUri, client, accessKey, cycleJSON);

		TextOutputFile.writeToLog("Created Test Cycle in JIRA " + cycleName );

		/**
		 * Add tests to Cycle IssueId's
		 * 
		 */
		return cycleID;
	}

	public void addTestToCycle(String issueID, String cycleID)
			throws URISyntaxException, IOException {
		String addTestsUri = zephyrBaseUrl + "/public/rest/api/1.0/executions/add/cycle/" + cycleID;
		String[] issueIds = { issueID }; //Issue Id's to be added to Test Cycle, add more issueKeys separated by comma

		JSONObject addTestsObj = new JSONObject();
		addTestsObj.put("issues", issueIds);
		addTestsObj.put("method", "1");
		addTestsObj.put("projectId", projectId);
		addTestsObj.put("versionId", versionId);

		StringEntity addTestsJSON = null;
		try {
			addTestsJSON = new StringEntity(addTestsObj.toString());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		addTestsToCycle(addTestsUri, client, accessKey, addTestsJSON);
		TextOutputFile.writeToLog("Tests added successfully to Test Cycle ");
	}
	
	@SuppressWarnings("resource")
	public static String addTestsToCycle(String uriStr, ZFJCloudRestClient client, String accessKey, StringEntity addTestsJSON)
			throws URISyntaxException, JSONException, IllegalStateException, IOException {

		URI uri = new URI(uriStr);
		int expirationInSec = 360;
		JwtGenerator jwtGenerator = client.getJwtGenerator();
		String jwt = jwtGenerator.generateJWT("POST", uri, expirationInSec);

		HttpResponse response = null;
		HttpClient restClient = new DefaultHttpClient();

		HttpPost addTestsReq = new HttpPost(uri);
		addTestsReq.addHeader("Content-Type", "application/json");
		addTestsReq.addHeader("Authorization", jwt);
		addTestsReq.addHeader("zapiAccessKey", accessKey);
		addTestsReq.setEntity(addTestsJSON);

		try {
			response = restClient.execute(addTestsReq);
		} catch (ClientProtocolException e) {
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}

		int statusCode = response.getStatusLine().getStatusCode();
		String string = null;
		if (statusCode >= 200 && statusCode < 300) {
			HttpEntity entity = response.getEntity();			
			try {
				string = EntityUtils.toString(entity);
				new JSONObject(entity);
			} catch (ParseException e) {
				TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			} catch (IOException e) {
				TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			}

		} else {
			try {
				throw new ClientProtocolException("Unexpected response status: " + statusCode);
			} catch (ClientProtocolException e) {
				TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			}
		}
		return string;
	}
	
	public void uploadAttachmentsToJIRA(String almTestCaseID, String jiraTestCaseId)
	{
		ReadAttachmentsMapping mapping = ReadAttachmentsMapping.getInstance();
		List<String> list = (List<String>)mapping.getAttachmentsForTestCase(almTestCaseID);
		int listCount = list.size();
		for (int index=0;index<listCount;index++)
		{
			String fileName = list.get(index);
			try {
				TextOutputFile.writeToLog(":"+fileName+":");
				if (!"".equals(fileName))
				{
					addAttachmentToIssue(jiraTestCaseId, fileName);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			}
		}
	}

	@SuppressWarnings("resource")
	public static String createCycle(String uriStr, ZFJCloudRestClient client, String accessKey, StringEntity cycleJSON)
			throws URISyntaxException, JSONException {

		URI uri = new URI(uriStr);
		int expirationInSec = 360;
		JwtGenerator jwtGenerator = client.getJwtGenerator();
		String jwt = jwtGenerator.generateJWT("POST", uri, expirationInSec);

		HttpResponse response = null;
		HttpClient restClient = new DefaultHttpClient();

		HttpPost createCycleReq = new HttpPost(uri);
		createCycleReq.addHeader("Content-Type", "application/json");
		createCycleReq.addHeader("Authorization", jwt);
		createCycleReq.addHeader("zapiAccessKey", accessKey);
		createCycleReq.setEntity(cycleJSON);

		try {
			response = restClient.execute(createCycleReq);
		} catch (ClientProtocolException e) {
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}

		int statusCode = response.getStatusLine().getStatusCode();
		String cycleId = "-1";
		if (statusCode >= 200 && statusCode < 300) {
			HttpEntity entity = response.getEntity();
			String string = null;
			try {
				string = EntityUtils.toString(entity);
				JSONObject cycleObj = new JSONObject(string);
				cycleId = cycleObj.getString("id");
			} catch (ParseException e) {
				TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			} catch (IOException e) {
				TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			}

		} else {
			try {
				throw new ClientProtocolException("Unexpected response status: " + statusCode);
			} catch (ClientProtocolException e) {
				TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			}
		}
		return cycleId;
	}
	
	public String getIssueDetails(String IssueId) throws IOException {
		
		HttpResponse response = null;
		try {
			String finalURL = getIssueUri + IssueId;
			HttpGet getIssueReq = new HttpGet(finalURL);
			getIssueReq.addHeader(header);
			getIssueReq.addHeader("Content-Type", "application/json");			
			@SuppressWarnings("resource")
			HttpClient restClient = new DefaultHttpClient();
			response = restClient.execute(getIssueReq);
		} catch (ClientProtocolException e) {
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		int statusCode = getHTTPResponseCode(response);
		String testId = null;
		HttpEntity entity = response.getEntity();
		if (statusCode >= 200 && statusCode < 300) {
			String string = null;
			try {
				string = EntityUtils.toString(entity);
			} catch (ParseException e) {
				TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			} catch (IOException e) {
				TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			}
			TextOutputFile.writeToLog("Details retrieved for issue " + IssueId );

		} else {
			try {
				String string = null;
				
				string = EntityUtils.toString(entity);
				RespMessage = "Failure~" + string;
				new JSONObject(string);
				throw new ClientProtocolException("Unexpected response status: " + statusCode);

			} catch (ClientProtocolException e) {
				TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			}
		}
		
		
		return testId;
	}
	
	
	public String linkIssue(String InwardissueId, String issueKey, String relation) throws IOException {
		
		
		StringEntity createTestJSON = createIssueLinkJSON(InwardissueId, issueKey, relation);
		HttpResponse response = executeCreateTestCase(issueLinkUri, header, createTestJSON);
		int statusCode = getHTTPResponseCode(response);
		String linkId = null;
		HttpEntity entity = response.getEntity();
		if (statusCode >= 200 && statusCode < 300) {			
			TextOutputFile.writeToLog("Issue Id " + InwardissueId + " is linked to issue key " + issueKey );
			RespMessage = SuccessMessage;
		} else {
			try {
				String string = null;
				
				string = EntityUtils.toString(entity);
				RespMessage = "Failure~" + string;
				new JSONObject(string);
				throw new ClientProtocolException("Unexpected response status: " + statusCode);

			} catch (ClientProtocolException e) {
				TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			}
		}
		return linkId;
	}

	
	private static JSONObject createTestStepJSON(String testStepDescription,
			String testStepData, 
			String testStepExpectedResult
			) {
		JSONObject testStepJsonObj = new JSONObject();
		testStepJsonObj.put("step", testStepDescription );
		testStepJsonObj.put("data", testStepData);
		testStepJsonObj.put("result", testStepExpectedResult );
		return testStepJsonObj;
	}

	private static String getTestCaseId(HttpEntity entity) {
		String testId;
		String string = null;
		try {
			string = EntityUtils.toString(entity);
		} catch (ParseException e) {
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}

		JSONObject createTestResp = new JSONObject(string);
		testId = createTestResp.getString("id");
		return testId;
	}
	
	private static String getTestCaseIDKey(HttpEntity entity) {
		String testId;
		String testKey;
		String string = null;
		try {
			string = EntityUtils.toString(entity);
		} catch (ParseException e) {
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}

		JSONObject createTestResp = new JSONObject(string);
		testId = createTestResp.getString("id");
		testKey = createTestResp.getString("key");
		return testId + ":" + testKey;
	}

	private static int getHTTPResponseCode(HttpResponse response) {
		int statusCode = response.getStatusLine().getStatusCode();
		return statusCode;
	}

	@SuppressWarnings("resource")
	private static HttpResponse executeCreateTestCase(final String createTestUri, Header header,
			StringEntity createTestJSON) {
		HttpResponse response = null;
		try {
			HttpPost createTestReq = new HttpPost(createTestUri);
			createTestReq.addHeader(header);
			createTestReq.addHeader("Content-Type", "application/json");
			createTestReq.setEntity(createTestJSON);
			HttpClient restClient = new DefaultHttpClient();
			response = restClient.execute(createTestReq);
		} catch (ClientProtocolException e) {
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	
	@SuppressWarnings({ "resource", "unused" })
	private static HttpResponse executeUpdateTestCase(final String createTestUri, Header header,
			StringEntity createTestJSON) {
		HttpResponse response = null;
		try {
			HttpPost updateTestReq = new HttpPost(createTestUri);
			updateTestReq.addHeader(header);
			updateTestReq.addHeader("Content-Type", "application/json");
			updateTestReq.setEntity(createTestJSON);
			HttpClient restClient = new DefaultHttpClient();
			response = restClient.execute(updateTestReq);
		} catch (ClientProtocolException e) {
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	private static StringEntity createTestCaseEntity(String testSummary, String testDescription, String ApplicationLabel, String sprint) {
		JSONObject createTestObj = createTestCaseJSON(testSummary, testDescription, ApplicationLabel, sprint);
		StringEntity createTestJSON = null;
		try {
			createTestJSON = new StringEntity(createTestObj.toString());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return createTestJSON;
	}

	public static Header createAuthorizationHeader() {
		byte[] bytesEncoded = Base64.encodeBase64((userName + ":" + password).getBytes());
		String authorizationHeader = "Basic " + new String(bytesEncoded);
		Header header = new BasicHeader(HttpHeaders.AUTHORIZATION, authorizationHeader);
		return header;
	}

	private static String getTextFromHTML(String almText)
	{
		Html2Text parser = new Html2Text();
		almText =parser.getTextFromHTML(almText);
		almText = almText.replaceAll("[\\xa0]", "");
		return almText;
	}
	
	@SuppressWarnings("null")
	private static JSONObject createTestCaseJSON(String testSummary, String testDescription,
			String ApplicationLabel, String sprint) {
		JSONObject projectObj = new JSONObject();
		projectObj.put("id", projectId); // Project ID where the Test to be
		// Created

		// type
		JSONObject issueTypeObj = new JSONObject();
		issueTypeObj.put("id", issueTypeId); // IssueType ID which is Test issue
		
		//Labels
		String[] LabelsObj = {ApplicationLabel.trim()};
		String[] LabelsInitObj = null;
		if(ApplicationLabel.contains(","))
		{	
			LabelsInitObj =  ApplicationLabel.split(",");
			LabelsObj = new String[ApplicationLabel.split(",").length];
			for (int i = 0; i < LabelsInitObj.length; i++)
				LabelsObj[i] = LabelsInitObj[i].trim();
		}		
		
		JSONArray LabelsArrayObj = new JSONArray(Arrays.asList(LabelsObj));		
		
		//Affects Version
//		JSONObject AffVersionObj = new JSONObject();
//		VersionObj.put("versions", affectsVersion);
//		
//		//Fix Version
//		JSONObject FixVersionObj = new JSONObject();
//		VersionObj.put("fixVersions", affectsVersion);
//		
//		JSONArray VersionArrayObj = new JSONArray(Arrays.asList(VersionObj));
		
		
		// JSONObject assigneeObj = new JSONObject();/
		// assigneeObj.put("name", userName); // Username of the assignee

		// JSONObject reporterObj = new JSONObject();
		// reporterObj.put("name", userName); // Username of the Reporter

		// Case
		// Summary/Name

		/**
		 * Create JSON payload to POST Add more field objects if required
		 * 
		 * ***DONOT EDIT BELOW ***
		 */

		JSONObject fieldsObj = new JSONObject();
		fieldsObj.put("project", projectObj);
		fieldsObj.put("summary", testSummary);
		fieldsObj.put("labels", LabelsArrayObj);
		fieldsObj.put("description", testDescription);
		fieldsObj.put("issuetype", issueTypeObj);
		//fieldsObj.put("versions", VersionArrayObj);
		//Optional field with condition
		if(sprint != null)
			fieldsObj.put(sprintCustFieldId, Integer.valueOf(sprint));
	
		
		// fieldsObj.put("assignee", assigneeObj);
		// fieldsObj.put("reporter", reporterObj);
		
		JSONObject createTestObj = new JSONObject();
		createTestObj.put("fields", fieldsObj);
		
		return createTestObj;
	}
	
	
	private static StringEntity createIssueLinkJSON(String InwardissueId, String issueKey, String relation) {

		//JSON object for relationship type of link
		JSONObject type = new JSONObject();
		//Put appropriate casing
		relation = relation.substring(0,1).toUpperCase() + relation.substring(1).toLowerCase();
		type.put("name", relation);
		
		//JSON object for Issue Key to be linked
		JSONObject inwardIssue = new JSONObject();
		inwardIssue.put("id", InwardissueId);
		
		//JSON object for Issue Key to be linked
		JSONObject outwardIssue = new JSONObject();
		outwardIssue.put("key", issueKey);
		
		
		JSONObject createLinkObj = new JSONObject();
		createLinkObj.put("type", type);
		createLinkObj.put("inwardIssue", inwardIssue);
		createLinkObj.put("outwardIssue", outwardIssue);
		
		StringEntity createLinkJSON = null;
		try {
			createLinkJSON = new StringEntity(createLinkObj.toString());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return createLinkJSON;
	}
	
	private String getFilePath(String fname)
	{
		
		Path p = Paths.get(fname);
		fname = p.getFileName().toString();
		
		File dir = new File(Globalvars.DownloadPath).getParentFile();
		File[] fileList = dir.listFiles();
		String returnFName = "";
		for (File file : fileList) {
			TextOutputFile.writeToLog(file.getName());
			if (file.getName().equals(fname))
			{
				returnFName= file.getAbsolutePath(); 
			}
		}
		return returnFName;
	}
	
	public boolean addAttachmentToIssue(String issueKey, String fullfilename) throws IOException{

		TextOutputFile.writeToLog("Full File Name :  " + fullfilename);
		fullfilename = getFilePath(fullfilename);
		TextOutputFile.writeToLog("Full File Name :  " + fullfilename);
		if ("".equals(fullfilename))
			return false;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(jiraBaseURL+"/rest/api/2/issue/"+issueKey+"/attachments");
		httppost.setHeader("X-Atlassian-Token", "nocheck");
		httppost.setHeader(createAuthorizationHeader());
		
		File fileToUpload = new File(fullfilename);
		FileBody fileBody = new FileBody(fileToUpload);

		HttpEntity entity = MultipartEntityBuilder.create()
				.addPart("file", fileBody)
				.build();
		
		httppost.setEntity(entity);
        httppost.getRequestLine();
        
        CloseableHttpResponse response = null;
		
        try {
			response = httpclient.execute(httppost);
        }catch(Exception e)
        {
        	TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		} finally {
			httpclient.close();
		}
        
        TextOutputFile.writeToLog("Added Attachment to Test Case " + fullfilename);
        
		if(response.getStatusLine().getStatusCode() == 200)
			return true;
		else
			return false;

	}
	
	public static String fn_ValidateKeys(JSONArray JSONProjectList) throws Exception {
		String ResponseMessage = "Success";
		
		try
		{

			HttpEntity entity = null;
			String finalURL = validateKeyUri + JSONProjectList.getJSONObject(0).get("id").toString();
			URI uri = new URI(finalURL);
			int expirationInSec = 360;
			ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(zephyrBaseUrl, accessKey, secretKey, userName)
					.build();
			JwtGenerator jwtGenerator = client.getJwtGenerator();
			String jwt = jwtGenerator.generateJWT("POST", uri, expirationInSec);
	
			HttpResponse responseConfig = null;
			HttpPost ConfigReq = new HttpPost(uri);
			ConfigReq.setHeader("Authorization", jwt);
			ConfigReq.setHeader("zapiAccessKey", accessKey);
			
			try {
				HttpClient restClient = new DefaultHttpClient();
				responseConfig = restClient.execute(ConfigReq);
			} catch (ClientProtocolException e) {
				TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			} catch (IOException e) {
				TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			}
			int statusCode = getHTTPResponseCode(responseConfig);
	
			if (statusCode == 400) {
				entity = responseConfig.getEntity();
				String string = null;
				try {
					string = EntityUtils.toString(entity);
					JSONObject testStepObj = new JSONObject(string);
					if(!testStepObj.get("errorCode").equals(123))
					{
						ResponseMessage = "Unauthorized API Access Key and Secrect Key";
					}
				} catch (ParseException e) {
					TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
				} catch (IOException e) {
					TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
				}
	
			} else {
				try {
					TextOutputFile.writeToLog("Failure~" + EntityUtils.toString(responseConfig.getEntity()));
					ResponseMessage = "Unauthorized API Access Key and Secrect Key";
					throw new ClientProtocolException("Unexpected response status: " + statusCode);
				} catch (ClientProtocolException e) {
					TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
				}
			}
			TextOutputFile.writeToLog("API Keys validation complete");
			
		}
		catch(Exception e)
		{
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		return ResponseMessage;
	}

	public String updateAffectsVersion(String testId, String affectsVersion) {

		
		try {
			StringEntity updateTestJSON = updateAffectsVersionJSON(testId, affectsVersion);
			HttpResponse response = executeUpdateTestCase(updateTestUri, header, updateTestJSON);
			int statusCode = getHTTPResponseCode(response);
			HttpEntity entity = response.getEntity();
			if (statusCode >= 200 && statusCode < 300) {			
				TextOutputFile.writeToLog("Affects version is update for the issue " + testId );

			} else {
				try {
					String string = null;
					
					string = EntityUtils.toString(entity);
					RespMessage = "Failure~" + string;
					new JSONObject(string);
					throw new ClientProtocolException("Unexpected response status: " + statusCode);

				} catch (ClientProtocolException e) {
					TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		return testId;
	}

	private StringEntity updateAffectsVersionJSON(String testId, String affectsVersion) {

		
		//update
		JSONObject nameObj = new JSONObject();
		nameObj.put("name", affectsVersion);
		
		JSONObject addObj = new JSONObject();
		addObj.put("add", nameObj);
		
		
		JSONArray affVersionsArray = new JSONArray();
		affVersionsArray.put(addObj);
		
		JSONObject updateObj = new JSONObject();
		updateObj.put("versions", affVersionsArray);
		
		
		JSONObject createupdateObj = new JSONObject();
		createupdateObj.put("update", updateObj);
		createupdateObj.put("id", testId);
		
		JSONArray issueUpdate = new JSONArray();
		issueUpdate.put(createupdateObj);
		
		JSONObject finalUpdateObj = new JSONObject();
		finalUpdateObj.put("issueUpdates", issueUpdate);
		
		
		StringEntity updateVersionJSON = null;
		try {
			updateVersionJSON = new StringEntity(finalUpdateObj.toString());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return updateVersionJSON;
	}


	
}