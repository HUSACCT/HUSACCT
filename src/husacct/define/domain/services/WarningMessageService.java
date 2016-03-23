package husacct.define.domain.services;

import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.seperatedinterfaces.IModuleSeperatedInterface;
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

public class WarningMessageService extends Observable implements Observer,IModuleSeperatedInterface {
	private  ArrayList<WarningMessage> warnings= new ArrayList<WarningMessage>();
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	private static WarningMessageService instance=null;
	private ArrayList<CodeLevelWarning> codelevelWarnings = new ArrayList<CodeLevelWarning>();

	public static WarningMessageService getInstance() {
		if(instance==null) {
			return instance =new WarningMessageService();
		}else {
			return instance;
		}
	}
	
	public WarningMessageService() {
		UndoRedoService.getInstance().registerObserver(this);
	}
	
	public void removeWarning(WarningMessage warning) {
		int index = warnings.indexOf(warning);
		warnings.remove(index);
		observers.remove(warning);
		notifyAllObservers(this,"warningremoved");
	}
	
	public ArrayList<WarningMessage> getWarningMessages() {
		return warnings;
	}

	public boolean hasWarnings() {
		return warnings.size()+codelevelWarnings.size()==0;
	}

	public boolean hasCodeLevelWarning(AnalyzedModuleComponent analyzedModuleToChek) {
		boolean haswarning=false;
		Iterator<CodeLevelWarning> it = codelevelWarnings.iterator();
		while (it.hasNext()) {
			AnalyzedModuleComponent analyzedModule =  it.next().getNotCodeLevelModule();
			String leftUniqName= analyzedModule.getUniqueName().toLowerCase();
			String rightUniqName = analyzedModuleToChek.getUniqueName().toLowerCase();
			if(leftUniqName.equals(rightUniqName))
			{
				haswarning=true;
				it.remove();
				break;
			}
		}
		return haswarning;
	}
	
	public void processModule(ModuleStrategy module){
		if (module.getId()!=0) {
			if (module.isMapped()) {
				removeImplementationWarning(module);
			} else {
				createModuleWarning(module);
			}	
		}
	}


	public void removeImplementationWarning(ModuleStrategy module) {
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
	
		this.warnings.add(warning);
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
				String rightUniqName =uniqname;
				if(leftUniqName.equals(rightUniqName)){
					haswarning=true;
					break;
				}
			}
		}
	   return haswarning;
	}

	public void addCodeLevelWarning(AnalyzedModuleComponent unitTobeRestored) {
		ArrayList<AbstractCombinedComponent> list =new ArrayList<AbstractCombinedComponent>();
		list.addAll(getnestedValues(unitTobeRestored));
		if (unitTobeRestored.isMapped()) {
			codelevelWarnings.add(new CodeLevelWarning(unitTobeRestored));
		}
		for (AbstractCombinedComponent ab : list) {
			codelevelWarnings.add(new CodeLevelWarning((AnalyzedModuleComponent) ab));
		}
	}

	private ArrayList<AbstractCombinedComponent> getnestedValues(AnalyzedModuleComponent an) {
		ArrayList<AbstractCombinedComponent> mapped = new ArrayList<AbstractCombinedComponent>();
		for (AbstractCombinedComponent  unit : an.getChildren()) {
			 mapped=(searchWithTailRecursive(unit,mapped));
		}
		return mapped;
	}

	private ArrayList<AbstractCombinedComponent> searchWithTailRecursive(AbstractCombinedComponent unit, ArrayList<AbstractCombinedComponent> mapped) {
		AnalyzedModuleComponent io = (AnalyzedModuleComponent)unit;
		if (io.isMapped()) {
			mapped.add(io);
		}
		for (AbstractCombinedComponent ab : io.getChildren()) {
			searchWithTailRecursive(ab, mapped);
		}
		return mapped;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
	}

	public void removeCodeLevelWarningss(String softwareUnit) {
		for (Iterator<CodeLevelWarning> warning = codelevelWarnings.iterator(); warning.hasNext();) {
			CodeLevelWarning message =  warning.next();
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
		/*for (AbstractCombinedComponent unit : StateService.instance().getmappedUnits()) {
			
			//JtreeController.instance().removeTreeItem((AnalyzedModuleComponent)unit);
			//StateService.instance().getAnalzedModuleRegistry().removeAnalyzedUnit((AnalyzedModuleComponent)unit);
		}
		*/
	}

	public String warningsCount() {
		// TODO Auto-generated method stub
		return warnings.size()+codelevelWarnings.size()+"";
	}

	@Override
	public void addSeperatedModule(ModuleStrategy module) {
		processModule(module);
	}


	@Override
	public void removeSeperatedModule(ModuleStrategy module) {
		removeImplementationWarning(module);
	}


	@Override
	public void layerUp(long moduleID) {
		// TODO Auto-generated method stub
	}


	@Override
	public void layerDown(long moduleID) {
		// TODO Auto-generated method stub
	}

	public void clearImplementationLevelWarnings() {
		warnings = new ArrayList<WarningMessage>();
	}

	public void resetNotAnalyzed() {
	codelevelWarnings = new ArrayList<CodeLevelWarning>();
	}
	
}
