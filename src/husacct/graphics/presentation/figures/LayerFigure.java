package husacct.graphics.presentation.figures;

import husacct.common.dto.AbstractDTO;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.RectangleFigure;

public class LayerFigure extends ModuleFigure {
	
	private static final long serialVersionUID = 101138923385231941L;
	private RectangleFigure body;
	
	public LayerFigure(AbstractDTO dto)
	{		
		super(dto);
		
		body = new RectangleFigure();		
		children.add(body);
	}	
	
	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead)
	{
		body.setBounds(anchor, lead);
	}	
	
	@Override
	public LayerFigure clone() {
		
		LayerFigure other = (LayerFigure)super.clone();
		other.body = body.clone();
		
		other.children = new ArrayList<Figure>();
		other.children.add(body);
		
		return other;
	}
}
