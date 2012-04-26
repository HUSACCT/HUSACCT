package husacct.graphics.presentation;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.jhotdraw.draw.CompositeFigure;
import org.jhotdraw.draw.DecoratedFigure;
import org.jhotdraw.draw.Figure;

import husacct.common.dto.*;
import husacct.graphics.presentation.decorators.DTODecorator;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.figures.NamedFigure;


public class Drawing extends org.jhotdraw.draw.DefaultDrawing
{
	private static final long serialVersionUID = 3212318618672284266L;
	
	public Drawing()
	{
		super();
	}

	public BaseFigure[] getShownModules() {
		ArrayList<BaseFigure> moduleFigures = new ArrayList<BaseFigure>();
		
		for(Figure f : this.getChildren()) {
			//TODO instanceof checking is a code smell
			if((f instanceof BaseFigure) && (f instanceof DTODecorator)) {
				AbstractDTO dto = ((DTODecorator)f).getDTO();
				if((dto instanceof ModuleDTO) || (dto instanceof AnalysedModuleDTO)) {
					moduleFigures.add((BaseFigure)f);
				}
			}
		}
		
		return moduleFigures.toArray(new BaseFigure[]{});
	}
	
	@Override 
	public boolean add(Figure f)
	{
		//this triggers at least the minimum sizes
		f.setBounds(new Point2D.Double(10, 10), new Point2D.Double(11, 11));
		
		//TODO implement layout mechanism here
		
		return super.add(f);
	}
	
	public void clear()
	{
		this.willChange();
		this.basicRemoveAllChildren();
		this.invalidate();
		this.changed();
	}
	
	/**
	 * @deprecated usage of this function can cause problems, because the name of the figure
	 * may be different from e.g. logicalPaths in the dtos 
	 */
	public BaseFigure findFigureByName(String name)
	{
		return this.findFigureByName(name, this.getChildren());
	}
	
	private BaseFigure findFigureByName(String name, List<Figure> figures)
	{
		for(Figure figure : figures)
		{
			BaseFigure foundChildFig = this.findFigureByName(name, figure);
			if(foundChildFig != null)
			{
				return foundChildFig;
			}
		}
		
		return null;
	}
	
	private BaseFigure findFigureByName(String name, Figure figure)
	{		
		if(figure instanceof NamedFigure)
		{
			if(((NamedFigure) figure).getName().equals(name))
			{
				return (BaseFigure)figure;
			}
		}
		
		if(figure instanceof DecoratedFigure)
		{
			BaseFigure foundChildFig = findFigureByName(name, ((DecoratedFigure) figure).getDecorator());
			if(foundChildFig != null)
			{
				return foundChildFig;
			}
		}
		
		if(figure instanceof CompositeFigure)
		{
			BaseFigure foundChildFig = findFigureByName(name, ((CompositeFigure) figure).getChildren());
			if(foundChildFig != null)
			{
				return foundChildFig;
			}
		}
		
		return null;
	}
}
