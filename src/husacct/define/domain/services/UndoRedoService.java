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
	 return instance==null ? new UndoRedoService():instance;
	 
 } 





	@Override
	public void addSeperatedModule(ModuleStrategy module) {
		for (IseparatedDefinition observer : observers) {
			observer.addSeperatedModule(module);
		}
		
	}

	@Override
	public void removeSeperatedModule(ModuleStrategy module) {
		for (IseparatedDefinition observer : observers) {
			observer.removeSeperatedModule(module);
		}
		
	}

	@Override
	public void addSeperatedSoftwareUnit(List<SoftwareUnitDefinition> units) {
		for (IseparatedDefinition observer : observers) {
			observer.addSeperatedSoftwareUnit(units);
		}
		
	}

	@Override
	public void removeSeperatedSoftUnit(List<SoftwareUnitDefinition> units) {
		for (IseparatedDefinition observer : observers) {
			observer.removeSeperatedSoftUnit(units);
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
	public void addSeperatedExeptionRule(List<AppliedRuleStrategy> rules) {
		for (IseparatedDefinition observer : observers) {
			observer.addSeperatedExeptionRule(rules);
		}
		
	}





	@Override
	public void removeSeperatedExeptionRule(List<AppliedRuleStrategy> rules) {
		for (IseparatedDefinition observer : observers) {
			observer.removeSeperatedAppliedRule(rules);
		}
		
	}

}
