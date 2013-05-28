package husacct.common.logging;

import husacct.control.task.LogController;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

public abstract class MethodLogger {
	
	private Logger logger = Logger.getLogger(LogController.class);
	private static ArrayList<HashMap<String, String>> loggedMethodCalls = new ArrayList<HashMap<String, String>>();
	
	public void logMethod(String message){
		Throwable t = new Throwable(); 
		StackTraceElement[] elements = t.getStackTrace(); 

		System.out.println();
		
		String classPath = elements[1].getClassName();
		String callerMethodName = elements[1].getMethodName();
		String calleeMethodName = elements[0].getMethodName(); 

		HashMap<String, String> loggedMethodInfo = new HashMap<String, String>();
		loggedMethodInfo.put("classPath", classPath);
		loggedMethodInfo.put("callerMethodName", callerMethodName);
		loggedMethodInfo.put("calleeMethodName", calleeMethodName);
		loggedMethodInfo.put("message", message);
		loggedMethodCalls.add(loggedMethodInfo);
	}
	
	public void printLoggedMethods(){
		String output = "Logged Method Calls: \n";
		for(HashMap<String, String> loggedMethod : loggedMethodCalls){
			output += "classPath: " + loggedMethod.get("classPath") + ", \n";
			output += "callerMethodName: " + loggedMethod.get("callerMethodName") + ", \n";
			output += "calleeMethodName: " + loggedMethod.get("calleeMethodName") + ", \n";
			output += "message: " + loggedMethod.get("message") + "\n";
			output += "========================================\n";
		}
		
		logger.info(output);
		
		//Sample output (will be deleted once this is done)
		/*
		 *  INFO [AWT-EventQueue-0] (MethodLogger.java:40) - Logged Method Calls: 
			classPath: husacct.control.task.LogController, 
			callerMethodName: <init>, 
			calleeMethodName: logMethod, 
			message: Instantiate logcontroller
			========================================
			classPath: husacct.control.task.LogController, 
			callerMethodName: logFileExists, 
			calleeMethodName: logMethod, 
			message: Check if C:\Users\Thijs\HUSACCT\applicationanalysishistory.xml exists.
			========================================
			classPath: husacct.control.task.LogController, 
			callerMethodName: logFileExists, 
			calleeMethodName: logMethod, 
			message: Check if C:\Users\Thijs\HUSACCT\applicationanalysishistory.xml exists.
			========================================
		 */
	}

}
