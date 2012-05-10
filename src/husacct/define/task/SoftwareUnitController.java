package husacct.define.task;

import husacct.ServiceProvider;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.services.SoftwareUnitDefinitionDomainService;
import husacct.define.presentation.jframe.JFrameSoftwareUnit;
import husacct.define.presentation.utils.UiDialogs;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;

import org.apache.log4j.Logger;


public class SoftwareUnitController extends PopUpController {

	private JFrameSoftwareUnit softwareUnitFrame;
	private Logger logger;
	
	private SoftwareUnitDefinitionDomainService softwareUnitDefinitionDomainService;
	
	public SoftwareUnitController(long moduleId) {
		logger = Logger.getLogger(SoftwareUnitController.class);
		this.setModuleId(moduleId);
		this.softwareUnitDefinitionDomainService = new SoftwareUnitDefinitionDomainService();
	}
	
	public void fillSoftwareUnitsList(ArrayList<SoftwareUnitDefinition> softwareUnitList){
		AnalysedModuleDTO[] modules = getAnalyzedModules();
		for(AnalysedModuleDTO module : modules) {
			SoftwareUnitDefinition softwareUnit = new SoftwareUnitDefinition(module.name, SoftwareUnitDefinition.Type.valueOf(module.type.toUpperCase()));
			softwareUnitList.add(softwareUnit);
		}
		
		filterAddedSoftwareUnits(softwareUnitList);
	}
	
	private void filterAddedSoftwareUnits(ArrayList<SoftwareUnitDefinition> softwareUnitList) {
		ArrayList<SoftwareUnitDefinition> addedsoftwareUnitList = this.softwareUnitDefinitionDomainService.getSoftwareUnit(currentModuleId);
		for (SoftwareUnitDefinition addedUnit : addedsoftwareUnitList){
			if (softwareUnitList.contains(addedUnit)) {
				softwareUnitList.remove(addedUnit);
			}
			
		}
	}
	
	public AnalyzedModuleComponent getSoftwareUnitTreeComponents() {
		AnalyzedModuleComponent rootComponent = new AnalyzedModuleComponent("root", "Software Units", "root", "public");
		AnalysedModuleDTO[] modules = this.getAnalyzedModules();
		for(AnalysedModuleDTO module : modules) {
			this.addChildComponents(rootComponent, module);
		}

		return rootComponent;
	}
	
	private AnalysedModuleDTO[] getAnalyzedModules() {
		AnalysedModuleDTO[] modules = ServiceProvider.getInstance().getAnalyseService().getRootModules();
		return modules;
	}
	
	private void addChildComponents(AnalyzedModuleComponent parentComponent, AnalysedModuleDTO module) {
		AnalyzedModuleComponent childComponent = new AnalyzedModuleComponent(module.uniqueName, module.name, module.type, module.visibility);
		AnalysedModuleDTO[] children = ServiceProvider.getInstance().getAnalyseService().getChildModulesInModule(module.uniqueName);
		for(AnalysedModuleDTO subModule : children) {
			this.addChildComponents(childComponent, subModule);
		}
		parentComponent.addChild(childComponent);
	}
	
	public void save(String softwareUnit, String type) {
		logger.info("Adding software unit to module with id " + this.getModuleId());
		try {
			this.softwareUnitDefinitionDomainService.addSoftwareUnit(this.getModuleId(), softwareUnit, type);
			DefinitionController.getInstance().notifyObservers();
		} catch (Exception e) {
			this.logger.error(e.getMessage());
			UiDialogs.errorDialog(softwareUnitFrame, e.getMessage(), "Error");
		}
	}
}
