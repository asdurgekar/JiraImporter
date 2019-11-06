package com.cigniti.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class Globalvars {
	public static String fSeperator=System.getProperty("file.separator");
	public static String workSpacePath=System.getProperty("user.dir")+fSeperator;
	public static String configPath=workSpacePath+"Config"+fSeperator;
	public static String currentPlatform;
	public static String currentOS;
	public static String currentEnvironment;
	public static String objRepoPath=workSpacePath+"ObjectRepository"+fSeperator;
	public static String dataDirPath=workSpacePath+"TestData"+fSeperator;
	public static String fSplitSeperator=Pattern.quote(System.getProperty("file.separator"));
	public static String fileExt;
//	public static String downloadsDirPath="c:\\temp\\testDownloads\\";
	public static String myAgrmntsDateFormat="MM/dd/yyyy";
	public static boolean mobileExecution;
	public static String currentResultsFolder;
	public static String outputDirPath=workSpacePath+"TestOutput"+fSeperator;
	public static String resultsDirPath=outputDirPath+"TestExecutionResults"+fSeperator;
	public static String SavedDataDirPath;
	public static String failedTCDirPath;
	
	
	public static String almServerUrl = "http://rapvhqhpqc2:8080/qcbin";
	public static String almDomain = "DEFAULT";
	public static String almProject = "IT13_757_Coupa_Integration";//"eCommerce";//IT13_757_Coupa_Integration
//	public static String callTestsFolderName = "Subject";
	public static String userName = "dinran";
	public static String password = "";
	//public static String almPath = "Subject\\Supply Chain Sprints and Stories";
	public static String almPath = "Subject";//Withdraw Requisition
	public static String DownloadPath = "C:\\temp\\almDownloads\\attachments.csv";
	public static DateFormat NowDateform= new SimpleDateFormat("yyyyMMdd_HHmmss");
	public static DateFormat LogTimeform= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	
	public static String JIRA_URL = "";
	public static String JIRA_userName = "";
	public static String JIRA_password = "";
	public static String JIRA_projectId = "";
	public static String JIRA_issueTypeId = "";
	public static String JIRA_RC_issueTypeId = "10300";
	public static String JIRA_GS_issueTypeId = "11518";
	public static String JIRA_RC_SprintCustomField = "customfield_10103";
	public static String JIRA_GS_SprintCustomField = "customfield_10004";
	
	public static String ExcelSheetPath = "";
	public static String ExcelWorkSheetName = "";
	public static double TotalTestCaseCount = 0;
	public static int TotalTestCaseUploaded = 0;
	public static int TotalTestCaseFailed = 0;
	
	//public static String strCloudLocation = "\\\\racns\\department_3$\\InfTech\\Jira\\JiraTestCaseImporter\\";
	public static String strCloudLocation = "https://raw.githubusercontent.com/asdurgekar/JiraImporterCodeBase/master/";	
	public static String strLocalLocation = System.getenv("APPDATA") + "\\JiraImporter\\";
	public static String strLogLocation = System.getenv("LOCALAPPDATA") + "\\JiraImporter\\Logs\\";
	public static String strLaunchLogFileName = "JiraImporter_Launch_" + NowDateform.format(new Date()) + ".log";
	public static String strCoreLogFileName = "JiraImporter_Core_" + NowDateform.format(new Date()) + ".log";
	public static String strPreferencesConfigFileName = "Preferences.config";
	public static String strOldExeFileName = "JiraTestCaseImporterCore.exe";
	public static String strExeFileName = "JiraTestCaseImporterCore.jar";
	public static String strVersionFileName = "version.ini";
	public static String strLatestReleaseInfoFileName = "ReleaseNotes.html";
	public static String strReleaseHistoryFileName = "ReleaseHistory.html";
	public static String strPreferencesPath = strLocalLocation + strPreferencesConfigFileName;
	public static String strCloudVersionPath = strCloudLocation + strVersionFileName;
	public static String strCloudExePath = strCloudLocation + strExeFileName;
	public static String strLocalVersionPath = strLocalLocation + strVersionFileName;
	public static String strLocalExePath = strLocalLocation + strExeFileName;
	public static String strLaunchLogPath = strLogLocation + strLaunchLogFileName;
	public static String strCoreLogPath = strLogLocation + strCoreLogFileName;
	public static String strLogPath = "";
	public static String strCertPath = "./resources/certs/cacerts";
	public static String strAESSecretKey = "JiraPassw0rdKey";
	//System.getProperty("user.dir").replace("\\", "/")+ 
	
		
	public static void initCustomVars(){
		//fileExt="."+EnvironmentVariables.getUserVariable("TestCaseExtension");
		//currentEnvironment=EnvironmentVariables.getUserVariable("ExecutionEnvironment");
		//currentPlatform=EnvironmentVariables.getPlatform();
		//currentOS=EnvironmentVariables.getOS(currentPlatform);
		//mobileExecution=EnvironmentVariables.getUserVarBoolean("ExecuteOnMobile");
		if(currentPlatform.equalsIgnoreCase("Linux")){
			fSplitSeperator="/";
		}
		//currentResultsFolder=EnvironmentVariables.getCurrentResultsFolder(resultsDirPath)+fSeperator;
		SavedDataDirPath=currentResultsFolder+"SavedData"+fSeperator;
		failedTCDirPath=currentResultsFolder+"FailedTestcase"+fSeperator;
		}
	}

