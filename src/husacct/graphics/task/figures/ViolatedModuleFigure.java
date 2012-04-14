package husacct.graphics.task.figures;

import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ViolationDTO;

public class ViolatedModuleFigure extends ModuleFigure 
		implements IViolatedFigure
{
	private ViolationDTO[] violationDTOs;

	public ViolatedModuleFigure(ModuleDTO moduleDTO,
			ViolationDTO[] violationDTOs)
	{
		super(moduleDTO);
		
		this.violationDTOs = violationDTOs;
	}

	@Override
	public ViolationDTO[] getViolations()
	{
		return this.violationDTOs;
	}

}
