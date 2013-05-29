package husacct.define.domain.services;

import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.domain.warningmessages.CodeLevelWarning;
import husacct.define.domain.warningmessages.ImplementationLevelWarning;
import husacct.define.domain.warningmessages.WarningMessage;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

public class WarningMessageService extends Observable implements Observer {
   private  ArrayList<WarningMessage> warnings= new ArrayList<WarningMessage>();
  private ArrayList<Observer> observers = new ArrayList<Observer>();
   private static WarningMessageService instance;
   private ArrayList<CodeLevelWarning> codelevelWarnings = new ArrayList<CodeLevelWarning>();
	

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
	
		
		codelevelWarnings.add((CodeLevelWarning)warning);
		observers.add(warning);
		notifyAllObservers(this,"warningadded");
	}
	
	public void removeWarning(WarningMessage warning)
	{
		int index = warnings.indexOf(warning);
		warnings.remove(index);
		observers.remove(warning);
		notifyAllObservers(this,"warningremoved");
		
	}
	
	public ArrayList<WarningMessage> getWarningMessages()
	{
	
		return warnings;
	}


	public boolean hasWarnings() {
	return warnings.size()+codelevelWarnings.size()==0;
		
	}
	public boolean hasCodeLevelWarning(AnalyzedModuleComponent analyzedModuleToChek)
	{
		ArrayList<WarningMessage> messagesTobeRemoved = new ArrayList<WarningMessage>();
		boolean haswarning=false;
		
			for (CodeLevelWarning message : codelevelWarnings) {
			
					AnalyzedModuleComponent analyzedModule =  message.getNotCodeLevelModule();
					
					String leftUniqName= analyzedModule.getUniqueName().toLowerCase();
					String rightUniqName = analyzedModuleToChek.getUniqueName().toLowerCase();
					if(leftUniqName.equals(rightUniqName))
					{
						
						
						
						haswarning=true;
						messagesTobeRemoved.add(message);
						
					}
					
				
			}
			for (WarningMessage warningMessage : messagesTobeRemoved) {
				removeWarning(warningMessage);
			}
		
		
		return haswarning;
	}
	

		
		
	
	
	public void processModule(ModuleStrategy module)
	{
		
		if (module.isMapped()) {
			removeImplementationWarning(module);
		} else {
			createModuleWarning(module);
		}
	}


	private void removeImplementationWarning(ModuleStrategy module) {
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


	private void createModuleWarning(ModuleStrategy module) {
		ImplementationLevelWarning warning = new ImplementationLevelWarning(module);
	
		warnings.add(warning);
		addObserver(warning);
		notifyAllObservers(this,"createModule");
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
				 if(leftUniqName.equals(rightUniqName)){
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
		codelevelWarnings.add(codeLevelWarning);
		
	}




	@Override
	public void update(Observable arg0, Object arg1) {

		
	}


	public void removeCodeLevelWarning(String softwareUnit) {
		for (Iterator<WarningMessage> warning = warnings.iterator(); warning.hasNext();) {
			WarningMessage message = (WarningMessage) warning.next();
			if (message instanceof CodeLevelWarning) {
			AnalyzedModuleComponent analyzedModule = ((CodeLevelWarning) message).getNotCodeLevelModule();
			String leftUniqName= analyzedModule.getUniqueName().toLowerCase();
			String rightUniqName =softwareUnit ;
			if(leftUniqName.equals(rightUniqName)){
				int index = warnings.indexOf(message);
				warnings.remove(index);
				break;
			   }
			}
		} 
	}


	public void notifyAllObservers() {
		notifyAllObservers(this, new Object());
		
	}
	
	public Object[] getvalueat(int index)
	{
		
	return warnings.get(index).getValue();
	}


	public ArrayList<CodeLevelWarning> getNotCodeLevelWarnings() {
		
		return codelevelWarnings;
	}


	public void updateWarnings() {
		
		
	}


	public void registerNotMappedUnits(AnalyzedModuleComponent root) {
	
		for (AbstractCombinedComponent unit : root.getChildren()) {
			
			StateService.instance().getAnalzedModuleRegistry().registerAnalyzedUnit((AnalyzedModuleComponent)unit);
			
			registerNotMappedUnits((AnalyzedModuleComponent)unit);
			
			
		}
		
		
	}


	public String warningsCount() {
		// TODO Auto-generated method stub
		return warnings.size()+codelevelWarnings.size()+"";
	}
	
	
}
