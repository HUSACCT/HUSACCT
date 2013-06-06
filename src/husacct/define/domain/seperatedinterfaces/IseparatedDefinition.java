package husacct.define.domain.seperatedinterfaces;


import java.util.List;

import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;

public interface IseparatedDefinition {

	
	
	public void addSeperatedModule(ModuleStrategy module);
	public void removeSeperatedModule(ModuleStrategy module);
	public void addSeperatedSoftwareUnit(List<SoftwareUnitDefinition> units);
	public void removeSeperatedSoftUnit(List<SoftwareUnitDefinition> units);
	public void addSeperatedAppliedRule(List<AppliedRuleStrategy> rules);
	public void removeSeperatedAppliedRule(List<AppliedRuleStrategy> rules);
	public void addSeperatedExeptionRule(List<AppliedRuleStrategy> rules);
	public void removeSeperatedExeptionRule(List<AppliedRuleStrategy> rules);
	
	
	 
}
