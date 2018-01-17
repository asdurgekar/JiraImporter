package com.cigniti.util;

import java.io.File;
import java.io.FilenameFilter;

public class FindFiles {

	public static void main(String[] args) throws Exception {

		File[] fileList = getFileList("C:\\temp\\testDownloads");

		for (File file : fileList) {
			System.out.println(file.getName());
			System.out.println(file.getAbsolutePath());
		}
	}

	private static File[] getFileList(String dirPath) {
		File dir = new File(dirPath);
		
		File[] fileList = dir.listFiles();

		/*File[] fileList = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".json");
			}
		});*/
		return fileList;
	}

}
