package com.cigniti.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class ReadAttachmentsMapping {

	private static ReadAttachmentsMapping readAttachmentsMapping = null;
	HashMap<String, List<String>> attachmentsMapping = new HashMap<String, List<String>>();

	private ReadAttachmentsMapping() {

	}

	public static ReadAttachmentsMapping getInstance() {
		if (readAttachmentsMapping == null) {
			readAttachmentsMapping = new ReadAttachmentsMapping();
			readAttachmentsMapping.readAttachmentMappingData();
		}
		return readAttachmentsMapping;
	}

	private void readAttachmentMappingData() {
		String csvFile = Globalvars.DownloadPath;
		try {
			CSVParser csvFileParser = CSVFormat.DEFAULT.parse(new FileReader(new File(csvFile)));
			for (CSVRecord csvRecord : csvFileParser) {
				List<String> list = getAttachmentsForTestCase(csvRecord.get(0));
				list.add(csvRecord.get(1));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
/*		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		try {
			br = new BufferedReader(new FileReader(csvFile));
			int count = 0;
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] attachmentData = line.split(cvsSplitBy);
				List<String> list = getAttachmentsForTestCase(attachmentData[0]);
				list.add(attachmentData[1]);
				count++;
			}
			System.out.println("Total Count " + count);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
*/
	}

	public List<String> getAttachmentsForTestCase(String testCaseId) {
		List<String> returnList = attachmentsMapping.get(testCaseId);
		if (returnList == null) {
			returnList = new ArrayList<String>();
			attachmentsMapping.put(testCaseId, returnList);
			//System.out.println( "Adding new list" + testCaseId);
		}
		return returnList;
	}

	public static void main(String[] args) {
		ReadAttachmentsMapping mapping = ReadAttachmentsMapping.getInstance();
		System.out.println(((List<String>)mapping.getAttachmentsForTestCase("57")).size());
		System.out.println(((List<String>)mapping.getAttachmentsForTestCase("2537")).size());
	}

}
