package com.cigniti.util;

public class FileOperations {
	
	public static void openFileinNotepad(String strFileName)
	{
		
		try {
			
			String cmds[] = new String[] { "cmd", "/c ","start","/max", "notepad", strFileName};
            Runtime.getRuntime().exec(cmds);
            
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
