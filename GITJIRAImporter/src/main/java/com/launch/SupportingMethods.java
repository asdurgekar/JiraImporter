package com.launch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import javax.swing.SwingWorker;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.cigniti.util.Globalvars;
import com.cigniti.util.TextOutputFile;

public class SupportingMethods{
	
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
		else if((new File(Globalvars.strLocalLocation + Globalvars.strOldExeFileName)).exists()) 
		{
			String command = "cmd /c start " + Globalvars.strLocalLocation + Globalvars.strOldExeFileName;
			Runtime rt = Runtime.getRuntime();
			Process pr = rt.exec(command);
		}
		else
		{
			TextOutputFile.writeToLog("Local Core exe file is missing in path : " + Globalvars.strLocalExePath);
		}
		//Exit for Version dialog
		System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
	}
	
	public void copyFiles_Launch() {

		try {
			copyFilesFromCloud();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		
	}

	public void copyFilesFromCloud() {
		
		
		
		try {
			bgWorker = new SwingWorker<Void, String>(){
				

				@Override
				protected Void doInBackground() throws Exception {
					// TODO Auto-generated method stub
					publish("Start Copying");
					
					Boolean bln_downSuccess = true;
					//Check if source location it is pointing to cloud
					if(Globalvars.strCloudLocation.contains("https:"))
					{
						
						bln_downSuccess = downloadFileFromCloud(Globalvars.strCloudExePath,Globalvars.strLocalExePath);
						if(!bln_downSuccess)
						{
							throw new Exception("Unable to download from cloud location: '" + Globalvars.strCloudExePath + "'");
						}
						bln_downSuccess = downloadFileFromCloud(Globalvars.strCloudVersionPath,Globalvars.strLocalVersionPath);
						if(!bln_downSuccess)
						{
							throw new Exception("Unable to download from cloud location: '" + Globalvars.strCloudVersionPath + "'");
						}
					}
					//otherwise source location pointing to Shared path in network
					else
					{
						File destination = new File(Globalvars.strLocalLocation);
						//copy exe file to local location
						File source = new File(Globalvars.strCloudExePath);			
						FileUtils.copyFileToDirectory(source, destination);
						
						//copy version file to local location
						source = new File(Globalvars.strCloudVersionPath);			
						FileUtils.copyFileToDirectory(source, destination);
					}
					TextOutputFile.writeToLog("Files copied from cloud successfully");
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
						TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
					}
					
				}
				
			};			
			bgWorker.execute();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		
	}

	protected Boolean downloadFileFromCloud(String str_CloudPath, String str_LocalPath) {
		
		try {
			String link = str_CloudPath;
			URL url  = new URL(str_CloudPath);
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			Map< String, List< String >> header = http.getHeaderFields();
			while( isRedirected( header )) {
				link = header.get( "Location" ).get( 0 );
				url    = new URL( link );
				http   = (HttpURLConnection)url.openConnection();
				header = http.getHeaderFields();
			}
			InputStream  input  = http.getInputStream();
			byte[]       buffer = new byte[4096];
			int          n      = -1;
			OutputStream output = new FileOutputStream( new File( str_LocalPath ));
			while ((n = input.read(buffer)) != -1) {
				output.write( buffer, 0, n );
			}
			output.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			return false;
		} 
		return true;
	}

	public Boolean verifyToolVersion(String str_CloudVersion) {
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
			
				//Compare with cloud version of Tool
				//if not the same version return false
				if(!localversion.trim().equals(str_CloudVersion.trim()))
				{
					return false;
				}
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		return true;
	}

	public String getCommonDriveCloudVersion() {

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
				TextOutputFile.writeToLog("Version file on cloud is corrupted");
				System.exit(0);
			}
			else if(content.trim().split("=").length <= 1)
			{
				TextOutputFile.writeToLog("Version file on cloud is corrupted");
				System.exit(0);
			}
			else
			{
				cloudversion = content.trim().split("=")[1];				
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			return cloudversion;
		}
		return cloudversion;
	}

	public String getLatestReleaseInfo() {
		
		StringBuilder contentBuilder = new StringBuilder();
		try {
		    BufferedReader in = new BufferedReader(new FileReader(Globalvars.strCloudLocation + Globalvars.strLatestReleaseInfoFileName));
		    String str;
		    while ((str = in.readLine()) != null) {
		        contentBuilder.append(str);
		    }
		    in.close();
		} catch (IOException e) {
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		String content = contentBuilder.toString();
		return content;
	}
	
	public String getReleaseHistory() {
		
		StringBuilder contentBuilder = new StringBuilder();
		try {
		    BufferedReader in = new BufferedReader(new FileReader(Globalvars.strCloudLocation + Globalvars.strReleaseHistoryFileName));
		    String str;
		    while ((str = in.readLine()) != null) {
		        contentBuilder.append(str);
		    }
		    in.close();
		} catch (IOException e) {
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		String content = contentBuilder.toString();
		return content;
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
				TextOutputFile.writeToLog("Local Version file on cloud is corrupted");
				try {
					throw new Exception("Local Version file on cloud is corrupted");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
				}
			}
			else if(content.trim().split("=").length <= 1)
			{
				TextOutputFile.writeToLog("Local Version file on cloud is corrupted");
				try {
					throw new Exception("Local Version file on cloud is corrupted");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
				}
			}
			else
			{
				localversion = content.trim().split("=")[1];				
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			return localversion;
		}
		return localversion;
		
	}

	public void displayReleaseInfo(String strClickedText) {
		
		//If clicked on View Release History, then switch content and text for Jlabel
		if(strClickedText.contains("View Release History"))
		{
			LaunchJiraTestCaseImporter.txtpnncolor.setText(LaunchJiraTestCaseImporter.strReleaseHistory);
			LaunchJiraTestCaseImporter.txtpnncolor.setCaretPosition(0);
			LaunchJiraTestCaseImporter.lblToggleReleaseInfo.setText("<html><a href=''>View Latest Version Info</a></html>");
		}
		else if(strClickedText.contains("View Latest Version Info"))
		{
			LaunchJiraTestCaseImporter.txtpnncolor.setText(LaunchJiraTestCaseImporter.strLatestReleaseInfo);
			LaunchJiraTestCaseImporter.txtpnncolor.setCaretPosition(0);
			LaunchJiraTestCaseImporter.lblToggleReleaseInfo.setText("<html><a href=''>View Release History</a></html>");
		}
		//If clicked on View Latest Version Info, then switch content and text for Jlabel 
		
	}
	
	public String getGitHubReleaseInfo() {
		
		String releaseinfo = "";
		try {
		  
		  String responseString = getGitHubFileContent(Globalvars.strCloudLocation + Globalvars.strLatestReleaseInfoFileName);
		  releaseinfo = responseString;	
		  TextOutputFile.writeToLog("Latest Version information is retrieved successfully from Cloud");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		return releaseinfo;
	}


	public String getGitHubVersion() {
		// TODO Auto-generated method stub
		
		String version = "";
		try {
		  
		  String responseString = getGitHubFileContent(Globalvars.strCloudVersionPath);
		  if(responseString==null || !responseString.trim().contains("="))
			{
			  	TextOutputFile.writeToLog("Version file on cloud is corrupted");
				System.exit(0);
			}
			else if(responseString.trim().split("=").length <= 1)
			{
				TextOutputFile.writeToLog("Version file on cloud is corrupted");
				System.exit(0);
			}
		  version = responseString.trim().split("=")[1];
		  TextOutputFile.writeToLog("Cloud version number is retrieved successfully");
		  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
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
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
			
		}
	  	return responseString;
	
	}

	
	
	private static boolean isRedirected( Map<String, List<String>> header ) 
	{
	      for( String hv : header.get( null )) {
	         if(   hv.contains( " 301 " )
	            || hv.contains( " 302 " )) return true;
	      }
	      return false;
	}

	public String getGitHubReleaseHistory() {

		String releaseHistory = "";
		try {
		  
		  String responseString = getGitHubFileContent(Globalvars.strCloudLocation + Globalvars.strReleaseHistoryFileName);
		  releaseHistory = responseString;	
		  TextOutputFile.writeToLog("Release History is retrieved from Cloud successfully");
		  

		} catch (Exception e) {
			// TODO Auto-generated catch block
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		return releaseHistory;
	}

	public void createLaunchLogFile() {

		try {
			TextOutputFile.createFile(Globalvars.strLaunchLogPath);
			Globalvars.strLogPath = Globalvars.strLaunchLogPath;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			TextOutputFile.writeToLog(ExceptionUtils.getStackTrace(e));
		}
		
	}



	

}
