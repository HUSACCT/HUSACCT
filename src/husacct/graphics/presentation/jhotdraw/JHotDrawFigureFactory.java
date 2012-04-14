package husacct.graphics.presentation.jhotdraw;

import husacct.graphics.presentation.jhotdraw.figures.*;
import husacct.graphics.task.figures.*;

public class JHotDrawFigureFactory
{	
	public AbstractJHotDrawFigure createFigure(AbstractFigure figure)
	{
		if(figure instanceof ViolatedModuleFigure)
		{
			return createFigure((ViolatedModuleFigure)figure);
		}
		else if(figure instanceof ModuleFigure)
		{
			return createFigure((ModuleFigure)figure);
		}
		else if(figure instanceof RelationFigure)
		{
			return createFigure((RelationFigure)figure);
		}
		else
		{
			throw new RuntimeException("type of figure not implemented");
		}
	}
	
	public AbstractJHotDrawFigure createFigure(ViolatedModuleFigure figure)
	{
		//TODO check the possible values of the moduleDTO type property
		switch(figure.getModuleDTO().type.toLowerCase())
		{
			case "component":
				return new ViolatedComponentFigure(figure);
				
			case "layer":
				return null;//TODO new ViolatedLayerFigure(figure);
				
			case "package":
				return null;//TODO new ViolatedPackageFigure(figure);
				
			case "class":
				return null;//TODO new ViolatedClassFigure(figure);
				
			default:
				throw new RuntimeException("type of module type not implemented");
		}		
	}
	
	public JHotDrawModuleFigure createFigure(ModuleFigure figure)
	{
		//TODO check the possible values of the moduleDTO type property
		switch(figure.getModuleDTO().type.toLowerCase())
		{
			case "component":
				return new ComponentFigure(figure);
				
			case "layer":
				return new LayerFigure(figure);
				
			case "package":
				return new PackageFigure(figure);
				
			case "class":
				return new ClassFigure(figure);
				
			default:
				throw new RuntimeException("type of module type not implemented");
		}
	}
	
	public JHotDrawRelationFigure createFigure(RelationFigure figure)
	{
		if(figure instanceof ViolatedRelationFigure)
		{
			return new JHotDrawViolatedRelationFigure((ViolatedRelationFigure)figure);
		}
		else
		{
			return new JHotDrawRelationFigure(figure);
		}
	}
}
