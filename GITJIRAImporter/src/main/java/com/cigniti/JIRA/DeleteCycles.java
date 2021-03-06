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
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.cigniti.util.Globalvars;
import com.cigniti.util.Html2Text;
import com.cigniti.util.ReadAttachmentsMapping;
import com.thed.zephyr.cloud.rest.ZFJCloudRestClient;
import com.thed.zephyr.cloud.rest.client.JwtGenerator;

@SuppressWarnings("deprecation")
public class DeleteCycles {

	private static String API_CREATE_TEST = "{SERVER}/rest/api/2/issue";
	private static String API_CREATE_TEST_STEP = "{SERVER}/public/rest/api/1.0/teststep/";
	private static Long versionId = -1l;

	/** Declare JIRA,Zephyr URL,access and secret Keys */
	// Jira Cloud URL for the instance
	private static String jiraBaseURL = "https://rentacenter.atlassian.net";
	// Replace zephyr baseurl <ZAPI_Cloud_URL> shared with the user for ZAPI
	// Cloud Installation
	private static String zephyrBaseUrl = "https://prod-api.zephyr4jiracloud.com/connect";
	// zephyr accessKey , we can get from Addons >> zapi section
	private static String accessKey = "N2RjOTAxZWUtYTc2Mi0zMzkzLWEwYmYtZWIxNjM2ZmM3MjAxIHJhbmdhbmF0aC5kaW5kdWt1cnRoaSBVU0VSX0RFRkFVTFRfTkFNRQ";
	// zephyr secretKey , we can get from Addons >> zapi section
	private static String secretKey = "isLXShDd7KWGjrR6Z3m6Ral_O1jXfy4t37otYAExqus";

	/** Declare parameter values here */

	private static String userName = Globalvars.JIRA_userName;
	private static String password = Globalvars.JIRA_password;
	private static String projectId = Globalvars.JIRA_projectId;
	private static String issueTypeId = Globalvars.JIRA_issueTypeId;
	
	private static final String createTestUri = API_CREATE_TEST.replace("{SERVER}", jiraBaseURL);
	private static final String createTestStepUri = API_CREATE_TEST_STEP.replace("{SERVER}", zephyrBaseUrl);

	static ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(zephyrBaseUrl, accessKey, secretKey, userName)
			.build();
	static Header header = createAuthorizationHeader();
	// JwtGenerator jwtGenerator = client.getJwtGenerator();

	public static void main(String[] args) throws JSONException, URISyntaxException, ParseException, IOException {

		/**
		 * Create Test Parameters, declare Create Test Issue fields Declare more
		 * field objects if required
		 */
		
		
		
		DeleteCycles createTestWithTestSteps = new DeleteCycles();
		createTestWithTestSteps.createAuthorizationHeader();
		createTestWithTestSteps.getCycleInformation();
		
		/*
		String cycleName = "Test Cycle -- API DEMO";
		String cycleDescription = "Created by ZAPI CLOUD API";
		
		String testSummary = "Sample Test case With Steps created through ZAPI Cloud_10_06_2017_3"; // Test
		String testDescription = "Sample Test case Description With Steps created through ZAPI Cloud"; // Test

		String testStepDescription = "Sample Test Step1006_3";
		String testStepData = "Sample Test Data";
		String testStepExpectedResult = "Sample Expected Result";

		String testStepDescription1 = "Sample Test Step1006_3_1";
		String testStepData1 = "Sample Test Data1";
		String testStepExpectedResult1 = "Sample Expected Result1";

		
		String testId = createTestWithTestSteps.createTestCaseinJira(testSummary, testDescription);

		String cycleID = createTestWithTestSteps.createCycleInJIRA(cycleName, cycleDescription);

		createTestWithTestSteps.addTestToCycle(testId, cycleID);

		createTestWithTestSteps.createTestStepinJira(testStepDescription, testStepData, testStepExpectedResult, testId);
		createTestWithTestSteps.createTestStepinJira(testStepDescription1, testStepData1, testStepExpectedResult1, testId);
		//addAttachment("10627", projectId);
	//	addAttachmentToIssue(testId, "C:\\Users\\dinran\\Downloads\\Zephyr1.png");
		//createTestWithTestSteps.addAttachmentToIssue(testId, "C:\\Users\\dinran\\Downloads\\pom.xml"); */
	}

	@SuppressWarnings("resource")
	public void createTestStepinJira(String testStepDescription,
			String testStepData, String testStepExpectedResult, String testId) throws URISyntaxException {
		
		testStepDescription = getTextFromHTML(testStepDescription);
		testStepData = getTextFromHTML(testStepData);
		testStepExpectedResult = getTextFromHTML(testStepExpectedResult);

		System.out.println("Test Step Description " + testStepDescription);
		System.out.println("Test Step Data " + testStepData);
		System.out.println("Test Step Expected Value" + testStepExpectedResult);
		
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
			HttpEntity entity = responseTestStep.getEntity();
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
				throw new ClientProtocolException("Unexpected response status: " + statusCode);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Created Test Step in JIRA " + testStepDescription );

	}

