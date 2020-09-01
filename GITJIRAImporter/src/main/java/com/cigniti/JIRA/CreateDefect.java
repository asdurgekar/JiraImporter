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
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

@SuppressWarnings("deprecation")
public class CreateDefect {

	private static String API_CREATE_TEST = "{SERVER}/rest/api/2/issue";
	// Jira Cloud URL for the instance
	private static String jiraBaseURL = "https://rentacenter.atlassian.net";
	/** Declare parameter values here */

	private static String userName = Globalvars.JIRA_userName;
	private static String password = Globalvars.JIRA_password;
	private static String projectId = "10367";
	private static String issueTypeId = "10103";
	
	private static final String createTestUri = API_CREATE_TEST.replace("{SERVER}", jiraBaseURL);

	static Header header = createAuthorizationHeader();
	// JwtGenerator jwtGenerator = client.getJwtGenerator();

	public static void main(String[] args) throws JSONException, URISyntaxException, ParseException, IOException {

		/**
		 * Create Test Parameters, declare Create Test Issue fields Declare more
		 * field objects if required
		 */
		
		CreateDefect createTestWithTestSteps = new CreateDefect();
		
		
		
		String testSummary = "Sample Defect Nov 7 2017"; // Test
		String testDescription = "Sample Defect Description Nov 7 2017"; // Test


		
		String testId = createTestWithTestSteps.createTestCaseinJira(testSummary, testDescription);
		System.out.println("Defect ID " + testId);


	}

	public String createTestCaseinJira(String testSummary,
			String testDescription) throws IOException {
		
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
		Header header = new BasicHeader(HttpHeaders.AUTHORIZATION, authorizationHeader);
		return header;
	}

	
	private static JSONObject createTestCaseJSON(String testSummary, String testDescription) {
		JSONObject projectObj = new JSONObject();
		projectObj.put("id", projectId); // Project ID where the Test to be
		// Created

		JSONObject issueTypeObj = new JSONObject();
		issueTypeObj.put("id", issueTypeId); // IssueType ID which is Test isse
		// type


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
	
