package husacct.define.domain.module.modules;

import java.util.ArrayList;

import husacct.define.domain.SoftwareUnitRegExDefinition;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;

public class Blank extends ModuleStrategy{

	public void set(){
		this.mappedSUunits = new ArrayList<SoftwareUnitDefinition>();
		this.mappedRegExSUunits = new ArrayList<SoftwareUnitRegExDefinition>();
		this.subModules = new ArrayList<ModuleStrategy>();
	}
}
