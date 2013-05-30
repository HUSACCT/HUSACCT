package husacct.control.task;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

public abstract class UserActionLogController {
	
	private Logger logger = Logger.getLogger(ApplicationAnalysisHistoryLogController.class);
	private static ArrayList<HashMap<String, String>> loggedUserActions;
	
	public UserActionLogController(){
		//TODO: Fix this nasty workaround
		if(loggedUserActions==null){
			loggedUserActions = new ArrayList<HashMap<String, String>>();
		}
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
	
	public ArrayList<HashMap<String, String>> getLoggedUserActionsArrayList(){
		return loggedUserActions;
	}

}
