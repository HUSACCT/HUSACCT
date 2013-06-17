package husacct.define.domain.seperatedinterfaces;

import husacct.define.domain.softwareunit.SoftwareUnitDefinition;

import java.util.List;

public interface ISofwareUnitSeperatedInterface extends IseparatedDefinition {
	public void addSeperatedSoftwareUnit(List<SoftwareUnitDefinition> units, long moduleID);
	public void removeSeperatedSoftwareUnit(List<SoftwareUnitDefinition> units, long moduleId);
}
