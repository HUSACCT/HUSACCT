package husacct.define.domain.module;

import husacct.ServiceProvider;
import husacct.define.task.DefinitionController;

public class ModuleFactory {
	private String moduleName="";
	private String moduleDescription="";

	public boolean createModule(String name,String description,String moduleType)
 {
	 moduleName=name;
	 moduleDescription=description;
		boolean result =false;
	 if(moduleType == ServiceProvider.getInstance().getLocaleService().getTranslatedString("SubSystem")) {
		result=	this.submitSubSystem();
		} else if(moduleType == ServiceProvider.getInstance().getLocaleService().getTranslatedString("Layer")) {
			result= this.submitLayer();
		} else if(moduleType == ServiceProvider.getInstance().getLocaleService().getTranslatedString("Component")) {
			result= this.submitComponent();
		} else if(moduleType == ServiceProvider.getInstance().getLocaleService().getTranslatedString("ExternalLibrary")) {
			result= this.submitExternalLibrary();
		}
 return result;
 }
	 
	

 
 
 

		private boolean submitSubSystem() {
			  
				
				DefinitionController definitionController = DefinitionController.getInstance();
				boolean hasBeenAdded = definitionController.addSubSystem(definitionController.getSelectedModuleId(), moduleName, moduleDescription);
				return hasBeenAdded;
					
				
				
			
		}
		
		private boolean submitLayer() {
		
				
				DefinitionController definitionController = DefinitionController.getInstance();
				boolean hasBeenAdded = definitionController.addLayer(definitionController.getSelectedModuleId(), moduleName, moduleDescription);
				return hasBeenAdded;
	
		}
		
		private boolean submitComponent() {
	
				
				DefinitionController definitionController = DefinitionController.getInstance();
				boolean hasBeenAdded = definitionController.addComponent(definitionController.getSelectedModuleId(), moduleName, moduleDescription);
				return hasBeenAdded;
		
		}
		
		private boolean submitExternalLibrary() {
	
				
				DefinitionController definitionController = DefinitionController.getInstance();
				boolean hasBeenAdded = definitionController.addExternalLibrary(definitionController.getSelectedModuleId(), moduleName, moduleDescription);
				return hasBeenAdded;
			
		}
 
 
 
 
 
 
 
 }
 
 
 
 
