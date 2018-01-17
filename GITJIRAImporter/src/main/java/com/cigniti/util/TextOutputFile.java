package com.cigniti.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import static com.cigniti.util.Globalvars.*;

public class TextOutputFile {
	private static TextOutputFile outputJsonInstance = null;
	String outputDirectorypath;
	JSONObject savedObjects;
	JSONObject currentTCObjects;
	JSONParser fileParser;
	//JSONObject jsonObjects;
	String outputFileName="Output.json";
	FileWriter outputFile;
	
	protected TextOutputFile() {
		this.outputDirectorypath=currentResultsFolder;
		this.savedObjects = new JSONObject();
		this.currentTCObjects=new JSONObject();
		//fileParser = new JSONParser();
	}
	
	public static TextOutputFile getInstance() {
	      if(outputJsonInstance == null) {
	    	  outputJsonInstance = new TextOutputFile();
	      }
	      return outputJsonInstance;
	   }
	
	@SuppressWarnings("unchecked")
	public void writeKVToJson(String key,String value,boolean saveUnderTestCase){
		
		if(saveUnderTestCase){
			this.currentTCObjects.put(key, value);
			this.savedObjects.put(key, value);
		}
		else{
			this.savedObjects.put(key, value);
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	public void writeMapToJson(String key,Map<String,String> outputMap,boolean saveUnderTestCase){
		
		if(saveUnderTestCase){
			this.currentTCObjects.put(key, outputMap);
		}
		else{
			this.savedObjects.put(key, outputMap);
		}		
	}
	
@SuppressWarnings("unchecked")
public void writeLinksToJson(Map<String,String> hasmap,String FileName,boolean saveUnderTestCase){
	   
		if(saveUnderTestCase){
			this.currentTCObjects.putAll(hasmap);
		}
		else{
			this.savedObjects.putAll(hasmap);
		}

	}
	
	@SuppressWarnings("unchecked")
	public void writeToJsonOutput() throws IOException{
		
			String currentTestCase=this.getcurrenttestcase();
			this.outputFile = new FileWriter(this.outputDirectorypath+this.outputFileName);
			if(this.currentTCObjects.size()>0){
				Map<String,String> tmpObject;
				if(this.savedObjects.containsKey(currentTestCase)){
					tmpObject=(Map<String,String>)this.savedObjects.get(currentTestCase);
				}
				else{
					tmpObject=new HashMap<String,String>();
				}
				
				tmpObject.putAll(this.currentTCObjects);
				this.savedObjects.put(currentTestCase, tmpObject);
			}
			this.outputFile.write(this.savedObjects.toJSONString());
			this.outputFile.flush();
			this.outputFile.close();
			this.currentTCObjects.clear();
	}
	public Object getJsonvalue(String jsonKey){
		
		try {
			return this.savedObjects.get(jsonKey);
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public String getcurrenttestcase(){
		String testcaseName=null;
		Object currentTC=this.savedObjects.get("CurrentTestcase");
		
		if(currentTC instanceof String){
			testcaseName=(String) currentTC;
		}
		else if(currentTC instanceof Map){
			Map<String,String> currentTestCase=(Map<String,String>)this.savedObjects.get("CurrentTestcase");
			testcaseName=currentTestCase.get("Name")+"-Data"+currentTestCase.get("DataRow");
		}
		
		return testcaseName;
	}
}
