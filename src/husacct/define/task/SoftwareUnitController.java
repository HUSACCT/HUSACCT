package husacct.define.task;

import java.util.ArrayList;

import husacct.analyse.AnalyseServiceStub;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.presentation.jframe.JFrameSoftwareUnit;
import husacct.define.presentation.utils.UiDialogs;


public class SoftwareUnitController extends PopUpController {

	private JFrameSoftwareUnit softwareUnitFrame;
	
	public SoftwareUnitController(long moduleId, String softwareUnitName) {
		setModuleId(moduleId);
//		softwareUnitFrame.setTitle("Map " + softwareUnitName);
	}
	
	public AnalysedModuleDTO[] getAnalyzedModules() {
		AnalyseServiceStub analyzeService = new AnalyseServiceStub();
		AnalysedModuleDTO[] modules = analyzeService.getRootModules();
		return modules;
	}

	@Override
	public void initUi() throws Exception {
		softwareUnitFrame = new JFrameSoftwareUnit(this);
		// Set the visibility of the jframe to true so the jframe is now visible
		UiDialogs.showOnScreen(0, softwareUnitFrame);
		softwareUnitFrame.setVisible(true);

	}

	public void save(String softwareUnit) {
		try {
			long moduleId = getModuleId();
			defineDomainService.addSoftwareUnit(moduleId, softwareUnit);
			
			pokeObservers();
		} catch (Exception e) {
			UiDialogs.errorDialog(softwareUnitFrame, e.getMessage(), "Error");
		}
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
		ArrayList<SoftwareUnitDefinition> addedsoftwareUnitList = this.defineDomainService.getSoftwareUnit(moduleId);
		for (SoftwareUnitDefinition addedUnit : addedsoftwareUnitList){
			if (softwareUnitList.contains(addedUnit)) {
				softwareUnitList.remove(addedUnit);
			}
			
		}
	}
}
