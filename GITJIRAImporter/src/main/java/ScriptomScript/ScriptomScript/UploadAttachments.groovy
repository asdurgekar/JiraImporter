package ScriptomScript.ScriptomScript

import org.codehaus.groovy.scriptom.Scriptom

import com.cigniti.JIRA.CreateTestWithTestSteps
import com.cigniti.util.Globalvars
import org.codehaus.groovy.scriptom.ActiveXObject

class UploadAttachments {

	//			def pathString  = "^" + fpath + "^";
	//	def flib = ActiveXObject("Scripting.FileSystemObject")

	public void getAttachmentsFromALM(String testCaseId, String jiraTestCaseId) {
		//System.setProperty("java.library.path", "C:\\Users\\dinran\\Downloads\\Delete\\jacob\\");
		String sUserName = Globalvars.userName
		String sPassword = Globalvars.password
		String sProject = Globalvars.almProject
		String sDomain = Globalvars.almDomain
		//String fpath = Globalvars.almPath
		String url = Globalvars.almServerUrl;
		Scriptom.inApartment {
			def QCConnection = new ActiveXObject("TDApiOle80.TDConnection")
			try {

				QCConnection.InitConnectionEx(url)
				//Authenticate your user ID and Password
				QCConnection.Login(sUserName, sPassword)
				//Login to your Domain and Project
				QCConnection.Connect(sDomain, sProject)
				//Quit if QC Authentication fails
				if (QCConnection.LoggedIn != true) {
					System.out.println("QC User Authentication Failed");
				}
				else{
					System.out.println("Login successfull");
				}
				def TstFactory = QCConnection.TestFactory;
				def myfilter = TstFactory.Filter();
				def fpath = "Subject\\Completed - Do Not Use for Testing\\Coupa (Store) Testing 01202014\\Creating Reqs";
				def pathString  = "^" + fpath + "^";
				myfilter.Filter("TS_SUBJECT") == pathString
//				myfilter.Filter("TS_TEST_ID") == testCaseId
				System.out.println("Setting test case id in filter " + testCaseId);
				def TestList = myfilter.NewList()
				int testCaseCount = TestList.Count();
				System.out.println("Test Case Count " + TestList.Count());
				for (int index =1;index <=testCaseCount; index++)
				{
					def TestCase = TestList.Item(index);
					
					String testCaseIdFromALM = (String)TestCase.Field("TS_TEST_ID")
					if (testCaseIdFromALM.equals(testCaseId))
					{
						System.out.println(testCaseIdFromALM);
						System.out.println(TestCase.Field("TS_TEST_ID"));
						if (TestCase.HasAttachment() == true)
						{
							def objAttachmentList = TestCase.Attachments.NewList("")
							int testCaseAttachmentCount = objAttachmentList.Count();
							for (int testCaseAttachmentIndex =1;
							testCaseAttachmentIndex <=testCaseAttachmentCount;
							testCaseAttachmentIndex++)
							{
	
								def objExtStorage = objAttachmentList.Item(testCaseAttachmentIndex).AttachmentStorage()
								def objAttachmentName =  objAttachmentList.Item(testCaseAttachmentIndex).DirectLink()
								String clientPath = (String)objExtStorage.ClientPath();
								String name = (String)objAttachmentName;
								name = clientPath + "\\" + name;
								System.out.println(name);
								CreateTestWithTestSteps createTestWithTestSteps = new CreateTestWithTestSteps();
								createTestWithTestSteps.addAttachmentToIssue(jiraTestCaseId, name);
							}
						}
						def ODesignStepFactory = TestCase.DesignStepFactory()
						def designStepList = ODesignStepFactory.NewList("")
						int testStepCount = designStepList.Count();
						for (int testStepIndex =1;testStepIndex <=testStepCount; testStepIndex++)
						{
							def testStep = designStepList.Item(testStepIndex)
							if (testStep.HasAttachment() == true)
							{
								System.out.println("Found attachment at Test STep");
								def testStepAttachmentList = testStep.Attachments.NewList("")
								def tCount = testStepAttachmentList.Count();
								int testStepAttachmentCount = (int)tCount
								for (int testStepAttachmentIndex =1;
								testStepAttachmentIndex <=testStepAttachmentCount;
								testStepAttachmentIndex++)
								{
									def objExtStorage = testStepAttachmentList.Item(testStepAttachmentIndex).AttachmentStorage()
									def objAttachmentName =  testStepAttachmentList.Item(testStepAttachmentIndex).DirectLink()
									String clientPath = (String)objExtStorage.ClientPath();
									String name = (String)objAttachmentName;
									//name = clientPath + "\\" + name;
									System.out.println(name);
									CreateTestWithTestSteps createTestWithTestSteps = new CreateTestWithTestSteps();
									createTestWithTestSteps.addAttachmentToIssue(jiraTestCaseId, name);
								}
							}
						}
	
					}
				}
				System.out.println(TestList.class);

			}catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				QCConnection.Disconnect();
			}
		}
	}
}

/*
 sUserName = "dinran"
 sPassword = "AnuAna27!"
 QCConnection.InitConnectionEx "http://rapvhqhpqc2:8080/qcbin"
 'Authenticate your user ID and Password
 QCConnection.Login sUserName, sPassword
 sProject = "IT13_757_Coupa_Integration"
 sDomain = "DEFAULT"
 'Login to your Domain and Project
 QCConnection.Connect sDomain, sProject
 'Quit if QC Authentication fails
 If (QCConnection.LoggedIn <> True) Then
 MsgBox "QC User Authentication Failed"
 End
 End If
 */
