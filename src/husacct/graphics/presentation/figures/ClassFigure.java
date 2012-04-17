package husacct.graphics.presentation.figures;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.TextFigure;

public class ClassFigure extends ModuleFigure {

	private static final long serialVersionUID = -468596930534802557L;
	private RectangleFigure top, middle, bottom;
	private TextFigure text;

	public ClassFigure(Rectangle2D.Double rect, String name) {
		super(rect, name);
	}

	@Override
	public void initializeComponents() {
		top = new RectangleFigure(anchor.x, anchor.y, 0, 0);
		middle = new RectangleFigure(anchor.x, anchor.y, 0, 0);
		bottom = new RectangleFigure(anchor.x, anchor.y, 0, 0);
		text = new TextFigure(getName());

		children.add(top);
		children.add(middle);
		children.add(text);
		children.add(bottom);

		super.initializeComponents();
	}

	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
		Point2D.Double topLeft = anchor;
		Point2D.Double bottomRight = new Point2D.Double(anchor.x + getWidth(),
				anchor.y + getHeight() * 0.2);
		top.setBounds(topLeft, bottomRight);

		topLeft = new Point2D.Double(anchor.x, anchor.y + getHeight() * 0.2);
		bottomRight = new Point2D.Double(anchor.x + getWidth(), anchor.y
				+ getHeight() * 0.6);
		middle.setBounds(topLeft, bottomRight);
		text.setBounds(topLeft, bottomRight);

		topLeft = new Point2D.Double(anchor.x, anchor.y + getHeight() * 0.6);
		bottomRight = new Point2D.Double(anchor.x + getWidth(), anchor.y
				+ getHeight());
		bottom.setBounds(topLeft, bottomRight);
	}
}
