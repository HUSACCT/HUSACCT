package husacct.define.domain.module.ToBeImplemented;





import husacct.define.domain.module.ToBeImplemented.modules.Component;
import husacct.define.domain.module.ToBeImplemented.modules.ExternalLibrary;
import husacct.define.domain.module.ToBeImplemented.modules.Facade;
import husacct.define.domain.module.ToBeImplemented.modules.Layer;
import husacct.define.domain.module.ToBeImplemented.modules.SubSystem;

import org.apache.log4j.Logger;

public class ModuleFactory {
	private static Logger logger = Logger.getLogger(ModuleFactory.class);
	
	public static String[] flavors = new String[]{
		"Layer",
		"Component",
		"Facade",
		"SubSystem",
		"ExternalLibrary"
	};
	
	public static Class<?>[] icecreams = new Class[]{
		Layer.class,
		Component.class,
		Facade.class,
		SubSystem.class,
		ExternalLibrary.class
	};
	
	public ModuleStrategy createModule(String choice){
		for(int i = 0; i < flavors.length; i++){
			if(flavors[i].equals(choice)) try{
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
			if(flavors[i].equals(choice)) try{
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
}
