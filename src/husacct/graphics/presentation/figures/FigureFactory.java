package husacct.graphics.presentation.figures;

import husacct.common.dto.*;

public final class FigureFactory {

	public BaseFigure createFigure(AbstractDTO dto)
	{
		BaseFigure retVal = null;
		
		if ((dto instanceof ModuleDTO)
				|| (dto instanceof AnalysedModuleDTO))
		{
			retVal = createModuleFigure(dto);
		}
		else if (dto instanceof DependencyDTO)
		{
			retVal = createFigure((DependencyDTO)dto);
		}

		// TODO: Use a DTODecorator to store the DTO along side with the newly created Figure. 
		// TODO: Determine whether it's Figure -> DTODecorator or DTODecorator -> Figure.
		if (retVal != null) 
			return retVal;
		
		throw new RuntimeException("Unimplemented dto type '"+dto.getClass().getSimpleName()+"' passed to FigureFactory");
	}

	private ModuleFigure createModuleFigure(AbstractDTO dto)
	{
		String type;
		
		if(dto instanceof ModuleDTO)
		{
			type = ((ModuleDTO)dto).type;
		}
		else if (dto instanceof AnalysedModuleDTO)
		{
			type = ((AnalysedModuleDTO)dto).type;
		}
		else
		{
			throw new RuntimeException("dto type '"+dto.getClass().getSimpleName()+"' is not recognized as a module dto");
		}
		
		switch(type.toLowerCase())
		{
			case "layer":
				return new LayerFigure(dto);
				
			case "class":
				return new ClassFigure(dto);
				
			case "package":
				return new PackageFigure(dto);
			
			default:
				return new ModuleFigure(dto);
		}
	}
	
	private RelationFigure createFigure(DependencyDTO dependencyDTO)
	{
		return new RelationFigure(dependencyDTO);
	}
}
