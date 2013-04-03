package husacct.define.task;

import husacct.ServiceProvider;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.services.SoftwareUnitDefinitionDomainService;
import husacct.define.presentation.jdialog.SoftwareUnitJDialog;
import husacct.define.presentation.utils.UiDialogs;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;


public class SoftwareUnitController extends PopUpController {

	private SoftwareUnitJDialog softwareUnitFrame;
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
		//AnalysedModuleDTO mockModule1 = new AnalysedModuleDTO("plant uml", "plant uml", "externallibrary", "true");
		//AnalysedModuleDTO mockModule2 = new AnalysedModuleDTO("plant this op", "plant this op", "subsystem", "true");
		
		//AnalysedModuleDTO[]	testreturnlist = new AnalysedModuleDTO[modules.length+2];
		
		
		
		
		
		
		//testreturnlist[modules.length]=mockModule1;
		//testreturnlist[modules.length+1]=mockModule2;
		return modules;
	}
	
	private void addChildComponents(AnalyzedModuleComponent parentComponent, AnalysedModuleDTO module) {
		AnalyzedModuleComponent childComponent = new AnalyzedModuleComponent(module.uniqueName, module.name, module.type, module.visibility);
		AnalysedModuleDTO[] children = ServiceProvider.getInstance().getAnalyseService().getChildModulesInModule(module.uniqueName);
		for(AnalysedModuleDTO subModule : children) {
			this.addChildComponents(childComponent, subModule);
			logger.debug(subModule.uniqueName+subModule.name+" ---"+subModule.type+"----"+subModule.subModules.size());
		}
		parentComponent.addChild(childComponent);
	}
	
	public void save(String softwareUnit, String type) {
		save(this.getModuleId(), softwareUnit, type);
	}
	
	public void save(Long moduleId, String softwareUnit, String type) {
		logger.info("Adding software unit to module with id " + this.getModuleId());
		try {
			this.softwareUnitDefinitionDomainService.addSoftwareUnit(moduleId, softwareUnit, type);
			DefinitionController.getInstance().notifyObservers();
		} catch (Exception e) {
			this.logger.error(e.getMessage());
			UiDialogs.errorDialog(softwareUnitFrame, e.getMessage());
		}
	}
}
