package husacct.define.task.components;


import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.module.modules.Component;
import husacct.define.domain.module.modules.ExternalLibrary;
import husacct.define.domain.module.modules.Facade;
import husacct.define.domain.module.modules.Layer;
import husacct.define.domain.module.modules.SubSystem;

import org.apache.log4j.Logger;

//import husacct.define.domain.module.ExternalSystem;

public class DefineComponentFactory {

	private static ComponentComponent createComponentComponent(ModuleStrategy module) {
		Component component = (Component) module;
		ComponentComponent componentComponent = new ComponentComponent();
		componentComponent.setModuleId(component.getId());
		componentComponent.setName(component.getName());
		return componentComponent;
	}

	private static ExternalLibraryComponent createExternalLibraryComponent(ModuleStrategy module) {
		ExternalLibrary externalLibrary = (ExternalLibrary) module;
		ExternalLibraryComponent externalLibraryComponent = new ExternalLibraryComponent();
		externalLibraryComponent.setModuleId(externalLibrary.getId());
		externalLibraryComponent.setName(externalLibrary.getName());
		return externalLibraryComponent;
	}

	private static AbstractDefineComponent createFacade(ModuleStrategy module) {
		FacadeComponent facade = new FacadeComponent();
		facade.setModuleId(module.getId());
		facade.setName(module.getName());
		return facade;
	}

	private static LayerComponent createLayerComponent(ModuleStrategy module) {
		Layer layer = (Layer) module;
		LayerComponent layerComponent = new LayerComponent();
		layerComponent.setModuleId(layer.getId());
		layerComponent.setHierarchicalLevel(layer.getHierarchicalLevel());
		layerComponent.setName(layer.getName());
		return layerComponent;
	}

	private static SubSystemComponent createModuleComponent(ModuleStrategy module) {
		SubSystemComponent subSystemComponent = new SubSystemComponent();
		subSystemComponent.setModuleId(module.getId());
		subSystemComponent.setName(module.getName());
		return subSystemComponent;
	}

	public static AbstractDefineComponent getDefineComponent(ModuleStrategy module) {
		AbstractDefineComponent returnComponent = null;
		if (module instanceof Layer) {
			returnComponent = DefineComponentFactory
					.createLayerComponent(module);
		} else if (module instanceof ExternalLibrary) {
			returnComponent = DefineComponentFactory
					.createExternalLibraryComponent(module);
		} else if (module instanceof Component) { // husacct.define.domain.module.Component
			returnComponent = DefineComponentFactory
					.createComponentComponent(module);
		} else if (module instanceof Facade) {
			returnComponent = DefineComponentFactory.createFacade(module);
		} else if (module instanceof SubSystem) {
			returnComponent = DefineComponentFactory
					.createModuleComponent(module);

		} else {
			Logger logger = Logger.getLogger(DefineComponentFactory.class);
			logger.error("ModuleType not implemented");
		}
		return returnComponent;
	}
}
