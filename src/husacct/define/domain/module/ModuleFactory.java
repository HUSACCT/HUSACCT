package husacct.define.domain.module;

import husacct.define.domain.module.modules.Blank;
import husacct.define.domain.module.modules.Component;
import husacct.define.domain.module.modules.ExternalLibrary;
import husacct.define.domain.module.modules.Facade;
import husacct.define.domain.module.modules.Layer;
import husacct.define.domain.module.modules.Root;
import husacct.define.domain.module.modules.SubSystem;

import org.apache.log4j.Logger;

public class ModuleFactory {
	private static Logger logger = Logger.getLogger(ModuleFactory.class);

	public static String[] flavors = new String[]{
		"Layer",
		"Component",
		"Facade",
		"SubSystem",
		"ExternalLibrary",
		"Root",
		"Blank"
	};

	public static Class<?>[] icecreams = new Class[]{
		Layer.class,
		Component.class,
		Facade.class,
		SubSystem.class,
		ExternalLibrary.class,
		Root.class,
		Blank.class
	};

	public ModuleStrategy createModule(String choice){
		for(int i = 0; i < flavors.length; i++){
			if(flavors[i].equalsIgnoreCase(choice)) try{
				ModuleStrategy newModule = (ModuleStrategy)icecreams[i].newInstance();
				newModule.setType(choice);
				return newModule;
			}catch (InstantiationException ex) {
				logger.error("Instantiation Error in ModuleFactory: " + ex.toString());
			} catch (IllegalAccessException ex) {
				logger.error("Instantiation Error in ModuleFactory: " + ex.toString());
			}
		}
		logger.error("Error in ModuleFactory: Illegal choice: ");
		throw new IllegalArgumentException("Illegal choice");
	}

	public ModuleStrategy createDummy(String choice){
		for(int i = 0; i < flavors.length; i++){
			if(flavors[i].equalsIgnoreCase(choice)) try{
				ModuleStrategy dummyModule = (ModuleStrategy)icecreams[i].newInstance();
				dummyModule.setType(choice);
				dummyModule.setId(-1);
				return dummyModule;
			}catch (InstantiationException ex) {
				logger.error("Instantiation Error in ModuleFactory: " + ex.toString());
			} catch (IllegalAccessException ex) {
				logger.error("Instantiation Error in ModuleFactory: " + ex.toString());
			}
		}
		logger.error("Error in ModuleFactory: Illegal choice: ");
		throw new IllegalArgumentException("Illegal choice");
	}


	public ModuleStrategy updateModuleType(ModuleStrategy oldModule,String choice){
		ModuleStrategy newModule = createModule(choice);
		oldModule.copyValuestoNewCompont(newModule);
		if (choice.toLowerCase().equals("component")) {
			ModuleStrategy facade=	this.createModule("Facade");
			facade.set(newModule.getName()+"Facade", "This the Facade of "+newModule.getName());
			newModule.addSubModule(0, facade);
		}	
		return newModule;		
	}
}
