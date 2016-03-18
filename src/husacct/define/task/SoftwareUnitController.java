package husacct.define.task;

import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.services.SoftwareUnitDefinitionDomainService;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.presentation.jdialog.SoftwareUnitJDialog;
import husacct.define.presentation.utils.UiDialogs;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class SoftwareUnitController extends PopUpController {

	private SoftwareUnitJDialog softwareUnitFrame;
	private Logger logger;
	private SoftwareUnitDefinitionDomainService softwareUnitDefinitionDomainService;

	public SoftwareUnitController(long moduleId) {

		logger = Logger.getLogger(SoftwareUnitController.class);
		setModuleId(moduleId);
		softwareUnitDefinitionDomainService = new SoftwareUnitDefinitionDomainService();
	}

	public void editRegEx(ArrayList<AnalyzedModuleComponent> components,
			String editingRegEx) {
		softwareUnitDefinitionDomainService.editRegex(DefinitionController.getInstance().getSelectedModuleId(),components,editingRegEx);
	}

	public void saveRegEx(ArrayList<AnalyzedModuleComponent> selectedComponents, String regExName) {
		logger.info("Adding software unit to module with id " + getModuleId() + " to regex " + regExName);
		try {
			softwareUnitDefinitionDomainService.addRegexToModule(getModuleId(), selectedComponents, regExName);
			DefinitionController.getInstance().notifyObservers();
		} catch (Exception e) {
			logger.error(e.getMessage());
			UiDialogs.errorDialog(softwareUnitFrame, e.getMessage());
		}
	}

	// Only used by bootstrap
	public void save(Long moduleId, String softwareUnit, String type) {
		logger.info("Adding software unit to module with id " + getModuleId());
		try {
			DefinitionController.getInstance().notifyObservers();
		} catch (Exception e) {
			logger.error(e.getMessage());
			UiDialogs.errorDialog(softwareUnitFrame, e.getMessage());
		}
	}

	public boolean save(ArrayList<AnalyzedModuleComponent> units) {
		// logger.info("Adding software unit to module with id " + this.getModuleId());
		try {
			StateService.instance().addSoftwareUnit(SoftwareArchitecture.getInstance().getModuleById(this.getModuleId()), units);
			softwareUnitDefinitionDomainService.addSoftwareUnitsToModule(this.getModuleId(), units);
			DefinitionController.getInstance().notifyObservers();
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
