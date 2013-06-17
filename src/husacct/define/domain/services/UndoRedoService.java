package husacct.define.domain.services;

import java.util.ArrayList;
import java.util.List;

import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.seperatedinterfaces.IAppliedRuleSeperatedInterface;
import husacct.define.domain.seperatedinterfaces.IModuleSeperatedInterface;
import husacct.define.domain.seperatedinterfaces.ISofwareUnitSeperatedInterface;
import husacct.define.domain.seperatedinterfaces.IseparatedDefinition;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;

public class UndoRedoService  implements IModuleSeperatedInterface,ISofwareUnitSeperatedInterface,IAppliedRuleSeperatedInterface{
 private List<IseparatedDefinition> observers = new ArrayList<IseparatedDefinition>();
 private static UndoRedoService instance =null;
 public static UndoRedoService getInstance()
 {
	if (instance==null) {
	
		return instance= new UndoRedoService();
	} else {
	
		return instance;
	}
	 
 } 





	@Override
	public void addSeperatedModule(ModuleStrategy module) {
		for (Object observer : getSeperatedSofwareUnitInterfacess(IModuleSeperatedInterface.class)) {
			((IModuleSeperatedInterface)observer).addSeperatedModule(module);
		}
		
	}

	@Override
	public void removeSeperatedModule(ModuleStrategy module) {
		
		for (Object observer : getSeperatedSofwareUnitInterfacess(IModuleSeperatedInterface.class)) {
			
			((IModuleSeperatedInterface)observer).removeSeperatedModule(module);
		}
		
	}
	

	@Override
	public void layerUp(long moduleID) {
		for (Object observer : getSeperatedSofwareUnitInterfacess(IModuleSeperatedInterface.class)) {
			((IModuleSeperatedInterface)observer).layerUp(moduleID);
		}
		
	}





	@Override
	public void layerDown(long moduleID) {
		for (Object observer : getSeperatedSofwareUnitInterfacess(IModuleSeperatedInterface.class)) {
			 ((IModuleSeperatedInterface)observer).layerDown(moduleID);
		}
		
	}
	

	@Override
	public void addSeperatedSoftwareUnit(List<SoftwareUnitDefinition> units, long moduleId) {
		for (Object observer : getSeperatedSofwareUnitInterfacess(ISofwareUnitSeperatedInterface.class)) {
			((ISofwareUnitSeperatedInterface)observer).addSeperatedSoftwareUnit(units, moduleId);
		}
		
	}

	@Override
	public void removeSeperatedSoftwareUnit(List<SoftwareUnitDefinition> units, long moduleId) {
		for (Object observer : getSeperatedSofwareUnitInterfacess(ISofwareUnitSeperatedInterface.class)) {
			((ISofwareUnitSeperatedInterface)observer).removeSeperatedSoftwareUnit(units, moduleId);
		}
		
	}

	@Override
	public void addSeperatedAppliedRule(List<AppliedRuleStrategy> rules) {
		for (Object observer : getSeperatedSofwareUnitInterfacess(IAppliedRuleSeperatedInterface.class)) {
			((IAppliedRuleSeperatedInterface)observer).addSeperatedAppliedRule(rules);
		}
		
	}

	@Override
	public void removeSeperatedAppliedRule(List<AppliedRuleStrategy> rules) {
		for (Object observer : getSeperatedSofwareUnitInterfacess(IAppliedRuleSeperatedInterface.class)) {
			((IAppliedRuleSeperatedInterface)observer).removeSeperatedAppliedRule(rules);
		}
		
	}





	@Override
	public void addSeperatedExeptionRule(long parentRuleID,List<AppliedRuleStrategy> rules) {
		for (Object observer : getSeperatedSofwareUnitInterfacess(IAppliedRuleSeperatedInterface.class)) {
			((IAppliedRuleSeperatedInterface)observer).addSeperatedExeptionRule(parentRuleID,rules);
		}
		
	}





	@Override
	public void removeSeperatedExeptionRule(long parentRuleID,List<AppliedRuleStrategy> rules) {
		for (Object observer : getSeperatedSofwareUnitInterfacess(IAppliedRuleSeperatedInterface.class)) {
			((IAppliedRuleSeperatedInterface)observer).removeSeperatedExeptionRule(parentRuleID,rules);
		}
		
	}





	

	


	
	public List<Object> getSeperatedSofwareUnitInterfacess(Class c)
	{
		ArrayList<Object> results = new ArrayList<Object>();
		
		for (IseparatedDefinition result : observers) {
			
			
			for(Class cl :result.getClass().getInterfaces())
			{
				
				if (cl.equals( c)) {
			
					results.add(result);
				}
				
			}
		}
		
		
		return results;
	}
	




	public void registerObserver(IseparatedDefinition observer) {
		
				observers.add(observer);System.out.println("Registerd");
			System.out.println("Registerd"+observers.size());
	
		
		
	}

}
