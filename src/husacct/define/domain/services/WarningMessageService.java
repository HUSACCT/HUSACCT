package husacct.define.domain.services;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


import husacct.define.domain.warningmessages.WarningMessage;
import husacct.graphics.util.threads.ObservableThread;

public class WarningMessageService extends Observable implements Observer {
   private  ArrayList<WarningMessage> warnings= new ArrayList<WarningMessage>();
  private ArrayList<Observer> observers = new ArrayList<Observer>();
   private static WarningMessageService instance;
	

	public static WarningMessageService getInstance()
	{
		if(instance==null)
		{
			instance = new WarningMessageService();
			return instance;
			
		}else{
			return instance;
		}
	}
	
	
	public void addWarning(WarningMessage warning )
	{
	
		
		warnings.add(warning);
		notifyAllObservers();
	}
	
	public void removeWarning(WarningMessage warning)
	{
		int index = warnings.indexOf(warning);
		warnings.remove(index);
		notifyAllObservers();
		
	}
	
	public ArrayList<WarningMessage> getWarningMessages()
	{
		
		return warnings;
	}


	public boolean hasWarnings() {
		if (warnings.size()==0) {
		return false;
		} else {
			return true;
		}
		
	}


	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
	public void addObserver(Observer o)
	{
		observers.add(o);
	}
	
	public void notifyAllObservers() {
		for (Observer observer : observers) {
			observer.update(this, new Object());
		}
	}
	
}
