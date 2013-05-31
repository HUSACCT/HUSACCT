package husacct.define.task;

import husacct.ServiceProvider;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ExternalSystemDTO;
import husacct.common.dto.ProjectDTO;
import husacct.define.domain.services.SoftwareUnitDefinitionDomainService;
import husacct.define.presentation.jdialog.SoftwareUnitJDialog;
import husacct.define.presentation.utils.UiDialogs;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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

	public AnalyzedModuleComponent getSoftwareUnitTreeComponents() {
		AnalyzedModuleComponent rootComponent = new AnalyzedModuleComponent(
				"root", "Application", "application", "public");
		addExternalComponents(rootComponent);

		ApplicationDTO application = ServiceProvider.getInstance()
				.getControlService().getApplicationDTO();

		for (ProjectDTO project : application.projects) {
			AnalyzedModuleComponent projectComponent = new AnalyzedModuleComponent(
					project.name, project.name, "root", "public");
			for (AnalysedModuleDTO module : project.analysedModules) {
				addChildComponents(projectComponent, module);
			}
			rootComponent.addChild(projectComponent);
		}

		Collections.sort(rootComponent.getChildren());
		rootComponent.updateChilderenPosition();
		return rootComponent;
	}

	private void addChildComponents(AnalyzedModuleComponent parentComponent,
			AnalysedModuleDTO module) {
		AnalyzedModuleComponent childComponent = new AnalyzedModuleComponent(
				module.uniqueName, module.name, module.type, module.visibility);
		AnalysedModuleDTO[] children = ServiceProvider.getInstance()
				.getAnalyseService().getChildModulesInModule(module.uniqueName);
		AnalysedModuleComparator comparator = new AnalysedModuleComparator();
		Arrays.sort(children, comparator);
		for (AnalysedModuleDTO subModule : children) {
			addChildComponents(childComponent, subModule);
		}
		parentComponent.addChild(childComponent);
		parentComponent.registerchildrenSize();
	}

	public void addExternalComponents(AnalyzedModuleComponent root) {
		AnalyzedModuleComponent rootOfExterexternalLibrary = new AnalyzedModuleComponent(
				"external library", "externallibrary", "externalpackage",
				"public");
		AnalyzedModuleComponent javalibrary = new AnalyzedModuleComponent(
				"externallibrary", "java library", "externallibrary", "public");
		AnalyzedModuleComponent externalsubsystem = new AnalyzedModuleComponent(
				"subsystem", "external subsystem", "subsystem", "public");
		rootOfExterexternalLibrary.addChild(javalibrary);
		rootOfExterexternalLibrary.addChild(externalsubsystem);
		ExternalSystemDTO[] externalSystems = ServiceProvider.getInstance()
				.getAnalyseService().getExternalSystems();
		for (ExternalSystemDTO exe : externalSystems) {
			if (exe.systemPackage.startsWith("java.")) {
				AnalyzedModuleComponent javalib = new AnalyzedModuleComponent(
						exe.systemPackage, exe.systemName, "externallibrary",
						"public");
				javalibrary.addChild(javalib);
			} else {
				AnalyzedModuleComponent subsystem = new AnalyzedModuleComponent(
						exe.systemPackage, exe.systemName, "subsystem",
						"public");
				externalsubsystem.addChild(subsystem);
			}

		}

		root.addChild(rootOfExterexternalLibrary);

	}

	public void save(String softwareUnit, String type) {
		save(getModuleId(), softwareUnit, type);
	}

	public void saveRegEx(
			ArrayList<AnalyzedModuleComponent> selectedComponents,
			String regExName) {
		logger.info("Adding software unit to module with id " + getModuleId()
				+ " to regex " + regExName);

		try {

			softwareUnitDefinitionDomainService.addSoftwareUnitToRegex(
					getModuleId(), selectedComponents, regExName);

			DefinitionController.getInstance().notifyObservers();
		} catch (Exception e) {
			logger.error(e.getMessage());
			UiDialogs.errorDialog(softwareUnitFrame, e.getMessage());
		}
	}

	public void save(Long moduleId, String softwareUnit, String type) {
		logger.info("Adding software unit to module with id " + getModuleId());
		try {

			DefinitionController.getInstance().notifyObservers();
		} catch (Exception e) {
			logger.error(e.getMessage());
			UiDialogs.errorDialog(softwareUnitFrame, e.getMessage());
		}
	}

	public void save(Long moduleid, AbstractCombinedComponent component) {
		logger.info("Adding software unit to module with id " + getModuleId());

	}

	public boolean save(AnalyzedModuleComponent selectedComponent) {
		logger.info("Adding software unit to module with id " + getModuleId());
		try {
			if (!selectedComponent.isComplete()) {
				UiDialogs.errorDialog(softwareUnitFrame,
						"Inconsistency detected: an unit of  \n name: "
								+ selectedComponent.getName() + " type: "
								+ selectedComponent.getType()
								+ " has been already mapped");
				logger.error("Inconsistancy detected");
				return false;
			} else if (selectedComponent.getType().toLowerCase()
					.equals("package")
					&& selectedComponent.isMapped()) {
				UiDialogs.errorDialog(softwareUnitFrame,
						"The package  \n name: " + selectedComponent.getName()
								+ " type: " + selectedComponent.getType()
								+ " has been already mapped");
				logger.error("Inconsistancy detected");
				return false;
			} else {
				softwareUnitDefinitionDomainService.addSoftwareUnit(
						getModuleId(), selectedComponent);
			}
			DefinitionController.getInstance().notifyObservers();
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}

	}

	public void editRegEx(ArrayList<AnalyzedModuleComponent> components,
			String editingRegEx) {

		JtreeController.instance().editRegex(getModuleId(), components,
				editingRegEx);

	}

	public boolean save(ArrayList<AnalyzedModuleComponent> units) {
		logger.info("Adding software unit to module with id "
				+ this.getModuleId());
		try {

			this.softwareUnitDefinitionDomainService.addSoftwareUnit(
					this.getModuleId(), units);

			DefinitionController.getInstance().notifyObservers();
			return true;
		} catch (Exception e) {
			this.logger.error(e.getMessage());
			return false;
		}

	}
}
