package com.cigniti.util;

import java.io.IOException;

public class FileOperations {
	
	public static void openFileinNotepad(String strFileName)
	{
		
		try {
			
			String cmds[] = new String[] { "cmd", "/c ","start","/max", "notepad", strFileName};
            Runtime.getRuntime().exec(cmds);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
