package husacct.graphics.task.layout;

import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.figures.ParentFigure;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jhotdraw.draw.Figure;

public class ContainerLayoutStrategy implements LayoutStrategy {
	private static final int MIN_FIGURES_PER_ROW = 3;
	private static final int HORZ_SPACING = 10;
	private static final int VERT_SPACING = 10;
	
	private ParentFigure container = null;
	private Point2D.Double location = new Point2D.Double(HORZ_SPACING, VERT_SPACING);
	
	public ContainerLayoutStrategy(ParentFigure theParent) {
		container = theParent;
	}
	
	@Override
	public void doLayout(int screenWidth, int screenHeight) {
		BaseFigure[] figures = container.getChildFigures();
		Rectangle2D.Double parentBounds = container.getBounds();
		int maxItemsPerRow = MIN_FIGURES_PER_ROW;
		int itemsOnRow = 0;
		double heightOnRow = 0;

		if (figures.length > MIN_FIGURES_PER_ROW) 
			maxItemsPerRow = (int)Math.ceil(Math.sqrt(figures.length));
		
		for (Figure f : figures) {
			BaseFigure figure = (BaseFigure)f;
			
			if (!figure.isLine()) {
				Rectangle2D.Double bounds = figure.getBounds();
				
				Point2D.Double anchor = new Point2D.Double(parentBounds.x + location.x, parentBounds.y + location.y);
				Point2D.Double lead = new Point2D.Double(parentBounds.x + location.x + bounds.width, parentBounds.y + location.y + bounds.height);
				
				//figure.willChange();
				figure.setBounds(anchor, lead);
				//figure.changed();
						
				location.x += bounds.width + HORZ_SPACING;
				itemsOnRow++;
				heightOnRow = Math.max(bounds.height, heightOnRow);
			}
			
			if (itemsOnRow >= maxItemsPerRow) {
				itemsOnRow = 0;
				location.y += heightOnRow + VERT_SPACING;
				location.x = HORZ_SPACING;
			}
		}
	}
}