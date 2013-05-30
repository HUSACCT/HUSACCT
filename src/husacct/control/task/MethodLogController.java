package husacct.control.task;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

public abstract class MethodLogController {
	
	private Logger logger = Logger.getLogger(ApplicationAnalysisHistoryLogController.class);
	private static ArrayList<HashMap<String, String>> loggedMethodCalls = new ArrayList<HashMap<String, String>>();
	
	public MethodLogController(){
	}
	
	public void logMethod(String message){
		Throwable t = new Throwable(); 
		StackTraceElement[] elements = t.getStackTrace(); 

		String classPath = elements[1].getClassName();
		String calledMethodName = elements[0].getMethodName(); 

		HashMap<String, String> loggedMethodInfo = new HashMap<String, String>();
		loggedMethodInfo.put("classPath", classPath);
		loggedMethodInfo.put("calledMethodName", calledMethodName);
		loggedMethodInfo.put("message", message);
		loggedMethodCalls.add(loggedMethodInfo);
	}
	
	public void printLoggedMethods(){
		String output = "Logged Method Calls: \n";
		for(HashMap<String, String> loggedMethod : loggedMethodCalls){
			output += "classPath: " + loggedMethod.get("classPath") + ", \n";
			output += "calledMethodName: " + loggedMethod.get("calledMethodName") + ", \n";
			output += "message: " + loggedMethod.get("message") + "\n";
			output += "========================================\n";
		}
		
		logger.info(output);
	}
	
	public ArrayList<HashMap<String, String>> getLoggedMethodCallsArrayList(){
		return loggedMethodCalls;
	}

}
