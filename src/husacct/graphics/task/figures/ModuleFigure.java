package husacct.graphics.task.figures;

import husacct.common.dto.ModuleDTO;

public class ModuleFigure extends AbstractFigure
{
	private ModuleDTO moduleDTO;
	
	public ModuleFigure(ModuleDTO moduleDTO)
	{
		this.moduleDTO = moduleDTO;
	}
	
	public ModuleDTO getModuleDTO()
	{
		return this.moduleDTO;
	}
}