	public String createTestCaseinJira(String testSummary,
			String testDescription) throws IOException {
		
		testDescription = getTextFromHTML(testDescription);
		testSummary = getTextFromHTML(testSummary);

		StringEntity createTestJSON = createTestCaseEntity(testSummary, testDescription);
		HttpResponse response = executeCreateTestCase(createTestUri, header, createTestJSON);
		int statusCode = getHTTPResponseCode(response);
		String testId = null;
		HttpEntity entity = response.getEntity();
		if (statusCode >= 200 && statusCode < 300) {
			testId = getTestCaseId(entity);
		} else {
			try {
				String string = null;
				string = EntityUtils.toString(entity);
				new JSONObject(string);
				throw new ClientProtocolException("Unexpected response status: " + statusCode);

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Created Test Case in JIRA " + testDescription );
		return testId;
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

		System.out.println("Created Test Cycle in JIRA " + cycleName );

		/**
		 * Add tests to Cycle IssueId's
		 * 
		 */
		return cycleID;
	}

	public void getCycleInformation()  {
		String createCycleUri = zephyrBaseUrl + "/public/rest/api/1.0/cycle?projectId="+projectId;
		System.out.println(createCycleUri);

		try {
			getCycles(createCycleUri, client, accessKey);
		} catch (JSONException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		/**
		 * Add tests to Cycle IssueId's
		 * 
		 */
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
		System.out.println("Tests added successfully to Test Cycle ");
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

	public static void getCycles(String uriStr, ZFJCloudRestClient client, String accessKey)
			throws URISyntaxException, JSONException {

		URI uri = new URI(uriStr);
		int expirationInSec = 360;
		JwtGenerator jwtGenerator = client.getJwtGenerator();
		String jwt = jwtGenerator.generateJWT("GET", uri, expirationInSec);

		HttpResponse response = null;
		HttpClient restClient = new DefaultHttpClient();

		HttpPost createCycleReq = new HttpPost(uri);
		createCycleReq.addHeader("Content-Type", "application/json");
		createCycleReq.addHeader("Authorization", jwt);
		createCycleReq.addHeader("zapiAccessKey", accessKey);

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
				System.out.println(string);
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

	private static int getHTTPResponseCode(HttpResponse response) {
		int statusCode = response.getStatusLine().getStatusCode();
		return statusCode;
	}

	@SuppressWarnings("resource")
	private static HttpResponse executeCreateTestCase(final String createTestUri, Header header,
			StringEntity createTestJSON) {
		HttpResponse response = null;
		try {
			// System.out.println(issueSearchURL);
			HttpPost createTestReq = new HttpPost(createTestUri);
			createTestReq.addHeader(header);
			createTestReq.addHeader("Content-Type", "application/json");
			createTestReq.setEntity(createTestJSON);
			HttpClient restClient = new DefaultHttpClient();
			response = restClient.execute(createTestReq);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	private static StringEntity createTestCaseEntity(String testSummary, String testDescription) {
		JSONObject createTestObj = createTestCaseJSON(testSummary, testDescription);
		StringEntity createTestJSON = null;
		try {
			createTestJSON = new StringEntity(createTestObj.toString());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return createTestJSON;
	}

	private static Header createAuthorizationHeader() {
		byte[] bytesEncoded = Base64.encodeBase64((userName + ":" + password).getBytes());
		String authorizationHeader = "Basic " + new String(bytesEncoded);
		System.out.println(authorizationHeader);
		Header header = new BasicHeader(HttpHeaders.AUTHORIZATION, authorizationHeader);
		return header;
	}

	private static String getTextFromHTML(String almText)
	{
		Html2Text parser = new Html2Text();
		almText =parser.getTextFromHTML(almText);
		almText = almText.replaceAll("[^\\x00-\\x7f]+", "");
		return almText;
	}
	
	private static JSONObject createTestCaseJSON(String testSummary, String testDescription) {
		JSONObject projectObj = new JSONObject();
		projectObj.put("id", projectId); // Project ID where the Test to be
		// Created

		JSONObject issueTypeObj = new JSONObject();
		issueTypeObj.put("id", issueTypeId); // IssueType ID which is Test isse
		// type

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
		fieldsObj.put("description", testDescription);
		fieldsObj.put("issuetype", issueTypeObj);
		// fieldsObj.put("assignee", assigneeObj);
		// fieldsObj.put("reporter", reporterObj);

		JSONObject createTestObj = new JSONObject();
		createTestObj.put("fields", fieldsObj);
		return createTestObj;
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
	
}