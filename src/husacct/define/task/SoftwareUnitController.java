package husacct.define.task;

import husacct.define.domain.services.ModuleDomainService;
import husacct.define.domain.services.SoftwareUnitDefinitionDomainService;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class SoftwareUnitController extends PopUpController {

	private Logger logger;
	private SoftwareUnitDefinitionDomainService softwareUnitDefinitionDomainService;

	public SoftwareUnitController(long moduleId) {

		logger = Logger.getLogger(SoftwareUnitController.class);
		setModuleId(moduleId);
		softwareUnitDefinitionDomainService = new SoftwareUnitDefinitionDomainService();
	}

	public boolean save(ArrayList<AnalyzedModuleComponent> units) {
		// logger.info("Adding software unit to module with id " + this.getModuleId());
		try {
			StateService.instance().addSoftwareUnit((new ModuleDomainService()).getModuleById(this.getModuleId()), units);
			softwareUnitDefinitionDomainService.addSoftwareUnitsToModule(this.getModuleId(), units);
			return true;
		} catch (Exception e) {
			this.logger.error(e.getMessage());
			return false;
		}
	}

	public void changeSoftwareUnit(long toModuleId, ArrayList<String> names) {
		long from = DefinitionController.getInstance().getSelectedModuleId();
		long to = toModuleId;
		softwareUnitDefinitionDomainService.changeSoftwareUnit(from,to,names);
	}
}
