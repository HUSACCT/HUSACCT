package husacct.graphics.presentation.figures;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.ModuleDTO;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

public final class FigureFactory {

	public BaseFigure createFigure(AbstractDTO dto)
	{
		BaseFigure retVal = null;
		Rectangle2D.Double position = new Rectangle2D.Double(10, 10, 200, 150);
		
		if (dto instanceof ModuleDTO)
			retVal = createFigure(position, (ModuleDTO)dto);
		else if (dto instanceof AnalysedModuleDTO)
			retVal = createFigure(position, (AnalysedModuleDTO) dto);

		// TODO: Use a DTODecorator to store the DTO along side with the newly created Figure. 
		// TODO: Determine whether it's Figure -> DTODecorator or DTODecorator -> Figure.
		if (retVal != null) 
			return retVal;
		
		throw new RuntimeException("Undefined module type passed to FigureFactory");
	}
	
	private BaseFigure createFigure(Double position,
			AnalysedModuleDTO dto) {

		if (dto.type == "package")
			return new PackageFigure(position, dto.uniqueName);
		else if (dto.type == "class")
			return new ClassFigure(position, dto.uniqueName);

		return new LayerFigure(position, "<<invalid module type>>" + dto.uniqueName);
	}

	private BaseFigure createFigure(Rectangle2D.Double position,
			ModuleDTO dto) {
		
		if (dto.type == "Layer") 
			return new LayerFigure(position, dto.logicalPath);
		else if (dto.type == "Module") 
			return new ClassFigure(position, dto.logicalPath);
		
		return new ClassFigure(position, "<<invalid module type>>" + dto.logicalPath);
	}
}
