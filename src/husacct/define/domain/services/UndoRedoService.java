package husacct.define.domain.services;

import java.util.ArrayList;
import java.util.List;

import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.seperatedinterfaces.IseparatedDefinition;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;

public class UndoRedoService  implements IseparatedDefinition{
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
		for (IseparatedDefinition observer : observers) {
			observer.addSeperatedModule(module);
		}
		
	}

	@Override
	public void removeSeperatedModule(ModuleStrategy module) {
		System.out.println("i tried queee "+observers.size());
		for (IseparatedDefinition observer : observers) {
			System.out.println("i tried");
			observer.removeSeperatedModule(module);
		}
		
	}

	@Override
	public void addSeperatedSoftwareUnit(List<SoftwareUnitDefinition> units, long moduleId) {
		for (IseparatedDefinition observer : observers) {
			observer.addSeperatedSoftwareUnit(units, moduleId);
		}
		
	}

	@Override
	public void removeSeperatedSoftwareUnit(List<SoftwareUnitDefinition> units, long moduleId) {
		for (IseparatedDefinition observer : observers) {
			observer.removeSeperatedSoftwareUnit(units, moduleId);
		}
		
	}

	@Override
	public void addSeperatedAppliedRule(List<AppliedRuleStrategy> rules) {
		for (IseparatedDefinition observer : observers) {
			observer.addSeperatedAppliedRule(rules);
		}
		
	}

	@Override
	public void removeSeperatedAppliedRule(List<AppliedRuleStrategy> rules) {
		for (IseparatedDefinition observer : observers) {
			observer.removeSeperatedAppliedRule(rules);
		}
		
	}





	@Override
	public void addSeperatedExeptionRule(long parentRuleID,List<AppliedRuleStrategy> rules) {
		for (IseparatedDefinition observer : observers) {
			observer.addSeperatedExeptionRule(parentRuleID,rules);
		}
		
	}





	@Override
	public void removeSeperatedExeptionRule(long parentRuleID,List<AppliedRuleStrategy> rules) {
		for (IseparatedDefinition observer : observers) {
			observer.removeSeperatedExeptionRule(parentRuleID,rules);
		}
		
	}





	@Override
	public void layerUp(long moduleID) {
		for (IseparatedDefinition observer : observers) {
			observer.layerUp(moduleID);
		}
		
	}





	@Override
	public void layerDown(long moduleID) {
		for (IseparatedDefinition observer : observers) {
			observer.layerDown(moduleID);
		}
		
	}





	public void registerObserver(IseparatedDefinition observer) {
		
		if (observers.size()==0) {
			observers.add(observer);System.out.println("Registerd");
			System.out.println("Registerd"+observers.size());
		} else {

		}
		
		
	}

}
