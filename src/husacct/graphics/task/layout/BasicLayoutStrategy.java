package husacct.graphics.task.layout;

import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.figures.RelationFigure;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.jhotdraw.draw.AbstractCompositeFigure;
import org.jhotdraw.draw.ConnectionFigure;
import org.jhotdraw.draw.Figure;

public class BasicLayoutStrategy implements LayoutStrategy {
	
	private static final double		VERT_ITEM_SPACING	= 40.0;
	private static final double		HORZ_ITEM_SPACING	= 50.0;
	
	private AbstractCompositeFigure	drawing				= null;
	
	public BasicLayoutStrategy(AbstractCompositeFigure theDrawing) {
		drawing = theDrawing;
	}
	
	private int countItemsToPosition(List<Figure> figures) {
		int count = 0;
		
		for (Figure f : figures) {
			BaseFigure bf = (BaseFigure) f;
			if (!bf.isInContainer() && !bf.isLine()) count++;
		}
		
		return count;
	}
	
	@Override
	public void doLayout() {
		double x = HORZ_ITEM_SPACING, y = VERT_ITEM_SPACING;
		double maxHeightOnLine = 0.0;
		int figuresOnLine = 0;
		
		ArrayList<Figure> figures = new ArrayList<Figure>();
		ArrayList<Figure> connectors = new ArrayList<Figure>();
		figures.addAll(drawing.getChildren());
		
		int itemsToPosition = countItemsToPosition(figures);
		int maxFiguresOnRow = (int) Math.ceil(Math.sqrt(itemsToPosition));
		
		for (Figure f : figures)
			if (!isConnector(f) && !inContainer(f)) {
				
				Rectangle2D.Double bounds = f.getBounds();
				
				if (figuresOnLine >= maxFiguresOnRow) {
					x = HORZ_ITEM_SPACING;
					y += maxHeightOnLine + VERT_ITEM_SPACING;
					figuresOnLine = 0;
					maxHeightOnLine = 0;
				}
				
				bounds.x = x;
				bounds.y = y;
				Point2D.Double anchor = new Point2D.Double(bounds.x, bounds.y);
				Point2D.Double lead = new Point2D.Double(bounds.x
						+ bounds.width, bounds.y + bounds.height);
				
				f.willChange();
				f.setBounds(anchor, lead);
				f.changed();
				
				x += bounds.width + HORZ_ITEM_SPACING;
				maxHeightOnLine = Math.max(maxHeightOnLine, bounds.height);
				figuresOnLine++;
			} else
				connectors.add(f);
	}
	
	private boolean inContainer(Figure f) {
		BaseFigure bf = (BaseFigure) f;
		return bf.isInContainer();
	}
	
	// NOTE: Patrick: I'm not quite sure if this code should be here. We really
	// need to discuss this kind of
	// code. It's ugly and unneccessary I think.
	private boolean isConnector(Figure figure) {
		return figure instanceof ConnectionFigure
				|| figure instanceof BaseFigure
				&& figure instanceof RelationFigure;
	}
}
