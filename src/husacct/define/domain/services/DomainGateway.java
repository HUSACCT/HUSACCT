package husacct.define.domain.services;

import java.util.ArrayList;
import java.util.List;

import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.Module;
import husacct.define.domain.module.ModuleFactory;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.task.DefinitionController;
import husacct.define.task.JtreeController;
import husacct.define.task.SoftwareUnitController;
import husacct.define.task.components.AnalyzedModuleComponent;

public class DomainGateway {

	private static DomainGateway instance=null;
	
	
	public static DomainGateway getInstance()
	{
		if (instance==null) {
			return instance= new DomainGateway();
		} else {
			return instance;
		}
		
	}
	
	
	
	
	
	public boolean addModule(String name,String description,String type)
	{
		StateService.instance().fromGui();
		ModuleFactory factory = new ModuleFactory();
		factory.createModule(name, description, type);
		
		return true;
		
		
	}
	
	public void moveLayerUp(long layerId)
	{
		DefinitionController.getInstance().moveLayerUp(layerId);
		
	}
	
	public void moveLayerDown(long layerId)
	{
		DefinitionController.getInstance().moveLayerDown(layerId);
		
	}



	public void updateModule(String moduleName, String moduleDescription,
			String type) {
		DefinitionController.getInstance().updateModule(moduleName, moduleDescription,type);
		
	}





	public boolean saveAnalzedModule(ArrayList<AnalyzedModuleComponent> units) {
	    long id= DefinitionController.getInstance().getSelectedModuleId();
		SoftwareUnitController softwareUnitController = new SoftwareUnitController(id);
		softwareUnitController.save(units);
		
		StateService.instance().addSoftwareUnit(SoftwareArchitecture.getInstance().getModuleById(id), units);
		
		
		return true;
	}





	public AnalyzedModuleComponent getRootModel() {
	AnalyzedModuleComponent root=	StateService.instance().getRootModel();
		JtreeController.instance().setTreeModel(root);
		return root;
	}





	public void removeSoftwareUnits(List<String> selectedModules,
			List<String> types) {
		DefinitionController.getInstance().removeSoftwareUnits(selectedModules, types);
		
	}





	public void removeModuleById(long moduleId) {
		DefinitionController.getInstance().removeModuleById(moduleId);
		
	}





	public void selectModule(long id) {
		DefinitionController.getInstance().setSelectedModuleId(id);
		
	}
	
	
	
	
	
	
}
