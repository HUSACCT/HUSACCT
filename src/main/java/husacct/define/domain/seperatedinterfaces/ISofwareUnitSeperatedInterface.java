package husacct.define.domain.seperatedinterfaces;

import husacct.define.domain.softwareunit.ExpressionUnitDefinition;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;

import java.util.List;

public interface ISofwareUnitSeperatedInterface extends IseparatedDefinition {
	public void addSeperatedSoftwareUnit(List<SoftwareUnitDefinition> units, long moduleID);
	public void removeSeperatedSoftwareUnit(List<SoftwareUnitDefinition> units, long moduleId);
	public void addExpression(long moduleId,ExpressionUnitDefinition expression);
	public void removeExpression(long moduleId,ExpressionUnitDefinition expression);
	public void editExpression(long moduleId,ExpressionUnitDefinition oldExpresion, ExpressionUnitDefinition newExpression);
	public void switchSoftwareUnitLocation(long fromModule,long toModule,List<String> uniqNames);
}
