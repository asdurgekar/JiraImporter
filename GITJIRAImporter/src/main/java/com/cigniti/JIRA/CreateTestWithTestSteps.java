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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
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
import com.thed.zephyr.cloud.rest.ZFJCloudRestClient;
import com.thed.zephyr.cloud.rest.client.JwtGenerator;





@SuppressWarnings("deprecation")
public class CreateTestWithTestSteps {

	private static String API_CREATE_TEST = "{SERVER}/rest/api/2/issue";
	private static String API_CREATE_TEST_STEP = "{SERVER}/public/rest/api/1.0/teststep/";
	private static Long versionId = -1l;
	private static String API_VALIDKEY = "{SERVER}/public/rest/api/1.0/teststep/-1?projectId=";
	private static String API_GET_ISSUE = "{SERVER}/rest/api/2/issue/";
	private static String API_LINK_ISSUE = "{SERVER}/rest/api/2/issueLink";
	private static String API_EXECUTION = "{SERVER}/public/rest/api/1.0/execution/";
	private static String API_CYCLEEXECUTION = "{SERVER}/public/rest/api/2.0/executions/search/cycle/";
	private static String API_SEARCHISSUE = "{SERVER}/rest/api/2/search";
	
	
	/** Declare JIRA,Zephyr URL,access and secret Keys */
	// Jira Cloud URL for the instance
	public static String jiraBaseURL = "https://gamestop.atlassian.net";
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

	public static String userName = Globalvars.JIRA_userName;
	public static String password = Globalvars.JIRA_password;
	public static String projectId = Globalvars.JIRA_projectId;
	private static String issueTypeId = Globalvars.JIRA_issueTypeId;
	private static String bugTypeId = Globalvars.JIRA_bugTypeId;
	
	private static final String createTestUri = API_CREATE_TEST.replace("{SERVER}", jiraBaseURL);
	private static final String createTestStepUri = API_CREATE_TEST_STEP.replace("{SERVER}", zephyrBaseUrl);
	private static final String validateKeyUri = API_VALIDKEY.replace("{SERVER}", zephyrBaseUrl);
	private static final String getIssueUri = API_GET_ISSUE.replace("{SERVER}", jiraBaseURL);
	private static final String issueLinkUri = API_LINK_ISSUE.replace("{SERVER}", jiraBaseURL);
	private static final String ExecutionUri = API_EXECUTION.replace("{SERVER}", zephyrBaseUrl);
	private static final String SearchCycleExecUri = API_CYCLEEXECUTION.replace("{SERVER}", zephyrBaseUrl);
	private static final String SearchIssueUri = API_SEARCHISSUE.replace("{SERVER}", jiraBaseURL);
	
	static ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(zephyrBaseUrl, accessKey, secretKey, userName)
			.build();
	static Header header = createAuthorizationHeader();
	// JwtGenerator jwtGenerator = client.getJwtGenerator();
	public static String RespMessage = "Success~All Test Cases are imported successfully";
	

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

		System.out.println("Test Step Description " + testStepDescription);
		System.out.println("Test Step Data " + testStepData);
		System.out.println("Test Step Expected Value" + testStepExpectedResult);
		
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int statusCode = getHTTPResponseCode(responseTestStep);

