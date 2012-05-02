package husacct.define.task;

import husacct.ServiceProvider;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.presentation.jframe.JFrameSoftwareUnit;
import husacct.define.presentation.utils.UiDialogs;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;

import org.apache.log4j.Logger;


public class SoftwareUnitController extends PopUpController {

	private JFrameSoftwareUnit softwareUnitFrame;
	private Logger logger;
	
	public SoftwareUnitController(long moduleId, String softwareUnitName) {
		logger = Logger.getLogger(SoftwareUnitController.class);
		this.setModuleId(moduleId);
	}

	@Override
	public void initUi() throws Exception {
		softwareUnitFrame = new JFrameSoftwareUnit(this);
		// Set the visibility of the jframe to true so the jframe is now visible
		UiDialogs.showOnScreen(0, softwareUnitFrame);
		softwareUnitFrame.setVisible(true);
	}

	public void save(String softwareUnit, String type) {
		logger.info("Adding software unit to module with id " + this.getModuleId());
		try {
			defineDomainService.addSoftwareUnit(this.getModuleId(), softwareUnit, type);
			pokeObservers();
		} catch (Exception e) {
			this.logger.error(e.getMessage());
			UiDialogs.errorDialog(softwareUnitFrame, e.getMessage(), "Error");
		}
	}
	
	@Deprecated
	public void fillSoftwareUnitsList(ArrayList<SoftwareUnitDefinition> softwareUnitList){
		AnalysedModuleDTO[] modules = getAnalyzedModules();
		for(AnalysedModuleDTO module : modules) {
			SoftwareUnitDefinition softwareUnit = new SoftwareUnitDefinition(module.name, SoftwareUnitDefinition.Type.valueOf(module.type.toUpperCase()));
			softwareUnitList.add(softwareUnit);
		}
		
		filterAddedSoftwareUnits(softwareUnitList);
	}
	
	@Deprecated
	private void filterAddedSoftwareUnits(ArrayList<SoftwareUnitDefinition> softwareUnitList) {
		ArrayList<SoftwareUnitDefinition> addedsoftwareUnitList = this.defineDomainService.getSoftwareUnit(moduleId);
		for (SoftwareUnitDefinition addedUnit : addedsoftwareUnitList){
			if (softwareUnitList.contains(addedUnit)) {
				softwareUnitList.remove(addedUnit);
			}
			
		}
	}
	
	public AnalyzedModuleComponent getSoftwareUnitTreeComponents() {
		logger.info("getting Sofware Unit Tree Components");
		
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
		for(AnalysedModuleDTO subModule : module.subModules) {
			this.addChildComponents(childComponent, subModule);
		}
		parentComponent.addChild(childComponent);
	}
	
	private ArrayList<SoftwareUnitDefinition> getAnalayzedSoftwareUnits(){
		ArrayList<SoftwareUnitDefinition> softwareUnits = new ArrayList<SoftwareUnitDefinition>();
		AnalysedModuleDTO[] modules = getAnalyzedModules();
		for(AnalysedModuleDTO module : modules) {
			SoftwareUnitDefinition softwareUnit = new SoftwareUnitDefinition(module.name, SoftwareUnitDefinition.Type.valueOf(module.type.toUpperCase()));
			softwareUnits.add(softwareUnit);
		}
		filterAddedSoftwareUnits(softwareUnits);
		return softwareUnits;
	}
}
