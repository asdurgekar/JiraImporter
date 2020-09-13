package com.cigniti.util;

import java.io.File;

public class FileOperations {
	
	public static void openFileinNotepad(String strFileName)
	{
		
		try {
			
			String strStartCommand = "";
    		if(System.getProperty("os.name").contains("Windows"))
    		{
    			strStartCommand = "cmd /c start /max notepad";
    			String command = strStartCommand + strFileName;
    			Runtime rt = Runtime.getRuntime();
    			Process pr = rt.exec(command);
    			
    		}
    		else if(System.getProperty("os.name").contains("Mac"))
    		{
    			strStartCommand = "/usr/bin/open";
    			Runtime.getRuntime().exec(new String[]{strStartCommand,strFileName});
    			
    		}
    		
            
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