		if (statusCode >= 200 && statusCode < 300) {
			entity = responseTestStep.getEntity();
			String string = null;
			try {
				string = EntityUtils.toString(entity);
				JSONObject testStepObj = new JSONObject(string);
				testStepObj.getString("id");
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			try {
				System.out.println("__________________________________________________________________________________________");
				System.out.println("Test Step Values : " + testStepDescription + ":" + testStepData +":" + testStepExpectedResult);
				System.out.println("__________________________________________________________________________________________");
				RespMessage = "Failure~" + EntityUtils.toString(responseTestStep.getEntity());
				throw new ClientProtocolException("Unexpected response status: " + statusCode);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Created Test Step in JIRA " + testStepDescription );

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
			
			
			System.out.println("Created Test Case in JIRA " + testDescription );

		} else {
			try {
				String string = null;
				
				string = EntityUtils.toString(entity);
				RespMessage = "Failure~" + string;
				new JSONObject(string);
				throw new ClientProtocolException("Unexpected response status: " + statusCode);

			} catch (ClientProtocolException e) {
				e.printStackTrace();
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

		System.out.println("Created Test Cycle in JIRA with name : " + cycleName );

		/**
		 * Add tests to Cycle IssueId's
		 * 
		 */
		return cycleID;
	}

	public String addTestToCycle(String issueID, String cycleID)
			throws URISyntaxException, IOException {
		String addTestsUri = zephyrBaseUrl + "/public/rest/api/1.0/executions/add/cycle/" + cycleID;
		String[] issueIds = { issueID }; //Issue Id's to be added to Test Cycle, add more issueKeys separated by comma

		JSONObject addTestsObj = new JSONObject();
		addTestsObj.put("issues", issueIds);
		addTestsObj.put("method", "1");
		addTestsObj.put("projectId", projectId);
		addTestsObj.put("versionId", -1);

		StringEntity addTestsJSON = null;
		try {
			addTestsJSON = new StringEntity(addTestsObj.toString());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String ExecutionId = addTestsToCycle(addTestsUri, client, accessKey, addTestsJSON);
		System.out.println("Test: " + issueID + " is added successfully to Test Cycle ");
		return ExecutionId;
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int statusCode = response.getStatusLine().getStatusCode();
		String string = null;
		if (statusCode >= 200 && statusCode < 300) {
			HttpEntity entity = response.getEntity();			
			try {
				string = EntityUtils.toString(entity);
				new JSONObject(entity);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			try {
				throw new ClientProtocolException("Unexpected response status: " + statusCode);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
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
				System.out.println(":"+fileName+":");
				if (!"".equals(fileName))
				{
					addAttachmentToIssue(jiraTestCaseId, fileName);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			try {
				throw new ClientProtocolException("Unexpected response status: " + statusCode);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int statusCode = getHTTPResponseCode(response);
		String testId = null;
		HttpEntity entity = response.getEntity();
		if (statusCode >= 200 && statusCode < 300) {
			String string = null;
			try {
				string = EntityUtils.toString(entity);
				// System.out.println(string1);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Details retrieved for issue " + IssueId );

		} else {
			try {
				String string = null;
				
				string = EntityUtils.toString(entity);
				RespMessage = "Failure~" + string;
				new JSONObject(string);
				throw new ClientProtocolException("Unexpected response status: " + statusCode);

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			}
		}
		
		
		return testId;
	}
	
	
	public String linkIssue(String InwardissueId, String issueKey, String relation) throws IOException {
		
		
		System.out.println();
		StringEntity createTestJSON = createIssueLinkJSON(InwardissueId, issueKey, relation);
		HttpResponse response = executeCreateTestCase(issueLinkUri, header, createTestJSON);
		int statusCode = getHTTPResponseCode(response);
		String linkId = null;
		HttpEntity entity = response.getEntity();
		if (statusCode >= 200 && statusCode < 300) {			
			System.out.println("Link is created for the issue " + issueKey );

		} else {
			try {
				String string = null;
				
				string = EntityUtils.toString(entity);
				RespMessage = "Failure~" + string;
				new JSONObject(string);
				throw new ClientProtocolException("Unexpected response status: " + statusCode);

			} catch (ClientProtocolException e) {
				e.printStackTrace();
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
			// System.out.println(string1);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
			// System.out.println(string1);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
			
			
			URI uri = null;
			try {
				uri = new URI(createTestUri);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			int expirationInSec = 360;
			JwtGenerator jwtGenerator = client.getJwtGenerator();
			String jwt = jwtGenerator.generateJWT("POST", uri, expirationInSec);

			HttpPost addTestStepReq = new HttpPost(uri);
			addTestStepReq.addHeader("Content-Type", "application/json");
			addTestStepReq.addHeader(HttpHeaders.AUTHORIZATION, jwt);
			addTestStepReq.addHeader("zapiAccessKey", accessKey);
			addTestStepReq.setEntity(createTestJSON);
			
			HttpClient restClient = new DefaultHttpClient();
			response = restClient.execute(addTestStepReq);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	
	@SuppressWarnings("resource")
	private static HttpResponse executePostwithHeader(final String createTestUri, Header header,
			StringEntity createTestJSON) {
		HttpResponse response = null;
		try {
			
			URI uri = null;
			try {
				uri = new URI(createTestUri);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			HttpPost addTestStepReq = new HttpPost(uri);
			addTestStepReq.addHeader("Content-Type", "application/json");
			addTestStepReq.setHeader(header);
			addTestStepReq.setEntity(createTestJSON);
			
			HttpClient restClient = new DefaultHttpClient();
			response = restClient.execute(addTestStepReq);
		} catch (Exception e) {
			e.printStackTrace();		
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
	
	private static StringEntity createBugEntity(String bugSummary, String bugDescription, String ApplicationLabel, String sprint) {
		JSONObject createBugObj = createBugJSON(bugSummary, bugDescription, ApplicationLabel, sprint);
		StringEntity createBugJSON = null;
		try {
			createBugJSON = new StringEntity(createBugObj.toString());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return createBugJSON;
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
		//almText = almText.replaceAll("[^\\x00-\\x7f]+", "");
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
		issueTypeObj.put("id", issueTypeId); // IssueType ID which is Test isse
		
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
		
		
//		JSONObject VersionObj = new JSONObject();
//		VersionObj.put("id", affectsVersion);
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
			fieldsObj.put("customfield_10103", Integer.valueOf(sprint));
	
		
		// fieldsObj.put("assignee", assigneeObj);
		// fieldsObj.put("reporter", reporterObj);

		JSONObject createTestObj = new JSONObject();
		createTestObj.put("fields", fieldsObj);
		return createTestObj;
	}
	
	private static JSONObject createBugJSON(String bugSummary, String bugDescription,
			String ApplicationLabel, String sprint) {
		
		
		JSONObject createTestObj = null;
		try {
			JSONObject projectObj = new JSONObject();
			projectObj.put("id", projectId); // Project ID where the Test to be
			// Created

			// type
			JSONObject issueTypeObj = new JSONObject();
			issueTypeObj.put("id", bugTypeId); // IssueType ID which is Test isse
			
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
			
			
//		JSONObject VersionObj = new JSONObject();
//		VersionObj.put("id", affectsVersion);
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
			fieldsObj.put("summary", bugSummary);
			fieldsObj.put("labels", LabelsArrayObj);
			fieldsObj.put("description", bugDescription);
			fieldsObj.put("issuetype", issueTypeObj);
			//fieldsObj.put("versions", VersionArrayObj);
			//Optional field with condition
			if(sprint != null && !sprint.trim().isEmpty())
				fieldsObj.put("customfield_10103", Integer.valueOf(sprint));

			
			// fieldsObj.put("assignee", assigneeObj);
			// fieldsObj.put("reporter", reporterObj);

			createTestObj = new JSONObject();
			createTestObj.put("fields", fieldsObj);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return createTestObj;
	}
	
	
	private static StringEntity createSearchRequest(String jiraProjectId, String issueTypeId,
			String bugSummary) {
		
		
		JSONObject createSearchObj = null;
		StringEntity createSearchStrEnt = null;
		try {
			/*
			JSONObject projectObj = new JSONObject();
			projectObj.put("id", projectId); // Project ID where the Test to be
			// Created

			// type
			JSONObject issueTypeObj = new JSONObject();
			issueTypeObj.put("id", bugTypeId); // IssueType ID which is Test isse
			
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
			*/
			
			//Affects Version
			
			
			//		JSONObject VersionObj = new JSONObject();
			//		VersionObj.put("id", affectsVersion);
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
			
			JSONArray fieldsArray = new JSONArray();
			fieldsArray.put("summary");
			fieldsArray.put("status");
			fieldsArray.put("assignee");

			String jqlString = "project = " + jiraProjectId + " AND issuetype = " + issueTypeId + 
					" AND summary ~ '" + bugSummary + "' ORDER BY created DESC";
			JSONObject fieldsObj = new JSONObject();
			fieldsObj.put("jql", jqlString);
			fieldsObj.put("startAt", 0);
			fieldsObj.put("maxResults", 5);
			fieldsObj.put("fields", fieldsArray);
			
			createSearchObj = fieldsObj;
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			createSearchStrEnt = new StringEntity(createSearchObj.toString());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		return createSearchStrEnt;
	}
	
	
	private static StringEntity createIssueLinkJSON(String InwardissueId, String issueKey, String relation) {

		//JSON object for relationship type of link
		JSONObject type = new JSONObject();
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
	
	private static StringEntity createExecutionJSON_old(String StatusId) {

		//JSON object for relationship type of link
		JSONObject createLinkObj = new JSONObject();
		createLinkObj.put("status", 1);
		
		StringEntity createLinkJSON = null;
		try {
			createLinkJSON = new StringEntity(createLinkObj.toString());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return createLinkJSON;
	}
	
	private static StringEntity createExecutionJSON(String projectId, String cycleId, String ExecutionId, String IssueId,
			String StatusId, String Comment, String[] defectIds) {

		//JSON object for relationship type of link
		JSONObject statusId = new JSONObject();
		statusId.put("id", Integer.parseInt(StatusId));
		
		
		
		
		JSONObject createLinkObj = new JSONObject();
		createLinkObj.put("cycleId", cycleId);
		createLinkObj.put("id", ExecutionId);
		createLinkObj.put("issueId", IssueId);
		createLinkObj.put("projectId", projectId);
		createLinkObj.put("status", statusId);
		
		if(defectIds != null)
		{
			JSONArray defects = new JSONArray();
			for(String inddefect :defectIds)
				defects.put(inddefect);
			createLinkObj.put("defects", defects);
		}
		
		if(Comment != null)
		{
			createLinkObj.put("comment", Comment);
		}
		createLinkObj.put("versionId", -1);
		
		
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
			System.out.println(file.getName());
			if (file.getName().equals(fname))
			{
				returnFName= file.getAbsolutePath(); 
			}
		}
		return returnFName;
	}
	
	public boolean addAttachmentToIssue(String issueKey, String fullfilename) throws IOException{

		System.out.println("Full File Name :  " + fullfilename);
		fullfilename = getFilePath(fullfilename);
		System.out.println("Full File Name :  " + fullfilename);
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
        	e.printStackTrace();
		} finally {
			httpclient.close();
		}
        
        System.out.println("Added Attachment to Test Case " + fullfilename);
        
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
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
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
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	
			} else {
				try {
					System.out.println("Failure~" + EntityUtils.toString(responseConfig.getEntity()));
					ResponseMessage = "Unauthorized API Access Key and Secrect Key";
					throw new ClientProtocolException("Unexpected response status: " + statusCode);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				}
			}
			System.out.println("API Keys validation complete");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return ResponseMessage;
	}

	public String UpdateExecution_old(String ExecutionId, String StatusId) throws IOException {
		
		
		
		String finalURL = ExecutionUri.replace("id",ExecutionId);
		URI uri = null;
		try {
			uri = new URI(finalURL);
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int expirationInSec = 360;
		ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(zephyrBaseUrl, accessKey, secretKey, userName)
				.build();
		JwtGenerator jwtGenerator = client.getJwtGenerator();
		String jwt = jwtGenerator.generateJWT("PUT", uri, expirationInSec);

		
		StringEntity createExecJSON = createExecutionJSON_old(StatusId);
		
		HttpResponse responseExec = null;
		HttpPut ExecReq = new HttpPut(uri);
		ExecReq.addHeader("Content-Type", "application/json");
		ExecReq.addHeader(HttpHeaders.AUTHORIZATION, jwt);
		ExecReq.addHeader("zapiAccessKey", accessKey);
		ExecReq.setEntity(createExecJSON);

		try {
			HttpClient restClient = new DefaultHttpClient();
			responseExec = restClient.execute(ExecReq);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int statusCode = getHTTPResponseCode(responseExec);
		
		
		String linkId = null;
		HttpEntity entity = responseExec.getEntity();
		if (statusCode >= 200 && statusCode < 300) {			
			System.out.println("Execution status is updated for Execution Id : " + ExecutionId );

		} else {
			try {
				String string = null;
				
				string = EntityUtils.toString(entity);
				RespMessage = "Failure~" + string;
				new JSONObject(string);
				throw new ClientProtocolException("Unexpected response status: " + statusCode);

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			}
		}
		return linkId;
	}
	
	
	public void UpdateExecution(String projectId, String cycleId, String ExecutionId, String IssueId, String StatusId,
		Object... OptParams) throws IOException {
		
	
		String Comment = "";
		String[] defectIds = null;
		
		if (OptParams.length > 0) 
		{
		      if (!(OptParams[0] instanceof String)) { 
		          throw new IllegalArgumentException("String argument need to be passed");
		      }
		      Comment = OptParams[0].toString();
		}
	    if (OptParams.length > 1) 
	    {
	        if (!(OptParams[1] instanceof String[])) { 
	            throw new IllegalArgumentException("String array need to be passed");
	        }
	        defectIds = (String[])OptParams[1];	        
	    }
	    
		String finalURL = ExecutionUri + ExecutionId + "?projectId=" +
				projectId + "&issueId=" + IssueId;
		URI uri = null;
		try {
			uri = new URI(finalURL);
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		StringEntity createExecJSON = createExecutionJSON(projectId, cycleId, ExecutionId, IssueId, StatusId, Comment, defectIds);
//		HttpResponse response = executeCreateTestCase(ExecutionUri + ExecutionId + "?projectId=" +
//				projectId + "&issueId=" + IssueId, header, createExecJSON);
		
		int expirationInSec = 360;
		ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(zephyrBaseUrl, accessKey, secretKey, userName)
				.build();
		JwtGenerator jwtGenerator = client.getJwtGenerator();
		String jwt = jwtGenerator.generateJWT("PUT", uri, expirationInSec);
		
		
		HttpResponse responseExec = null;
		HttpPut ExecReq = new HttpPut(uri);
		ExecReq.addHeader("Content-Type", "application/json");
		ExecReq.addHeader(HttpHeaders.AUTHORIZATION, jwt);
		ExecReq.addHeader("zapiAccessKey", accessKey);
		ExecReq.setEntity(createExecJSON);
		
		
		try {
			HttpClient restClient = new DefaultHttpClient();
			responseExec = restClient.execute(ExecReq);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int statusCode = getHTTPResponseCode(responseExec);		
		HttpEntity entity = responseExec.getEntity();
		if (statusCode >= 200 && statusCode < 300) {			
			System.out.println("Execution status is updated for  " + ExecutionId );

		} else {
			try {
				String string = null;
				
				string = EntityUtils.toString(entity);
				RespMessage = "Failure~" + string;
				new JSONObject(string);
				throw new ClientProtocolException("Unexpected response status: " + statusCode);

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			}
		}
		
	}

	public List<String> searchCycleExecutions(String cycleID, String JIRA_projectId) throws ParseException, IOException {

		List<String> executionIds = new ArrayList<String>();
		String finalURL = SearchCycleExecUri + cycleID + "?projectId=" +
				JIRA_projectId + "&versionId=" + versionId;
		URI uri = null;
		try {
			uri = new URI(finalURL);
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//StringEntity createExecJSON = createExecutionJSON(ExecutionId, projectId, IssueId, StatusId);
//		HttpResponse response = executeCreateTestCase(ExecutionUri + ExecutionId + "?projectId=" +
//				projectId + "&issueId=" + IssueId, header, createExecJSON);
		
		int expirationInSec = 360;
		ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(zephyrBaseUrl, accessKey, secretKey, userName)
				.build();
		JwtGenerator jwtGenerator = client.getJwtGenerator();
		String jwt = jwtGenerator.generateJWT("GET", uri, expirationInSec);
		
		
		HttpResponse responseCycle = null;
		HttpGet SearchReq = new HttpGet(uri);
		SearchReq.addHeader("Content-Type", "application/json");
		SearchReq.addHeader(HttpHeaders.AUTHORIZATION, jwt);
		SearchReq.addHeader("zapiAccessKey", accessKey);
		
		
		
		try {
			HttpClient restClient = new DefaultHttpClient();
			responseCycle = restClient.execute(SearchReq);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int statusCode = getHTTPResponseCode(responseCycle);		
		HttpEntity entity = responseCycle.getEntity();
		if (statusCode >= 200 && statusCode < 300) {
			String result = EntityUtils.toString(responseCycle.getEntity());
			JSONObject JSONCycleTests = new JSONObject(result);
			executionIds = fn_GetExecutionIds(JSONCycleTests);
			System.out.println("Cycle executions are retrieved for Cycle Id : " + cycleID );

		} else {
			try {
				String string = null;
				
				try {
					string = EntityUtils.toString(entity);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				RespMessage = "Failure~" + string;
				new JSONObject(string);
				throw new ClientProtocolException("Unexpected response status: " + statusCode);

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			}
		}
		return executionIds;
		
	}

	private List<String> fn_GetExecutionIds(JSONObject jSONCycleTests) {
		
		// TODO Auto-generated method stub
		List<String> executionIds = new ArrayList<String>();
		
		JSONObject searObjList = jSONCycleTests.getJSONObject("searchResult");
		JSONArray SearchJSONArray = searObjList.getJSONArray("searchObjectList");
		
		for (int arrCounter = 0; arrCounter < SearchJSONArray.length(); arrCounter++) 
		{
		    JSONObject IndTest = SearchJSONArray.getJSONObject(arrCounter);
		    JSONObject IndTestExec = IndTest.getJSONObject("execution");
		    executionIds.add(IndTestExec.get("id").toString());
		}
		
		return executionIds;
	}
	
	public String createBuginJira(String bugSummary, String bugDescription,
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
		bugDescription = getTextFromHTML(bugDescription);
		bugSummary = getTextFromHTML(bugSummary);

		StringEntity createTestJSON = createBugEntity(bugSummary, bugDescription, ApplicationLabel, sprint);
		HttpResponse response = executePostwithHeader(createTestUri, header, createTestJSON);
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
			
			
			System.out.println("Created Bug in JIRA with description : " + bugDescription );

		} else {
			try {
				String string = null;
				
				string = EntityUtils.toString(entity);
				RespMessage = "Failure~" + string;
				new JSONObject(string);
				throw new ClientProtocolException("Unexpected response status: " + statusCode);

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			}
		}
		return str_TestCaseReturn;
	}

	public String getExistingDefectId(String JIRA_projectId, String bugSummary) 
			throws ParseException, IOException {

		String str_defectId = null;
		String str_defectKey = null;
		StringEntity createSearchJSON = createSearchRequest(JIRA_projectId, bugTypeId, bugSummary);
		HttpResponse response = executePostwithHeader(SearchIssueUri, header, createSearchJSON);
		int statusCode = getHTTPResponseCode(response);
		String testId = null;
		String testKey = null;
		HttpEntity entity = response.getEntity();
		
		if (statusCode >= 200 && statusCode < 300) {
			
			//retrieve defect id
			JSONObject IssuesObj = new JSONObject(EntityUtils.toString(entity));
			JSONArray IssuesObjArr = IssuesObj.getJSONArray("issues");
			if(IssuesObjArr.length() > 0)
			{
				JSONObject IssueObj = IssuesObjArr.getJSONObject(0);
				str_defectId = IssueObj.get("id").toString();
				str_defectKey = IssueObj.get("key").toString();
				System.out.println("Existing defect found  : " + str_defectKey);
			}
			else
			{
				System.out.println("No existing defect with Summary contaning  : '" + bugSummary + "'");
			}

		} else {
			try {
				String string = null;
				
				string = EntityUtils.toString(entity);
				RespMessage = "Failure~" + string;
				new JSONObject(string);
				throw new ClientProtocolException("Unexpected response status: " + statusCode);

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			}
		}
		return str_defectId;
	}
	
}