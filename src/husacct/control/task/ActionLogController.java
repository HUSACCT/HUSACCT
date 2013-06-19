package husacct.control.task;

import husacct.control.task.configuration.ConfigurationManager;
import husacct.control.task.configuration.IConfigListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ActionLogController implements IConfigListener {
	private ArrayList<HashMap<String, String>> loggedActions = new ArrayList<HashMap<String, String>>();
	private int maxActions = 10;
	private MainController mainController;
	
	public ActionLogController(MainController mainController){
		this.mainController = mainController;
		ConfigurationManager.addListener(this);
	}
	
	public void addAction(String message){
		if(message==""){
			return;
		}
		
		if(maxNumberOfActionsAreLogged()){
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
		loggedActions.add(loggedMethodInfo);
		
		mainController.getMainGui().getActionLogPanel().refreshActionLogPanel();
	}
	
	private boolean maxNumberOfActionsAreLogged(){
		return loggedActions.size()==maxActions;
	}
	
	private void removeFirstLoggedUserAction(){
		loggedActions.remove(0);
	}
	
	public ArrayList<HashMap<String, String>> getLoggedActionsArrayList(){
		return loggedActions;
	}
	
	public void setActionLogVisibility(boolean isVisible){
		mainController.getMainGui().getActionLogPanel().setVisible(isVisible);
	}

	@Override
	public void onConfigUpdate() {
		boolean visible = Boolean.parseBoolean(ConfigurationManager.getProperty("ActionLogger"));
		setActionLogVisibility(visible);
	}
	
}