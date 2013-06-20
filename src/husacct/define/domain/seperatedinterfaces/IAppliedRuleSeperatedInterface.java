package husacct.define.domain.seperatedinterfaces;

import husacct.define.domain.appliedrule.AppliedRuleStrategy;

import java.util.List;

public interface IAppliedRuleSeperatedInterface extends IseparatedDefinition{


	public void addSeperatedAppliedRule(List<AppliedRuleStrategy> rules);
	public void removeSeperatedAppliedRule(List<AppliedRuleStrategy> rules);
	public void addSeperatedExeptionRule(long parentRuleID,List<AppliedRuleStrategy> rules);
	public void removeSeperatedExeptionRule(long parentRuleID,List<AppliedRuleStrategy> rules);
}
