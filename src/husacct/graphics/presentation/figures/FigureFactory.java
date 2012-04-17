package husacct.graphics.presentation.figures;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.ModuleDTO;

// TODO: Should this class be final? 
public final class FigureFactory {

	public BaseFigure createFigure(AbstractDTO dto)
	{
		Rectangle2D.Double position = new Rectangle2D.Double(10, 10, 200, 150);
		
		if (dto instanceof ModuleDTO)
			return createFigure(position, (ModuleDTO)dto);

		else if (dto instanceof AnalysedModuleDTO)
			return createFigure(position, (AnalysedModuleDTO) dto);
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
