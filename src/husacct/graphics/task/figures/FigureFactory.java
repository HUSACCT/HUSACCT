package husacct.graphics.task.figures;

import java.awt.geom.Rectangle2D;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.ModuleDTO;

public final class FigureFactory {

	public static AbstractFigure getFigure(AbstractDTO dto)
	{
		if(dto instanceof ModuleDTO)
		{
			ModuleDTO moduleDTO = (ModuleDTO)dto;
			Rectangle2D.Double rect = new Rectangle2D.Double(10, 10, 100, 75);
			if (moduleDTO.type == "Layer") 
				return new LayerFigure(rect, moduleDTO);
			else if (moduleDTO.type == "Module") 
				return new ClassFigure(rect, moduleDTO);
		}
		
		throw new RuntimeException("Undefined module type passed to FigureFactory");
	}
}
