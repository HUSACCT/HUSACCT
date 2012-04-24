package husacct.graphics.task;

import husacct.graphics.presentation.Drawing;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.jhotdraw.draw.Figure;

public class BasicLayoutStrategy {

//	private class InternalFigure {
//		private BaseFigure figure;
//		private Point2D.Double anchor;
//
//		public InternalFigure(BaseFigure figure) {
//			this.figure = figure;
//		}
//
//		public Point2D.Double getLocation() {
//			Rectangle2D.Double bounds = figure.getBounds();
//
//			return new Point2D.Double(bounds.x, bounds.y);
//		}
//
//		public Point2D.Double getSize() {
//			Rectangle2D.Double bounds = figure.getBounds();
//
//			return new Point2D.Double(bounds.width, bounds.height);
//		}
//	}

	private static final int MAX_HORIZONTAL_ITEMS = 5;
	private static final int ITEM_SPACING = 20;

	private Drawing drawing = null;

	public BasicLayoutStrategy(Drawing drawing) {

		this.drawing = drawing;
	}

	public void doLayout() {
		// Rectangle2D.Double drawingBounds = drawing.getBounds();

		int x = 10, y = 10;
		double maxHeightOnLine = 0;
		int itemsOnCurrentLine = 0;

		ArrayList<Figure> figures = new ArrayList<Figure>();
		figures.addAll(drawing.getChildren());

		for (Figure f : figures) {
			Rectangle2D.Double bounds = f.getBounds();

			bounds.x = x;
			bounds.y = y;
			itemsOnCurrentLine++;

			x += bounds.width + ITEM_SPACING;
			maxHeightOnLine = Math.max(maxHeightOnLine, bounds.height);

			if (itemsOnCurrentLine >= MAX_HORIZONTAL_ITEMS) {

				y += maxHeightOnLine + ITEM_SPACING;
				itemsOnCurrentLine = 0;
			}

			Point2D.Double anchor = new Point2D.Double(bounds.x, bounds.y);
			Point2D.Double lead = new Point2D.Double(bounds.x + bounds.width, bounds.y + bounds.height);			
			f.setBounds(anchor, lead);
		}
	}
}
