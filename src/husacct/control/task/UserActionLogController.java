package husacct.control.task;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

public abstract class UserActionLogController {
	private Logger logger = Logger.getLogger(ApplicationAnalysisHistoryLogController.class);
	private static ArrayList<HashMap<String, String>> loggedUserActions;
	private int maxUserActions = 10;
	
	
	public UserActionLogController(){
		//TODO: Fix this nasty workaround
		if(loggedUserActions==null){
			loggedUserActions = new ArrayList<HashMap<String, String>>();
		}
		addDummyUserActions();
	}
	
	private void addDummyUserActions(){
		for(int i=0;i<5;i++){
			HashMap<String, String> loggedMethodInfo = new HashMap<String, String>();
			loggedMethodInfo.put("classPath", "husacct.control.task.ApplicationAnalysisHistoryLogController");
			loggedMethodInfo.put("calledMethodName", "logMethod");
			loggedMethodInfo.put("message", "Dummy action: User opened HUSACCT");
			loggedUserActions.add(loggedMethodInfo);
		}
	}
	
	public void logUserAction(String message){
		if(message==""){
			return;
		}
		
		if(loggedMaxUserActions()){
			removeFirstLoggedUserAction();
		}
		
		Throwable t = new Throwable(); 
		StackTraceElement[] elements = t.getStackTrace(); 

		String classPath = elements[1].getClassName();
		String calledMethodName = elements[0].getMethodName(); 

		HashMap<String, String> loggedMethodInfo = new HashMap<String, String>();
		loggedMethodInfo.put("classPath", classPath);
		loggedMethodInfo.put("calledMethodName", calledMethodName);
		loggedMethodInfo.put("message", message);
		loggedUserActions.add(loggedMethodInfo);
		
		//TODO: Refresh user actions dialog
		//mainController.getMainGui().refreshUserActionsDialog();
	}
	
	private boolean loggedMaxUserActions(){
		return loggedUserActions.size()==maxUserActions;
	}
	
	private void removeFirstLoggedUserAction(){
		loggedUserActions.remove(0);
	}
	
	public ArrayList<HashMap<String, String>> getLoggedUserActionsArrayList(){
		return loggedUserActions;
	}

}
