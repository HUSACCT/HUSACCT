package husacct.define.domain.services;

import husacct.define.domain.module.ModuleStrategy;
import husacct.define.task.DefinitionController;

public class DragAndDropService  {

	
	private static DragAndDropService  instance =null;
	
	private ModuleDomainService moduleService = new ModuleDomainService();
	
	
	public static DragAndDropService getInstance()
	{
		
		return instance==null ? instance=new DragAndDropService():instance;
		
	}
	
	
	
	public void moveToModule(long fromModuleid,long toModuleId)
	{
		ModuleStrategy from = moduleService.getModuleById(fromModuleid);
		ModuleStrategy to = moduleService.getModuleById(toModuleId);
		UndoRedoService.getInstance().removeSeperatedModule(from);
		from.setParent(to);
		UndoRedoService.getInstance().addSeperatedModule(from);
		DefinitionController.getInstance().notifyObservers();
		
	}


}
