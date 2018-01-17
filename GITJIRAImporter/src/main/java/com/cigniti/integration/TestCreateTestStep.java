package com.cigniti.integration;

import com.cigniti.JIRA.CreateTestWithTestSteps;

public class TestCreateTestStep {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String testStepDescription = "Click on Innerworkings Mexico  under Shop Online!@#$%^&*()_+~~`:;\"\"'{}[]|\\<>?/.,";
		testStepDescription = testStepDescription.replaceAll("[^\\x00-\\x7f]+", "");
		String testStepExpected ="Should see the Innerworkings Mexico  Materiales screenPer business-Display of the screen is enough as the settings are different .";
		testStepExpected = testStepExpected.replaceAll("[^\\x00-\\x7f]+", "");
		String jiraTestCaseId = "13502";
		CreateTestWithTestSteps createTestWithTestSteps = new CreateTestWithTestSteps();
		//			createTestWithTestSteps.createTestStepinJira(testStepDescription, "", testStepExpected, jiraTestCaseId);
		createTestWithTestSteps.uploadAttachmentsToJIRA("5990", "15090");

	}

}
