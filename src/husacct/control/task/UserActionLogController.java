package husacct.control.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class UserActionLogController {
	private ArrayList<HashMap<String, String>> loggedUserActions = new ArrayList<HashMap<String, String>>();
	private int maxUserActions = 10;
	private MainController mainController;
	
	public UserActionLogController(MainController mainController){
		this.mainController = mainController;
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
		message = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + " " + message;
		
		HashMap<String, String> loggedMethodInfo = new HashMap<String, String>();
		loggedMethodInfo.put("classPath", classPath);
		loggedMethodInfo.put("calledMethodName", calledMethodName);
		loggedMethodInfo.put("message", message);
		loggedUserActions.add(loggedMethodInfo);
		
		mainController.getMainGui().getUserActionLogDialog().refreshUserActionsDialog();
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
