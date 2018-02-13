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
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
public class GetListofCycles {

	private static String API_CREATE_TEST = "{SERVER}/rest/api/2/issue";
	private static String API_CREATE_TEST_STEP = "{SERVER}/public/rest/api/1.0/teststep/";
	private static Long versionId = -1l;
	//private static String API_CYCLES = "{SERVER}/public/rest/api/1.0/cycles/search?versionId=" + versionId + "&projectId=10357";
	//private static String API_CYCLES = "{SERVER}/public/rest/api/1.0/zlicense/addoninfo";
	private static String API_CYCLES = "{SERVER}/public/rest/api/1.0/teststep/-1?projectId=10357";
	//private static String API_CYCLES = "{SERVER}/public/rest/api/1.0/zql/search";
	
	
	/** Declare JIRA,Zephyr URL,access and secret Keys */
	// Jira Cloud URL for the instance
	public static String jiraBaseURL = "https://rentacenter.atlassian.net";
	// Replace zephyr baseurl <ZAPI_Cloud_URL> shared with the user for ZAPI
	// Cloud Installation
	private static String zephyrBaseUrl = "https://prod-api.zephyr4jiracloud.com/connect";
	// zephyr accessKey , we can get from Addons >> zapi section
	public static String accessKey = "N2RjOTAxZWUtYTc2Mi0zMzkzLWEwYmYtZWIxNjM2ZmM3MjAxIGFrYXJzaC5kdXJnZWthciBVU0VSX0RFRkFVTFRfTkFNRQ";
	
	// zephyr secretKey , we can get from Addons >> zapi section
	public static String secretKey = "xEWGV8W7J1eiu2glXJS_ChSUntc-hIS_f_39i5w_B3M";

	/** Declare parameter values here */

	public static String userName = Globalvars.JIRA_userName;
	public static String password = Globalvars.JIRA_password;
	public static String projectId = Globalvars.JIRA_projectId;
	private static String issueTypeId = Globalvars.JIRA_issueTypeId;
	
	private static final String createTestUri = API_CREATE_TEST.replace("{SERVER}", jiraBaseURL);
	private static final String createTestStepUri = API_CREATE_TEST_STEP.replace("{SERVER}", zephyrBaseUrl);
	private static final String getCyclesUri = API_CYCLES.replace("{SERVER}", zephyrBaseUrl);
	
	static ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(zephyrBaseUrl, accessKey, secretKey, userName)
			.build();
	static Header header = createAuthorizationHeader();
	// JwtGenerator jwtGenerator = client.getJwtGenerator();
	public static String RespMessage = "Success~All Test Cases are imported successfully";
	

	public static void main(String[] args) throws JSONException, URISyntaxException, ParseException, IOException {

		HttpEntity entity = null;
		String finalURL = getCyclesUri;
		URI uri = new URI(finalURL);
		int expirationInSec = 360;
		JwtGenerator jwtGenerator = client.getJwtGenerator();
		
		//GET
//		String jwt = jwtGenerator.generateJWT("GET", uri, expirationInSec);		
//		HttpResponse responseConfig = null;
//		HttpGet ConfigReq = new HttpGet(uri);
//		ConfigReq.setHeader("Authorization", jwt);
//		ConfigReq.setHeader("zapiAccessKey", accessKey);
		
		
		//POST
		String jwt = jwtGenerator.generateJWT("POST", uri, expirationInSec);
		HttpResponse responseConfig = null;
		HttpPost ConfigReq = new HttpPost(uri);
		ConfigReq.setHeader("Authorization", jwt);
		ConfigReq.setHeader("zapiAccessKey", accessKey);
		JSONObject testStepJsonObj = createTestStepJSON("StepFromCode", "DataFromCode", "ResultFromCode");
		ConfigReq.setEntity(new StringEntity(testStepJsonObj.toString()));
		
		
		try {
			HttpClient restClient = new DefaultHttpClient();
			responseConfig = restClient.execute(ConfigReq);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int statusCode = getHTTPResponseCode(responseConfig);

		if (statusCode >= 200 && statusCode < 300) {
			entity = responseConfig.getEntity();
			String string = null;
			try {
				string = EntityUtils.toString(entity);
				JSONObject testStepObj = new JSONObject(string);
//				testStepObj.getString("id");
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			try {
				System.out.println("Failure~" + EntityUtils.toString(responseConfig.getEntity()));
				RespMessage = "Failure~" + EntityUtils.toString(responseConfig.getEntity());
				throw new ClientProtocolException("Unexpected response status: " + statusCode);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			}
		}
		System.out.println("General Configuration details retrieved");

		
	}


	private static int getHTTPResponseCode(HttpResponse response) {
		int statusCode = response.getStatusLine().getStatusCode();
		return statusCode;
	}

	public static Header createAuthorizationHeader() {
		byte[] bytesEncoded = Base64.encodeBase64((userName + ":" + password).getBytes());
		String authorizationHeader = "Basic " + new String(bytesEncoded);
		Header header = new BasicHeader(HttpHeaders.AUTHORIZATION, authorizationHeader);
		return header;
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
	
}