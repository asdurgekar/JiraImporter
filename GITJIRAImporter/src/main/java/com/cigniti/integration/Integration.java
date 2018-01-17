package com.cigniti.integration;

import com.cigniti.ALMGetTestCase;
import com.cigniti.util.Globalvars;

public class Integration {

	public static void main(String args[])
	{
		ALMGetTestCase newDownload = null;
		System.out.println("Start Time" + System.currentTimeMillis());

		try {
			newDownload = new ALMGetTestCase(Globalvars.almServerUrl, Globalvars.almDomain,
					Globalvars.almProject, null, null);
			String callTestsFolderId = newDownload.getFolderId(Globalvars.almPath, "test-folder");
			System.out.println(callTestsFolderId);
			newDownload.GetTestCasesInFolder(callTestsFolderId,Globalvars.almPath);
		} catch (Exception e1) {
			System.out.println("Failed to login to ALM.Verify your ALM config and credentials");
			e1.printStackTrace();
		}
		System.out.println("End Time" + System.currentTimeMillis());
	}
}
