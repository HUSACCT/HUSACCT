package husacct.define.task.components;

import husacct.define.domain.module.Module;
import husacct.define.domain.module.Layer;
import husacct.define.domain.module.ExternalLibrary;
import husacct.define.domain.module.Component;
import org.apache.log4j.Logger;

public class DefineComponentFactory {
	
	public static AbstractDefineComponent getDefineComponent(Module module) {
		AbstractDefineComponent returnComponent = null;
		if(module instanceof Layer) {
			returnComponent = DefineComponentFactory.createLayerComponent(module);
		} else if(module instanceof ExternalLibrary) {
			returnComponent = DefineComponentFactory.createExternalLibraryComponent(module);
		} else if(module instanceof Component) { //husacct.define.domain.module.Component
			returnComponent = DefineComponentFactory.createComponentComponent(module);
		} else if(module instanceof Module) {
			returnComponent = DefineComponentFactory.createModuleComponent(module);
		} else {
			Logger logger = Logger.getLogger(DefineComponentFactory.class);
			logger.error("ModuleType not implemented");
		}
		return returnComponent;
	}
	
	private static ModuleComponent createModuleComponent(Module module) {
		ModuleComponent moduleComponent = new ModuleComponent();
		moduleComponent.setModuleId(module.getId());
		moduleComponent.setName(module.getName());
		return moduleComponent;
	}
	
	private static LayerComponent createLayerComponent(Module module) {
		Layer layer = (Layer) module;
		LayerComponent layerComponent = new LayerComponent();
		layerComponent.setModuleId(layer.getId());
		layerComponent.setHierarchicalLevel(layer.getHierarchicalLevel());
		layerComponent.setName(layer.getName());
		return layerComponent;
	}
	
	private static ExternalLibraryComponent createExternalLibraryComponent(Module module) {
		ExternalLibrary externalLibrary = (ExternalLibrary) module;
		ExternalLibraryComponent externalLibraryComponent = new ExternalLibraryComponent();
		externalLibraryComponent.setModuleId(externalLibrary.getId());
		externalLibraryComponent.setName(externalLibrary.getName());
		return externalLibraryComponent;
	}
	
	private static ComponentComponent createComponentComponent(Module module) {
		Component component = (Component) module;
		ComponentComponent componentComponent = new ComponentComponent();
		componentComponent.setModuleId(component.getId());
		componentComponent.setName(component.getName());
		return componentComponent;
	}
}
