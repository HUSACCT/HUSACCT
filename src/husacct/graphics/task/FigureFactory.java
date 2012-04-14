package husacct.graphics.task;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.task.figures.*;

public class FigureFactory
{
	public AbstractFigure createFigure(AbstractDTO dto)
	{
		if(dto instanceof ModuleDTO)
		{
			return this.createFigure((ModuleDTO)dto);
		}
		else if(dto instanceof DependencyDTO)
		{
			return this.createFigure((DependencyDTO)dto);
		}
		
		throw new RuntimeException("type of dto not implemented");
	}
	
	public AbstractFigure createFigure(AbstractDTO dto, ViolationDTO[] violationDTOs)
	{
		if(dto instanceof ModuleDTO)
		{
			return new ViolatedModuleFigure((ModuleDTO)dto, violationDTOs);
		}
		else if(dto instanceof DependencyDTO)
		{
			return new ViolatedRelationFigure((DependencyDTO)dto, violationDTOs);
		}
		
		throw new RuntimeException("type of dto with exception not implemented");
	}
	
	public ModuleFigure createFigure(ModuleDTO dto)
	{
		return new ModuleFigure(dto);
	}
	
	public RelationFigure createFigure(DependencyDTO dto)
	{
		return new RelationFigure(dto);
	}
}
