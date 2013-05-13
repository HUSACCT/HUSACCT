package husacct.define.domain.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.antlr.tool.LeftRecursionCyclesMessage;


import husacct.define.domain.module.Module;
import husacct.define.domain.warningmessages.CodeLevelWarning;
import husacct.define.domain.warningmessages.ImplementationLevelWarning;
import husacct.define.domain.warningmessages.WarningMessage;
import husacct.define.task.JtreeStateEngine;
import husacct.define.task.components.AnalyzedModuleComponent;
import husacct.graphics.util.threads.ObservableThread;

public class WarningMessageService extends Observable implements Observer {
   private  ArrayList<WarningMessage> warnings= new ArrayList<WarningMessage>();
  private ArrayList<Observer> observers = new ArrayList<Observer>();
   private static WarningMessageService instance;
	public enum removalType{partialRemoval,fullRemoval};

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
		notifyAllObservers(this,"warningadded");
	}
	
	public void removeWarning(WarningMessage warning)
	{
		int index = warnings.indexOf(warning);
		warnings.remove(index);
		notifyAllObservers(this,"warningremoved");
		
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
	public boolean hasCodeLevelWarning(AnalyzedModuleComponent analyzedModuleToChek,removalType removaltype)
	{
		ArrayList<WarningMessage> messagesTobeRemoved = new ArrayList<WarningMessage>();
		boolean haswarning=false;
		
			for (WarningMessage message : warnings) {
				if (message instanceof CodeLevelWarning) {
					AnalyzedModuleComponent analyzedModule = ((CodeLevelWarning) message).getNotCodeLevelModule();
					
					String leftUniqName= analyzedModule.getUniqueName().toLowerCase();
					String rightUniqName = analyzedModuleToChek.getUniqueName().toLowerCase();
					if(leftUniqName.equals(rightUniqName))
					{
						
						if (removalType.partialRemoval==removaltype) {
							JtreeStateEngine.instance().registerCodeRenewal(analyzedModuleToChek);
						} 
						
						haswarning=true;
						messagesTobeRemoved.add(message);
						
					}
					
				}
			}
			for (WarningMessage warningMessage : messagesTobeRemoved) {
				removeWarning(warningMessage);
			}
		
		
		return haswarning;
	}
	
	public boolean isCodeLevelWarning(AnalyzedModuleComponent analyzedModuleToChek)
	{
		
		    boolean haswarning=false;
		    for (WarningMessage message : warnings) {
				if (message instanceof CodeLevelWarning) {
					AnalyzedModuleComponent analyzedModule = ((CodeLevelWarning) message).getNotCodeLevelModule();
					String leftUniqName= analyzedModule.getUniqueName().toLowerCase();
					String rightUniqName = analyzedModuleToChek.getUniqueName().toLowerCase();
					if(leftUniqName.equals(rightUniqName))
					{
						haswarning=true;
						break;
						
					}
					
				}
			}
		
		
		
		return haswarning;
	}
	
	public void processModule(Module module)
	{
		
		if (module.isMapped()) {
			chekIfImplementationWarningExist(module);
		} else {
			createModuleWarning(module);
		}
	}


	private void chekIfImplementationWarningExist(Module module) {
		for (WarningMessage warning : warnings) {
			
			if(warning instanceof ImplementationLevelWarning)
			{
				Long idOfWarningModule = ((ImplementationLevelWarning)warning).getModule().getId();
				if(module.getId()==idOfWarningModule)
				{
					int index= warnings.indexOf(warning);
					warnings.remove(index);
					break;
				}
			}
			
		}
		notifyAllObservers(this, "removedModule");
	}


	private void createModuleWarning(Module module) {
		ImplementationLevelWarning warning = new ImplementationLevelWarning(module);
	
		warnings.add(warning);
		notifyAllObservers(this,"createModule");
	}


	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
	public void addObserver(Observer o)
	{
		observers.add(o);
	}
	
	public void notifyAllObservers(Observable o, Object arg) {
		for (Observer observer : observers) {
			observer.update(this, arg);
		}
	}


	public boolean isCodeLevelWarning(String uniqname) {
		
	    
		boolean haswarning=false;
	    
			 for (WarningMessage message : warnings) {
			if (message instanceof CodeLevelWarning) {
				AnalyzedModuleComponent analyzedModule = ((CodeLevelWarning) message).getNotCodeLevelModule();
				String leftUniqName= analyzedModule.getUniqueName().toLowerCase();
				String rightUniqName =uniqname ;
				if(leftUniqName.equals(rightUniqName))
				{
					haswarning=true;
					break;
					
				}
				
			}
		
		
	    
	    }
	   
	
	
	
	return haswarning;
	}


	public void addCodeLevelWarning(Long key,
			AnalyzedModuleComponent unitTobeRestored) {
		CodeLevelWarning codeLevelWarning = new CodeLevelWarning(key, unitTobeRestored);
		warnings.add(codeLevelWarning);
		
	}


	public void removeCodeLevelWarning(AnalyzedModuleComponent unitTobeRemoved) {
		// TODO Auto-generated method stub
		
	}
	
}
