package husacct.graphics.presentation.figures;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.TextFigure;

public class ClassFigure extends NamedFigure {

	private static final long serialVersionUID = -468596930534802557L;
	protected RectangleFigure top, middle, bottom;
	protected TextFigure classNameText;

	public static final int MIN_WIDTH = 100;
	public static final int MIN_HEIGHT = 80;

	public ClassFigure(String name) {
		super(name);

		top = new RectangleFigure();
		middle = new RectangleFigure();
		bottom = new RectangleFigure();
		classNameText = new TextFigure(getName());
		classNameText.set(AttributeKeys.FONT_BOLD, true);

		children.add(top);
		children.add(middle);
		children.add(classNameText);
		children.add(bottom);
		
		set(AttributeKeys.FILL_COLOR, defaultBackgroundColor);
		set(AttributeKeys.CANVAS_FILL_COLOR, defaultBackgroundColor);
	}
	
	public TextFigure getClassNameText() {
		return this.classNameText;
	}

	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
		if ((lead.x - anchor.x) < MIN_WIDTH) {
			lead.x = anchor.x + MIN_WIDTH;
		}
		if ((lead.y - anchor.y) < MIN_HEIGHT) {
			lead.y = anchor.y + MIN_HEIGHT;
		}

		double width = lead.x - anchor.x;
		double totalHeight = lead.y - anchor.y;
		double middleHeight = Math.floor(totalHeight / 3);
		double bottomHeight = Math.floor(totalHeight / 3);
		double topHeight = totalHeight - middleHeight - bottomHeight;

		top.setBounds(anchor, new Point2D.Double(anchor.x + width, anchor.y + topHeight));
		middle.setBounds(new Point2D.Double(anchor.x, anchor.y + topHeight), new Point2D.Double(anchor.x + width,
				anchor.y + topHeight + middleHeight));
		bottom.setBounds(new Point2D.Double(anchor.x, anchor.y + topHeight + middleHeight), new Point2D.Double(anchor.x
				+ width, anchor.y + topHeight + middleHeight + bottomHeight));

		// textbox centralising
		double plusX = ((top.getBounds().width - this.classNameText.getBounds().width) / 2);
		double plusY = ((top.getBounds().height - this.classNameText.getBounds().height) / 2);

		Point2D.Double textAnchor = (Double) anchor.clone();
		textAnchor.x += plusX;
		textAnchor.y += plusY;
		classNameText.setBounds(textAnchor, null);

		this.invalidate();
	}

	@Override
	public ClassFigure clone() {
		ClassFigure other = (ClassFigure) super.clone();

		other.top = top.clone();
		other.middle = middle.clone();
		other.classNameText = classNameText.clone();
		other.bottom = bottom.clone();

		other.children = new ArrayList<Figure>();
		other.children.add(other.top);
		other.children.add(other.middle);
		other.children.add(other.classNameText);
		other.children.add(other.bottom);

		return other;
	}

	@Override
	public boolean isModule() {
		return true;
	}

	@Override
	public boolean isLine() {
		return false;
	}
}
