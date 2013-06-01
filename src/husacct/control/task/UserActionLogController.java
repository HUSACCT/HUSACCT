package husacct.control.task;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class UserActionLogController {
	private static ArrayList<HashMap<String, String>> loggedUserActions;
	private int maxUserActions = 10;
	
	public UserActionLogController(){
		//TODO: Fix this nasty workaround
		if(loggedUserActions==null){
			loggedUserActions = new ArrayList<HashMap<String, String>>();
		}
		addDummyUserActions(maxUserActions);
	}
	
	private void addDummyUserActions(int numberOfUserActions){
		for(int i=0;i<numberOfUserActions;i++){
			addUserAction("Dummy action: User opened HUSACCT");
		}
	}
	
	public void addUserAction(String message){
		if(message==""){
			return;
		}
		
		if(maxUserActionsAreLogged()){
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
	
	private boolean maxUserActionsAreLogged(){
		return loggedUserActions.size()==maxUserActions;
	}
	
	private void removeFirstLoggedUserAction(){
		loggedUserActions.remove(0);
	}
	
	public ArrayList<HashMap<String, String>> getLoggedUserActionsArrayList(){
		return loggedUserActions;
	}

}
