package ScriptomScript.ScriptomScript

import org.codehaus.groovy.scriptom.Scriptom
import com.cigniti.util.Globalvars
import org.codehaus.groovy.scriptom.ActiveXObject

class MainTest {

	static main(args) {
		//System.setProperty("java.library.path", "C:\\Users\\dinran\\Downloads\\Delete\\jacob\\");
		Scriptom.inApartment
		{
			def QCConnection = new ActiveXObject("TDApiOle80.TDConnection")
			def sUserName = Globalvars.userName
			def sPassword = Globalvars.password
			def sProject = "IT13_757_Coupa_Integration"
			def sDomain = "DEFAULT"
			def fpath = "Subject\\Completed - Do Not Use for Testing\\Coupa (Store) Testing 01202014\\Creating Reqs";

			try
			{
				QCConnection.InitConnectionEx("http://rapvhqhpqc2:8080/qcbin")
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
				def pathString  = "^" + fpath + "^";
				myfilter.Filter("TS_SUBJECT") == pathString
				def TestList = myfilter.NewList()
				//	def flib = ActiveXObject("Scripting.FileSystemObject")
				int testCaseCount = TestList.Count();
				System.out.println(TestList.Count());
				for (int index =1;index <=testCaseCount; index++)
				{
					def TestCase = TestList.Item(index);
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
								name = clientPath + "\\" + name; 
								System.out.println(name);
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


			//Visibility should be set to FALSE for production.
			//xlApp.Visible = debug

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
