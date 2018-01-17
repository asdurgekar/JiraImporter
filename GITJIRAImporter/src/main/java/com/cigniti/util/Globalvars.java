package com.cigniti.util;

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
	
	
	public static String JIRA_userName = "akarsh.durgekar@rentacenter.com";
	public static String JIRA_password = "Cign1234";
	public static String JIRA_projectId = "10357";
	public static String JIRA_issueTypeId = "10300";
	
	public static String ExcelSheetPath = "";
	public static String ExcelWorkSheetName = "";
	
	public static String strPreferencesPath = System.getenv("APPDATA") + "\\JiraImporter\\Preferences.config";
	

	
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

