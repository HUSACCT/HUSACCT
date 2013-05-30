package husacct.control.task;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

public abstract class UserActionLogController {
	
	private Logger logger = Logger.getLogger(ApplicationAnalysisHistoryLogController.class);
	private static ArrayList<HashMap<String, String>> loggedUserActions = new ArrayList<HashMap<String, String>>();
	
	public UserActionLogController(){
		addDummyUserActions();
	}
	
	public void addDummyUserActions(){
		HashMap<String, String> loggedMethodInfo = new HashMap<String, String>();
		loggedMethodInfo.put("classPath", "husacct.control.task.ApplicationAnalysisHistoryLogController");
		loggedMethodInfo.put("calledMethodName", "logMethod");
		loggedMethodInfo.put("message", "Example entry: Checked if log file FILENAME exists");
		loggedUserActions.add(loggedMethodInfo);
	}
	
	public void logUserAction(String message){
		Throwable t = new Throwable(); 
		StackTraceElement[] elements = t.getStackTrace(); 

		String classPath = elements[1].getClassName();
		String calledMethodName = elements[0].getMethodName(); 

		HashMap<String, String> loggedMethodInfo = new HashMap<String, String>();
		loggedMethodInfo.put("classPath", classPath);
		loggedMethodInfo.put("calledMethodName", calledMethodName);
		loggedMethodInfo.put("message", message);
		loggedUserActions.add(loggedMethodInfo);
	}
	
	public void printLoggedMethods(){
		String output = "Logged User Actions: \n";
		for(HashMap<String, String> loggedMethod : loggedUserActions){
			output += "classPath: " + loggedMethod.get("classPath") + ", \n";
			output += "calledMethodName: " + loggedMethod.get("calledMethodName") + ", \n";
			output += "message: " + loggedMethod.get("message") + "\n";
			output += "========================================\n";
		}
		
		logger.info(output);
	}
	
	public ArrayList<HashMap<String, String>> getLoggedUserActionsArrayList(){
		return loggedUserActions;
	}

}
