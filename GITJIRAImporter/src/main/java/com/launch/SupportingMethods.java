package com.launch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.SwingWorker;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.cigniti.JIRA.CreateTestWithTestSteps;
import com.cigniti.JIRA.ImportTestCases;
import com.cigniti.util.Globalvars;
import com.cigniti.util.UtilFunctions;
import com.onedrive4j.AccessToken;
import com.onedrive4j.Drive;
import com.onedrive4j.DriveItem;
import com.onedrive4j.DriveService;
import com.onedrive4j.OneDrive;
import com.onedrive4j.Scope;

public class SupportingMethods {
	
	
	private SwingWorker<Void, String> bgWorker;
	
	public void LaunchTool()
	{
		try {
		@SuppressWarnings("unused")
		File file = new File(Globalvars.strLocalExePath);
		if(file.exists()) 
		{
			String command = "cmd /c start " + Globalvars.strLocalExePath;
			Runtime rt = Runtime.getRuntime();
			Process pr = rt.exec(command);
		}
		else
		{
			System.out.println("Local Core exe file is missing in path : " + Globalvars.strLocalExePath);
		}
		//Exit for Version dialog
		System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void copyFiles_Launch() {

		try {
			copyFilesFromCloud();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void copyFilesFromCloud() {
		
		
		
		try {
			bgWorker = new SwingWorker<Void, String>(){
				

				@Override
				protected Void doInBackground() throws Exception {
					// TODO Auto-generated method stub
					publish("Start Copying");
					
					File destination = new File(Globalvars.strLocalLocation);
					//copy exe file to local location
					File source = new File(Globalvars.strCloudExePath);			
					FileUtils.copyFileToDirectory(source, destination);
					
					//copy version file to local location
					source = new File(Globalvars.strCloudVersionPath);			
					FileUtils.copyFileToDirectory(source, destination);
					publish("Complete");
					return null;
				}
				
				@Override
				protected void process(List<String> chunks) {
					
										
					//change UI components
					for(String line:chunks)
					{
						
						if(line.contains("Complete"))
						{
							LaunchJiraTestCaseImporter.lblCopyLoading.setVisible(false);
						}
						else
						{
							LaunchJiraTestCaseImporter.lblCopyLoading.setVisible(true);
						}
						
					}
					
					
				}
				
				@Override
				protected void done() {
					// TODO Auto-generated method stub
					//super.done();
					//after task complete					
					try {
						
						Thread.sleep(1500);
						LaunchTool();
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			};			
			bgWorker.execute();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public Boolean verifyToolVersion() {
		// TODO Auto-generated method stub
		
		try {
			
			//if folder is not present, create it and return false	
			// and
			//if folder is present and file is not present, return false
			File file = new File(Globalvars.strLocalVersionPath);
			if(!file.exists()) 
			{ 
				//If not, create the folder and file
				file.getParentFile().mkdirs();				
				return false;
			}
			//if file exists but not data					
			String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
			if(content==null || !content.trim().contains("="))
			{
				return false;
			}
			else
				//compare version
			{	
				//Get local version of Tool				
				if(content.trim().split("=").length <= 1)
				{
					return false;
				}
				String localversion = content.trim().split("=")[1];
			
				//Get cloud version of Tool
				file = new File(Globalvars.strCloudVersionPath);		
				content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
				if(content==null || !content.trim().contains("="))
				{
					System.out.println("Version file on cloud is corrupted");
					System.exit(0);
				}
				else if(content.trim().split("=").length <= 1)
				{
					System.out.println("Version file on cloud is corrupted");
					System.exit(0);
				}
				else
				{
					String cloudversion = content.trim().split("=")[1];
					
					//if not the same version return false
					if(!localversion.trim().equals(cloudversion.trim()))
					{
						return false;
					}
				}
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public String getCloudVersion() {

		String cloudversion = null; 
		try {
			
			//if folder is not present, create it and return false	
			// and
			//if folder is present and file is not present, return false
			File file = new File(Globalvars.strCloudVersionPath);
			String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
			content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
			if(content==null || !content.trim().contains("="))
			{
				System.out.println("Version file on cloud is corrupted");
				System.exit(0);
			}
			else if(content.trim().split("=").length <= 1)
			{
				System.out.println("Version file on cloud is corrupted");
				System.exit(0);
			}
			else
			{
				cloudversion = content.trim().split("=")[1];				
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return cloudversion;
		}
		return cloudversion;
	}

	public String getReleaseInfo() {
		
		StringBuilder contentBuilder = new StringBuilder();
		try {
		    BufferedReader in = new BufferedReader(new FileReader(Globalvars.strCloudLocation + Globalvars.strReleaseInfoFileName));
		    String str;
		    while ((str = in.readLine()) != null) {
		        contentBuilder.append(str);
		    }
		    in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String content = contentBuilder.toString();
		return content;
	}
	
	public String getGitHubReleaseInfo() {
		
		String releaseinfo = "";
		try {
		  
		  String link ="https://raw.githubusercontent.com/asdurgekar/JiraImporterCodeBase/master/ReleaseNotes.html";
		  		  
		  String responseString = getGitHubFileContent(link);
		  releaseinfo = responseString;	
		  System.out.println(releaseinfo);
		  

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return releaseinfo;
	}

	public String fnGetLocalVersion(){

		String localversion = null; 
		try {
			
			//if folder is not present, create it and return false	
			// and
			//if folder is present and file is not present, return false
			File file = new File(Globalvars.strLocalVersionPath);
			String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
			content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
			if(content==null || !content.trim().contains("="))
			{
				System.out.println("Local Version file on cloud is corrupted");
				try {
					throw new Exception("Local Version file on cloud is corrupted");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(content.trim().split("=").length <= 1)
			{
				System.out.println("Local Version file on cloud is corrupted");
				try {
					throw new Exception("Local Version file on cloud is corrupted");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				localversion = content.trim().split("=")[1];				
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return localversion;
		}
		return localversion;
		
	}

	public String getGDriveVersion() {
		
		// TODO Auto-generated method stub
		
		
		
		return null;
	}

	public String getOneDriveVersion() {
		
		try {
			
			Properties prop = UtilFunctions.loadApiSettings();
			OneDrive oneDrive = new OneDrive(prop.getProperty("client_id"), prop.getProperty("client_secret"), prop.getProperty("callback_url"));
			DriveService driveService = oneDrive.getDriveService();
	
			String authzUrl = oneDrive.authorize(new Scope[] { Scope.SKYDRIVE_UPDATE,  Scope.SKYDRIVE });
		
		
			System.out.println("Follow this URL to authorise yourself on OneDrive");
	        System.out.println(authzUrl);
	        System.out.println("Paste in the access code from the URL that you got redirected to:");
	        System.out.print(">>");	        
			Scanner scanner = new Scanner(System.in);
			String code = scanner.nextLine();
			
			AccessToken token = oneDrive.getAccessToken(code);
	        //AccessToken token = oneDrive.getAccessTokenfromAuth(new Scope[] { Scope.SKYDRIVE_UPDATE,  Scope.SKYDRIVE });
	       // AccessToken token = oneDrive.getAccessToken_Latest(prop.getProperty("tenant_id"), new Scope[] { Scope.SKYDRIVE_UPDATE,  Scope.SKYDRIVE });
			System.out.println("UserId: " + token.getUserId());
			System.out.println("AccessToken: " + token.getAccessToken());
			
			System.out.println("==============");
			System.out.print("READ:: OneDrive Drives");
			List<Drive> drives = driveService.getDrives(token.getAccessToken());
			for (Drive d : drives) {
				System.out.println(d.toString());
			}

			System.out.println("==============");
			System.out.print("READ:: OneDrive Drive Items");
			List<DriveItem> items = driveService.getRootItems(token.getAccessToken(), drives.get(0).id);
			for (DriveItem i : items) {
				System.out.println("ID: " + i.name.toString() + "\n" +
								"Name: " + i.name.toString() + "\n" + 
							"DownloadURL: " + i.downloadUrl);
			}

			//scanner.close();					
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
public String getOneDriveVersion_Latest() {
		
		try {
			
			Properties prop = UtilFunctions.loadApiSettings();
			OneDrive oneDrive = new OneDrive(prop.getProperty("client_id"), prop.getProperty("client_secret"), prop.getProperty("callback_url"));
			DriveService driveService = oneDrive.getDriveService();
	
			/*
			String authzUrl = oneDrive.authorize(new Scope[] { Scope.SKYDRIVE_UPDATE,  Scope.SKYDRIVE });
		
		
			System.out.println("Follow this URL to authorise yourself on OneDrive");
	        System.out.println(authzUrl);
	        System.out.println("Paste in the access code from the URL that you got redirected to:");
	        System.out.print(">>");	        
			Scanner scanner = new Scanner(System.in);
			String code = scanner.nextLine();
			*/
			//AccessToken token = oneDrive.getAccessToken(code);
	        //AccessToken token = oneDrive.getAccessTokenfromAuth(new Scope[] { Scope.SKYDRIVE_UPDATE,  Scope.SKYDRIVE });
	        AccessToken token = oneDrive.getAccessToken_Latest(prop.getProperty("tenant_id"), new Scope[] { Scope.SKYDRIVE_UPDATE,  Scope.SKYDRIVE });
			System.out.println("UserId: " + token.getUserId());
			System.out.println("AccessToken: " + token.getAccessToken());
			
			System.out.println("==============");
			System.out.print("READ:: OneDrive Drives");
			List<Drive> drives = driveService.getDrives_Latest(token.getAccessToken());
			for (Drive d : drives) {
				System.out.println(d.toString());
			}

			System.out.println("==============");
			System.out.print("READ:: OneDrive Drive Items");
			List<DriveItem> items = driveService.getRootItems(token.getAccessToken(), drives.get(0).id);
			for (DriveItem i : items) {
				System.out.println("ID: " + i.name.toString() + "\n" +
								"Name: " + i.name.toString() + "\n" + 
							"DownloadURL: " + i.downloadUrl);
			}

			//scanner.close();					
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public String getGitHubVersion() {
		// TODO Auto-generated method stub
		
		String version = "";
		try {
		  
		  String link ="https://raw.githubusercontent.com/asdurgekar/JiraImporterCodeBase/master/version.ini";
		  		  
		  String responseString = getGitHubFileContent(link);
		  version = responseString.replace("Version=","").trim();	
		  System.out.println(version);
		  

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return version;
	
	}
	

	public String getGitHubFileContent(String URL) {
		// TODO Auto-generated method stub
		
		String responseString = "";
		try {
		  

		  HttpClient client = new DefaultHttpClient();
		  HttpGet request = new HttpGet(URL);
		  HttpResponse response = client.execute(request);
		  responseString = new BasicResponseHandler().handleResponse(response);
		  

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	  	return responseString;
	
	}

	

}
